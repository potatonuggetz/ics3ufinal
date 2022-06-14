public class Bug {
    int xPos;
    int yPos=0;
    int label;
    boolean dead;
    int gravity;
    String str;

    public Bug(int a,int b,int c,boolean d,int e,String f){
        xPos=a;
        yPos=b;
        label=c;
        dead=d;
        gravity=e;
        str=f;
    }

    public void update(){
        if(!dead) yPos+=gravity;
        //toggle sprite
    }
    public void getLabel(int x){
        str=Integer.toString(label,x).toUpperCase();
    }
}
