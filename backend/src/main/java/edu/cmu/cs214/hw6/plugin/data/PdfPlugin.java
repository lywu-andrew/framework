package edu.cmu.cs214.hw6.plugin.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.ESAFramework;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfPlugin implements DataPlugin {
    private final String PLUGIN_NAME = "pdf";
    
    private ESAFramework framework;

    /**
     * Gets the name of the data plugin.
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Convert the text in the PDF at the given path to a String.
     * https://www.tutorialspoint.com/how-to-read-data-from-pdf-file-and-display-on-console-in-java
     * @throws IOException
     */
    @Override
    public String convertToString(Path filePath) throws IOException {
        // Loading an existing document
        File file = filePath.toFile();
        PDDocument document = Loader.loadPDF(file);

        // Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();

        // Retrieving text from PDF document
        String text = pdfStripper.getText(document);

        // Closing the document
        document.close();
        
        return text;
    }

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link ESAFramework} instance with which
     *                  the plug-in was registered.
     */
    @Override
    public void onRegister(ESAFramework f) {
        framework = f;
    }
}
