import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class DaleMainFrame extends JFrame {

    private File currentlyPlayingSong;
    boolean isPlaybackCompleted;

    private Client client;
    private Server server ;
    private AdvancedPlayer player;
    private Thread currentlyPlayingThread;

    public DaleMainFrame() throws HeadlessException {
        Dimension size
                = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(null);
        setSize(new Dimension((int) (size.getWidth()*0.8), (int) (size.getHeight()*0.8)));
        //AudioPanel
        JPanel audioPanel = new AudioPanel(this);
        audioPanel.setBounds(0, getHeight() - 70, getWidth(), 50);
        add(audioPanel);

        //SONGS PANEL
        JPanel songsPanel = new SongsPanel(this);
        songsPanel.setBounds(0,0, (int) (getWidth()*0.2),getHeight()-60);
        add(songsPanel);

        //SEARCH PANEL
        JPanel searchBar = new SearchBar();
        searchBar.setBounds((int) (getWidth()*0.2),0, (int) (getWidth()*0.8), (int) (getHeight()*0.1));
        add(searchBar);

        //IMAGE TEXT PANEL(Tekrardan build yapmanın yolunu bulmak lazım)
        URL imageUrl = DaleMainFrame.class.getResource("images/frog.png");
        Image image = new ImageIcon(imageUrl).getImage();
        JPanel centerTextPanel = new ImageTextPanel(image,(currentlyPlayingSong != null?currentlyPlayingSong.getName():null) );
        int panelX = (getWidth() - getWidth()) / 2;
        int panelY = (getHeight() - getHeight()) / 2;
        centerTextPanel.setLocation(panelX, panelY);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
    }
    public void setCurrentlyPlayingSong(File file) {
        this.currentlyPlayingSong = file;

    }

    public File getCurrentlyPlayingSong() {
        return currentlyPlayingSong;
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
    public void  sendCurrentlyPlayedSong(Server serverOpposite){
        //Server Adress
        String serverAdress =serverOpposite.getServerSocket().getInetAddress().getHostAddress();
        //Server Port
        int serverPort=serverOpposite.getServerSocket().getLocalPort();

        client.sendFileToServer(serverAdress,serverPort,currentlyPlayingSong.getAbsolutePath());
    }

    public void receiveSong() throws IOException {
        server.receiveFile();
    }

    public static void main(String[] args) {
            new DaleMainFrame();
    }


    }

