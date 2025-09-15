package database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectDB {
    private static final Properties props = new Properties();
    private static Connection testConn;

    static {
        try (InputStream input = ConnectDB.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find db.properties in resources");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    public static Connection getConnection() throws Exception {
        if (testConn != null) {
            return testConn;
        }
        String url = "jdbc:mariadb://" + props.getProperty("DB_HOST") + ":" +
                props.getProperty("DB_PORT") + "/" + props.getProperty("DB_NAME");
        String user = props.getProperty("DB_USER");
        String password = props.getProperty("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }

    public static void settestConn(Connection con) {
        testConn = con;
    }
    public static Connection gettestConn() {
        return testConn;
    }
}
