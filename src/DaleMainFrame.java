import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.net.URL;

public class DaleMainFrame extends JFrame {

    private File currentlyPlayingSong;
    boolean isPlaybackCompleted;
    private Client client = new Client();
    private Server server = new Server(3000);
    private AdvancedPlayer player;
    private Thread currentlyPlayingThread;
    JPanel audioPanel;
    JPanel songsPanel;
    JPanel searchBar;
    JPanel centerTextPanel;

    public DaleMainFrame() throws HeadlessException, IOException {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setSize(new Dimension((int) (size.getWidth()*0.8), (int) (size.getHeight()*0.8)));
        //AudioPanel
        audioPanel = new AudioPanel(this);
        audioPanel.setBounds(0, getHeight() - 70, getWidth(), 50);
        add(audioPanel);

        //SONGS PANEL
        songsPanel = new SongsPanel(this);
        songsPanel.setBounds(0,0, (int) (getWidth()*0.2),getHeight()-60);
        add(songsPanel);

        //SEARCH PANEL
        searchBar = new SearchBar();
        searchBar.setBounds((int) (getWidth()*0.2),0, (int) (getWidth()*0.8), (int) (getHeight()*0.1));
        add(searchBar);

        //IMAGE TEXT PANEL(Tekrardan build yapmanın yolunu bulmak lazım)
        URL imageUrl = DaleMainFrame.class.getResource("images/frog.png");
        Image image = new ImageIcon(imageUrl).getImage();
        centerTextPanel = new ImageTextPanel(image,(currentlyPlayingSong != null?currentlyPlayingSong.getName():null) );
        int panelX = (getWidth() - getWidth()) / 2;
        int panelY = (getHeight() - getHeight()) / 2;
        centerTextPanel.setLocation(panelX, panelY);

        //SEND SONG BUTTON
        JButton sendButton = new JButton("Send Song");
        sendButton.setBounds(getWidth()-200,100,100,100);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendCurrentlyPlayedSong();
            }
        });
        add(sendButton);

        //RECEIVE SONG BUTTON
        JButton receiveButton = new JButton("Receive\nSong");
        receiveButton.setBounds(getWidth()-200,200,100,100);
        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    receiveSong();
                } catch (IOException | UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(receiveButton);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }
    public void setCurrentlyPlayingSong(File file) {
        this.currentlyPlayingSong = file;

    }

    public void playCurrentSong() throws UnsupportedAudioFileException, IOException {
        // Check if there is already a song playing
        if (currentlyPlayingThread != null && currentlyPlayingThread.isAlive()) {
            // Stop the currently playing song
            stopCurrentSong();
        }

        // Create a new thread and start it
        currentlyPlayingThread = new Thread(() -> {
            try {
                playCurrentSongInternal();
            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        });

        currentlyPlayingThread.start();
        System.out.println(currentlyPlayingSong);
    }

    public void stopCurrentSong() {
        // Check if there is a song playing
        if (currentlyPlayingThread != null && currentlyPlayingThread.isAlive()) {
            // Interrupt the thread to stop the playback
            currentlyPlayingThread.interrupt();
            currentlyPlayingThread = null;
            player.stop();
        }
    }


    private void playCurrentSongInternal() throws UnsupportedAudioFileException, IOException {
        String mp3FilePath = currentlyPlayingSong.getAbsolutePath();

        try (FileInputStream fileInputStream = new FileInputStream(mp3FilePath)) {
            player = new AdvancedPlayer(fileInputStream);

            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    System.out.println("Playback finished");
                }
            });

            System.out.println("Playing: " + mp3FilePath);
            player.play();

        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
    public void sendCurrentlyPlayedSong(){
        String IP = SearchBar.currentUserToSendMusic;
        if(IP == null){
            JOptionPane.showMessageDialog(DaleMainFrame.this, "You haven't searched yet.");
        }else{
            client.sendFileToServer(IP,currentlyPlayingSong.getAbsolutePath());
        }
    }

    public void receiveSong() throws IOException, UnsupportedAudioFileException {
        String fileName = server.receiveFile();
        // Relative path to the "ReceivedSongs" folder
        String relativePath = "ReceivedSongs";

        // Get the absolute path
        String absolutePath = getAbsolutePath(relativePath) + "\\" + fileName;
        currentlyPlayingSong = new File(absolutePath);
        playCurrentSong();
    }

    private static String getAbsolutePath(String relativePath) {
        // Get the base directory of the project
        String basePath = new File("").getAbsolutePath();

        // Combine the base path and the relative path to get the absolute path
        String absolutePath = new File(basePath, relativePath).getAbsolutePath();

        return absolutePath;
    }

    public static void main(String[] args) throws IOException {
            new DaleMainFrame();
    }


    }

