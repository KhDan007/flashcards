package flashcards;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

public class FlashcardApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private HashMap<String, User> users = new HashMap<>();
    private User currentUser; // Stores the currently logged-in user
    
    // UI Components
    private JPanel loginPanel, mainPanel;
    private JLabel usernameLabel, passwordLabel, signupLabel, welcomeLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, signupButton, createFlashcardButton, logoutButton;
    private GridBagConstraints gbc_1;
    private GridBagConstraints gbc_2;
    private GridBagConstraints gbc_3;
    private GridBagConstraints gbc_4;
    private JList<Deck> deckList;
    private DefaultListModel<Deck> deckListModel;
    
    public FlashcardApp() {
        // Load user data from file
        loadUserData();

        this.setSize(430, 932);
        
        // Initialize UI components
        initUI();

        // Set up event listeners
        setupEventListeners();
    }

    private void initUI() {
    	initLoginPanel();
    	
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void loadUserData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (HashMap<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found)
        }
    }

    private void saveUserData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            out.writeObject(users);
        } catch (IOException e) {
            // Handle exceptions
        }
    }
    
    private void switchToMainPanel(JPanel panel) {
        getContentPane().remove(panel); // Remove the login panel
        initMainPanel();  // Initialize the main panel
        getContentPane().add(mainPanel, BorderLayout.CENTER); // Add the main panel
        welcomeLabel.setText("Welcome, " + currentUser.getUsername());  // Update welcome message
        revalidate(); // Refresh the UI
        repaint();
    }
    
    // ----------------------------------------------
    
    private void setupEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignup();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username == "" || password == "") {
        	JOptionPane.showMessageDialog(this, "Please enter a username and a password", "Login Error", JOptionPane.ERROR_MESSAGE);	
        	return;
        }
        
        User user = users.get(username);
        if (user != null && user.getPasswordHash().equals(user.hashPassword(password))) {
            // Successful login
            currentUser = user;
            System.out.println(users);
            switchToMainPanel(loginPanel);
            // ... (Transition to the main flashcard interface)
        } else {
            // Invalid login, show error message
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        System.out.println(username);
        System.out.println(password);
        
        if (!users.containsKey(username)) {
            // Create new user and add to the map
            User newUser = new User(username, password);
            users.put(username, newUser);
            saveUserData();  // Save updated user data
            
            switchToMainPanel(loginPanel);
        } else if (username == "" || password == "") {
        	JOptionPane.showMessageDialog(this, "Please enter a username and a password", "Signup Error", JOptionPane.ERROR_MESSAGE);	
        } else {
            // Username already exists, show error message
            JOptionPane.showMessageDialog(this, "Username already taken", "Signup Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initLoginPanel() {
        // Login Panel
        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(121, 199, 255));
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc; // No initialization here 

        // Username
        gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(0, 0, 5, 0);
        usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Lucida Console", Font.PLAIN, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; 
        loginPanel.add(usernameLabel, gbc);

        gbc_1 = new GridBagConstraints();
        gbc_1.insets = new Insets(0, 0, 30, 0);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Lucida Console", Font.PLAIN, 20));
        gbc_1.gridx = 0; 
        gbc_1.gridy = 1; 
        gbc_1.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(usernameField, gbc_1);
        
        // Password
        gbc_2 = new GridBagConstraints(); 
        gbc_2.insets = new Insets(0, 0, 5, 0);
        passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Lucida Console", Font.PLAIN, 24));
        gbc_2.gridx = 0;
        gbc_2.gridy = 2;
        gbc_2.anchor = GridBagConstraints.CENTER; 
        loginPanel.add(passwordLabel, gbc_2);
        
        gbc_3 = new GridBagConstraints(); 
        gbc_3.insets = new Insets(0, 0, 5, 0);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Lucida Console", Font.PLAIN, 20));
        gbc_3.gridx = 0; 
        gbc_3.gridy = 3; 
        gbc_3.fill = GridBagConstraints.HORIZONTAL; 
        loginPanel.add(passwordField, gbc_3);
        
        // Login Button
        gbc_4 = new GridBagConstraints(); 
        gbc_4.insets = new Insets(10, 0, 200, 0);
        loginButton = new JButton("Log in");
        loginButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
        gbc_4.gridx = 0;
        gbc_4.gridy = 4; // Moved to row 4 to be below password field
        gbc_4.gridwidth = 1; // Occupies one column
        gbc_4.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc_4);
        
        // Sign up label
        gbc = new GridBagConstraints();
        signupLabel = new JLabel("Don't have an account?");
        signupLabel.setFont(new Font("Lucida Console", Font.PLAIN, 18));
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.gridx = 0;
        gbc.gridy = 5; // Moved to row 4 to be below password field
        gbc.gridwidth = 1; // Occupies one column
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(signupLabel, gbc);
        
        // Sign up Button
        gbc = new GridBagConstraints();
        signupButton = new JButton("Sign Up");
        signupButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 6; // Moved to row 4 to be below password field
        gbc.gridwidth = 1; // Occupies one column
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(signupButton, gbc);

        // Add login panel to the frame
        getContentPane().add(loginPanel, BorderLayout.CENTER);
    }
        private void initMainPanel() {
            mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.setBackground(new Color(121, 199, 255));

            // Heading
            welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getUsername() : ""));
            welcomeLabel.setFont(new Font("Lucida Console", Font.BOLD, 28));
            welcomeLabel.setHorizontalAlignment(JLabel.CENTER); 
            mainPanel.add(welcomeLabel, BorderLayout.NORTH);

            // Deck List
            deckListModel = new DefaultListModel<>(); // To hold the decks
            deckList = new JList<>(deckListModel);
            deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(deckList); // Add scrollbar if needed
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Bottom Panel with Buttons
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            createFlashcardButton = new JButton("Create New Flashcard");
            logoutButton = new JButton("Log Out");

            createFlashcardButton.addActionListener(e -> {
                // ... (logic to show the flashcard creation screen)
            });
            
            logoutButton.addActionListener(e -> {
                saveUserData(); 
                currentUser = null;
                welcomeLabel.setText("Welcome, ");  // Clear the welcome message
                remove(mainPanel);  // Remove the main panel
                add(loginPanel, BorderLayout.CENTER); // Show the login panel
                loginPanel.setVisible(true); 
                revalidate(); // Refresh the UI
                repaint(); 
            });

            bottomPanel.add(createFlashcardButton);
            bottomPanel.add(logoutButton); // Add logout button
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
    }

    
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlashcardApp window = new FlashcardApp();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
