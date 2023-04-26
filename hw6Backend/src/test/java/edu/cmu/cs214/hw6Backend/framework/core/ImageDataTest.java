package edu.cmu.cs214.hw6Backend.framework.core;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ImageDataTest {
    private ImageData imageDataWithInt;
    private ImageData imageDataWithByte;
    private ImageData imageDataWithByteNoAlpha;

    @Before
    public void setUp() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        DataBufferInt data = (DataBufferInt)image.getRaster().getDataBuffer();
        int[] pixels = data.getData();
        pixels[0] = 0xffff0000;
        pixels[1] = 0xff00ff00;
        pixels[2] = 0xff0000ff;
        pixels[3] = 0xffff0000;
        imageDataWithInt = new ImageData(2, 2, data);

        BufferedImage image2 = new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR);
        DataBufferByte data2 = (DataBufferByte) image2.getRaster().getDataBuffer();
        byte[] pixels2 = data2.getData();
        pixels2[0] = (byte) 0xff; // Alpha
        pixels2[1] = (byte) 0x00; // Blue
        pixels2[2] = (byte) 0xc0; // Green
        pixels2[3] = (byte) 0x0a; // Red
        imageDataWithByte = new ImageData(2, 2, data2, true);

        BufferedImage image3 = new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR);
        DataBufferByte data3 = (DataBufferByte) image3.getRaster().getDataBuffer();
        byte[] pixels3 = data3.getData();
        pixels3[0] = (byte) 0xff; // Blue
        pixels3[1] = (byte) 0x00; // Green
        pixels3[2] = (byte) 0xc0; // Red
        imageDataWithByteNoAlpha = new ImageData(2, 2, data3, false);
    }

    @Test
    public void checkWidth() {
        assertEquals(2, imageDataWithInt.getWidth());
        assertEquals(2, imageDataWithByte.getWidth());
        assertEquals(2, imageDataWithByteNoAlpha.getWidth());
    }

    @Test
    public void checkHeight() {
        assertEquals(2, imageDataWithInt.getHeight());
        assertEquals(2, imageDataWithByte.getHeight());
        assertEquals(2, imageDataWithByteNoAlpha.getWidth());
    }

    @Test
    public void checkInitColors() {
        assertEquals(new RgbPixel(255, 0, 0), imageDataWithInt.getColor(0, 0));
        assertEquals(new RgbPixel(0, 255, 0), imageDataWithInt.getColor(0, 1));
        assertEquals(new RgbPixel(0, 0, 255), imageDataWithInt.getColor(1, 0));
        assertEquals(new RgbPixel(255, 0, 0), imageDataWithInt.getColor(1, 1));

        assertEquals(new RgbPixel(0x0a, 0xc0, 0x00, 0xff), imageDataWithByte.getColor(0));

        assertEquals(new RgbPixel(0xc0, 0x00, 0xff), imageDataWithByteNoAlpha.getColor(0));
    }

    @Test
    public void checkSort() {
        imageDataWithInt.sortByA(); // ascending order, so it should be 'blue', 'green', 'red' and 'red'.
        assertEquals(new RgbPixel(0, 0, 255), imageDataWithInt.getColor(0));
        assertEquals(new RgbPixel(0, 255, 0), imageDataWithInt.getColor(1));
        assertEquals(new RgbPixel(255, 0, 0), imageDataWithInt.getColor(2));
        assertEquals(new RgbPixel(255, 0, 0), imageDataWithInt.getColor(3));
    }

    @Test
    public void checkGetMaxMinByR() {
        assertEquals(new RgbPixel(255, 0, 0), imageDataWithInt.getMaxByR());
        assertEquals(new RgbPixel(0, 0, 255), imageDataWithInt.getMinByR());
    }

    @Test
    public void checkGetMaxMinByG() {
        assertEquals(new RgbPixel(0, 255, 0), imageDataWithInt.getMaxByG());
        assertEquals(new RgbPixel(0, 0, 255), imageDataWithInt.getMinByG());
    }

    @Test
    public void checkGetMaxMinByB() {
        assertEquals(new RgbPixel(0, 0, 255), imageDataWithInt.getMaxByB());
        assertEquals(new RgbPixel(0, 255, 0), imageDataWithInt.getMinByB());
    }

    @Test
    public void checkGetMaxMinByA() {
        assertEquals(new RgbPixel(255, 0, 0, 255), imageDataWithInt.getMaxByA());
        assertEquals(new RgbPixel(0, 0, 255, 255), imageDataWithInt.getMinByA());
    }
}
