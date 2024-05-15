package flashcards;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String passwordHash; // Store hashed password, not plain text
    private ArrayList<Deck> decks = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.passwordHash = hashPassword(password); // Hash password on creation
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    // Add a deck to the user's collection
    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    // Remove a deck from the user's collection
    public boolean removeDeck(Deck deck) {
        return decks.remove(deck);
    }

    // Password hashing method (using SHA-256)
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            // Handle the unlikely case where SHA-256 is not available
            e.printStackTrace();
            return null; // Or throw an exception
        }
    }

    // Helper method to convert byte array to hex string
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
