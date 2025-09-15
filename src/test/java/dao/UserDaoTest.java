package dao;

import database.ConnectDB;
import models.User;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private static Connection connection;
    private UserDao userDao;

    @BeforeAll
    static void setupDatabase() throws Exception {
        connection = DriverManager.getConnection ("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL");
        ConnectDB.settestConn(connection);

        try (Statement statement = connection.createStatement()) {
            statement.execute ("CREATE TABLE user_accounts (" +
                    "user_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "user_name VARCHAR(20) NOT NULL," +
                    "user_password VARCHAR(255) NOT NULL," +
                    "role VARCHAR(50) NOT NULL)");
        }
    }
    @AfterAll
    static void tearDown() throws Exception {
        connection.close();
    }
    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @AfterEach
    void cleanUp() throws Exception {
        try(Statement statement = connection.createStatement()) {
            statement.execute ("DELETE FROM user_accounts");
        }
    }

    @Test
    void testCreateUser() throws Exception {
        boolean created = userDao.createUser("test1", "test1");
        assertTrue(created);

        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS count FROM user_accounts WHERE user_name='test1'")) {
            assertTrue(resultSet.next());
            assertEquals(1, resultSet.getInt("count"));
        }
    }

    @Test
    void testFindByUsername() throws Exception {
        try (Statement statement = connection.createStatement()) {
            statement.execute ("INSERT INTO user_accounts (user_name, user_password, role) VALUES ('jesse', 'secret', 'student')");
        }

        User user = userDao.findByUsername("jesse");
        assertNotNull(user);

        User nonexistent = userDao.findByUsername("nonexistent");
        assertNull(nonexistent);
    }
}