package com.database;

import java.sql.*;

public class PostgreClient {
    public Connection connection;

    public PostgreClient() throws SQLException {
        this.connection = this.connect();
    }

    private Connection connect() throws SQLException {
        Connection c = null;
        try {

            System.out.println("Opened main.database successfully");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "postgres");

            return c;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
            throw e;
        }
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public ResultSet runSqlToSelect(String query) throws SQLException {
        Statement stmt = this.connection.createStatement();

        return stmt.executeQuery(query);
    }

    public void runSql(String query) throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate(query);
    }

}

