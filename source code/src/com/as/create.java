package com.as;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

class create{

    private int DEFAULT_WIDTH = 450, DEFAULT_HEIGHT = 450;
    public BufferedImage createGraphics() {

            Random randomalizacja=new Random();

            BufferedImage bufferedImage = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImage.createGraphics();
            double leftX = 70;
            double topY = 20;
            double width = 280;
            double height = 330;
            //rysuje białe tło
            var backRect = new Rectangle2D.Double(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            g2.setColor(Color.WHITE);
            g2.fill(backRect);
            var rect = new Rectangle2D.Double(leftX, topY, width, height);
            // rysuje twarz - elipsę
            g2.setColor(new Color(randomalizacja.nextInt(100)+150, randomalizacja.nextInt(100)+150, randomalizacja.nextInt(100)+150));
            var ellipse = new Ellipse2D.Double();
            ellipse.setFrame(rect);
            g2.fill(ellipse);
            g2.setColor(Color.BLACK);
            g2.draw(ellipse);
            //oczy
            g2.fillOval(randomalizacja.nextInt(30)+Double.valueOf(leftX).intValue() + 70, Double.valueOf(topY).intValue() + 100, randomalizacja.nextInt(12)+10, randomalizacja.nextInt(12)+10);
            g2.fillOval(randomalizacja.nextInt(30)+Double.valueOf(leftX).intValue() + 140, Double.valueOf(topY).intValue() + 100, randomalizacja.nextInt(12)+10, randomalizacja.nextInt(12)+10);
            //brwi
            g2.draw(new Line2D.Float(randomalizacja.nextInt(30)+120,randomalizacja.nextInt(30)+90,randomalizacja.nextInt(30)+170,randomalizacja.nextInt(30)+90));
            g2.draw(new Line2D.Float(randomalizacja.nextInt(30)+190,randomalizacja.nextInt(30)+90,randomalizacja.nextInt(30)+240,randomalizacja.nextInt(30)+90));
            //nos
            g2.setStroke(new BasicStroke(3));
            int nosx=randomalizacja.nextInt(30)+160;
            int nosy=randomalizacja.nextInt(30)+190;
            g2.draw(new Line2D.Float(randomalizacja.nextInt(30)+190, randomalizacja.nextInt(30)+110, nosx, nosy));
            g2.draw(new Line2D.Float(nosx, nosy, randomalizacja.nextInt(30)+180, randomalizacja.nextInt(30)+190));
            //usta
            g2.drawArc(Double.valueOf(leftX).intValue() + 100, Double.valueOf(topY).intValue() + 220, randomalizacja.nextInt(30)+50, randomalizacja.nextInt(30)+20, 190, randomalizacja.nextInt(40)+140);
            //uszy
            g2.drawArc(Double.valueOf(leftX).intValue()+14, Double.valueOf(topY).intValue()+50, 30, 40, 63, 160);
            g2.drawArc(Double.valueOf(leftX).intValue()+240, Double.valueOf(topY).intValue()+50, 30, 40, 310, 180);
            g2.dispose();


            return bufferedImage;

    }

}

class FaceContainer implements Serializable {
    private ArrayList<BufferedImage> bufferedImages;
    private ArrayList<ByteArrayWrapper> wrappers = new ArrayList<>();

    public ArrayList<ByteArrayWrapper> getWrappers() {
        return wrappers;
    }

    public void setWrappers(ArrayList<ByteArrayWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    public FaceContainer() {
        bufferedImages = new ArrayList<>();
    }

    public ArrayList<BufferedImage> getBufferedImages() {
        return bufferedImages;
    }

    public void setBufferedImages(ArrayList<BufferedImage> bufferedImages) {
        this.bufferedImages = bufferedImages;
    }
    public byte[] enbuffer(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public BufferedImage debuffer(byte[] bytearray) throws IOException {
        InputStream in = new ByteArrayInputStream(bytearray);
        return  ImageIO.read(in);
    }
}


class ByteArrayWrapper implements Serializable {
    private byte[] image;

    public ByteArrayWrapper() {
    }

    public ByteArrayWrapper(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

