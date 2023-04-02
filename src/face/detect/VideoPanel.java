package face.detect;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import org.opencv.core.Mat;

public class VideoPanel extends JPanel {

    private BufferedImage image;

    public void setImageWithMat(Mat mat) {
        image = mat2BufferedImage.matToBufferedImage(mat);
        this.repaint();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
    }

    public static VideoPanel show(String title, int width, int height, int open) {
        JFrame frame = new JFrame(title);
        if(open==0) {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        frame.setSize(width, height);
        frame.setBounds(0, 0, width, height);
        VideoPanel videoPanel = new VideoPanel();
        videoPanel.setSize(width, height);
        frame.setContentPane(videoPanel);
        frame.setVisible(true);
        return videoPanel;
    }
}
