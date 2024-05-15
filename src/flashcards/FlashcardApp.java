package flashcards;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;

public class FlashcardApp extends JFrame implements ActionListener, Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel logoLabel, usernameLabel, passwordLabel;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, signupButton;
    HashMap<String, String> users = new HashMap<>();

    @SuppressWarnings("unchecked")
	public FlashcardApp() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setSize(400, 300);

        logoLabel = new JLabel("Logo");
        logoLabel.setBounds(150, 20, 100, 50);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 90, 80, 20);
        usernameField = new JTextField();
        usernameField.setBounds(140, 90, 200, 20);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 120, 80, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(140, 120, 200, 20);

        loginButton = new JButton("Log In");
        loginButton.setBounds(100, 170, 100, 30);
        loginButton.addActionListener(this);

        signupButton = new JButton("Sign Up");
        signupButton.setBounds(200, 170, 100, 30);
        signupButton.addActionListener(this);

        add(logoLabel);
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(signupButton);

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"));
            users = (HashMap<String, String>) in.readObject();
            in.close();
        } catch (Exception e) {
            // File not found or empty
        }

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (users.containsKey(username) && users.get(username).equals(password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        } else if (e.getSource() == signupButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (!users.containsKey(username)) {
                users.put(username, password);
                try {
                    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"));
                    out.writeObject(users);
                    out.close();
                    JOptionPane.showMessageDialog(this, "Signup successful!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving user data.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Username already exists.");
            }
        }
    }

    public static void main(String[] args) {
        new FlashcardApp();
    }
}
