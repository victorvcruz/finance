import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        PostgreClient postgresql = new PostgreClient();
        Create create = new Create();

//        create.createAccount("Ysamim", "0014");
        create.createIncome("Salário: Segunda quinzena", "Salário", "8dce47ec-c3d1-44be-853f-bc1125f2c315", 1100);



    }
}
