import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SongsPanel extends JPanel {

    private JPanel scrollPanel;
    private DaleMainFrame mainFrame;

    public SongsPanel(DaleMainFrame daleMainFrame) {
        mainFrame = daleMainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.blue);

        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Songs");
        titleLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.white);
        add(titlePanel, BorderLayout.NORTH);

        // Scrollable Panel for Wav Files
        scrollPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Upload Playlist Button
        JButton uploadButton = new JButton("Upload Playlist");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open file explorer
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileFilter(new FileNameExtensionFilter("MP3 Files", "mp3"));

                int result = fileChooser.showOpenDialog(SongsPanel.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    // Clear existing files from the scrollable panel
                    scrollPanel.removeAll();

                    // Add selected Wav files to the scrollable panel
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    for (File file : selectedFiles) {
                        if (file.isDirectory()) {
                            // If it's a directory, list all Wav files inside
                            File[] mp3Files = file.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
                            if (mp3Files != null) {
                                for (File wavFile : mp3Files) {
                                    addWavToPanel(wavFile, scrollPanel);
                                }
                            }
                        } else {
                            // If it's a file, check if it's a Wav file
                            if (file.getName().toLowerCase().endsWith(".mp3")) {
                                addWavToPanel(file, scrollPanel);
                            }
                        }
                    }

                    // Update the UI
                    revalidate();
                    repaint();
                }
            }
        });

        // Add Upload Button to the bottom
        add(uploadButton, BorderLayout.SOUTH);
    }

    private void addWavToPanel(File file, JPanel panel) {
        JButton fileLabelWithButton = new JButton(file.getName());
        fileLabelWithButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setCurrentlyPlayingSong(file);

                // Play the selected song using Clip
                try {
                    playSelectedSong(file);
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }


        });
        panel.add(fileLabelWithButton);
    }

    private void playSelectedSong(File file) throws UnsupportedAudioFileException, IOException {
        mainFrame.setCurrentlyPlayingSong(file);
        mainFrame.playCurrentSong();
    }
}