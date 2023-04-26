package edu.cmu.cs214.hw6Backend.framework.gui;

import edu.cmu.cs214.hw6Backend.framework.core.ColorFrameworkImpl;

/**
 * Immutable class for sending ColorFramework's states to the frontend.
 */
public final class ColorFrameworkState {
    /**
     * Array of supported file type names (e.g. "png", "JPG", "pdf")
     */
    private final String[] supportedFileTypes;

    /**
     * Number of colors that will be included in the output list.
     */
    private final int numColors;

    /**
     * Result of analyzing input image.
     */
    private final ColorOutputValues output;

    /**
     * Data URI of uploaded image, if any.
     */
    private final String dataURI;

    private ColorFrameworkState(String[] supportedFileTypes, int numColors,
                                ColorOutputValues output, String dataURI) {
        this.supportedFileTypes = supportedFileTypes;
        this.numColors = numColors;
        this.output = output;
        this.dataURI = dataURI;
    }

    /**
     * Constructs immutable ColorFrameworkState object.
     * @param framework Framework to convert
     * @return An instance of this class, representing input ColorFramework
     */
    public static ColorFrameworkState forColorFramework(ColorFrameworkImpl framework) {
        String[] fileTypes = framework.getSupportedFileTypes();
        int numColors = framework.getNumColors();
        ColorOutputValues output = ColorOutputValues.forColorOutput(framework.getColorOutput());
        String dataURI = framework.getDataURI();
        return new ColorFrameworkState(fileTypes, numColors, output, dataURI);
    }

    /**
     * Convert list of supported file type strings to valid JSON.
     * @return JSON string
     */
    private String supportedFileTypesToString() {
        StringBuilder str = new StringBuilder("[");
        for (int i = 0; i < supportedFileTypes.length - 1; i++) {
            str.append('"');
            str.append(supportedFileTypes[i]);
            str.append('"');
            str.append(", ");
        }
        str.append('"');
        str.append(supportedFileTypes[supportedFileTypes.length - 1]);
        str.append('"');
        str.append("]");
        return str.toString();
    }

    @Override
    public String toString() {
        return """
                {
                    "fileTypes": %s,
                    "numColors": %d,
                    "dataURI": "%s",
                    "output": %s
                }
               """.formatted(supportedFileTypesToString(),
                             this.numColors, this.dataURI, this.output.toString());
    }
}
