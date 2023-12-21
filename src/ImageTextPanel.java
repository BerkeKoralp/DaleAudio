import javax.swing.*;
import java.awt.*;

class ImageTextPanel extends JPanel {

    private Image image;
    private String text;

    public ImageTextPanel(Image image, String text) {
        this.image = image;
        this.text = text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the image at the top
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight() / 2, this);
        }

        // Draw the text at the bottom
        if (text != null && !text.isEmpty()) {
            FontMetrics fontMetrics = g.getFontMetrics();
            int textHeight = fontMetrics.getHeight();

            int textX = 10; // Adjust the X position as needed
            int textY = getHeight() / 2 + textHeight; // Place text below the image
            g.drawString(text, textX, textY);
        }
    }
}