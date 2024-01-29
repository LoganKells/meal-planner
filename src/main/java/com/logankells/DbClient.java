package com.logankells;


import java.sql.*;

public class DbClient {

    private String url;
    private String user;
    private String pass;
    private Connection connection;

    DbClient() {

    }

    void setUrl(String url) {
        this.url = url;
    }

    void setUser(String user) {
        this.user = user;
    }

    void setPass(String pass) {
        this.pass = pass;
    }

    Connection getConnection() throws SQLException {
        this.connection = DriverManager.getConnection(url, user, pass);
        return this.connection;
    }

    void executeUpdateRawSql(String sql) {
        try (
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stmnt = con.createStatement();
             ) {
            stmnt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ResultSet executeQueryRawSql(String sql) {
        try (
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stmnt = con.createStatement();
        ) {
            return stmnt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
