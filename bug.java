public class Bug {
    int xPos;
    int yPos=0;
    int label;
    boolean dead;
    int gravity;

    public Bug(int a,int b,int c,boolean d,int e){
        xPos=a;
        yPos=b;
        label=c;
        dead=d;
        gravity=e;
    }

    public void update(){
        yPos+=gravity;
        //toggle sprite
    }
}
