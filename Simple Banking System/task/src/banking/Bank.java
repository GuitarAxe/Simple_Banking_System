package banking;

import java.util.*;

public class Bank {

    private final SQLInsertApp insertApp = new SQLInsertApp();
    private final SQLSelectApp selectApp = new SQLSelectApp();
    private final SQLDeleteApp deleteApp = new SQLDeleteApp();

    private final Scanner scanner = new Scanner(System.in);
    private String loggedAccountNumber;
    private Account loggedAccount;

    private boolean isLoggedIn = false;

    public void menu() {
        boolean quit = false;

        while (!quit) {
            if (!isLoggedIn) {
                System.out.println("1. Create an account\n" +
                        "2. Log into account\n" +
                        "0. Exit");

                switch (scanner.nextInt()) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        logIntoAccount();
                        break;
                    case 0:
                        System.out.println("Bye!");
                        quit = true;
                        break;
                    default:
                        System.out.println("Wrong command!");
                        break;
                }
            } else {
                System.out.println("1. Balance\n" +
                        "2. Add income\n" +
                        "3. Do transfer\n" +
                        "4. Close account\n" +
                        "5. Log out\n" +
                        "0. Exit");

                switch (scanner.nextInt()) {
                    case 1:
                        printBalance();
                        break;
                    case 2:
                        addIncome();
                        break;
                    case 3:
                        transferMoney();
                        break;
                    case 4:
                        deleteAccount();
                        break;
                    case 5:
                        isLoggedIn = false;
                        break;
                    case 0:
                        System.out.println("Bye!");
                        quit = true;
                        break;
                    default:
                        System.out.println("Wrong command!");
                        break;
                }
            }
        }
    }

    private void createAccount() {
        Account account = new Account();
        String cardNumber = account.getCard().getNumber();
        String pin = account.getCard().getPin();
        int balance = account.getBalance();

        insertApp.insert(cardNumber, pin, balance);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin);
        System.out.println();
    }

    private void logIntoAccount() {
        System.out.println("Enter your card number:");
        String insertedCardNumber = scanner.next();
        scanner.nextLine();
//        System.out.println("test " + insertedCardNumber);
        System.out.println("Enter your PIN:");
        String insertedPIN = scanner.next();
//        System.out.println("test " + insertedPIN);
        scanner.nextLine();



//        for (Map.Entry<String, String> entry : cardDatabase.entrySet()) {
//            System.out.println("TEST " + entry.getKey() + ":" + entry.getValue().toString());
//        }

        try {
            Account account = selectApp.selectAccount(insertedCardNumber);

            if (account.getCard().getPin().equals(insertedPIN)) {
                System.out.println("You have successfully logged in!");
                loggedAccountNumber = account.getCard().getNumber();
                loggedAccount = account;
                isLoggedIn = true;
            }else {
                System.out.println("Wrong card number or PIN!");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printBalance() {
        int balance = loggedAccount.getBalance();
        System.out.println(balance);
    }

    private void addIncome() {
        System.out.println("Enter income:");
        try {
            int income  = scanner.nextInt();
            scanner.nextLine();
            int newBalance = selectApp.selectBalance(loggedAccountNumber) + income;
            loggedAccount.setBalance(newBalance);
            insertApp.insertBalance(newBalance, loggedAccountNumber);
            System.out.println("Income was added!");
        }catch (InputMismatchException e) {
            System.out.println("Wrong input!");
        }
    }

    private void transferMoney() {
        System.out.println("Transfer\n" +
                "Enter card number:");
        String insertedCardNumber = scanner.next();
        scanner.nextLine();
        if (insertedCardNumber.equals(loggedAccountNumber)) {
            System.out.println("You can't transfer money to the same account!");
        }else if (!checkLuhnAlgorithm(insertedCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
        }else if (selectApp.selectNumber(insertedCardNumber)) {
            System.out.println("Enter how much money you want to transfer:");
            int balance = loggedAccount.getBalance();
            int amountToTransfer  = scanner.nextInt();
            scanner.nextLine();
            if (balance >= amountToTransfer) {
                int newBalanceSender = balance - amountToTransfer;
                loggedAccount.setBalance(newBalanceSender);
                insertApp.insertBalance(newBalanceSender, loggedAccountNumber);
                System.out.println("test sender " + selectApp.selectBalance(loggedAccountNumber));

                int newBalanceReceiver = selectApp.selectBalance(insertedCardNumber) + amountToTransfer;
                insertApp.insertBalance(newBalanceReceiver, insertedCardNumber);
                System.out.println("test receiver " + selectApp.selectBalance(insertedCardNumber));
                System.out.println("Success!");
            }else {
                System.out.println("Not enough money!");
            }
        }else {
            System.out.println("Such a card does not exist.");
        }
    }

    public boolean checkLuhnAlgorithm(String tempCardNumber) {
        try {
            String[] individualNumbers = tempCardNumber.split("");
            int total = 0;
            for (int x = 0; x < 15; x++) {
                int currentDigit = Integer.parseInt(individualNumbers[x]);
                total += (x + 1) % 2 == 0 ? currentDigit :
                        (currentDigit * 2 > 9) ? (currentDigit * 2) - 9 :(currentDigit * 2);
            }
            int lastDigit = (total % 10 == 0) ? 0 : 10 - (total % 10);
            return lastDigit == Integer.parseInt(individualNumbers[15]);
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteAccount() {
        deleteApp.delete(loggedAccountNumber);
        loggedAccountNumber = "";
        loggedAccount = null;
        isLoggedIn = false;
        System.out.println("The account has been closed!");
    }
}
