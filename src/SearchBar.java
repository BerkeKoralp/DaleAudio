import javax.swing.*;
import java.awt.*;

public class SearchBar extends JPanel {
    public SearchBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 60));
        setBackground(Color.BLUE);

        JButton searchButton = new JButton("Search");
        JTextField textField = new JTextField(15);

        add(textField, BorderLayout.CENTER);
        add(searchButton, BorderLayout.WEST);
    }

}
