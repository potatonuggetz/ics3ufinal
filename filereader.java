import java.util.Scanner;
import java.io.*;
public class filereader {
    //update the file of high scores with a new score
    public static void pushScores(int x,int y) throws IOException{
        int[] scores = new int[3];
        Scanner sc=new Scanner(new File("scores.txt"));
        for(int i=0;i<3;i++){
            if(!sc.hasNextInt()) scores[i]=0;
            else scores[i]=sc.nextInt();
        }
        if(y>scores[x]) scores[x]=y;
        sc.close();
        PrintWriter p=new PrintWriter(new FileWriter("scores.txt"));
        for(int i=0;i<3;i++){
            p.println(scores[i]);
        }
        p.close();
    }
    public static int[] pullScores() throws IOException{
        int[]scores = new int[3];
        Scanner sc=new Scanner(new File("scores.txt"));
        for(int i=0;i<3;i++){
            if(!sc.hasNextInt()) scores[i]=0;
            else scores[i]=sc.nextInt();
        }
        sc.close();
        return scores;
    }
}
