package jogltest;

import java.io.*;

import java.awt.*;
import java.awt.font.*;
import java.awt.image.*;


public class DynamicFont
{

    private Font basefont;

    private Font font;

    private FontRenderContext context;

    private float fontsize;

    private static final boolean DEBUG = true;

/**
       * create a font from an input stream
       * @param is InputStream
       */
    public DynamicFont(InputStream is) {
        basefont = loadBaseFont(is);
        context = getContext();
        fontsize = 48f;
        setSize(fontsize);
    }



    /**
       * derive a font with a new size from basefont
       * @param size float
       * @return Font
       */
    private Font getFontBySize(float s) {
        return basefont.deriveFont(s);
    }



    /**
       * set size of font
       * @param size float
       */
    public void setSize(float size) {
        fontsize = size;
        font = getFontBySize(size);
    }



    /**
       * load the base font
       * @param name String
       * @return Font
       */
    private Font loadBaseFont(InputStream is) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException ex) {
            System.err.println("### ERROR @DynamicFont IOException");
        } catch (FontFormatException ex) {
            System.err.println("### ERROR @DynamicFont FontFormatException");
        }
        return font;
    }



    /**
       * get a font rendering context
       * @return FontRenderContext
       */
    private FontRenderContext getContext() {
        BufferedImage b = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = b.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext frc = g.getFontRenderContext();
        return frc;
    }



    /**
       * get the pixel data from the specified text
       * @param text String
       * @return byte[]
       */
    /*public byte[] getData(String text) {
            BufferedImage myImage = getImage(text);
            Raster myRaster = myImage.getData();
            DataBufferByte myDataBufferByte = (DataBufferByte)myRaster.getDataBuffer();
            return myDataBufferByte.getData();
      }*/



    /**
       * check if characters are supproted
       * @param text String
       * @return char
       */
    private void checkString(String text) {
        int illegalCharacter = font.canDisplayUpTo(text);
        if (illegalCharacter > -1) {
            System.err.println("### INFO cann t display character #" + illegalCharacter);
        }
    }



    /**
       * get a buffered image from the specified text 
       * @param text String
       * @return BufferedImage
       */
    public BufferedImage getImage(String text) {
        if (DEBUG) {
            checkString(text);
        }
        int width = (int) font.getStringBounds(text, context).getWidth();
        int height = (int) font.getMaxCharBounds(context).getHeight();
        //int x = (int)font.getStringBounds(text, context).getX();
        int y = (int) font.getStringBounds(text, context).getY();
        BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR); //BufferedImage.TYPE_4BYTE_ABGR
        Graphics2D g = bitmap.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.drawString(text, 0, -y);
        return bitmap;
    }
}