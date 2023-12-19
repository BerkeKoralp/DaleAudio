import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AudioPanel extends JPanel {
    JButton playButton;
    JButton stopButton;
    public AudioPanel(DaleMainFrame frame) {
        setBackground(Color.GRAY);
      // Position at the bottom
        setLayout(new FlowLayout(FlowLayout.CENTER));

        playButton = new JButton("Play");
        stopButton = new JButton("Stop");

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.stopCurrentSong();
            }
        });

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.playCurrentSong();
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        add(playButton);
        add(stopButton);
        ;
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        setVisible(true);

    }

}
