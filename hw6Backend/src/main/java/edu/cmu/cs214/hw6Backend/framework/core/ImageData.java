package edu.cmu.cs214.hw6Backend.framework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class ImageData {
    /**
     * Width of image.
     */
    private int width;

    /**
     * Height of image.
     */
    private int height;

    /**
     * Colors in the image.
     */
    private List<RgbPixel> colors;

    /**
     * Constructs an ImageData instance from DataBufferInt.
     * @param width Width of image, in pixels
     * @param height Height of image, in pixels
     * @param data DataBufferInt object, usually from a BufferedImage.TYPE_INT_RGB
     */
    public ImageData(int width, int height, DataBufferInt data) {
        this.width = width;
        this.height = height;
        colors = new ArrayList<RgbPixel>();

        // Get the int pixel array from the bufferedImage
        // Temporarily, assume the bufferedImage is in 'ARGB' type.
        int[] pixels = data.getData();
        
        for (int i = 0; i < width * height; i++) {
            // Solution: Add data instead of pure white
            int pixel = pixels[i];

            // bitwise operation to extract different color channel values from the pixel color value.
            int alpha = (pixel >> 24) & RgbPixel.COLOR_MAX;
            int red = (pixel >> 16) & RgbPixel.COLOR_MAX;
            int green = (pixel >> 8) & RgbPixel.COLOR_MAX;
            int blue = pixel & RgbPixel.COLOR_MAX;
            colors.add(new RgbPixel(red, green, blue, alpha));
        }
    }

    /**
     * Constructs an ImageData instance from DataBufferInt
     * @param width Width of image, in pixels
     * @param height Height of image, in pixels
     * @param colors List of colors to store in this ImageData instance
     */
    public ImageData(int width, int height, List<RgbPixel> colors) {
        this.width = width;
        this.height = height;
        this.colors = new ArrayList<RgbPixel>(colors);
    }

    /**
     * Default constructor.
     * Use for error-handling ONLY.
     */
    public ImageData() {
        this(0, 0, new ArrayList<RgbPixel>());
    }

    /**
     * Constructs an ImageData instance from DataBufferByte.
     * @param width Image width
     * @param height Image height
     * @param data DataBufferByte containing all pixels
     * @param hasAlpha Whether the image has an alpha channel
     * 
     * Note: For BufferedImage.TYPE_3BYTE_BGR, hasAlpha is false.
     * For BufferedImage.TYPE_4BYTE_ABGR, hasAlpha is true.
     */
    public ImageData(int width, int height, DataBufferByte data, boolean hasAlpha) {
        this.width = width;
        this.height = height;

        // Get the int pixel array from the bufferedImage
        // Temporarily, assume the bufferedImage is in 'ARGB' type.
        byte[] pixels = data.getData();
        colors = convertToRGBArray(pixels, hasAlpha);
    }

    /**
     * Inspired by this post:
     * https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image
     * Converts a byte array to List<RgbPixel>
     * @param data Byte array containing all pixels
     * @param hasAlpha Whether the image has an alpha channel
     */
    private List<RgbPixel> convertToRGBArray(byte[] pixels, boolean hasAlpha) {
        List<RgbPixel> result = new ArrayList<RgbPixel>(width * height);
        if (hasAlpha) {
           final int pixelLength = 4;
           for (int pixel = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
              int alpha = (int) pixels[pixel] & RgbPixel.COLOR_MAX;
              int blue = (int) pixels[pixel + 1] & RgbPixel.COLOR_MAX;
              int green = (int) pixels[pixel + 2] & RgbPixel.COLOR_MAX;
              int red = (int) pixels[pixel + 3] & RgbPixel.COLOR_MAX;
              result.add(new RgbPixel(red, green, blue, alpha));
           }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
               int alpha = RgbPixel.COLOR_MAX;
               int blue = (int) pixels[pixel] & RgbPixel.COLOR_MAX;
               int green = (int) pixels[pixel + 1] & RgbPixel.COLOR_MAX;
               int red = (int) pixels[pixel + 2] & RgbPixel.COLOR_MAX;
               result.add(new RgbPixel(red, green, blue, alpha));
            }
        }
        return result;
     }
    
    // ------------------------ Other utilities ------------------------

    /**
     * Get the corresponding RgbPixel according to the row and col.
     * @param row the row of the pixel in the image
     * @param col the col of the pixel in the image
     * @return the target RgbPixel object
     */
    public RgbPixel getColor(int row, int col) {
        if ((row < 0) || (row >= height) || (col < 0) || (col >= width)) {
            return new RgbPixel();
        }
        int idx = row * width + col;
        if (idx >= colors.size()) {
            return new RgbPixel();
        }
        RgbPixel res = colors.get(idx);
        return new RgbPixel(res.getRed(), res.getGreen(), res.getBlue(), res.getAlpha());
    }

    /**
     * Get the corresponding RgbPixel according to the index.
     * @param idx the index of the pixel in the color array
     * @return the target RgbPixel object
     */
    public RgbPixel getColor(int idx) {
        if ((idx < 0) || (idx >= colors.size())) {
            return new RgbPixel();
        }
        RgbPixel res = colors.get(idx);
        return new RgbPixel(res.getRed(), res.getGreen(), res.getBlue(), res.getAlpha());
    }

    /**
     * Get the height of the image data.
     * @return the height of the image data
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width of the image data.
     * @return the width of the image data
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get a copy of all colors in this image.
     * @return All colors stored in this image
     */
    public List<RgbPixel> getAllColors() {
        return new ArrayList<RgbPixel>(colors);
    }
    
    /**
     * Sort pixels, based on value of red, in ascending order
     */
    public void sortByR() {
        colors.sort(new RedComparator());
    }

    /**
     * Get the pixel with the maximum red value.
     * @return Pixel with the max R
     */
    public RgbPixel getMaxByR() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.max(colors, new RedComparator());
    }

    /**
     * Get the pixel with the minimum red value.
     * @return Pixel with the min R
     */
    public RgbPixel getMinByR() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.min(colors, new RedComparator());
    }

    private static class RedComparator implements Comparator<RgbPixel> {
        @Override
        public int compare(RgbPixel pixel1, RgbPixel pixel2) {
            int r1 = pixel1.getRed();
            int r2 = pixel2.getRed();
            if (r1 != r2) {
                return Integer.compare(r1, r2);
            }
            int g1 = pixel1.getGreen();
            int g2 = pixel2.getGreen();
            if (g1 != g2) {
                return Integer.compare(g1, g2);
            }
            int b1 = pixel1.getBlue();
            int b2 = pixel2.getBlue();
            if (b1 != b2) {
                return Integer.compare(b1, b2);
            }
            return Integer.compare(pixel1.getAlpha(), pixel2.getAlpha());
        }
    }

    /**
     * Sort pixels, based on value of green, in ascending order
     */
    public void sortByG() {
        colors.sort(new GreenComparator());
    }

    /**
     * Get the pixel with the maximum green value.
     * @return Pixel with the max G
     */
    public RgbPixel getMaxByG() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.max(colors, new GreenComparator());
    }

    /**
     * Get the pixel with the minimum green value.
     * @return Pixel with the min G
     */
    public RgbPixel getMinByG() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.min(colors, new GreenComparator());
    }

    private static class GreenComparator implements Comparator<RgbPixel> {
        @Override
        public int compare(RgbPixel pixel1, RgbPixel pixel2) {
            int g1 = pixel1.getGreen();
            int g2 = pixel2.getGreen();
            if (g1 != g2) {
                return Integer.compare(g1, g2);
            }
            int r1 = pixel1.getRed();
            int r2 = pixel2.getRed();
            if (r1 != r2) {
                return Integer.compare(r1, r2);
            }
            int b1 = pixel1.getBlue();
            int b2 = pixel2.getBlue();
            if (b1 != b2) {
                return Integer.compare(b1, b2);
            }
            return Integer.compare(pixel1.getAlpha(), pixel2.getAlpha());
        }
    }

    /**
     * Sort pixels, based on value of blue, in ascending order
     */
    public void sortByB() {
        colors.sort(new BlueComparator());
    }

    /**
     * Get the pixel with the maximum blue value.
     * @return Pixel with the max B
     */
    public RgbPixel getMaxByB() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.max(colors, new BlueComparator());
    }

    /**
     * Get the pixel with the minimum blue value.
     * @return Pixel with the min B
     */
    public RgbPixel getMinByB() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.min(colors, new BlueComparator());
    }

    private static class BlueComparator implements Comparator<RgbPixel> {
        @Override
        public int compare(RgbPixel pixel1, RgbPixel pixel2) {
            int b1 = pixel1.getBlue();
            int b2 = pixel2.getBlue();
            if (b1 != b2) {
                return Integer.compare(b1, b2);
            }
            int r1 = pixel1.getRed();
            int r2 = pixel2.getRed();
            if (r1 != r2) {
                return Integer.compare(r1, r2);
            }
            int g1 = pixel1.getGreen();
            int g2 = pixel2.getGreen();
            if (g1 != g2) {
                return Integer.compare(g1, g2);
            }
            return Integer.compare(pixel1.getAlpha(), pixel2.getAlpha());
        }
    }

    /**
     * Sort pixels, based on value of alpha, in ascending order
     */
    public void sortByA() {
        colors.sort(new AlphaComparator());
    }

    /**
     * Get the pixel with the maximum alpha value.
     * @return Pixel with the max A
     */
    public RgbPixel getMaxByA() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.max(colors, new AlphaComparator());
    }

    /**
     * Get the pixel with the minimum alpha value.
     * @return Pixel with the min A
     */
    public RgbPixel getMinByA() {
        if (colors.size() == 0) {
            return new RgbPixel();
        }
        return Collections.min(colors, new AlphaComparator());
    }

    private static class AlphaComparator implements Comparator<RgbPixel> {
        @Override
        public int compare(RgbPixel pixel1, RgbPixel pixel2) {
            int a1 = pixel1.getAlpha();
            int a2 = pixel2.getAlpha();
            if (a1 != a2) {
                return Integer.compare(a1, a2);
            }
            int r1 = pixel1.getRed();
            int r2 = pixel2.getRed();
            if (r1 != r2) {
                return Integer.compare(r1, r2);
            }
            int g1 = pixel1.getGreen();
            int g2 = pixel2.getGreen();
            if (g1 != g2) {
                return Integer.compare(g1, g2);
            }
            return Integer.compare(pixel1.getBlue(), pixel2.getBlue());
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Dimensions: ");
        str.append(width);
        str.append(" x ");
        str.append(height);
        str.append("\nColors:\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                str.append(getColor(i, j).toString());
                str.append(", ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
