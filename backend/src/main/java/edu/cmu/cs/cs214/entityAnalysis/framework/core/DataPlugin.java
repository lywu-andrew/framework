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

}
