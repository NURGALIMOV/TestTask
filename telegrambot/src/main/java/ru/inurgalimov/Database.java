package ru.inurgalimov;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private Connection connect;
    private Statement stmt;
    private ResultSet rs;
    private String sql;

    public void openDBFile(String dbName) { // open database
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection(dbName);
            stmt = connect.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable(String sqlCreateTable) { // create table
        try {
            stmt.executeUpdate(sqlCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(String surname, String name, String data) { // add record
        try {
            stmt.execute("INSERT INTO " + "users" + " (surname, name, data) "
                    + "VALUES ('" + surname + "', '" + name + "', '" + data + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String surname, String data) { // update record/passwd by login
        try {
            stmt.executeUpdate("UPDATE " + "users" + " set DATA ='" + data
                    + "' where SURNAME='" + surname + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String surname) { // delete record by login
        try {
            stmt.executeUpdate("DELETE from " + "users"
                    + " where SURNAME='" + surname + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> list() { // list all records
        List<String> list = new ArrayList<>();
        try {
            rs = stmt.executeQuery("SELECT * FROM " + "users" + ";");
            while (rs.next()) {
                list.add(rs.getString("surname") + "\t\t"
                        + rs.getString("name") + "\t\t" + rs.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
