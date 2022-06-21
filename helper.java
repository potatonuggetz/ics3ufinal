import java.awt.*;

public class helper {
    //draws a string at the center of a bounding box
    public static void drawCenteredString(Graphics g, String text, int ulx, int uly, int brx, int bry) {
        Font font=g.getFont();
        FontMetrics metrics = g.getFontMetrics(font);
        int x = ulx + (brx-ulx - metrics.stringWidth(text)) / 2;
        int y = uly + (((bry-uly) - metrics.getHeight()+metrics.getLeading())/2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
