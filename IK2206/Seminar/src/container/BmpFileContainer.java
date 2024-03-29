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
    final int RED_COLOR = 1;
    final int GREEN_COLOR = 2;
    final int BLUE_COLOR = 0;
    int currentIndex = -1;
    int currentX;
    int currentY;
    BufferedImage image;
    int imageByteSize;
    int height;
    int width;

    public BmpFileContainer() {
        currentIndex = 0;
        currentX = -1;
        currentY = 0;
        imageByteSize = 0;
        height = 0;
        width = 0;
    }

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

    public byte getNextByte() throws IndexOutOfBoundsException {
        byte result = 0;

        if (currentY >= height) {
            throw new IndexOutOfBoundsException();
        }

        currentIndex++;  // update the current index after getting the pixel color
        int currentColor = currentIndex % PIXEL_SIZE_IN_BYTES; // determine the correspondant RGB
        if( currentColor == RED_COLOR) {
            currentX++; // every time its red we updated the currentX
        }
        
        if (currentX >= width) { // in case currentX is bigget than width we go to next pixel row.
            currentX = 0; 
            currentY++;
        }

        int pixelColor = image.getRGB(currentX, currentY);

        switch (currentColor) {
            case RED_COLOR:
                result = (byte) ((pixelColor & 0x00ff0000) >> 16);
                break; // red
            case GREEN_COLOR:
                result = (byte) ((pixelColor & 0x0000ff00) >> 8);
                break; // green
            case BLUE_COLOR:
                result = (byte) ((pixelColor & 0x000000ff));
                break; // blue
        }
        
        return result;
    }

    public void setByte(byte newByte) {
        int currentColor = currentIndex % PIXEL_SIZE_IN_BYTES; // determine the correspondant RGB
        int oldPixelColor = image.getRGB(currentX, currentY);
        int newPixelColor = oldPixelColor;
        int newIntByte = (newByte & 0x000000FF);
        switch (currentColor) {
            case RED_COLOR:
            	newPixelColor = newPixelColor & (0xFF00FFFF);
                newPixelColor = newPixelColor | (newIntByte << 16); // red
                break;
            case GREEN_COLOR:
            	newPixelColor = newPixelColor & (0xFFFF00FF);
            	newPixelColor = newPixelColor | (newIntByte << 8); // green
                break;
            case BLUE_COLOR:
            	newPixelColor = newPixelColor & (0xFFFFFF00);
                newPixelColor = newPixelColor | newIntByte; // blue
                break;
        }
        
        image.setRGB(currentX, currentY, newPixelColor);
    }

    public int getSize() {
        return imageByteSize;
    }

    @Override
    public boolean hasMoreBytes() {
        return (imageByteSize > currentIndex);
    }

    @Override
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

    @Override
    public byte getByte(int index) throws IndexOutOfBoundsException {
        byte result = 0;
        
        if (index >= imageByteSize) {
            throw new IndexOutOfBoundsException();
        }
        
        int pixelNumber = index / 3;
        int pixelColor = (index + 1) % 3;
        
        
        int posY = pixelNumber / width;
        int posX = pixelNumber % width;
        
        int pixelValue = image.getRGB(posX, posY);
        
        switch (pixelColor) {
            case RED_COLOR:
                result = (byte) ((pixelValue & 0x00ff0000) >> 16);
                break; // red
            case GREEN_COLOR:
                result = (byte) ((pixelValue & 0x0000ff00) >> 8);
                break; // green
            case BLUE_COLOR:
                result = (byte) ((pixelValue & 0x000000ff));
                break; // blue
        }
        
        return result;
    }

    @Override
    public void setByte(int index, byte newByte) throws IndexOutOfBoundsException {
        if (index >= imageByteSize) {
            throw new IndexOutOfBoundsException();
        }
        
        int pixelNumber = index / 3;
        int pixelColor = (index + 1) % 3;
        
        
        int posY = pixelNumber / width;
        int posX = pixelNumber % width;
        
        
        int oldPixelColor = image.getRGB(posX, posY);
        int newPixelColor = oldPixelColor;
        int newIntByte = (newByte & 0x000000FF);
        switch (pixelColor) {
            case RED_COLOR:
            	newPixelColor = newPixelColor & (0xFF00FFFF);
                newPixelColor = newPixelColor | (newIntByte << 16); // red
                break;
            case GREEN_COLOR:
            	newPixelColor = newPixelColor & (0xFFFF00FF);
            	newPixelColor = newPixelColor | (newIntByte << 8); // green
                break;
            case BLUE_COLOR:
            	newPixelColor = newPixelColor & (0xFFFFFF00);
                newPixelColor = newPixelColor | newIntByte; // blue
                break;
        }
        
        image.setRGB(posX, posY, newPixelColor);
    }
}
