package banking;

public class Main {

    public static void main(String[] args) {


        SQLDatabaseCreatorApp database = new SQLDatabaseCreatorApp();
        database.createNewDatabase();

        SQLInsertApp insertApp = new SQLInsertApp();
//        insertApp.updateId();

        Bank bank = new Bank();
        bank.menu();

    }
}