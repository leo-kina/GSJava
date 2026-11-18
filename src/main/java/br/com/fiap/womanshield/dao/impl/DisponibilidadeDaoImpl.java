package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.DisponibilidadeDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.Disponibilidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DisponibilidadeDaoImpl implements DisponibilidadeDao {

    @Override
    public List<Disponibilidade> listarPorContato(Long idContato) {
        String sql = "SELECT ID_DISPONIBILIDADE, ID_CONTATO, NR_DIA_SEMANA, HR_INICIO, HR_FIM "
                + "FROM T_WS_DISPONIB_CONTATO WHERE ID_CONTATO = ? ORDER BY NR_DIA_SEMANA, HR_INICIO";
        List<Disponibilidade> disponibilidades = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idContato);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disponibilidades.add(mapear(rs));
                }
            }
            return disponibilidades;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar disponibilidade do contato " + idContato + ".", e);
        }
    }

    private Disponibilidade mapear(ResultSet rs) throws SQLException {
        Disponibilidade disponibilidade = new Disponibilidade();
        disponibilidade.setIdDisponibilidade(rs.getLong("ID_DISPONIBILIDADE"));
        disponibilidade.setIdContato(rs.getLong("ID_CONTATO"));
        disponibilidade.setDiaSemana(rs.getInt("NR_DIA_SEMANA"));
        disponibilidade.setHoraInicio(rs.getInt("HR_INICIO"));
        disponibilidade.setHoraFim(rs.getInt("HR_FIM"));
        return disponibilidade;
    }
}
