package edu.cmu.cs214.hw6.framework.core;

import java.util.List;

import edu.cmu.cs214.hw6.framework.core.types.Text;

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
     * Convert the file at the given path to a Text object.
     */
    Text convertToText(String filePath);

    /**
     * Called (only once) when the plug-in is first registered with the
     * framework, giving the plug-in a chance to perform any initial set-up
     * before the game has begun (if necessary).
     *
     * @param framework The {@link ESAFramework} instance with which
     *                  the plug-in was registered.
     */
    void onRegister(ESAFrameworkImpl framework);

}