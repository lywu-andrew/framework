package main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core;

import java.util.List;

import main.java.edu.cmu.cs.cs214.entityAnalysis.framework.core.types.Text;

public interface DataPlugin {

    /**
     * Gets the name of the data plugin.
     */
    String getPluginName();

    /**
     * Gets a list of the texts.
     */
    List<Text> getTexts();

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link EntityAnalysisFramework} instance with which
     *                  the plug-in was registered.
     */
    void onRegister(EntityAnalysisFramework framework);
    
}
