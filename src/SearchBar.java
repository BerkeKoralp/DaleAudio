import javax.swing.*;
import java.awt.*;

public class SearchBar extends JPanel {
    public SearchBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 60));
        setBackground(Color.BLUE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Search");
        JTextField textField = new JTextField(15);

        titleLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.cyan, 10));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);

        add(titlePanel, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
    }




}
