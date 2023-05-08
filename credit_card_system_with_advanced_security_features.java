import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class CreditCard {
    private String cardNumber;
    private String encryptedCardData;

    public CreditCard(String cardNumber, String encryptionKey) throws Exception {
        this.cardNumber = cardNumber;
        this.encryptedCardData = encryptCardData(cardNumber, encryptionKey);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getEncryptedCardData() {
        return encryptedCardData;
    }

    private String encryptCardData(String cardNumber, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedBytes = cipher.doFinal(cardNumber.getBytes());
        byte[] iv = cipher.getIV();

        byte[] combinedBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combinedBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combinedBytes, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combinedBytes);
    }

    // Other credit card methods...
}

class Database {
    private Map<String, String> cardDataMap;

    public Database() {
        this.cardDataMap = new HashMap<>();
    }

    public void storeCardData(String cardNumber, String encryptedCardData) {
        cardDataMap.put(cardNumber, encryptedCardData);
    }

    public String retrieveCardData(String cardNumber) {
        return cardDataMap.get(cardNumber);
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        // Initialization
        String encryptionKey = "AESEncryptionKey";
        Database database = new Database();

        // Registering a new credit card
        String cardNumber = "1234567890123456";
        CreditCard creditCard = new CreditCard(cardNumber, encryptionKey);
        database.storeCardData(cardNumber, creditCard.getEncryptedCardData());

        // Retrieving the card data
        String retrievedEncryptedData = database.retrieveCardData(cardNumber);
        CreditCard retrievedCreditCard = new CreditCard(cardNumber, encryptionKey);
        String decryptedCardNumber = retrievedCreditCard.getCardNumber();

        // Verify card data
        boolean isCardDataValid = cardNumber.equals(decryptedCardNumber);
        System.out.println("Card Data Valid: " + isCardDataValid);
    }
}
