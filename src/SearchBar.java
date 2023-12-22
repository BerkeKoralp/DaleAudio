import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchBar extends JPanel {
    public static String currentUserToSendMusic;
    public SearchBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 60));
        setBackground(Color.BLUE);

        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(App.database.retrieveIP(searchField.getText()) != null){
                    currentUserToSendMusic = App.database.retrieveIP(searchField.getText());
                }else{
                    JOptionPane.showMessageDialog(SearchBar.this, "Invalid username.");
                }
            }
        });

        add(searchField, BorderLayout.CENTER);
        add(searchButton, BorderLayout.WEST);
    }

}
