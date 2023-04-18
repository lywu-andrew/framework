package edu.cmu.cs214.hw6;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.cmu.cs214.hw6.plugin.PdfPlugin;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
 
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

/**
 * Unit tests for PdfPlugin
 * 
 * This test file mainly tests PdfPlugin.convertToString. We mock a file system
 * using JUnit TemporaryFolder and iTextPDF and write a small PDF with styling.
 * 
 * https://www.geeksforgeeks.org/formatting-the-text-in-a-pdf-using-java/
 */
public class PdfPluginTest {
    private PdfPlugin plugin;    
    private File file;

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() {
        plugin = new PdfPlugin();
        createTempFileSystem();
    }

    private void createTempFileSystem() {
        try {
            file = folder.newFile("testfile.pdf");
        }
        catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }
    }

    /**
     * Test a small pdf files with 2 paragraphs
     */
    @Test
    public void testSmallPdfFile() {
        // Write data to test files
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(file));
            Document doc = new Document(pdfDoc);

            // Adding text to the document
            Text text1 = new Text("Hi I'm Sunny Liang.");

            // Setting styling of text 1
            text1.setFontColor(ColorConstants.BLACK);
            text1.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));

            // Creating a paragraph 1
            Paragraph para1 = new Paragraph(text1);

            Text text2 = new Text("I'm a student at Carnegie Mellon University.");

            // Setting styling of text 2
            text2.setFontColor(ColorConstants.BLACK);
            text2.setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA));

            // Creating a paragraph 2
            Paragraph para2 = new Paragraph(text2);

            // Adding paragraphs to the document
            doc.add(para1);
            doc.add(para2);

            // Closing the document
            doc.close();
            System.out.println("Text added successfully..");
        }
        catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }

        // Test convertToString
        try {
            String str = plugin.convertToString(file.toPath());
            System.out.println(str);
            String expectedStr = "Hi I'm Sunny Liang.\nI'm a student at Carnegie Mellon University.\n";
            assertEquals(str, expectedStr);
        }
        catch (IOException ioe) {
            System.err.println("error running convertToString");
        }
    }
}
