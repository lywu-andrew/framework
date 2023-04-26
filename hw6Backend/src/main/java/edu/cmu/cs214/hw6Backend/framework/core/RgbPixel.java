package edu.cmu.cs214.hw6Backend.framework.core;

import java.util.List;
import java.util.Objects;

public final class RgbPixel {
    /**
     * Max value of one color (R/G/B/A).
     */
    public static final int COLOR_MAX = 255;
    
    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    /**
     * Takes average of all pixels provided.
     * @param pixels Pixels to mix
     * @return Average of all pixels
     */
    public static RgbPixel mixPixels(List<RgbPixel> pixels) {
        if (pixels == null || pixels.size() == 0) {
            return new RgbPixel();
        }
        int size = pixels.size();
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        for (RgbPixel pixel : pixels) {
            red = red + pixel.getRed();
            green = green + pixel.getGreen();
            blue = blue + pixel.getBlue();
            alpha = alpha + pixel.getAlpha();
        }
        red = red / size;
        green = green / size;
        blue = blue / size;
        alpha = alpha / size;
        return new RgbPixel(red, green, blue, alpha);
    }

    /**
     * Converts a hex number representing a pixel to a RgbPixel.
     * @param hex Hex representing a pixel
     * @return Corresponding RgbPixel
     */
    public static RgbPixel getPixelFromHex(int hex) {
        int lower8Mask = 0xff;
        int alphaMask = lower8Mask << 24;
        int alpha = ((alphaMask & hex) >> 24) & lower8Mask; // Avoid sign retention when right shifting
        int redMask = lower8Mask << 16;
        int red = (redMask & hex) >> 16;
        int greenMask = lower8Mask << 8;
        int green = (greenMask & hex) >> 8;
        int blue = lower8Mask & hex;
        return new RgbPixel(red, green, blue, alpha);
    }

    /**
     * Constructs a RgbPixel instance from ARGB.
     * @param red Red (0-255)
     * @param green Green (0-255)
     * @param blue Blue (0-255)
     * @param alpha Alpha (0-255)
     */
    public RgbPixel(int red, int green, int blue, int alpha) {
        if ((red < 0) || (red > COLOR_MAX) || (green < 0) || (green > COLOR_MAX) ||
            (blue < 0) || (blue > COLOR_MAX) || (alpha < 0) || (alpha > COLOR_MAX)) {
            // Shouldn't be allowed... Return a white pixel
            this.red = COLOR_MAX;
            this.blue = COLOR_MAX;
            this.green = COLOR_MAX;
            this.alpha = COLOR_MAX;
            return;
        }
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
    }

    /**
     * Constructs a RgbPixel instance from RGB.
     * @param red Red (0-255)
     * @param green Green (0-255)
     * @param blue Blue (0-255)
     * Alpha will be initialized to max (255)
     */
    public RgbPixel(int red, int green, int blue) {
        this(red, green, blue, COLOR_MAX);
    }

    /**
     * Default constructor, create a pure white pixel.
     * Use for error-handling ONLY!
     */
    public RgbPixel() {
        this(COLOR_MAX, COLOR_MAX, COLOR_MAX, COLOR_MAX);
    }

    // ------------------------ Other utilities ------------------------

    /**
     * Get the pixel's color value in hex.
     * ARGB in hex should still be an int value.
     * @return ARGB hexadecimal color value 
     */
    public int getColorHex() {
        // Change logic: convert the four channel values into one hexadecimal value
        int argb = (alpha << 24) | (red << 16) | (green << 8) | blue;
        return argb;
    }

    /**
     * A supplementary method to 'getColorHex'. It returns the Hex string.
     * @return ARGB hexadecimal color value string
     */
    public String getColorHexString() {
        int argb = getColorHex();
        return Integer.toHexString(argb);
    }

    /**
     * Get the red channel value of this pixel.
     * @return red channel value
     */
    public int getRed() {
        return red;
    }

    /**
     * Get the green channel value of this pixel.
     * @return green channel value
     */
    public int getGreen() {
        return green;
    }

    /**
     * Get the blue channel value of this pixel.
     * @return blue channel value
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Get the alpha channel value of this pixel.
     * @return alpha channel value
     */
    public int getAlpha() {
        return alpha;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RgbPixel)) {
            return false;
        }
        RgbPixel other = (RgbPixel) obj;
        return alpha == other.alpha && red == other.red && green == other.green && blue == other.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, red, green, blue);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("(R: ");
        str.append(red);
        str.append(", G: ");
        str.append(green);
        str.append(", B: ");
        str.append(blue);
        str.append(", A: ");
        str.append(alpha);
        str.append(", Hex: ");
        str.append(getColorHexString());
        str.append(")");
        return str.toString();
    }
}
