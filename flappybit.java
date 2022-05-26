import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class flappybit extends JPanel implements MouseListener{

    public static int mouseX;
    public static int mouseY;
    public static int difficulty=0;
    public static int gameState=0;
    /*gamestate 0: main menu
    * gamestate 1: play menu
    * gamestate 2: settings menu
    * gamestate 3: high score menu
    */

    public static BufferedImage mainMenu;    

    public flappybit(){
        setPreferredSize(new Dimension(400,600));
		setBackground(new Color(100,255,255));
        addMouseListener(this);
        try{
            mainMenu=ImageIO.read(new File("mainmenu.png"));
        }catch(Exception e){};
    }

    public void paintComponent(Graphics g){
        if(gameState==0){
            g.drawImage(mainMenu,0,0,null);
        }else if(gameState==1) System.out.println("play");
        else if(gameState==2) System.out.println("settings");
        else if(gameState==3) System.out.println("high score");
    }

    public static void main(String[] args) {
		JFrame myFrame = new JFrame("Flappy Bit");
		flappybit myPanel=new flappybit();
		myFrame.add(myPanel);
		myFrame.pack();
		myFrame.setVisible(true);
	}

    public void mousePressed(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
        if(gameState==0){
            if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                //clicked play
                gameState=1;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=400&&mouseY>=325){
                //clicked settings
                gameState=2;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=525&&mouseY>=450){
                //clicked high score
                gameState=3;
                paintComponent(this.getGraphics());
            }
        }
    }

    
    public void mouseClicked(MouseEvent e) {      
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
}