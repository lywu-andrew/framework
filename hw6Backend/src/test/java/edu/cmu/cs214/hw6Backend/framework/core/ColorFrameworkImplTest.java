package edu.cmu.cs214.hw6Backend.framework.core;

import static org.junit.Assert.*;

import java.awt.image.DataBufferInt;

import org.junit.Before;
import org.junit.Test;

public class ColorFrameworkImplTest {
    private static final String sampleDataURI = "data:text/plain;base64,YXNkYXNkZmFz";
    /**
     * ColorFramework to be tested.
     */
    private ColorFrameworkImpl frameworkToTest;

    /**
     * Fake DataPlugin for mimicking DataPlugin behaviors.
     */
    private DataPlugin fakeDataPlugin;

    /**
     * Fake implementation of DataPlugin.
     */
    private static class FakeDataPlugin implements DataPlugin {
        public void onRegister(ColorFramework framework) {
            return;
        }

        public boolean isValidFileType(String filePath) {
            if (filePath.equals("true")) {
                return true;
            }
            return false;
        }

        public String convertFileToDataURI(String filePath) {
            return sampleDataURI;
        }

        public ImageData convertFileToImage(String filePath) {
            if (filePath.equals("false")) {
                return new ImageData();
            }
            RgbPixel pixel1 = new RgbPixel(255, 192, 30);
            RgbPixel pixel2 = new RgbPixel(255, 66, 123);
            RgbPixel pixel3 = new RgbPixel(123, 66, 30, 254);
            RgbPixel pixel4 = new RgbPixel(55, 192, 30, 60);
            RgbPixel pixel5 = new RgbPixel(255, 192, 30);
            RgbPixel pixel6 = new RgbPixel(255, 192, 30);
            RgbPixel pixel7 = new RgbPixel(55, 192, 30, 60);
            RgbPixel pixel8 = new RgbPixel(123, 66, 30, 254);
            RgbPixel pixel9 = new RgbPixel(255, 192, 30);
            int[] data = {pixel1.getColorHex(), pixel2.getColorHex(), pixel3.getColorHex(),
                          pixel4.getColorHex(), pixel5.getColorHex(), pixel6.getColorHex(),
                          pixel7.getColorHex(), pixel8.getColorHex(), pixel9.getColorHex()};
            DataBufferInt dataBuffer = new DataBufferInt(data, 9);
            return new ImageData(3, 3, dataBuffer);
        }

        public String getSupportedFileType() {
            return "Dummy";
        }

        public String getPluginName() {
            return "This is a fake...";
        }
    }

    @Before
    public void setUp() {
        frameworkToTest = new ColorFrameworkImpl();
        fakeDataPlugin = new FakeDataPlugin();
        frameworkToTest.setFilePath("true");
    }

    @Test
    public void testRegisterDataPlugin() {
        assertTrue(frameworkToTest.registerDataPlugin(fakeDataPlugin));
        assertFalse(frameworkToTest.registerDataPlugin(fakeDataPlugin));
        assertFalse(frameworkToTest.registerDataPlugin(fakeDataPlugin));
    }

    @Test
    public void testSetNumColor() {
        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        frameworkToTest.setNumColors(9);
        assertEquals(9, frameworkToTest.getNumColors());
        frameworkToTest.setNumColors(1029319);
        assertEquals(1029319, frameworkToTest.getNumColors());
    }

    @Test
    public void testSetFilePath() {
        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        frameworkToTest.setFilePath("false");
        assertEquals("false", frameworkToTest.getFilePath());
        assertFalse(frameworkToTest.loadImageAndGetDataURI());
        frameworkToTest.setFilePath("");
        assertEquals("false", frameworkToTest.getFilePath());
        assertFalse(frameworkToTest.loadImageAndGetDataURI());
        frameworkToTest.setFilePath(null);
        assertEquals("false", frameworkToTest.getFilePath());
        assertFalse(frameworkToTest.loadImageAndGetDataURI());
    }

    @Test
    public void testLoadImageAndGetDataURI() {
        // Should fail without registering a DataPlugin
        assertFalse(frameworkToTest.loadImageAndGetDataURI());

        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertEquals(sampleDataURI, frameworkToTest.getDataURI());
    }


