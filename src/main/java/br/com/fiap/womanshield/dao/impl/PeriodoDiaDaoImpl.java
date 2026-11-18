package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.PeriodoDiaDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.PeriodoDia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PeriodoDiaDaoImpl implements PeriodoDiaDao {

    private static final String SELECT_BASE =
            "SELECT ID_PERIODO, NM_PERIODO, HR_INICIO, HR_FIM, VL_FATOR_TEMPO, VL_FATOR_RISCO FROM T_WS_PERIODO_DIA";

    @Override
    public List<PeriodoDia> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY HR_INICIO";
        List<PeriodoDia> periodos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                periodos.add(mapear(rs));
            }
            return periodos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar periodos do dia.", e);
        }
    }

    @Override
    public Optional<PeriodoDia> buscarPorHora(int hora) {
        String sql = SELECT_BASE + " WHERE ? >= HR_INICIO AND ? < HR_FIM";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, hora);
            stmt.setInt(2, hora);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar periodo do dia para a hora " + hora + ".", e);
        }
    }

    @Override
    public Optional<PeriodoDia> buscarPorId(Integer idPeriodo) {
        String sql = SELECT_BASE + " WHERE ID_PERIODO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPeriodo);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar periodo " + idPeriodo + ".", e);
        }
    }

    private PeriodoDia mapear(ResultSet rs) throws SQLException {
        PeriodoDia periodo = new PeriodoDia();
        periodo.setIdPeriodo(rs.getInt("ID_PERIODO"));
        periodo.setNomePeriodo(rs.getString("NM_PERIODO"));
        periodo.setHoraInicio(rs.getInt("HR_INICIO"));
        periodo.setHoraFim(rs.getInt("HR_FIM"));
        periodo.setFatorTempo(rs.getBigDecimal("VL_FATOR_TEMPO"));
        periodo.setFatorRisco(rs.getBigDecimal("VL_FATOR_RISCO"));
        return periodo;
    }
}
