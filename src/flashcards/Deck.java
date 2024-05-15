package flashcards;

import java.io.Serializable;
import java.util.*;

public class Deck implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    
    // Constructor
    public Deck(String name) {
        this.name = name;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // Add a flashcard to the deck
    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    // Remove a flashcard from the deck
    public boolean removeFlashcard(Flashcard flashcard) {
        return flashcards.remove(flashcard);
    }

    // Get the number of flashcards in the deck
    public int size() {
        return flashcards.size();
    }

    // Shuffle the flashcards in the deck
    public void shuffle() {
        Collections.shuffle(flashcards);
    }

    // Sort flashcards based on a basic spaced repetition algorithm
    public void sortBySpacedRepetition() {
        Collections.sort(flashcards, (card1, card2) -> {
            if (card1.getLastReviewed() == null && card2.getLastReviewed() != null) {
                return -1; // Prioritize cards that have never been reviewed
            } else if (card1.getLastReviewed() != null && card2.getLastReviewed() == null) {
                return 1;
            } else if (card1.getLastReviewed() == null && card2.getLastReviewed() == null) {
                return 0;
            } else {
                // Sort by last reviewed date (oldest first), then review count (lowest first)
                int dateComparison = card1.getLastReviewed().compareTo(card2.getLastReviewed());
                if (dateComparison == 0) {
                    return Integer.compare(card1.getReviewCount(), card2.getReviewCount());
                }
                return dateComparison;
            }
        });
    }

    // Get a flashcard at a specific index
    public Flashcard getFlashcard(int index) {
        if (index >= 0 && index < flashcards.size()) {
            return flashcards.get(index);
        } else {
            throw new IndexOutOfBoundsException("Invalid flashcard index");
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Deck: ").append(name).append("\n");
        for (Flashcard card : flashcards) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }
}
