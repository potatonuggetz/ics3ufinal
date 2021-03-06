import java.util.Scanner;
import java.io.*;
public class filereader {
    public static void initializeScoreFile() throws IOException{
        if(!new File("scores.txt").isFile()){
            resetScores();
        }
    }

    //update the file of high scores with a new score
    public static void resetScores()throws IOException{
        PrintWriter p=new PrintWriter(new FileWriter("scores.txt"));
        for(int i=0;i<33;i++){
            for(int j=0;j<3;j++){
                p.print("0 ");
            }
            p.println();
        }
        p.close();
    }
    //pushes any new scores to the file
    public static void pushScores(int x,int y,int z) throws IOException{
        int[][] scores = new int[33][3];
        Scanner sc=new Scanner(new File("scores.txt"));
        for(int i=0;i<33;i++){
            for(int j=0;j<3;j++){
                scores[i][j]=sc.nextInt();
            }
        }
        if(y>scores[z][x]) scores[z][x]=y;
        sc.close();
        PrintWriter p=new PrintWriter(new FileWriter("scores.txt"));
        for(int i=0;i<33;i++){
            for(int j=0;j<3;j++){
                p.print(scores[i][j]+" ");
            }
            p.println();
        }
        p.close();
    }
    //retrieves high scores
    public static int[] pullScores(int x) throws IOException{
        int[]scores = new int[3];
        Scanner sc=new Scanner(new File("scores.txt"));
        for(int i=0;i<=32;i++){
            if(i==x){
                for(int j=0;j<3;j++){
                    if(!sc.hasNextInt()) scores[j]=0;
                    else scores[j]=sc.nextInt();
                }
            } else sc.nextLine();
        }
        sc.close();
        return scores;
    }
    //gets a setting from the file
    public static boolean pullSetting(int x) throws IOException{
        boolean[] settings = new boolean[3];
        Scanner sc=new Scanner(new File("settings.txt"));

        for(int i=0;i<3;i++){
            if(sc.hasNextBoolean()){
                settings[i]=sc.nextBoolean();
            } else settings[i]=false;
        }
        sc.close();
        return settings[x];
    }
    //pushes a setting to the file
    public static void pushSetting(int x,boolean y) throws IOException{
        Scanner sc=new Scanner(new File("settings.txt"));
        boolean[] settings = new boolean[3];
        for(int i=0;i<3;i++){
            if(sc.hasNextInt()){
                settings[i]=sc.nextBoolean();
            } else settings[i]=false;
        }
        settings[x]=y;
        sc.close();
        PrintWriter p=new PrintWriter(new FileWriter("settings.txt"));
        for(int i=0;i<3;i++){
            p.println(settings[i]);
        }
        p.close();
    }
}
