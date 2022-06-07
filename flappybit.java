import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class flappybit extends JPanel implements Runnable,KeyListener,MouseListener{

    public static boolean shootConfirm=false;
    public static int mouseX;
    public static int mouseY;
    public static int difficulty=0;
    public static int score=0;
    public static int spawnInterval=0;
    public static int lastSpawn=0;
    public static int currentGrav=1;
    //which base the game will be played in
    public static int gamemode=0;
    public static int gameState=0;
    public static int scores[]=new int[3];
    public static ArrayList<Bug> bugs=new ArrayList<Bug>();
    public static Bug tempBug;
    static Random rand=new Random();
    /*gamestate 0: main menu
    * gamestate 1: base selection menu
    * gamestate 2: settings menu
    * gamestate 3: high score menu
    * gamestate 4: difficulty menu
    * gamestate 5: ingame
    * gamestate 6: difficulty help menu
    */

    public static BufferedImage mainMenu;    
    public static BufferedImage tempMenu;
    public static BufferedImage tempMenuHelp;
    public static BufferedImage playfield;
    public static BufferedImage bug;     
    public static BufferedImage helpMenu;      

    public flappybit(){
        setPreferredSize(new Dimension(400,600));
		setBackground(new Color(100,255,255));
        addMouseListener(this);
        try{
            mainMenu=ImageIO.read(new File("mainmenu.png"));
            tempMenu=ImageIO.read(new File("tempmenu.png"));
            tempMenuHelp=ImageIO.read(new File("tempmenuhelp.png"));
            playfield=ImageIO.read(new File("playfield.png"));
            helpMenu=ImageIO.read(new File("helpmenu.png"));
            bug=ImageIO.read(new File("bug.png"));
            shootConfirm=filereader.pullSetting(0);
        }catch(Exception e){};
        this.setFocusable(true);
        addKeyListener(this);
        Thread thread=new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g){
        if(gameState==0){
            super.paintComponent(g);
            g.drawImage(mainMenu,0,0,null);
        }
        else if(gameState==1) {
            g.setFont(new Font("Calibri",Font.BOLD,32));
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Octal",100,200,300,275);
            helper.drawCenteredString(g,"Hexadecimal",100,325,300,400);
            helper.drawCenteredString(g,"Decimal",100,450,300,525);
        }
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
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Easy: "+scores[0],100,200,300,275);
            helper.drawCenteredString(g,"Medium: "+scores[1],100,325,300,400);
            helper.drawCenteredString(g,"Hard: "+scores[2],100,450,300,525);
        }
        else if(gameState==4) {
            g.setFont(new Font("Calibri",Font.BOLD,48));
            super.paintComponent(g);
            g.drawImage(tempMenuHelp,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Easy",100,200,300,275);
            helper.drawCenteredString(g,"Medium",100,325,300,400);
            helper.drawCenteredString(g,"Hard",100,450,300,525);
        }
        else if(gameState==5) {
            g.setFont(new Font("Calibri",Font.BOLD,24));
            super.paintComponent(g);
            //g.drawImage(playfield,0,0,null);
            g.setColor(new Color(0,0,0));
            for(Bug i:bugs){
                g.drawImage(bug,i.xPos,i.yPos,null);
                helper.drawCenteredString(g,i.str,i.xPos,i.yPos,i.xPos+50,i.yPos+50);
            }
            
        }
        else if(gameState==6) {
            g.setFont(new Font("Calibri",Font.BOLD,24));
            super.paintComponent(g);
            g.drawImage(helpMenu,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"On Easy difficulty, a counter shows ",100,200,300,225);
            helper.drawCenteredString(g,"the current number you have inputted",100,225,300,250);
            helper.drawCenteredString(g,"(in the target base), as well as a",100,250,300,275);
            helper.drawCenteredString(g,"helper that shows the value of a digit.",100,275,300,300);
            helper.drawCenteredString(g,"On Medium difficulty, a helper",100,325,300,362);
            helper.drawCenteredString(g,"shows the value of each digit.",100,362,300,400);
            helper.drawCenteredString(g,"On Hard difficulty, you get no",100,425,300,462);
            helper.drawCenteredString(g,"helping tools. Good luck!",100,462,300,500);
        }
    }

    public static void bugUpdate(){
        for(Bug i:bugs){
            i.update();
        }
        lastSpawn++;
        spawnInterval=25;
        if(spawnInterval==lastSpawn){
            tempBug = new Bug(rand.nextInt(351),0,rand.nextInt(256),false,currentGrav,"arknights");
            tempBug.getLabel(gamemode);
            bugs.add(tempBug);
            lastSpawn=0;
        }
        //System.out.println(lastSpawn);
    }

    public void run() {
        while(true) {
			repaint();
            bugUpdate();
            try {
				Thread.sleep(100);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
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
        }else if(gameState==1){
            if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=0;
                paintComponent(this.getGraphics());
            }else if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                //clicked octal
                gamemode=8;
                gameState=4;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=400&&mouseY>=325){
                //clicked hexadecimal
                gamemode=16;
                gameState=4;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=525&&mouseY>=450){
                //clicked decimal
                gamemode=10;
                gameState=4;
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
        }else if(gameState==4){
            if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=1;
                paintComponent(this.getGraphics());
            }else if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                //clicked easy
                difficulty=0;
                gameState=5;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=400&&mouseY>=325){
                //clicked medium
                difficulty=1;
                gameState=5;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=525&&mouseY>=450){
                //clicked hard
                difficulty=2;
                gameState=5;
                paintComponent(this.getGraphics());
            } else if(mouseX>=325&&mouseX<=400&&mouseY<=75&&mouseY>=0){
                //clicked help
                gameState=6;
                paintComponent(this.getGraphics());
            }
        }else if(gameState==6){
            if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=4;
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

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}