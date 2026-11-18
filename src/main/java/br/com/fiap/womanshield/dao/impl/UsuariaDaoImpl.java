package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.UsuariaDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.Usuaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariaDaoImpl implements UsuariaDao {

    private static final String SELECT_BASE =
            "SELECT ID_USUARIA, NM_APELIDO, NR_TELEFONE, DT_CADASTRO, ST_ATIVA FROM T_WS_USUARIA";

    @Override
    public List<Usuaria> listarTodas() {
        String sql = SELECT_BASE + " ORDER BY ID_USUARIA";
        List<Usuaria> usuarias = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarias.add(mapear(rs));
            }
            return usuarias;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar usuarias.", e);
        }
    }

    @Override
    public Optional<Usuaria> buscarPorId(Long idUsuaria) {
        String sql = SELECT_BASE + " WHERE ID_USUARIA = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public boolean existeTelefone(String telefone) {
        String sql = "SELECT COUNT(*) FROM T_WS_USUARIA WHERE NR_TELEFONE = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telefone);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao conferir telefone da usuaria.", e);
        }
    }

    @Override
    public Usuaria inserir(Usuaria usuaria) {
        String sql = "INSERT INTO T_WS_USUARIA (ID_USUARIA, NM_APELIDO, NR_TELEFONE, DT_CADASTRO, ST_ATIVA) "
                + "VALUES (?, ?, ?, ?, 'S')";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            long id = proximoId(conn);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setLong(1, id);
                stmt.setString(2, usuaria.getApelido());
                stmt.setString(3, usuaria.getTelefone());
                stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
                stmt.executeUpdate();
            }
            usuaria.setIdUsuaria(id);
            usuaria.setDataCadastro(LocalDate.now());
            usuaria.setAtiva(true);
            return usuaria;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cadastrar usuaria.", e);
        }
    }

    @Override
    public void atualizar(Usuaria usuaria) {
        String sql = "UPDATE T_WS_USUARIA SET NM_APELIDO = ?, NR_TELEFONE = ? WHERE ID_USUARIA = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuaria.getApelido());
            stmt.setString(2, usuaria.getTelefone());
            stmt.setLong(3, usuaria.getIdUsuaria());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar usuaria " + usuaria.getIdUsuaria() + ".", e);
        }
    }

    @Override
    public void desativar(Long idUsuaria) {
        String sql = "UPDATE T_WS_USUARIA SET ST_ATIVA = 'N' WHERE ID_USUARIA = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao desativar usuaria " + idUsuaria + ".", e);
        }
    }

    private long proximoId(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT SQ_WS_USUARIA.NEXTVAL FROM DUAL")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private Usuaria mapear(ResultSet rs) throws SQLException {
        Usuaria usuaria = new Usuaria();
        usuaria.setIdUsuaria(rs.getLong("ID_USUARIA"));
        usuaria.setApelido(rs.getString("NM_APELIDO"));
        usuaria.setTelefone(rs.getString("NR_TELEFONE"));
        usuaria.setDataCadastro(rs.getDate("DT_CADASTRO").toLocalDate());
        usuaria.setAtiva("S".equals(rs.getString("ST_ATIVA")));
        return usuaria;
    }
}
