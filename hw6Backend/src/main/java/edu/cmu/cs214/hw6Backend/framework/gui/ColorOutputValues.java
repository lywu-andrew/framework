package edu.cmu.cs214.hw6Backend.framework.gui;

import edu.cmu.cs214.hw6Backend.framework.core.ColorOutput;

import java.util.Arrays;

/**
 * Immutable class for sending ColorOutput to the frontend.
 */
public final class ColorOutputValues {
    /**
     * Colors in the output, in hex, in the form 0xAARRGGBB.
     */
    private final int[] colors;

    private ColorOutputValues(int[] colors) {
        this.colors = colors;
    }

    /**
     * Constructs immutable ColorOutputValues object.
     * @param output ColorOutput to convert
     * @return An instance of this class, representing input ColorOutput
     */
    public static ColorOutputValues forColorOutput(ColorOutput output) {
        int[] result = new int[output.getSize()];
        output.sort();
        for (int i = 0; i < result.length; i++) {
            result[i] = output.getColorHex(i);
        }
        return new ColorOutputValues(result);
    }

    @Override
    public String toString() {
        return Arrays.toString(this.colors);
    }
}
