import javax.swing.*;
import java.awt.*;

public class AudioPanel extends JPanel {
    JButton playButton;
    JButton stopButton;
    public AudioPanel() {
        setBackground(Color.GRAY);
      // Position at the bottom
        setLayout(new FlowLayout(FlowLayout.CENTER));

        playButton = new JButton("Play");
        stopButton = new JButton("Stop");

        add(playButton);
        add(stopButton);
        ;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        setVisible(true);

    }

}
