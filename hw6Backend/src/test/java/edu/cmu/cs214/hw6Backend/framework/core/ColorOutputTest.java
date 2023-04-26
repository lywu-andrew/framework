package edu.cmu.cs214.hw6Backend.framework.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ColorOutputTest {
    private ColorOutput output;
    private ColorOutput outputForSorting;

    @Before
    public void setUp() {
        List<RgbPixel> pixels = new ArrayList<RgbPixel>(3);
        for (int i = 0; i < 3; i++) {
            pixels.add(new RgbPixel(255, 0, 0));
        }
        output = new ColorOutput(pixels);

        pixels = new ArrayList<RgbPixel>(3);
        pixels.add(new RgbPixel(25, 61, 209));
        pixels.add(new RgbPixel(255, 30, 69, 2));
        pixels.add(new RgbPixel(0, 0, 255, 255));
        outputForSorting = new ColorOutput(pixels);
    }

    @Test
    public void checkGetSize() {
        assertEquals(3, output.getSize());
    }

    @Test
    public void checkGetColorOut() {
        // 4 is out of bound 3; we handle this by returning a white pixel.
        RgbPixel pixel = output.getColor(4);
        assertEquals(new RgbPixel(), pixel);
    }

    @Test
    public void checkGetColorIn() {
        RgbPixel pixel = output.getColor(1);
        assertEquals(new RgbPixel(255, 0, 0), pixel);
    }

    @Test
    public void checkGetColorHex() {
        int hexValue = output.getColorHex(0);
        assertEquals(0xffff0000, hexValue);
    }

    @Test
    public void checkGetColorHexString() {
        String hexString = output.getColorHexString(0);
        assertEquals("ffff0000", hexString);
    }

    private void verifyRGBA(RgbPixel pixel, int expectedR, int expectedG, int expectedB, int expectedA) {
        assertEquals(expectedR, pixel.getRed());
        assertEquals(expectedG, pixel.getGreen());
        assertEquals(expectedB, pixel.getBlue());
        assertEquals(expectedA, pixel.getAlpha());
    }

    @Test
    public void checkSorting() {
        outputForSorting.sort();
        verifyRGBA(outputForSorting.getColor(0), 0, 0, 255, 255);
        verifyRGBA(outputForSorting.getColor(1), 25, 61, 209, 255);
        verifyRGBA(outputForSorting.getColor(2), 255, 30, 69, 2);
    }

    @Test
    public void checkGetAllColors() {
        List<RgbPixel> result = outputForSorting.getAllColors();
        verifyRGBA(result.get(0), 25, 61, 209, 255);
        verifyRGBA(result.get(1), 255, 30, 69, 2);
        verifyRGBA(result.get(2), 0, 0, 255, 255);

        // Check no aliasing
        result.clear();
        assertTrue(result.isEmpty());
        assertFalse(outputForSorting.getAllColors().isEmpty());
    }
}
