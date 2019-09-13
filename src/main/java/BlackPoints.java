import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

class BlackPoints {
    public static String image = Reader.image;
    
    public static int numberOfBlackPixels(BufferedImage img){
        int t = 0;
        int width = img.getWidth();
        int height = img.getHeight();
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = img.getRGB(i, j);
                int red = (pixel >> 16) & 0x000000FF;
                int green = (pixel >> 8) & 0x000000FF;
                int blue = (pixel) & 0x000000FF;
                if (red <=64 && green <=64 && blue <=64) { // for the taking the dark pixels
                    t++;
                }
            }
        }
        return t;
    }
    
    /** Take x coordinates of desired pixels  **/
    public static int[] getXCoordinates(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] ArrayX = new int[numberOfBlackPixels(img)];
        int d = -1;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int pixel = img.getRGB(w, h);
                int red = (pixel >> 16) & 0x000000FF;
				
                int green = (pixel >> 8) & 0x000000FF;
				
		int blue = (pixel) & 0x000000FF;
				
		if (red <=64 && green <=64 && blue <=64) {
		    d++;
                    ArrayX[d] = w;
                }
            }
        }
        return ArrayX;
    }
    
    /** Take y coordinates of desired pixels  **/
    public static int[] getYCoordinates(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] ArrayY = new int[numberOfBlackPixels(img)];
        int d = -1;
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int pixel = img.getRGB(w, h);
                int red = (pixel >> 16) & 0x000000FF;
                
                int green = (pixel >> 8) & 0x000000FF;
                
                int blue = (pixel) & 0x000000FF;
                
                if (red <=64 && green <=64 && blue <=64) {
                    d++;
                    ArrayY[d] = h;
                }
            }
        }
        return ArrayY;
    }
    
    
    protected static void drawBinaryImage() throws IOException {
        BufferedImage x = ImageIO.read(new File(image));
        BufferedImage sample = new BufferedImage(x.getWidth(),x.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics gd = sample.createGraphics();
        
        int c[]=getXCoordinates(x);
        int d[]=getYCoordinates(x);
        
        gd.setColor(Color.white);
        gd.fillRect(0, 0,sample.getWidth(),sample.getHeight());
        gd.setColor(Color.black);
        
        for (int y = 0; y < c.length; y++) {
            gd.drawLine(c[y], d[y], c[y], d[y]);
        }
        gd.dispose();
        
        File file = new File("current.png");
        try {
            ImageIO.write(sample, "png", file);
        }
        catch (IOException ex) {
            Logger.getLogger(BlackPoints.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}