public class bug {
    int xPos;
    int yPos=0;
    int label;
    boolean dead;
    int gravity;

    public void update(){
        yPos+=gravity;
        //toggle sprite
    }
}