    private void verifyRGBA(RgbPixel pixel, int expectedR, int expectedG, int expectedB, int expectedA) {
        assertEquals(expectedR, pixel.getRed());
        assertEquals(expectedG, pixel.getGreen());
        assertEquals(expectedB, pixel.getBlue());
        assertEquals(expectedA, pixel.getAlpha());
    }
    
    @Test
    public void testConvertFileToImageData() {
        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertTrue(frameworkToTest.convertFileToImageData());

        ImageData img = frameworkToTest.getImageData();
        assertTrue(img != null);

        verifyRGBA(img.getColor(0), 255, 192, 30, 255);
        verifyRGBA(img.getColor(4), 255, 192, 30, 255);
        verifyRGBA(img.getColor(5), 255, 192, 30, 255);
        verifyRGBA(img.getColor(8), 255, 192, 30, 255);
        verifyRGBA(img.getColor(1), 255, 66, 123, 255);
        verifyRGBA(img.getColor(2), 123, 66, 30, 254);
        verifyRGBA(img.getColor(7), 123, 66, 30, 254);
        verifyRGBA(img.getColor(3), 55, 192, 30, 60);
        verifyRGBA(img.getColor(6), 55, 192, 30, 60);
    }

    @Test
    public void testConvertToColorNotEnoughUniqueColors() {
        assertFalse(frameworkToTest.convertImageToColors());
        frameworkToTest.setNumColors(6);

        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertTrue(frameworkToTest.convertFileToImageData());
        assertTrue(frameworkToTest.convertImageToColors());

        ColorOutput result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(6, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 123, 66, 30, 254);
        verifyRGBA(result.getColor(2), 55, 192, 30, 60);
        verifyRGBA(result.getColor(3), 255, 66, 123, 255);
        verifyRGBA(result.getColor(4), 255, 192, 30, 255);
        verifyRGBA(result.getColor(5), 123, 66, 30, 254);
    }

    @Test
    public void testConvertToColorEnoughUniqueColors() {
        frameworkToTest.setNumColors(3);

        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertTrue(frameworkToTest.convertFileToImageData());
        assertTrue(frameworkToTest.convertImageToColors());

        ColorOutput result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(3, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 55, 192, 30, 60);
        verifyRGBA(result.getColor(2), 123, 66, 30, 254);
    }

    @Test
    public void testIncreaseNumColorAfterFirstComputation() {
        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        frameworkToTest.setNumColors(3);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertTrue(frameworkToTest.convertFileToImageData());
        assertTrue(frameworkToTest.convertImageToColors());

        ColorOutput result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(3, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 55, 192, 30, 60);
        verifyRGBA(result.getColor(2), 123, 66, 30, 254);

        // Increase number of colors
        frameworkToTest.setNumColors(6);
        assertEquals(6, frameworkToTest.getNumColors());

        result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(6, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 123, 66, 30, 254);
        verifyRGBA(result.getColor(2), 55, 192, 30, 60);
        verifyRGBA(result.getColor(3), 255, 66, 123, 255);
        verifyRGBA(result.getColor(4), 255, 192, 30, 255);
        verifyRGBA(result.getColor(5), 123, 66, 30, 254);
    }


    @Test
    public void testDecreaseNumColorAfterFirstComputation() {
        frameworkToTest.registerDataPlugin(fakeDataPlugin);
        frameworkToTest.setNumColors(6);
        assertTrue(frameworkToTest.loadImageAndGetDataURI());
        assertTrue(frameworkToTest.convertFileToImageData());
        assertTrue(frameworkToTest.convertImageToColors());

        ColorOutput result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(6, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 123, 66, 30, 254);
        verifyRGBA(result.getColor(2), 55, 192, 30, 60);
        verifyRGBA(result.getColor(3), 255, 66, 123, 255);
        verifyRGBA(result.getColor(4), 255, 192, 30, 255);
        verifyRGBA(result.getColor(5), 123, 66, 30, 254);

        // Increase number of colors
        frameworkToTest.setNumColors(3);
        assertEquals(3, frameworkToTest.getNumColors());

        result = frameworkToTest.getColorOutput();
        assertTrue(result != null);
        assertEquals(3, result.getSize());

        verifyRGBA(result.getColor(0), 255, 192, 30, 255);
        verifyRGBA(result.getColor(1), 55, 192, 30, 60);
        verifyRGBA(result.getColor(2), 123, 66, 30, 254);
    }
}
