package edu.cmu.cs214.hw6Backend.framework.gui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import edu.cmu.cs214.hw6Backend.framework.core.ColorFramework;
import edu.cmu.cs214.hw6Backend.framework.core.ColorFrameworkImpl;
import edu.cmu.cs214.hw6Backend.framework.core.ColorOutput;
import edu.cmu.cs214.hw6Backend.framework.core.DataPlugin;
import edu.cmu.cs214.hw6Backend.framework.core.ImageData;
import edu.cmu.cs214.hw6Backend.framework.core.RgbPixel;

import java.awt.image.DataBufferInt;

import org.junit.Before;
import org.junit.Test;

public class ColorFrameworkStateTest {
    private static final String sampleDataURI = "data:text/plain;base64,YXNkYXNkZmFz";
    private DataPlugin usefulPlugin;
    private DataPlugin dummyPlugin1;
    private DataPlugin dummyPlugin2;
    private ColorFrameworkImpl framework;

    /**
     * Fake implementation of DataPlugin, with some useful responses.
     */
    private static class FakeDataPluginMain implements DataPlugin {
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
        usefulPlugin = new FakeDataPluginMain();
        framework = new ColorFrameworkImpl(3);

        // Set up some stubs
        dummyPlugin1 = mock(DataPlugin.class);
        when(dummyPlugin1.isValidFileType("true")).thenReturn(false);
        when(dummyPlugin1.convertFileToDataURI("true")).thenReturn("");
        when(dummyPlugin1.convertFileToImage("true")).thenReturn(new ImageData());
        when(dummyPlugin1.getSupportedFileType()).thenReturn("Lite1");
        when(dummyPlugin1.getPluginName()).thenReturn("Haha");
        dummyPlugin2 = mock(DataPlugin.class);
        when(dummyPlugin2.isValidFileType("true")).thenReturn(false);
        when(dummyPlugin2.convertFileToDataURI("true")).thenReturn("");
        when(dummyPlugin2.convertFileToImage("true")).thenReturn(new ImageData());
        when(dummyPlugin2.getSupportedFileType()).thenReturn("Lite2");
        when(dummyPlugin2.getPluginName()).thenReturn("Blah");

        framework.registerDataPlugin(usefulPlugin);
        framework.registerDataPlugin(dummyPlugin1);
        framework.registerDataPlugin(dummyPlugin2);
        framework.setFilePath("true");
    }

    @Test
    public void testRegularOutput() {
        assertTrue(framework.loadImageAndGetDataURI());
        assertTrue(framework.convertFileToImageData());
        assertTrue(framework.convertImageToColors());

        ColorOutputValues values = ColorOutputValues.forColorOutput(framework.getColorOutput());
        ColorFrameworkState actual = ColorFrameworkState.forColorFramework(framework);

        String expected = """
            {
                "fileTypes": ["%s", "%s", "%s"],
                "numColors": %d,
                "dataURI": "%s",
                "output": %s
            }
           """.formatted(usefulPlugin.getSupportedFileType(), dummyPlugin1.getSupportedFileType(),
                         dummyPlugin2.getSupportedFileType(), framework.getNumColors(),
                         sampleDataURI, values.toString());

        assertEquals(expected, actual.toString());
    }

    @Test
    public void testNoOutput() {
        assertTrue(framework.loadImageAndGetDataURI());

        ColorOutputValues values = ColorOutputValues.forColorOutput(new ColorOutput());
        ColorFrameworkState actual = ColorFrameworkState.forColorFramework(framework);

        String expected = """
            {
                "fileTypes": ["%s", "%s", "%s"],
                "numColors": %d,
                "dataURI": "%s",
                "output": %s
            }
           """.formatted(usefulPlugin.getSupportedFileType(), dummyPlugin1.getSupportedFileType(),
                         dummyPlugin2.getSupportedFileType(), framework.getNumColors(),
                         sampleDataURI, values.toString());

        assertEquals(expected, actual.toString());
    }

    @Test
    public void testNoImageLoaded() {
        ColorOutputValues values = ColorOutputValues.forColorOutput(new ColorOutput());
        ColorFrameworkState actual = ColorFrameworkState.forColorFramework(framework);

        String expected = """
            {
                "fileTypes": ["%s", "%s", "%s"],
                "numColors": %d,
                "dataURI": "%s",
                "output": %s
            }
           """.formatted(usefulPlugin.getSupportedFileType(), dummyPlugin1.getSupportedFileType(),
                         dummyPlugin2.getSupportedFileType(), framework.getNumColors(),
                         "", values.toString());

        assertEquals(expected, actual.toString());
    }
}
