import entities.Account;
import entities.Category;
import entities.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class PostgreClient {
    public Connection connection;

    public PostgreClient() throws SQLException, ClassNotFoundException {
        this.connection = this.connect();
    }

    private Connection connect() throws SQLException, ClassNotFoundException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Opened database successfully");
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


    public void InsertDbAccount(Account account) throws SQLException {
        String sql = "INSERT INTO account(id, username, password, created_at, updated_at) "
                + "VALUES('" + account.getId() + "', '" + account.getUsername() + "', '" + account.getPassword() +
                "', '" + account.getCreated_at() + "', '" + account.getUpdate_at() + "');";

        this.runSql(sql);
    }

    public void InsertDbTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction(id, account_id, category_id, description, value, canceled, created_at, updated_at) "
                + "VALUES('" + transaction.getId() + "', '" + transaction.getAccount_id() + "', '" + transaction.getCategory_id() +
                "', '" + transaction.getDescription() + "', '" + transaction.getValue() + "', '" + transaction.isCanceled() + "', '" + transaction.getCreated_at() + "', '" + transaction.getUpdated_at() + "');";

        this.runSql(sql);

    }

    public void InsertDbCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category(id, name, account_id, created_at, updated_at) "
                + "VALUES('" + category.getId() + "', '" + category.getName() + "', '" + category.getAccount_id() +
                "', '" + category.getCreated_at() + "', '" + category.getUpdated_at() + "');";

        this.runSql(sql);
    }

    public ResultSet SelectAccountId(String account_id) throws SQLException {
        String sql = "SELECT id, username, password, created_at, updated_at " +
                "FROM account " +
                "WHERE id = '" + account_id + "'";

        return runSqlToSelect(sql);
    }

    public ResultSet selectCategoryName(String category_name, String account_id) throws SQLException {
        String sql = "SELECT id, name, account_id, created_at, updated_at " +
                "FROM category " +
                "WHERE name = '" + category_name + "' " +
                "AND account_id = '" + account_id + "'";

        return runSqlToSelect(sql);
    }

    public double getAccountBalance(String account_id) throws SQLException {
        String sql = "SELECT " +
                "SUM(value) " +
                "FROM transaction " +
                "WHERE account_id = '" + account_id + "' " +
                "AND canceled = 'false'";
        ResultSet result = runSqlToSelect(sql);
        result.next();
        return result.getDouble("sum");
        }
}

