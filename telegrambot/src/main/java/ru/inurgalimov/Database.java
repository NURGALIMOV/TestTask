package ru.inurgalimov;

import java.util.*;
import java.sql.*;


public class Database {
    Connection connect;
    Statement stmt;
    ResultSet rs;
    String sql;

    void openDBFile(String dbName) { // open database
        try {
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection(dbName);
            stmt = connect.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createTable(String sqlCreateTable) { // create table
        try {
            stmt.executeUpdate(sqlCreateTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void add(String login, String passwd) { // add record
        try {
            stmt.executeUpdate("INSERT INTO " + "users" +
                    " (login, passwd) " +
                    "VALUES ('" + login + "', '" + passwd + "');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update(String login, String passwd) { // update record/passwd by login
        try {
            stmt.executeUpdate("UPDATE " + "users" +
                    " set PASSWD='" + passwd +
                    "' where LOGIN='" + login + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void delete(String login) { // delete record by login
        try {
            stmt.executeUpdate("DELETE from " + "users" +
                    " where LOGIN='" + login + "';");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void list() { // list all records
        try {
            System.out.println("LOGIN\tPASSWD");
            rs = stmt.executeQuery("SELECT * FROM " + "users" + ";");
            while (rs.next())
                System.out.println(rs.getString("login") + "\t\t" +
                        rs.getString("passwd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
