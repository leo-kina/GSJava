package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.AlertaContatoDao;
import br.com.fiap.womanshield.dao.AlertaDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.Alerta;
import br.com.fiap.womanshield.model.AlertaContato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlertaDaoImpl implements AlertaDao {

    private static final String SELECT_BASE =
            "SELECT ID_ALERTA, ID_USUARIA, ID_BAIRRO, ID_PERIODO, DT_HR_ALERTA, ST_ORIGEM_LOCAL FROM T_WS_ALERTA";

    private final AlertaContatoDao alertaContatoDao = new AlertaContatoDaoImpl();

    @Override
    public Alerta inserir(Alerta alerta) {
        String sqlAlerta = "INSERT INTO T_WS_ALERTA (ID_ALERTA, ID_USUARIA, ID_BAIRRO, ID_PERIODO, DT_HR_ALERTA, ST_ORIGEM_LOCAL) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlContato = "INSERT INTO T_WS_ALERTA_CONTATO "
                + "(ID_ALERTA, ID_CONTATO, NR_ORDEM, NR_MINUTOS_ESTIMADOS, ST_DISPONIVEL, ST_AVISADO) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                long idAlerta = proximoId(conn);
                try (PreparedStatement stmt = conn.prepareStatement(sqlAlerta)) {
                    stmt.setLong(1, idAlerta);
                    stmt.setLong(2, alerta.getIdUsuaria());
                    stmt.setInt(3, alerta.getIdBairro());
                    stmt.setInt(4, alerta.getIdPeriodo());
                    stmt.setTimestamp(5, Timestamp.valueOf(alerta.getDataHora()));
                    stmt.setString(6, alerta.getOrigemLocal());
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement(sqlContato)) {
                    for (AlertaContato ac : alerta.getContatosAvisados()) {
                        stmt.setLong(1, idAlerta);
                        stmt.setLong(2, ac.getIdContato());
                        stmt.setInt(3, ac.getOrdem());
                        stmt.setInt(4, ac.getMinutosEstimados());
                        stmt.setString(5, ac.isDisponivel() ? "S" : "N");
                        stmt.setString(6, ac.isAvisado() ? "S" : "N");
                        stmt.executeUpdate();
                    }
                }
                conn.commit();
                alerta.setIdAlerta(idAlerta);
                return alerta;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao registrar alerta.", e);
        }
    }

    @Override
    public Optional<Alerta> buscarPorId(Long idAlerta) {
        String sql = SELECT_BASE + " WHERE ID_ALERTA = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idAlerta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Alerta alerta = mapear(rs);
                alerta.setContatosAvisados(alertaContatoDao.listarPorAlerta(idAlerta));
                return Optional.of(alerta);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar alerta " + idAlerta + ".", e);
        }
    }

    @Override
    public List<Alerta> listarPorUsuaria(Long idUsuaria) {
        String sql = SELECT_BASE + " WHERE ID_USUARIA = ? ORDER BY DT_HR_ALERTA DESC";
        List<Alerta> alertas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    alertas.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar alertas da usuaria " + idUsuaria + ".", e);
        }
        for (Alerta alerta : alertas) {
            alerta.setContatosAvisados(alertaContatoDao.listarPorAlerta(alerta.getIdAlerta()));
        }
        return alertas;
    }

    @Override
    public void remover(Long idAlerta) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt =
                             conn.prepareStatement("DELETE FROM T_WS_ALERTA_CONTATO WHERE ID_ALERTA = ?")) {
                    stmt.setLong(1, idAlerta);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM T_WS_ALERTA WHERE ID_ALERTA = ?")) {
                    stmt.setLong(1, idAlerta);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover alerta " + idAlerta + ".", e);
        }
    }

    private long proximoId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SQ_WS_ALERTA.NEXTVAL FROM DUAL")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private Alerta mapear(ResultSet rs) throws SQLException {
        Alerta alerta = new Alerta();
        alerta.setIdAlerta(rs.getLong("ID_ALERTA"));
        alerta.setIdUsuaria(rs.getLong("ID_USUARIA"));
        alerta.setIdBairro(rs.getInt("ID_BAIRRO"));
        alerta.setIdPeriodo(rs.getInt("ID_PERIODO"));
        alerta.setDataHora(rs.getTimestamp("DT_HR_ALERTA").toLocalDateTime());
        alerta.setOrigemLocal(rs.getString("ST_ORIGEM_LOCAL"));
        return alerta;
    }
}
