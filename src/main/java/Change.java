import repository_entities.AccountRepository;
import repository_entities.CategoryRepository;
import repository_entities.TransactionRepository;

import java.sql.SQLException;

public class Change {
    public AccountRepository account_repository;
    public CategoryRepository category_repository;
    public TransactionRepository transaction_repository;

    public Change(AccountRepository account_repository, CategoryRepository category_repository, TransactionRepository transaction_repository) throws SQLException, ClassNotFoundException {
        this.account_repository = account_repository;
        this.category_repository = category_repository;
        this.transaction_repository = transaction_repository;
    }

    public void cancelTransaction(String transaction_id, String bool) throws SQLException {
        transaction_repository.updateCancelTransaction(transaction_id, bool);
    }

    public void changeTransaction(String transaction_id, String description, String date, double value) throws SQLException {
        transaction_repository.updateAttributesTransaction(transaction_id, description, date, value);
    }

    public void changeAccount(String account_id, String username, String password) throws SQLException {
        account_repository.updateAttributesAccount(account_id, username, password);
    }
}
