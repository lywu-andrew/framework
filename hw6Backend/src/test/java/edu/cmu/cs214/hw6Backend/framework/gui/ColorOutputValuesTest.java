package edu.cmu.cs214.hw6Backend.framework.gui;

import static org.junit.Assert.*;

import edu.cmu.cs214.hw6Backend.framework.core.ColorOutput;
import edu.cmu.cs214.hw6Backend.framework.core.RgbPixel;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ColorOutputValuesTest {
    @Test
    public void testRegularOutput() {
        List<RgbPixel> pixels = new ArrayList<RgbPixel>(3);
        pixels.add(new RgbPixel(25, 61, 209));
        pixels.add(new RgbPixel(255, 30, 69, 2));
        pixels.add(new RgbPixel(0, 0, 255, 255));
        ColorOutput output = new ColorOutput(pixels);
        ColorOutputValues actual = ColorOutputValues.forColorOutput(output);

        String expected = "[%d, %d, %d]".formatted(pixels.get(2).getColorHex(), 
                                                   pixels.get(0).getColorHex(),
                                                   pixels.get(1).getColorHex());

        assertEquals(expected, actual.toString());
    }

    @Test
    public void testEmptyOutput() {
        List<RgbPixel> pixels = new ArrayList<RgbPixel>(0);
        ColorOutput output = new ColorOutput(pixels);
        ColorOutputValues actual = ColorOutputValues.forColorOutput(output);

        String expected = "[]";

        assertEquals(expected, actual.toString());
    }
}
