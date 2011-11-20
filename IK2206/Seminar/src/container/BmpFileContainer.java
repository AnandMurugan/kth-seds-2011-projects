/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import common.IContainerFile;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author julio
 */
public class BmpFileContainer implements IContainerFile {
    final int PIXEL_SIZE_IN_BYTES = 3;
    final String BMP_FORMAT_STRING = "bmp";
    int currentIndex = -1;
    int currentX;
    int currentY;
    BufferedImage image;
    int imageByteSize;
    int height;
    int width;

    public BmpFileContainer() {
        currentIndex = 0;
        currentX = 0;
        currentY = 0;
        imageByteSize = 0;
        height = 0;
        width = 0;
    }

    @Override
    public void loadFile(String path) {
        File imageFile = new File(path);
        try {
            image = ImageIO.read(imageFile);
            height = image.getHeight();
            width = image.getWidth();
            imageByteSize = height * width * PIXEL_SIZE_IN_BYTES;
        } catch (IOException ex) {
            Logger.getLogger(BmpFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public byte getNextByte() throws IndexOutOfBoundsException {
        byte result = 0;

        if (currentY >= height) {
            throw new IndexOutOfBoundsException();
        }

        currentIndex++;  // update the current index after getting the pixel color
        int currentColor = currentIndex % PIXEL_SIZE_IN_BYTES; // determine the correspondant RGB
        
        if( currentColor == 0) {
            currentX++;
        }
        
        if (currentX >= width) {
            currentX = 0;
            currentY++;
        }

        int pixelColor = image.getRGB(currentX, currentY);

        switch (currentColor) {
            case 1:
                result = (byte) ((pixelColor & 0x00ff0000) >> 16);
                break; // red
            case 2:
                result = (byte) ((pixelColor & 0x0000ff00) >> 8);
                break; // green
            case 0:
                result = (byte) ((pixelColor & 0x000000ff));
                
                break; // blue
        }

        return result;
    }

    @Override
    public void setByte(byte newByte) {
        int currentColor = currentIndex % PIXEL_SIZE_IN_BYTES; // determine the correspondant RGB
        int oldPixelColor = image.getRGB(currentX, currentY);
        int newPixelColor = oldPixelColor;
        switch (currentColor) {
            case 1:
                newPixelColor = newPixelColor | (newByte << 16); // red
                break;
            case 2:
                newPixelColor = newPixelColor | (newByte << 8); // green
                break;
            case 0:
                newPixelColor = newPixelColor | newByte; // blue
                break;
        }

        image.setRGB(currentX, currentY, newPixelColor);
    }

    @Override
    public int getSize() {
        return imageByteSize;
    }

    public boolean hasMoreBytes() {
        return (imageByteSize > currentIndex);
    }

    public void saveFile(String path) {
        try {
            File outputfile = new File(path);
            ImageIO.write(image, BMP_FORMAT_STRING, outputfile);
        } catch (IOException ex) {
            Logger.getLogger(BmpFileContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // TODO. Remove test code
    /*public static void main(String args[]) {
    BmpFileContainer fileContainer = new BmpFileContainer();
    try {
    fileContainer.getNextByte();
    } catch(Exception ex) {
    ex.printStackTrace();
    }
    
    fileContainer.loadFile("C:\\Users\\julio\\Pictures\\flower.bmp");
    System.out.println("File size in bytes: " + fileContainer.getSize());
    int i= 0;
    while (fileContainer.hasMoreBytes()) {            
    fileContainer.getNextByte();
    //System.out.println(fileContainer.getNextByte());
    //i++;
    //System.out.println("current byte: " + i);
    }
    } */
}
