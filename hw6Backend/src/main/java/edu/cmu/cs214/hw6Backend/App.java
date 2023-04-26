package edu.cmu.cs214.hw6Backend;

import edu.cmu.cs214.hw6Backend.framework.core.ColorFramework;
import edu.cmu.cs214.hw6Backend.framework.core.ColorFrameworkImpl;
import edu.cmu.cs214.hw6Backend.framework.core.DataPlugin;
import edu.cmu.cs214.hw6Backend.framework.gui.ColorFrameworkState;

import fi.iki.elonen.NanoHTTPD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class App extends NanoHTTPD {
  private ColorFrameworkImpl framework;
  private List<DataPlugin> dataPlugins;
  private File imageFile;

  public static void main(String[] args) {
    try {
      new App();
    } catch (IOException ioe) {
      System.err.println("Couldn't start server:\n" + ioe);
    }
  }
 
  /**
   * Start the server at :8080 port.
   *
   * @throws IOException
   */
  public App() throws IOException {
    super(8080);

    this.framework = new ColorFrameworkImpl();
    dataPlugins = loadDataPlugins();
    for (DataPlugin p : dataPlugins) {
      framework.registerDataPlugin(p);
    }

    start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    System.out.println("\nRunning!\n");
  }

  /**
   * Load plugins listed in META-INF/services/...
   *
   * @return List of instantiated plugins
   */
  private static List<DataPlugin> loadDataPlugins() {
    ServiceLoader<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class);
    List<DataPlugin> result = new ArrayList<>();
    for (DataPlugin plugin : plugins) {
      System.out.println("Loaded plugin " + plugin.getPluginName());
      result.add(plugin);
    }
    return result;
  }

  /**
   * Processes request and generates response.
   *
   * @return Response in JSON format
   */
  @Override
  public Response serve(IHTTPSession session) {
    parseRequest(session);

    // Extract the view-specific data from the game and apply it to the template.
    ColorFrameworkState colors = ColorFrameworkState.forColorFramework(this.framework);
    // System.out.println(colors);
    Response response = newFixedLengthResponse(colors.toString());
    response.addHeader("Access-Control-Allow-Origin", "*"); // Allow CORS
    return response;
  }

  /**
   * Interprets HTTP request from UI client.
   *
   * @param uri Request URI
   * @param params Request parameters, if any
   */
  private void parseRequest(IHTTPSession session) {
    String uri = session.getUri();
    Map<String, String> params = session.getParms();
    Method method = session.getMethod();

    if (method == Method.POST && uri.equals("/upload")) {
      String filePath = uploadImage(session, params);
      if (filePath == null) {
        framework.clearResults();
        return;
      }

      framework.setFilePath(filePath);
      runColorAnalysis();

    } else if (uri.equals("/sliderChange")) {
      int num = Integer.parseInt(params.get("x"));
      framework.setNumColors(num);

    } else if (uri.equals("/getUrl")) {
      String inputUrl = params.get("x");
      try {
        String filePath = downloadFileFromUrl(inputUrl);
        framework.setFilePath(filePath);
        runColorAnalysis();
      } catch (IOException e) {
        framework.clearResults();
      }
      
    } else {
      framework.clearResults();
    }
  }

  /**
   * Runs color analysis with the framework.
   * Clears all results if framework encounters an error.
   */
  private void runColorAnalysis() {
    if ((!framework.loadImageAndGetDataURI()) ||
         !framework.convertFileToImageData() ||
         !framework.convertImageToColors()) {
      framework.clearResults();
    }
  }

  /**
   * Processes an uploaded image from the frontend.
   * @param session IHTTP server session
   * @param params Parameters from the current request
   * @return Absolute file path to local version of uploaded file
   */
  private String uploadImage(IHTTPSession session, Map<String, String> params) {
    try {
      Map<String, String> files = new HashMap<String, String>();
      session.parseBody(files);
      String extension = ColorFramework.getFileExtension(session.getParms().get("image_file"));

      // Create a temporary file to hold the uploaded data
      imageFile =
          new File(
              files.get(
                  "image_file")); // <------- ideally use imageFile, writing and reading from the
      // saved file is slower
      InputStream inputStream = new FileInputStream(imageFile);

      // Create a new file to hold the uploaded data
      String dirPath = "src/main/java/edu/cmu/cs214/hw6Backend/files";
      File outputFile = new File(dirPath);
      outputFile.mkdirs();
      outputFile = new File(dirPath + "/file." + extension);
      OutputStream outputStream = new FileOutputStream(outputFile);

      // Read the contents of the temporary file and write them to the new file
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      // Close the input and output streams
      inputStream.close();
      outputStream.close();

      // Update the "image_file" parameter to the new file path
      String outputPath = outputFile.getAbsolutePath();
      params.put("image_file", outputPath);

      return outputPath;

    } catch (IOException | ResponseException error) {
      error.printStackTrace();
      return null;
    }
  }

  /**
   * Downloads a file from the input url.
   * @param urlStr URL of file
   * @throws IOException if URL is incorrect
   * @return Absolute file path to local version of uploaded file
   */
  private String downloadFileFromUrl(String urlStr) throws IOException {
    InputStream in = new URL(urlStr).openStream();
    String pathToProj = System.getProperty("user.dir");
    String localPath = "/src/main/java/edu/cmu/cs214/hw6Backend/files/file." + ColorFramework.getFileExtension(urlStr);
    String absPath = pathToProj + localPath;
    Files.copy(in, Paths.get(absPath), StandardCopyOption.REPLACE_EXISTING);
    return absPath;
  }
}
