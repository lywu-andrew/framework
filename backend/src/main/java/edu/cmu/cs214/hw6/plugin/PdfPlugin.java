package edu.cmu.cs214.hw6.plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.ESAFramework;

import com.snowtide.PDF;
import com.snowtide.pdf.Document;
import com.snowtide.pdf.OutputTarget;

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
     * @throws IOException
     */
    @Override
    public String convertToString(Path filePath) throws IOException {
        String pdfFilePath = filePath.toString();
 
        Document pdf = PDF.open(pdfFilePath);
        StringBuilder text = new StringBuilder(1024);
        pdf.pipe(new OutputTarget(text));
        pdf.close();
        
        return text.toString();
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
