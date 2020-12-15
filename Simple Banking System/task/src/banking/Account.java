package banking;


public class Account {
    private Card card;
    private int balance;

    public Account() {
        this.card = generateCard();
        this.balance = 0;
    }

    public Account(Card card, int balance) {
        this.card = card;
        this.balance = balance;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public Card generateCard() {
        SQLSelectApp selectApp = new SQLSelectApp();
        String number = generateCardNumber();

        while (selectApp.selectNumber(number)) {
            System.out.println(selectApp.selectNumber(number));
            number = generateCardNumber();
        }
        String pin = generatePinNumber();

        card = new Card(number, pin);
        return card;
    }

    private String generateCardNumber() {
        int randomDigit;
        int[] cardNumberArray = new int[15];
        String cardNumber;
        StringBuilder cardNumberBuilder = new StringBuilder();

        for (int i=0; i<15; i++) {
            if (i == 0) {
                randomDigit = 4;
            }else if (i <= 5) {
                randomDigit = 0;
            }else {
                randomDigit = (int) (Math.random() * 10);
            }
            cardNumberBuilder.append(randomDigit);
            cardNumberArray[i] = randomDigit;
        }
        cardNumber = cardNumberBuilder.toString() + generateLuhnAlgorithmDigit(cardNumberArray);
        return cardNumber;
    }

    private int generateLuhnAlgorithmDigit(int[] cardNumberArray) {
        int sum = 0;

        for (int i=0; i<cardNumberArray.length; i++) {
            //step 1: Multiply odd digits by 2
            if (i % 2 == 0) {
                cardNumberArray[i] = cardNumberArray[i] * 2;
            }
            //step 2: Subtract 9 to numbers over 9
            if (cardNumberArray[i] > 9) {
                cardNumberArray[i] -= 9;
            }
            //step 3: Add all numbers
            sum += cardNumberArray[i];
        }
        //step 4: find last digit
        int luhnNumber = 10 - (sum % 10);

        return luhnNumber == 10 ? 0 : luhnNumber;
    }

    private static String generatePinNumber() {
        StringBuilder randomPINNumber = new StringBuilder();
        for (int i=0; i<4; i++) {
            int randomDigit = (int) (Math.random() * 10);
            randomPINNumber.append(randomDigit);
        }
        return randomPINNumber.toString();
    }
}
