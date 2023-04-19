package edu.cmu.cs214.hw6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.ESAFramework;
import edu.cmu.cs214.hw6.framework.core.ESAFrameworkImpl;
import edu.cmu.cs214.hw6.framework.core.VisualizationPlugin;
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
    private List<DataPlugin> dataPlugins;
    private List<VisualizationPlugin> visPlugins;
    private static final int PORT = 8080;

    public App() throws IOException {
        super(PORT);
        this.framework = new ESAFrameworkImpl();
        dataPlugins = loadDataPlugins();
        for (DataPlugin p: dataPlugins){
            framework.registerDataPlugin(p);
        }
        visPlugins = loadVisPlugins();
        for (VisualizationPlugin p: visPlugins){
            framework.registerVisPlugin(p);
        }

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/newapp")) { // will remove this later, used to debug for now
            this.framework = new ESAFrameworkImpl();
            dataPlugins = loadDataPlugins();
            for (DataPlugin p: dataPlugins){
                framework.registerDataPlugin(p);
            }
            visPlugins = loadVisPlugins();
            for (VisualizationPlugin p: visPlugins){
                framework.registerVisPlugin(p);
            }
        } else if (uri.equals("/dataplugin")) {
            // e.g., /selectData?i=i
            framework.selectDataPlugin(Integer.parseInt(params.get("i")));
        } else if (uri.equals("/visplugin")) {
            // e.g., /selectVis?i=i
            framework.selectVisPlugin(Integer.parseInt(params.get("i")));
        }
        if (framework.getCurrDataPlugin() != null && framework.getCurrVisPlugin() != null && framework.getDirectoryPathStr() != null) {
            try {
                framework.uploadData(); // how to input text directory...
                framework.getAnalyzedVisualization();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AppState state = AppState.forApp(this.framework);
        System.out.println(state.toString());
        return newFixedLengthResponse(state.toString());
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
            result.add(plugin);
        }
        return result;
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<VisualizationPlugin> loadVisPlugins() {
        ServiceLoader<VisualizationPlugin> plugins = ServiceLoader.load(VisualizationPlugin.class);
        List<VisualizationPlugin> result = new ArrayList<>();
        for (VisualizationPlugin plugin : plugins) {
            result.add(plugin);
        }
        return result;
    }
}
