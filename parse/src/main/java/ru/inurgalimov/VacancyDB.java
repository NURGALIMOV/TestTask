package ru.inurgalimov;

import java.sql.*;

/**
 * В базе должна быть таблица vacancy (id, name, text, link)
 * id - первичный ключ
 * name - имя вакансии
 * text - текст вакансии
 * link - текст, ссылка на вакансию
 *
 * @author Ilshat Nurgalimov
 * @version 06.05.2019
 */
public class VacancyDB implements AutoCloseable {
    private Connection connection;
    private final Config config;

    public VacancyDB(Config config) {
        this.config = config;
    }

    public boolean init() throws Exception {
        Class.forName(config.get("jdbc.driver"));
        this.connection = DriverManager.getConnection(
                config.get("jdbc.url"),
                config.get("jdbc.username"),
                config.get("jdbc.password")
        );
        return this.connection != null;
    }

    public void add(Vacancy v) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(
                "INSERT INTO vacancy (name, text, link, date)"
                        + "VALUES (?, ?, ?, ?)")) {
            pst.setString(1, v.getName());
            pst.setString(2, v.getText());
            pst.setString(3, v.getLink());
            pst.setTimestamp(4, Timestamp.valueOf(v.getDate()));
            pst.executeUpdate();
        }
    }

    public boolean initTable() throws SQLException {
        boolean state = false;
        try (Statement statement = connection.createStatement()) {
            state = statement.execute("CREATE TABLE IF NOT EXISTS vacancy("
                    + "id SERIAL PRIMARY KEY,name VARCHAR(2000) UNIQUE,"
                    + "text TEXT,"
                    + "link TEXT, "
                    + "date TIMESTAMP"
                    + ");"
            );
        }
        return state;
    }

    public boolean duplicateСhecking(Vacancy v) throws SQLException {
        int count = 0;
        try (PreparedStatement pst = connection.prepareStatement(
                "SELECT COUNT(id) FROM vacancy AS v WHERE v.name = ?")) {
            pst.setString(1, v.getName());
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                count = rst.getInt(1);
            }
        }
        return count != 0;
    }

    public boolean isEmpty() throws SQLException {
        int count = 0;
        try (Statement st = connection.createStatement()) {
            ResultSet rst = st.executeQuery("SELECT COUNT(id) FROM vacancy;");
            while (rst.next()) {
                count = rst.getInt(1);
            }
        }
        return count == 0;
    }

    public String getLastDate() throws SQLException {
        String result = null;
        try (Statement st = connection.createStatement()) {
            ResultSet rst = st.executeQuery("SELECT MAX(v.date) FROM vacancy AS v;");
            while (rst.next()) {
                result = rst.getString(1);
            }
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
