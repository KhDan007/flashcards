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
    
    // UI Component for Flashcard Creation
    private JPanel flashcardPanel;
    private JLabel questionLabel, answerLabel;
    private JTextField questionField, answerField;
    private JComboBox<String> deckComboBox; // For deck selection
    private JButton saveFlashcardButton;
    
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
    	
        initFlashcardPanel();
        
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

    private void showFlashcardCreationScreen() {
        // Initialize flashcardPanel if not already done
        if (flashcardPanel == null) {
            initFlashcardPanel();
        }

        // Remove the main panel (or the currently active panel)
        getContentPane().remove(mainPanel); // Assuming you're currently on mainPanel

        // Add the flashcard panel
        getContentPane().add(flashcardPanel, BorderLayout.CENTER);
        
        // Update UI
        revalidate();
        repaint();
    }
    
    // Flashcard Creation Screen
    private void initFlashcardPanel() {
        flashcardPanel = new JPanel();
        flashcardPanel.setLayout(new GridBagLayout());
        
        
        // ... (Add labels, text fields, deckComboBox, and saveFlashcardButton to flashcardPanel)
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.equals("") || password.equals("")) {
        	JOptionPane.showMessageDialog(this, "Please enter a username and a password", "Login Error", JOptionPane.ERROR_MESSAGE);	
        	return;
        }
        
        User user = users.get(username);
        if (user != null && user.getPasswordHash().equals(user.hashPassword(password))) {
            // Successful login
            currentUser = user;
            
            initMainPanel();
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
            
            currentUser = newUser;
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
            mainPanel.setLayout(new BorderLayout(20, 20)); // Add spacing to BorderLayout
            mainPanel.setBackground(new Color(245, 245, 245)); // Soft gray background

            // Heading with Icon (Replace "path/to/icon.png" with your actual icon path)
            JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            headingPanel.setBackground(mainPanel.getBackground());
            welcomeLabel = new JLabel("Welcome, " + (currentUser != null ? currentUser.getUsername() : ""));
            welcomeLabel.setFont(new Font("Open Sans", Font.BOLD, 28));

            // Add Icon (replace the path with your actual icon file)
            try {
                ImageIcon icon = new ImageIcon("src/icon.png"); // Assuming icon is in "src" folder
                Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
                headingPanel.add(iconLabel);
            } catch (Exception e) {
                System.err.println("Error loading icon: " + e.getMessage()); // Print error message
                // If loading fails, proceed without the icon
            }

            headingPanel.add(welcomeLabel);
            mainPanel.add(headingPanel, BorderLayout.NORTH);

            // Deck List/Grid (using GridBagLayout)
            JPanel deckPanel = new JPanel(new GridBagLayout());
            deckPanel.setBackground(mainPanel.getBackground());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            int gridy = 0;

            // Add decks to the list model and create deck cards
            deckListModel = new DefaultListModel<>();
            for (Deck deck : currentUser.getDecks()) {
                deckListModel.addElement(deck);
                createDeckCard(deckPanel, deck, gbc, gridy++);
            }
            deckList = new JList<>(deckListModel);
            deckList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(deckList);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            // Bottom Panel with Buttons
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            createFlashcardButton = new JButton("Create New Flashcard");
            logoutButton = new JButton("Log Out");

            createFlashcardButton.addActionListener(e -> {
            	System.out.println(1);
                showFlashcardCreationScreen(); // Show flashcard creation screen
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
        
        private void createDeckCard(JPanel deckPanel, Deck deck, GridBagConstraints gbc, int gridy) {
            JPanel deckCard = new JPanel(new BorderLayout());
            deckCard.setBackground(Color.WHITE);
            deckCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true));

            JLabel deckNameLabel = new JLabel(deck.getName());
            deckNameLabel.setFont(new Font("Open Sans", Font.BOLD, 18));
            deckNameLabel.setHorizontalAlignment(JLabel.CENTER); // Center horizontally
            deckCard.add(deckNameLabel, BorderLayout.CENTER);

            // (Optional) Add deck image, progress bar here

            deckCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // Handle deck selection logic here (e.g., open the study screen)
                    // You can get the selected deck using:
                    // Deck selectedDeck = deckList.getSelectedValue();
                }
            });

            gbc.gridy = gridy;
            deckPanel.add(deckCard, gbc);
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
