import entities.Account;
import entities.Category;
import entities.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Account a = new Account("Torugo", "12345");
        System.out.println(a.toString());

        Category c = new Category("Comida", a);
        System.out.println(c.toString());

        Transaction t = new Transaction(a,c, "Presente da minha tia", -15.90);
        System.out.print(t.toString());


    }
}
