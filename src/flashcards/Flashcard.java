package flashcards;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flashcard implements Serializable { // Serializable for saving/loading

    private String question;
    private String answer;
    private int reviewCount = 0;
    private Date lastReviewed = null;
    private static final long serialVersionUID = 1L; // For serialization

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // Getters and Setters (for all attributes)
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public Date getLastReviewed() { return lastReviewed; }
    public void setLastReviewed(Date lastReviewed) { this.lastReviewed = lastReviewed; }

    // Spaced Repetition helper methods
    public void incrementReviewCount() { this.reviewCount++; }

    public void updateLastReviewed() { this.lastReviewed = new Date(); }

    // Method for displaying the flashcard as a string
    @Override
    public String toString() {
        String lastReviewedStr = (lastReviewed == null) ? "Never" : 
                                new SimpleDateFormat("yyyy-MM-dd").format(lastReviewed);
        return "Question: " + question + "\nAnswer: " + answer +
               "\nReview Count: " + reviewCount + "\nLast Reviewed: " + lastReviewedStr;
    }

    // Methods for editing (you might need to adapt these for your UI)
    public void editQuestion(String newQuestion) {
        this.question = newQuestion;
    }

    public void editAnswer(String newAnswer) {
        this.answer = newAnswer;
    }
}