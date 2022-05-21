import entities.Account;
import entities.Category;
import entities.Transaction;

import java.sql.SQLException;

public class Create {


    public Create() throws SQLException, ClassNotFoundException {
    }

    PostgreClient postgresql = new PostgreClient();
    DbValidate dbValidate = new DbValidate();

    public void createAccount(String username, String password) throws SQLException {
        Account account = new Account(username, password);
        System.out.println("Account Created your id is:" + account.getId());
        postgresql.InsertDbAccount(account);

    }

    public void createIncome(String description, String category_name, String account_id, double value) throws SQLException {
        if(dbValidate.accountIdValidate(account_id, postgresql)){
            Account account = dbValidate.createAccountValidate(account_id, postgresql);

            Category category = new Category(category_name, account);

            if (!dbValidate.categoryNameValidate(category_name, account_id, postgresql)){
                postgresql.InsertDbCategory(category);
            }

            Transaction transaction = new Transaction(account, category, description, value);
            postgresql.InsertDbTransaction(transaction);
        }
    }


}
