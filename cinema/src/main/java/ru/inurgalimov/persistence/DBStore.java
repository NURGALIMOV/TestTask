package ru.inurgalimov.persistence;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Хранилище забронированных мест.
 */
public class DBStore implements IStorage<Place> {

    /**
     * Логгер.
     */
    private static final Logger LOGGER = LogManager.getLogger(ru.inurgalimov.persistence.DBStore.class.getName());

    /**
     * Пул коннектов.
     */
    private static final BasicDataSource SOURCE = new BasicDataSource();

    /** Единственный экземпляр класса. */
    private static final DBStore INSTANCE = new DBStore();

    /**
     * Конструктор.
     */
    private DBStore() {
        init();
    }

    /**
     * Инициализация.
     */
    private void init() {
        try (InputStream in = ru.inurgalimov.persistence.DBStore.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties config = new Properties();
            config.load(in);
            SOURCE.setDriverClassName(config.getProperty("driver-class-name"));
            SOURCE.setUrl(config.getProperty("url"));
            SOURCE.setUsername(config.getProperty("username"));
            SOURCE.setPassword(config.getProperty("password"));
            SOURCE.setMinIdle(5);
            SOURCE.setMaxIdle(10);
            SOURCE.setMaxOpenPreparedStatements(100);
        } catch (Exception e) {
            LOGGER.error("Ошибка инициализации.", e);
        }
    }

    /**
     * Возвращает единственный экземпляр класса.
     *
     * @return экземпляр класса.
     */
    public static DBStore getInstance() {
        return INSTANCE;
    }

    public Place add(Place place) {
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "INSERT INTO halls (hallid, row, place, state, accountid) VALUES (?, ?, ?, ?, ?)")) {
            pst.setInt(1, place.getHallId());
            pst.setInt(2, place.getRow());
            pst.setInt(3, place.getPlaceNum());
            pst.setBoolean(4, place.isBooked());
            pst.setLong(5, place.getUser().getPhoneNumber());
            pst.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Ошибка записи данных в БД.", e);
        }
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "INSERT INTO accounts (id, surname, name, middleName) VALUES (?, ?, ?, ?)")) {
            User user = place.getUser();
            pst.setLong(1, user.getPhoneNumber());
            pst.setString(2, user.getSurname());
            pst.setString(3, user.getName());
            pst.setString(4, user.getMiddleName());
            pst.executeUpdate();
        } catch (Exception e) {
            LOGGER.error("Ошибка записи данных в БД.", e);
        }
        return place;
    }

    public boolean update(Place place) {
        boolean result = false;
        Place old = findById(place);
        if (old != null) {
            try (Connection connection = SOURCE.getConnection();
                 PreparedStatement pst = connection.prepareStatement("UPDATE halls AS i "
                         + "SET state = ?, account = ? "
                         + "WHERE i.hallid = ? AND i.row = ? AND i.place = ?")) {
                pst.setBoolean(1, place.isBooked());
                pst.setLong(2, place.getUser().getPhoneNumber());
                pst.setInt(3, place.getHallId());
                pst.setInt(4, place.getRow());
                pst.setInt(5, place.getPlaceNum());
                pst.executeUpdate();
                result = true;
            } catch (Exception e) {
                LOGGER.error("Ошибка при обновлении данных пользователя.", e);
            }
        }
        return result;
    }

    public boolean delete(Place place) {
        boolean result = true;
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "DELETE FROM halls AS i WHERE i.hallId = ? AND i.row = ? AND i.place = ?")) {
            pst.setInt(1, place.getHallId());
            pst.setInt(2, place.getRow());
            pst.setInt(3, place.getPlaceNum());
            pst.executeUpdate();
            result = findById(place) == null;
        } catch (Exception e) {
            LOGGER.error("Ошибка удаления данных из БД.", e);
        }
        return result;
    }

    public Collection<Place> findAll() {
        Map<Long, User> users = getUsers();
        List<Place> result = new ArrayList<>();
        try (Connection connection = SOURCE.getConnection();
             Statement st = connection.createStatement()) {
            ResultSet rst = st.executeQuery("SELECT * FROM halls");
            while (rst.next()) {
                result.add(new Place(
                        rst.getInt("hallId"),
                        rst.getInt("row"),
                        rst.getInt("place"),
                        rst.getBoolean("state"),
                        users.get(rst.getLong("accountid")))
                );
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при получении списка забронированных мест.", e);
        }
        return result;
    }

    public Place findById(Place place) {
        Place result = null;
        try (Connection connection = SOURCE.getConnection();
             PreparedStatement pst = connection.prepareStatement(
                     "SELECT * FROM halls AS i WHERE i.hallid = ? AND i.row = ? AND i.place = ?")) {
            pst.setInt(1, place.getHallId());
            pst.setInt(2, place.getRow());
            pst.setInt(3, place.getPlaceNum());
            ResultSet rst = pst.executeQuery();
            while (rst.next()) {
                result = new Place(
                        rst.getInt("hallid"),
                        rst.getInt("row"),
                        rst.getInt("place"),
                        rst.getBoolean("state"),
                        getUsers().get(rst.getLong("accountid"))
                );
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при поиске забронированного места.", e);
        }
        return result;
    }

    private Map<Long, User> getUsers() {
        Map<Long, User> result = new HashMap<>();
        try (Connection connection = SOURCE.getConnection();
             Statement st = connection.createStatement()) {
            ResultSet rst = st.executeQuery("SELECT * FROM accounts");
            while (rst.next()) {
                User user = new User(
                        rst.getLong("id"),
                        rst.getString("surname"),
                        rst.getString("name"),
                        rst.getString("middleName")
                );
                result.put(user.getPhoneNumber(), user);
            }
        } catch (Exception e) {
            LOGGER.error("Ошибка при получении списка пользователей.", e);
        }
        return result;
    }
}
