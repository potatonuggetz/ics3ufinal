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
    public static boolean banOverlap=false;
    public static int mouseX;
    public static int mouseY;
    public static int difficulty=0;
    public static int score=0;
    public static int spawnInterval=0;
    public static int lastSpawn=0;
    public static int currentGrav=1;
    //which base the game will be played in
    public static int gamemode=16;
    public static int gameState=0;
    public static int scores[]=new int[3];
    public static ArrayList<Bug> bugs=new ArrayList<Bug>();
    public static Bug tempBug;
    public static int activeNumber=0;
    public static boolean[]bitArr=new boolean[8];
    public static String tempString;
    static Random rand=new Random();
    /*gamestate 0: main menu
    * gamestate 1: base selection menu
    * gamestate 2: settings menu
    * gamestate 3: high score menu
    * gamestate 4: difficulty menu
    * gamestate 5: ingame
    * gamestate 6: difficulty help menu
    * gamestate 7: game over screen
    * gamestate 8: custom base screen
    * gamestate 9: confirm delete scores
    */

    public static BufferedImage mainMenu;    
    public static BufferedImage tempMenu;
    public static BufferedImage tempMenuHelp;
    public static BufferedImage garbage;
    public static BufferedImage bug;     
    public static BufferedImage helpMenu;      

    public flappybit(){
        setPreferredSize(new Dimension(400,600));
		setBackground(new Color(100,255,255));
        addMouseListener(this);
        try{
            //importing all the images
            mainMenu=ImageIO.read(new File("mainmenu.png"));
            tempMenu=ImageIO.read(new File("tempmenu.png"));
            tempMenuHelp=ImageIO.read(new File("tempmenuhelp.png"));
            garbage=ImageIO.read(new File("playfield.png"));
            helpMenu=ImageIO.read(new File("helpmenu.png"));
            bug=ImageIO.read(new File("bug.png"));
            shootConfirm=filereader.pullSetting(0);
            banOverlap=filereader.pullSetting(1);
        }catch(Exception e){};
        this.setFocusable(true);
        addKeyListener(this);
        Thread thread=new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g){
        if(gameState==0){
            //draw the main menu screen
            super.paintComponent(g);
            g.drawImage(mainMenu,0,0,null);
        }
        else if(gameState==1) {
            //this is after user clicks play, draws the different bases the user can play in
            g.setFont(new Font("Calibri",Font.BOLD,32));
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Octal",100,200,300,275);
            helper.drawCenteredString(g,"Hexadecimal",100,325,300,400);
            helper.drawCenteredString(g,"Decimal",100,450,300,525);
        }
        else if(gameState==2) {
            //settings menu
            g.setFont(new Font("Calibri",Font.BOLD,19));
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            //the confirm shot setting
            if(shootConfirm) g.setColor(new Color(0,255,0));
            else g.setColor(new Color(255,0,0));
            g.fillRect(100,200,200,75);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Confirm shot with Enter",100,200,300,275);
            //the banoverlap setting
            g.setFont(new Font("Calibri",Font.BOLD,15));
            if(banOverlap) g.setColor(new Color(0,255,0));
            else g.setColor(new Color(255,0,0));
            g.fillRect(100,325,200,75);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Prevent bugs from overlapping",100,325,300,375);
            g.setFont(new Font("Calibri",Font.BOLD,13));
            helper.drawCenteredString(g,"Lowers final score by a factor of 0.8",100,375,300,400);
            //the change base menu setting
            g.setFont(new Font("Calibri",Font.BOLD,32));
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Change base",100,450,300,525);
        }
        else if(gameState==3) {
            //high score menu, shows high scores in different difficulties
            g.setFont(new Font("Calibri",Font.BOLD,32));
            try {
                scores=filereader.pullScores(gamemode);
            } catch (IOException e) {}
            super.paintComponent(g);
            g.drawImage(tempMenu,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Easy: "+scores[0],100,200,300,275);
            helper.drawCenteredString(g,"Medium: "+scores[1],100,325,300,400);
            helper.drawCenteredString(g,"Hard: "+scores[2],100,450,300,525);
            g.drawImage(garbage,325,525,null);
        }
        else if(gameState==4) {
            //after base selection, the difficulty menu (shows different amounts of helper guides)
            g.setFont(new Font("Calibri",Font.BOLD,48));
            super.paintComponent(g);
            g.drawImage(tempMenuHelp,0,0,null);
            g.setColor(new Color(0,0,0));
            helper.drawCenteredString(g,"Easy",100,200,300,275);
            helper.drawCenteredString(g,"Medium",100,325,300,400);
            helper.drawCenteredString(g,"Hard",100,450,300,525);
        }
        else if(gameState==5) {
            //actual gameplay
            g.setFont(new Font("Calibri",Font.BOLD,24));
            super.paintComponent(g);
            //g.drawImage(playfield,0,0,null);
            g.setColor(new Color(0,0,0));
            //loops through arraylist of all bugs, and draws its current position
            for(Bug i:bugs){
                if(!i.dead){
                    g.drawImage(bug,i.xPos,i.yPos,null);
                    helper.drawCenteredString(g,i.str,i.xPos,i.yPos,i.xPos+50,i.yPos+50);
                }
            }
            //creates the string at the bottom with the number display
            tempString="";
            for(int i=7;i>=0;i--){
                tempString+=bitArr[i]?'1':'0';
            }
            //draws the binary string and the death line 
            helper.drawCenteredString(g,tempString,0,500,400,600);
            if(difficulty==0)helper.drawCenteredString(g, Integer.toString(activeNumber,gamemode).toUpperCase(), 0, 400, 400, 500);
            helper.drawCenteredString(g, ""+score, 350, 0, 400, 50);
            g.setColor(new Color(255,0,0));
            g.drawLine(0, 500, 400, 500);
        }
        else if(gameState==6) {
            //help menu describing the different difficulties
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
        else if(gameState==7){
            //game over screen
            g.setFont(new Font("Calibri",Font.BOLD,48));
            g.setColor(new Color(0,0,0));
            super.paintComponent(g);
            helper.drawCenteredString(g, "Game Over", 0, 0, 400, 150);
            helper.drawCenteredString(g, "Score: "+score, 0, 150, 400, 300);
            g.setFont(new Font("Calibri",Font.BOLD,24));
            helper.drawCenteredString(g, "Click anywhere to continue", 0, 300, 400, 600);
            resetGame();
        }
        else if(gameState==8){
            //secret base change screen
            g.setFont(new Font("Calibri",Font.BOLD,36));
            g.setColor(new Color(0,0,0));
            super.paintComponent(g);
            helper.drawCenteredString(g, "Enter a base between", 0, 200, 400, 250);
            helper.drawCenteredString(g, "2 and 32 (inclusive)", 0, 250,400, 300);
            g.setFont(new Font("Calibri",Font.BOLD,24));
            helper.drawCenteredString(g, "Otherwise it will default to 10", 0,300,400, 400);
            g.setFont(new Font("Calibri",Font.BOLD,96));
            helper.drawCenteredString(g, tempString, 0,400,400, 600);
        }
        else if(gameState==9){
            g.setFont(new Font("Calibri",Font.BOLD,36));
            g.setColor(new Color(0,0,0));
            super.paintComponent(g);
            helper.drawCenteredString(g, "Are you sure you want to", 0, 200, 400, 250);
            helper.drawCenteredString(g, "delete all your scores?", 0, 250,400, 300);
            g.setFont(new Font("Calibri",Font.BOLD,24));
            helper.drawCenteredString(g, "This cannot be undone.", 0,300,400, 400);
            g.setFont(new Font("Calibri",Font.BOLD,96));
        }
    }

    //sets all the variables back to "default state"
    public static void resetGame(){
        bugs=new ArrayList<Bug>();
        currentGrav=1;
        lastSpawn=0;
        spawnInterval=25;
    }

    public static void bugUpdate(){
        //updates the position of each bug and ends game if any living bug is at the bottom
        for(Bug i:bugs){
            i.update();
            //bug reached the bottom, we kill the player and apply score adjustments
            if(i.yPos>=450&&!i.dead){
                if(banOverlap) score*=0.8;
                gameState=7;
            }
        }
        //changes spawn rate and gravity based on how much score has been accumulated
        spawnInterval=25;
        if(score>=20) currentGrav=2;
        if(score>=30) spawnInterval=20;
        if(score>=40) spawnInterval=15;
        if(score>=50) spawnInterval=10;
        lastSpawn++;
        //add a bug with random attributes initialized
        if(spawnInterval<=lastSpawn){
            tempBug = new Bug(rand.nextInt(351),0,rand.nextInt(256),false,currentGrav,"arknights");
            tempBug.getLabel(gamemode);
            bugs.add(tempBug);
            lastSpawn=0;
        }
        //System.out.println(lastSpawn);
    }

    public void run() {
        //timer function
        while(true) {
			repaint();
            if(gameState==5){
                bugUpdate();
            }
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
                gameState=4;
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
                gameState=2;
                paintComponent(this.getGraphics());
            }else if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                //clicked octal
                gamemode=8;
                gameState=2;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=400&&mouseY>=325){
                //clicked hexadecimal
                gamemode=16;
                gameState=2;
                paintComponent(this.getGraphics());
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=525&&mouseY>=450){
                //clicked decimal
                gamemode=10;
                gameState=2;
                paintComponent(this.getGraphics());
            } else {
                //secret custom base mode
                gameState=8;
                tempString="";
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
            } else if(mouseX>=100&&mouseX<=300&&mouseY<=400&&mouseY>=325){
                //clicked banoverlap
                banOverlap=!banOverlap;
                try{
                    filereader.pushSetting(1,banOverlap);
                } catch(Exception e1){};
                paintComponent(this.getGraphics());
            }
            else if(mouseX>=100&&mouseX<=300&&mouseY<=525&&mouseY>=450){
                //clicked change base
                gameState=1;
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
            }else if(mouseX>=325&&mouseX<=400&&mouseY>=525&&mouseY<=600){
                //clicked reset score
                gameState=9;
                paintComponent(this.getGraphics());
            }
        }else if(gameState==4){
            if(mouseX>=0&&mouseX<=75&&mouseY<=600&&mouseY>=525){
                //clicked back
                gameState=0;
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
        }else if(gameState==7){
            try{
                filereader.pushScores(difficulty, score, gamemode);
            } catch(Exception sodfhis){}
            gameState=0;
            score=0;
            paintComponent(this.getGraphics());
        }
    }

    public void keyTyped(KeyEvent e) {
        if(gameState==5){
            //checks if we are in play mode and if the button pressed is between 1 and 8
            if(e.getKeyChar()>='1'&&e.getKeyChar()<='8'){
                //if the bit was already disabled, turn it on
                if(!bitArr[7-(e.getKeyChar()-'1')]){
                    activeNumber+=Math.pow(2,7-(e.getKeyChar()-'1'));
                    bitArr[7-(e.getKeyChar()-'1')]=true;
                    repaint();
                }else{
                    //if the bit was already enabled, turn it off
                    activeNumber-=Math.pow(2,7-(e.getKeyChar()-'1'));
                    bitArr[7-(e.getKeyChar()-'1')]=false;
                    repaint();
                }
                //check if the bug is dead
                for(Bug i:bugs){
                    if(i.label==activeNumber&&!i.dead&&!shootConfirm){
                        score++;
                        i.dead=true;
                        activeNumber=0;
                        for(int j=0;j<8;j++){
                            bitArr[j]=false;
                        }
                    }
                }
            }
            //debug command
            if(e.getKeyChar()=='/'){
                score+=10;
            }
        } else if(gameState==8){
            //base change screen
            if(e.getKeyChar()>='0'&&e.getKeyChar()<='9'){
                tempString+=e.getKeyChar();
                repaint();
            }
        } else if(gameState==9){
            //reset score confirm screen
            if(mouseX>=100&&mouseX<=300&&mouseY<=275&&mouseY>=200){
                gameState=3;
            }
            try{
                filereader.resetScores();
            }catch(Exception adsf){}
        }
    }

    public void keyPressed(KeyEvent e) {
        //if shootConfirm is enabled
        if(gameState==5){
            if(e.getKeyCode()==KeyEvent.VK_ENTER&&shootConfirm){
                for(Bug i:bugs){
                    if(i.label==activeNumber&&!i.dead){
                        score++;
                        i.dead=true;
                        activeNumber=0;
                        for(int j=0;j<8;j++){
                            bitArr[j]=false;
                        }
                    }
                }
            }
        } else if(gameState==8){
            if(e.getKeyCode()==KeyEvent.VK_ENTER){
                gamemode=Integer.valueOf(tempString);
                if(gamemode>32||gamemode<2) gamemode=10;
                gameState=2;
                repaint();
            }
        }
    }

    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
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
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
}