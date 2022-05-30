import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class flappybit extends JPanel implements MouseListener{

    public static boolean shootConfirm=false;
    public static int mouseX;
    public static int mouseY;
    public static int difficulty=0;
    public static int gameState=0;
    public static int scores[]=new int[3];
    /*gamestate 0: main menu
    * gamestate 1: play menu
    * gamestate 2: settings menu
    * gamestate 3: high score menu
    */

    public static BufferedImage mainMenu;    
    public static BufferedImage tempMenu;    

    public flappybit(){
        setPreferredSize(new Dimension(400,600));
		setBackground(new Color(100,255,255));
        addMouseListener(this);
        try{
            mainMenu=ImageIO.read(new File("mainmenu.png"));
            tempMenu=ImageIO.read(new File("tempmenu.png"));
            shootConfirm=filereader.pullSetting(0);
        }catch(Exception e){};
    }

    public void paintComponent(Graphics g){
        if(gameState==0){
            super.paintComponent(g);
            g.drawImage(mainMenu,0,0,null);
        }
        else if(gameState==1) System.out.println("play");
        else if(gameState==2) {
            g.setFont(new Font("Calibri",Font.BOLD,19));
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            if(shootConfirm) g.setColor(new Color(0,255,0));
            else g.setColor(new Color(255,0,0));
            g.fillRect(100,200,200,75);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Confirm shot with Enter",100,200,300,275);
        }
        else if(gameState==3) {
            g.setFont(new Font("Calibri",Font.BOLD,32));
            try {
                scores=filereader.pullScores();
            } catch (IOException e) {}
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            helper.drawCenteredString(g,"Easy: "+scores[0],100,200,300,275);
            helper.drawCenteredString(g,"Medium: "+scores[1],100,325,300,400);
            helper.drawCenteredString(g,"Hard: "+scores[2],100,450,300,525);
        }
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
        }else if(gameState==2){
            if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                //clicked shootconfirm
                shootConfirm=!shootConfirm;
                try{
                    filereader.pushSetting(0,shootConfirm);
                } catch(Exception e1){};
                paintComponent(this.getGraphics());
            }
            else if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=0;
                paintComponent(this.getGraphics());
            }
        }else if(gameState==3){
            if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=0;
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