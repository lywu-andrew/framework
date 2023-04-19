package edu.cmu.cs214.hw6;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.cmu.cs214.hw6.plugin.data.TxtPlugin;

import org.apache.commons.text.RandomStringGenerator;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Unit tests for TxtPlugin
 * 
 * This test file mainly tests TxtPlugin.convertToString. We mock a file system
 * using JUnit TemporaryFolder and IO FileWriter and write small, medium, and
 * large test files containing characters from UTF-8 ' ' to '~'.
 * 
 * https://blogs.oracle.com/javamagazine/post/working-and-unit-testing-with-temporary-files-in-java
 */
public class TxtPluginTest {
    private TxtPlugin plugin;    
    private File file1, file2;

    /* This folder and the files created in it will be deleted after
     * tests are run, even in the event of failures or exceptions.
     */
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() {
        plugin = new TxtPlugin();
        createTempFileSystem();
    }

    private void createTempFileSystem() {
        try {
            file1 = folder.newFile( "testfile1.txt" );
            file2 = folder.newFile( "testfile2.txt" );
        }
        catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }
    }

    /**
     * Test 2 small txt files with <20 characters each
     */
    @Test
    public void testSmallTxtFiles() {
        // Write data to test files
        try {
            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter bw1 = new BufferedWriter(fw1);
            bw1.write("content for file1");
            bw1.close();
            
            FileWriter fw2 = new FileWriter(file2);
            BufferedWriter bw2 = new BufferedWriter(fw2);
            bw2.write("content for file2");
            bw2.close();
        }
        catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }

        // Test convertToString
        try {
            String str1 = plugin.convertToString(file1.toPath());
            assertEquals(str1, "content for file1");
        }
        catch (IOException ioe) {
            System.err.println("error running convertToString");
        }

        try {
            String str2 = plugin.convertToString(file2.toPath());
            assertEquals(str2, "content for file2");
        }
        catch (IOException ioe) {
            System.err.println("error running convertToString");
        }
    }

    /**
     * Test a medium txt file with 2000 randomly generated characters
     */
    @Test
    public void testMediumTxtFile() {
        // Generates a 20 code point string, using only the letters a-z
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange(' ', '~')
            .build();
        String randomLetters = generator.generate(2000);

        // Write data to test file
        try {
            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter bw1 = new BufferedWriter(fw1);
            bw1.write(randomLetters);
            bw1.close();
        } catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }

        try {
            String str1 = plugin.convertToString(file1.toPath());
            assertEquals(str1, randomLetters);
        }
        catch (IOException ioe) {
            System.err.println("error running convertToString");
        }
    }

    /**
     * Test a large txt file with 100000 randomly generated characters
     */
    @Test
    public void testLargeTxtFile() {
        // Generates a 20 code point string, using only the letters a-z
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange(' ', '~')
            .build();
        String randomLetters = generator.generate(100000);

        // Write data to test file
        try {
            FileWriter fw1 = new FileWriter(file1);
            BufferedWriter bw1 = new BufferedWriter(fw1);
            bw1.write(randomLetters);
            bw1.close();
        } catch (IOException ioe) {
            System.err.println( 
                "error creating temporary test file in " +
                this.getClass().getSimpleName() );
        }

        try {
            String str1 = plugin.convertToString(file1.toPath());
            assertEquals(str1, randomLetters);
        }
        catch (IOException ioe) {
            System.err.println("error running convertToString");
        }
    }

}
