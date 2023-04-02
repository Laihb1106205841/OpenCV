import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class OpenCV extends JPanel {
    private static final long serialVersionUIU = 1L;
    private static BufferedImage mImg;

    private BufferedImage mat2BI(Mat mat) {
        int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
        byte[] data = new byte[dataSize];
        mat.get(0, 0, data);
        int type = mat.channels() == 1 ?
                BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
        if (type == BufferedImage.TYPE_3BYTE_BGR) {
            for (int i = 0; i < dataSize; i += 3) {
                byte blue = data[i + 0];
                data[i + 0] = data[i + 2];
                data[i + 2] = blue;

            }
        }
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;

    }

    public void paintComponnent(Graphics g) {
        if (mImg != null) {
            g.drawImage(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(), this);

        }
    }

    public static void main(String[] args) {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            Mat capImg = new Mat();
            VideoCapture capture = new VideoCapture(0);
            int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
            int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
            if (height == 0 || width == 0) {
                throw new Exception("Camera not found");
            }
            JFrame frame = new JFrame("camera");
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            OpenCV panel = new OpenCV();
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("click");
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("mousePressed");
                }


                @Override
                public void mouseReleased(MouseEvent e) {
                    System.out.println("mouseReleased");
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    System.out.println("mouseEntered");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    System.out.println("mouseExited");
                }


                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    System.out.println("mouseMoved");
                }
            });
            frame.setContentPane(panel);
            frame.setVisible(true);
            frame.setSize(width + frame.getInsets().left + frame.getInsets().right, height + frame.getInsets().top + frame.getInsets().bottom);
            int n = 0;
            Mat temp = new Mat();
            while (frame.isShowing() && n < 500) {
                capture.read(capImg);
                Imgproc.cvtColor(capImg, temp, Imgproc.COLOR_RGB2BGR555);
                Mat detectFace = detectFace(capImg);
                panel.mImg = panel.mat2BI(detectFace);
                panel.repaint();
            }
            capture.release();
            frame.dispose();
        } catch (Exception e) {
            System.out.println("例外：" + e);
        } finally {
            System.out.println("--done--");

        }
    }


    public static Mat detectFace(Mat img) throws Exception {
        System.out.println("Running DetecrFace...");
        CascadeClassifier faceDetector = new CascadeClassifier("D:\\Java\\OpenCV\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(img, faceDetections);
        Rect[] rects = faceDetections.toArray();
        if (rects != null && rects.length >= 1) {
            for (Rect rect : rects) {
                Imgproc.rectangle(img, new org.opencv.core.Point(rect.x, rect.y), new org.opencv.core.Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 245), 2);

            }
            //拍照
            SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
            String name = sdf.format(new Date());
//获取桌面路径
            File path = FileSystemView.getFileSystemView().getHomeDirectory();
            System.out.println(path);
            String format = "jpg";
            File f = new File(path + File.separator + name + "." + format);
            try {
                ImageIO.write(mImg, format, f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;

    }
}



