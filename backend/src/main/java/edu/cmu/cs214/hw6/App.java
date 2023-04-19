package edu.cmu.cs214.hw6;

import java.io.IOException;
import java.util.Map;

import edu.cmu.cs214.hw6.framework.core.ESAFramework;
import edu.cmu.cs214.hw6.framework.core.ESAFrameworkImpl;
import fi.iki.elonen.NanoHTTPD;

/**
 * App for Entity Sentiment Analysis Framework
 */
public class App extends NanoHTTPD {
    public static void main( String[] args )
    {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private ESAFramework framework;
    private static final int PORT = 8080;

    public App() throws IOException {
        super(PORT);
        this.framework = new ESAFrameworkImpl();

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/newapp")) {
            this.framework = new ESAFrameworkImpl();
        } else if (uri.equals("/selectData")) {
            // e.g., /selectData?i=i
            framework.selectDataPlugin(Integer.parseInt(params.get("i")));
        } else if (uri.equals("/selectVis")) {
            // e.g., /selectVis?i=i
            framework.selectVisPlugin(Integer.parseInt(params.get("i")));
        }
        if (framework.getCurrDataPlugin() != null && framework.getCurrVisPlugin() != null) {
            try {
                framework.uploadData("."); // how to input text directory
                framework.getAnalyzedVisualization();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AppState state = AppState.forApp(this.framework);
        return newFixedLengthResponse(state.toString());
    }
}
