import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class AudioPanel extends JPanel {
    JButton playButton;
    JButton stopButton;
    public static Icon loadAndResizeIcon(String path, int width, int height) {
        ImageIcon icon = createImageIcon(path);
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Icon not found: " + path);
            return null;
        }
    }

    private static ImageIcon createImageIcon(String path) {
        ClassLoader classLoader = AudioPanel.class.getClassLoader();
        URL iconUrl = classLoader.getResource(path);
        return (iconUrl != null) ? new ImageIcon(iconUrl) : null;
    }
    public AudioPanel(DaleMainFrame frame) {
        setBackground(Color.GRAY);
      // Position at the bottom
        setLayout(new FlowLayout(FlowLayout.CENTER));

        playButton = new JButton("Play");

        playButton.setIcon(loadAndResizeIcon("images/playbutton.png",16,16));

        stopButton = new JButton("Stop");

        stopButton.setIcon(loadAndResizeIcon("images/stopIcon.png",16,16));
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
