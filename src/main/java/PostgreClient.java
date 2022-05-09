import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void runSql(String query) throws SQLException {
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate(query);
    }
}