package edu.cmu.cs214.hw6Backend.framework.core;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RgbPixelTest {
    private List<RgbPixel> pixels;
    @Before
    public void setUp() {
        pixels = new ArrayList<>();
        pixels.add(new RgbPixel(255, 0, 0, 255));
        pixels.add(new RgbPixel(0, 255, 0, 255));
        pixels.add(new RgbPixel(0, 0, 255, 255));
    }
    @Test
    public void checkGetRed() {
        RgbPixel pixel = pixels.get(2);
        assertEquals(0, pixel.getRed());
    }
    @Test
    public void checkGetGreen() {
        RgbPixel pixel = pixels.get(2);
        assertEquals(0, pixel.getGreen());
    }
    @Test
    public void checkGetBlue() {
        RgbPixel pixel = pixels.get(2);
        assertEquals(255, pixel.getBlue());
    }
    @Test
    public void checkGetAlpha() {
        RgbPixel pixel = pixels.get(2);
        assertEquals(255, pixel.getAlpha());
    }
    @Test
    public void checkMixPixels() {
        RgbPixel newPixel = RgbPixel.mixPixels(pixels);
        assertEquals(85, newPixel.getRed());
        assertEquals(85, newPixel.getGreen());
        assertEquals(85, newPixel.getBlue());
    }
    @Test
    public void checkGetColorHex() {
        RgbPixel pixel = pixels.get(0);
        int value = pixel.getColorHex();
        assertEquals(0xffff0000, value);
    }
    @Test
    public void checkGetColorHexString() {
        RgbPixel pixel = pixels.get(1);
        String string = pixel.getColorHexString();
        assertEquals("ff00ff00", string);
    }
}
