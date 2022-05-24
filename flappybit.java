import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class flappybit extends JPanel {

    public static int difficulty=0;
    

    public flappybit(){
        setPreferredSize(new Dimension(400,600));
		setBackground(new Color(100,255,255));
    }

    public static void main(String[] args) {
		JFrame myFrame = new JFrame("Flappy bit");
		flappybit myPanel=new flappybit();
		myFrame.add(myPanel);
		myFrame.pack();
		myFrame.setVisible(true);
	}
}