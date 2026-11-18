package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.AlertaContatoDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.AlertaContato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertaContatoDaoImpl implements AlertaContatoDao {

    @Override
    public List<AlertaContato> listarPorAlerta(Long idAlerta) {
        String sql = "SELECT ac.ID_ALERTA, ac.ID_CONTATO, ac.NR_ORDEM, ac.NR_MINUTOS_ESTIMADOS, "
                + "ac.ST_DISPONIVEL, ac.ST_AVISADO, c.NM_CONTATO, c.NR_TELEFONE "
                + "FROM T_WS_ALERTA_CONTATO ac JOIN T_WS_CONTATO_CONFIANCA c ON c.ID_CONTATO = ac.ID_CONTATO "
                + "WHERE ac.ID_ALERTA = ? ORDER BY ac.NR_ORDEM";
        List<AlertaContato> contatos = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idAlerta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contatos.add(mapear(rs));
                }
            }
            return contatos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar contatos avisados do alerta " + idAlerta + ".", e);
        }
    }

    private AlertaContato mapear(ResultSet rs) throws SQLException {
        AlertaContato ac = new AlertaContato();
        ac.setIdAlerta(rs.getLong("ID_ALERTA"));
        ac.setIdContato(rs.getLong("ID_CONTATO"));
        ac.setOrdem(rs.getInt("NR_ORDEM"));
        ac.setMinutosEstimados(rs.getInt("NR_MINUTOS_ESTIMADOS"));
        ac.setDisponivel("S".equals(rs.getString("ST_DISPONIVEL")));
        ac.setAvisado("S".equals(rs.getString("ST_AVISADO")));
        ac.setNomeContato(rs.getString("NM_CONTATO"));
        ac.setTelefoneContato(rs.getString("NR_TELEFONE"));
        return ac;
    }
}
