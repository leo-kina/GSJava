package br.com.fiap.womanshield.factory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton responsavel por abrir conexoes com o Oracle da FIAP.
 *
 * Credenciais: copie db.properties.example para db.properties (mesma pasta) e
 * preencha os 3 valores - esse arquivo fica fora do Git (ver .gitignore) para
 * nao vazar senha no repositorio. Em producao (Render), prefira configurar as
 * variaveis de ambiente WS_DB_URL / WS_DB_USER / WS_DB_PASSWORD, que sempre
 * tem prioridade sobre o db.properties.
 */
public final class ConnectionFactory {

    private static ConnectionFactory instance;

    private final String url;
    private final String user;
    private final String password;

    private ConnectionFactory() {
        Properties props = new Properties();
        try (InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel ler db.properties.", e);
        }

        this.url = overrideComEnv(props.getProperty("db.url"), "WS_DB_URL");
        this.user = overrideComEnv(props.getProperty("db.user"), "WS_DB_USER");
        this.password = overrideComEnv(props.getProperty("db.password"), "WS_DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Credenciais do Oracle nao configuradas: copie db.properties.example para db.properties "
                            + "e preencha, ou defina WS_DB_URL/WS_DB_USER/WS_DB_PASSWORD.");
        }
    }

    private String overrideComEnv(String valorPadrao, String nomeVariavel) {
        String valorEnv = System.getenv(nomeVariavel);
        return (valorEnv == null || valorEnv.isBlank()) ? valorPadrao : valorEnv;
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
