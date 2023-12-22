import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() throws HeadlessException {
        setTitle("Login");
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setSize(new Dimension((int) (size.getWidth()*0.8), (int) (size.getHeight()*0.8)));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);

        ImageIcon originalImageIcon = new ImageIcon("monet.jpg");

        // Scale the image to fit the panel
        int imagePanelWidth = getWidth() * 2 / 5;
        int imagePanelHeight = getHeight();
        Image scaledImage = originalImageIcon.getImage().getScaledInstance(imagePanelWidth, imagePanelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledImageIcon);
        imagePanel.add(imageLabel);
        add(imagePanel, BorderLayout.WEST);

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(200,25));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200,25));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean loginSuccessful = validateLogin(usernameField.getText(), new String(passwordField.getPassword()));

                if (loginSuccessful) {
                    App.database.updateIPAddress(usernameField.getText());
                    dispose(); // Close the login frame
                    new DaleMainFrame(); // Open the main frame
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password");
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(loginButton, gbc);

        add(loginPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        return App.database.authenticateUser(username, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}
