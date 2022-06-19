public class Bug {
    //object attributes
    int xPos;
    int yPos=0;
    int label;
    boolean dead;
    int gravity;
    String str;

    //object constructor
    public Bug(int a,int b,int c,boolean d,int e,String f){
        xPos=a;
        yPos=b;
        label=c;
        dead=d;
        gravity=e;
        str=f;
    }

    //lowers the y position by the gravity attribute of the specific bug
    public void update(){
        if(!dead) yPos+=gravity;
        //toggle sprite
    }

    //converts the bug's integer into a string of the target base
    public void getLabel(int x){
        str=Integer.toString(label,x).toUpperCase();
    }
}
