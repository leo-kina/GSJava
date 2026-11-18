package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.BairroDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.Bairro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BairroDaoImpl implements BairroDao {

    private static final String SELECT_BASE =
            "SELECT ID_BAIRRO, ID_MUNICIPIO, NM_BAIRRO, NR_LATITUDE, NR_LONGITUDE, NR_INDICE_RISCO, QT_OCORR_PREVISTA "
                    + "FROM T_WS_BAIRRO";

    @Override
    public List<Bairro> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY NM_BAIRRO";
        List<Bairro> bairros = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bairros.add(mapear(rs));
            }
            return bairros;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar bairros.", e);
        }
    }

    @Override
    public Optional<Bairro> buscarPorId(Integer idBairro) {
        String sql = SELECT_BASE + " WHERE ID_BAIRRO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBairro);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar bairro " + idBairro + ".", e);
        }
    }

    private Bairro mapear(ResultSet rs) throws SQLException {
        Bairro bairro = new Bairro();
        bairro.setIdBairro(rs.getInt("ID_BAIRRO"));
        bairro.setIdMunicipio(rs.getInt("ID_MUNICIPIO"));
        bairro.setNomeBairro(rs.getString("NM_BAIRRO"));
        bairro.setLatitude(rs.getBigDecimal("NR_LATITUDE"));
        bairro.setLongitude(rs.getBigDecimal("NR_LONGITUDE"));
        bairro.setIndiceRisco(rs.getInt("NR_INDICE_RISCO"));
        bairro.setOcorrenciaPrevista(rs.getBigDecimal("QT_OCORR_PREVISTA"));
        return bairro;
    }
}
