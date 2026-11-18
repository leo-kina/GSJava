package br.com.fiap.womanshield.dao.impl;

import br.com.fiap.womanshield.dao.ContatoConfiancaDao;
import br.com.fiap.womanshield.exception.PersistenciaException;
import br.com.fiap.womanshield.factory.ConnectionFactory;
import br.com.fiap.womanshield.model.ContatoConfianca;
import br.com.fiap.womanshield.model.Disponibilidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

public class ContatoConfiancaDaoImpl implements ContatoConfiancaDao {

    private static final String SELECT_BASE =
            "SELECT ID_CONTATO, ID_USUARIA, ID_BAIRRO, NM_CONTATO, NR_TELEFONE, ST_PRINCIPAL "
                    + "FROM T_WS_CONTATO_CONFIANCA";

    @Override
    public List<ContatoConfianca> listarPorUsuaria(Long idUsuaria) {
        String sql = SELECT_BASE
                + " WHERE ID_USUARIA = ? ORDER BY CASE WHEN ST_PRINCIPAL = 'S' THEN 0 ELSE 1 END, NM_CONTATO";
        java.util.List<ContatoConfianca> contatos = new java.util.ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contatos.add(mapear(rs));
                }
            }
            return contatos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar contatos da usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public Optional<ContatoConfianca> buscarPorId(Long idContato) {
        String sql = SELECT_BASE + " WHERE ID_CONTATO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idContato);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar contato " + idContato + ".", e);
        }
    }

    @Override
    public int contarPorUsuaria(Long idUsuaria) {
        String sql = "SELECT COUNT(*) FROM T_WS_CONTATO_CONFIANCA WHERE ID_USUARIA = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar contatos da usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public boolean existeTelefoneNaUsuaria(Long idUsuaria, String telefone, Long idContatoParaIgnorar) {
        String sql = "SELECT COUNT(*) FROM T_WS_CONTATO_CONFIANCA WHERE ID_USUARIA = ? AND NR_TELEFONE = ? "
                + "AND ID_CONTATO <> ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            stmt.setString(2, telefone);
            stmt.setLong(3, idContatoParaIgnorar == null ? -1L : idContatoParaIgnorar);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao conferir telefone do contato.", e);
        }
    }

    @Override
    public Optional<ContatoConfianca> buscarPrincipal(Long idUsuaria) {
        String sql = SELECT_BASE + " WHERE ID_USUARIA = ? AND ST_PRINCIPAL = 'S'";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar contato principal da usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public Optional<ContatoConfianca> buscarOutroContato(Long idUsuaria, Long idContatoParaIgnorar) {
        String sql = SELECT_BASE + " WHERE ID_USUARIA = ? AND ID_CONTATO <> ? ORDER BY ID_CONTATO "
                + "FETCH FIRST 1 ROWS ONLY";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            stmt.setLong(2, idContatoParaIgnorar);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar outro contato da usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public ContatoConfianca inserir(ContatoConfianca contato) {
        String sqlContato = "INSERT INTO T_WS_CONTATO_CONFIANCA "
                + "(ID_CONTATO, ID_USUARIA, ID_BAIRRO, NM_CONTATO, NR_TELEFONE, ST_PRINCIPAL) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                long idContato = proximoId(conn, "SQ_WS_CONTATO");
                try (PreparedStatement stmt = conn.prepareStatement(sqlContato)) {
                    stmt.setLong(1, idContato);
                    stmt.setLong(2, contato.getIdUsuaria());
                    stmt.setInt(3, contato.getIdBairro());
                    stmt.setString(4, contato.getNome());
                    stmt.setString(5, contato.getTelefone());
                    stmt.setString(6, contato.isPrincipal() ? "S" : "N");
                    stmt.executeUpdate();
                }
                contato.setIdContato(idContato);
                inserirDisponibilidades(conn, idContato, contato.getDisponibilidades());
                conn.commit();
                return contato;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao cadastrar contato de confianca.", e);
        }
    }

    @Override
    public void atualizar(ContatoConfianca contato) {
        String sqlContato = "UPDATE T_WS_CONTATO_CONFIANCA SET ID_BAIRRO = ?, NM_CONTATO = ?, NR_TELEFONE = ? "
                + "WHERE ID_CONTATO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement(sqlContato)) {
                    stmt.setInt(1, contato.getIdBairro());
                    stmt.setString(2, contato.getNome());
                    stmt.setString(3, contato.getTelefone());
                    stmt.setLong(4, contato.getIdContato());
                    stmt.executeUpdate();
                }
                if (contato.getDisponibilidades() != null && !contato.getDisponibilidades().isEmpty()) {
                    removerDisponibilidades(conn, contato.getIdContato());
                    inserirDisponibilidades(conn, contato.getIdContato(), contato.getDisponibilidades());
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar contato " + contato.getIdContato() + ".", e);
        }
    }

    @Override
    public void marcarPrincipal(Long idContato, boolean principal) {
        String sql = "UPDATE T_WS_CONTATO_CONFIANCA SET ST_PRINCIPAL = ? WHERE ID_CONTATO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, principal ? "S" : "N");
            stmt.setLong(2, idContato);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao marcar contato principal.", e);
        }
    }

    @Override
    public void desmarcarPrincipalDaUsuaria(Long idUsuaria) {
        String sql = "UPDATE T_WS_CONTATO_CONFIANCA SET ST_PRINCIPAL = 'N' WHERE ID_USUARIA = ? AND ST_PRINCIPAL = 'S'";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuaria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao desmarcar contato principal da usuaria " + idUsuaria + ".", e);
        }
    }

    @Override
    public void remover(Long idContato) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            conn.setAutoCommit(false);
            try {
                removerDisponibilidades(conn, idContato);
                try (PreparedStatement stmt =
                             conn.prepareStatement("DELETE FROM T_WS_CONTATO_CONFIANCA WHERE ID_CONTATO = ?")) {
                    stmt.setLong(1, idContato);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao remover contato " + idContato + ".", e);
        }
    }

    @Override
    public boolean possuiHistoricoDeAlertas(Long idContato) {
        String sql = "SELECT COUNT(*) FROM T_WS_ALERTA_CONTATO WHERE ID_CONTATO = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idContato);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao conferir historico de alertas do contato " + idContato + ".", e);
        }
    }

    private void inserirDisponibilidades(Connection conn, long idContato, List<Disponibilidade> disponibilidades)
            throws SQLException {
        String sql = "INSERT INTO T_WS_DISPONIB_CONTATO (ID_DISPONIBILIDADE, ID_CONTATO, NR_DIA_SEMANA, HR_INICIO, HR_FIM) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Disponibilidade disponibilidade : disponibilidades) {
                stmt.setLong(1, proximoId(conn, "SQ_WS_DISPONIB"));
                stmt.setLong(2, idContato);
                stmt.setInt(3, disponibilidade.getDiaSemana());
                stmt.setInt(4, disponibilidade.getHoraInicio());
                stmt.setInt(5, disponibilidade.getHoraFim());
                stmt.executeUpdate();
            }
        }
    }

    private void removerDisponibilidades(Connection conn, long idContato) throws SQLException {
        try (PreparedStatement stmt =
                     conn.prepareStatement("DELETE FROM T_WS_DISPONIB_CONTATO WHERE ID_CONTATO = ?")) {
            stmt.setLong(1, idContato);
            stmt.executeUpdate();
        }
    }

    private long proximoId(Connection conn, String sequenceName) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT " + sequenceName + ".NEXTVAL FROM DUAL")) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private ContatoConfianca mapear(ResultSet rs) throws SQLException {
        ContatoConfianca contato = new ContatoConfianca();
        contato.setIdContato(rs.getLong("ID_CONTATO"));
        contato.setIdUsuaria(rs.getLong("ID_USUARIA"));
        contato.setIdBairro(rs.getInt("ID_BAIRRO"));
        contato.setNome(rs.getString("NM_CONTATO"));
        contato.setTelefone(rs.getString("NR_TELEFONE"));
        contato.setPrincipal("S".equals(rs.getString("ST_PRINCIPAL")));
        return contato;
    }
}
