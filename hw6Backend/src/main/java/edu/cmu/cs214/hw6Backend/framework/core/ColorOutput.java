package edu.cmu.cs214.hw6Backend.framework.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColorOutput {
    /**
     * Colors in the theme.
     */
    private List<RgbPixel> colors;

    /**
     * Default constructor, create an empty output.
     * Only used for error-handling.
     */
    public ColorOutput() {
        this(new ArrayList<RgbPixel>(0));
    }

    /**
     * Constructs a ColorOutput instance from input list of colors.
     * @param colors Colors for this output.
     */
    public ColorOutput(List<RgbPixel> colors) {
        this.colors = new ArrayList<RgbPixel>(colors);
    }

    /**
     * Get the number of colors.
     * @return the number of distinct colors in the image.
     */
    public int getSize() {
        return colors.size();
    }

    /**
     * Get color at index.
     * @param index Index to get color from
     * @return Color at index
     */
    public RgbPixel getColor(int index) {
        if ((index < 0) || (index >= getSize())) {
            return new RgbPixel();
        }
        // Change logic: returns the Color at index.
        return colors.get(index);
    }

    /**
     * Get color at index, in hex. (ARGB value)
     * ARGB in hex should still be an int value.
     * @param index Index to get color from
     * @return Color at index, in hex
     */
    public int getColorHex(int index) {
        return getColor(index).getColorHex();
    }


    /**
     * A supplementary method to 'getColorHex'. It returns the Hex string.
     * @param index Index to get color from
     * @return ARGB hexadecimal color value string
     */
    public String getColorHexString(int index) {
        return getColor(index).getColorHexString();
    }

    /**
     * Get color at index.
     * @param index Index to get color from
     * @return Color at index
     */
    public List<RgbPixel> getAllColors() {
        return new ArrayList<RgbPixel>(colors);
    }

    /**
     * Sort pixels, from 0x00_00_00_00 to 0xFF_FF_FF_FF, in ascending order
     * Order: A_R_G_B
     * This gaurantees that pure black (0x00000000) is always ranked first
     * and pure white (0xFFFFFFFF) is always ranked last.
     */
    public void sort() {
        colors.sort(new RgbPixelComparator());
    } 

    private static class RgbPixelComparator implements Comparator<RgbPixel> {
        @Override
        public int compare(RgbPixel pixel1, RgbPixel pixel2) {
            int argb1 = ((pixel1.getAlpha() << 24) | (pixel1.getRed() << 16) |
                         (pixel1.getGreen() << 8) | pixel1.getBlue());
            int argb2 = ((pixel2.getAlpha() << 24) | (pixel2.getRed() << 16) |
                         (pixel2.getGreen() << 8) | pixel2.getBlue());
            return Integer.compare(argb1, argb2);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Size: ");
        str.append(colors.size());
        str.append("\nColors:\n");
        for (RgbPixel color : colors) {
            str.append(color.toString());
            str.append(", ");
        }
        return str.toString();
    }
}
