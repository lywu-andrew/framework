package edu.cmu.cs214.hw6;

import java.util.Arrays;
import java.util.List;

import edu.cmu.cs214.hw6.framework.core.DataPlugin;
import edu.cmu.cs214.hw6.framework.core.ESAFramework;
import edu.cmu.cs214.hw6.framework.core.VisualizationPlugin;

public class AppState {

    private Cell[] dataCells;
    private Cell[] visCells;
    private String imgPath;

    private AppState(Cell[] dataCells, Cell[] visCells, String imgPath) {
        this.dataCells = dataCells;
        this.visCells = visCells;
        this.imgPath = imgPath;
    }

    public static AppState forApp(ESAFramework f) {
        Cell[] dataCells = getDataCells(f);
        Cell[] visCells = getVisCells(f);
        return new AppState(dataCells, visCells, f.getImgPath());
    }

    private static Cell[] getDataCells(ESAFramework f) {
        List<DataPlugin> dataPlugins = f.getDataPlugins();
        DataPlugin currDP = f.getCurrDataPlugin();
        Cell[] cells = new Cell[dataPlugins.size()];
        for (int i = 0; i < dataPlugins.size(); i++) {
            String name = dataPlugins.get(i).getPluginName();
            boolean selected = false;
            if (currDP != null) selected = name.equals(currDP.getPluginName());
            cells[i] = new Cell(i, name, selected);
        }
        return cells;
    }

    private static Cell[] getVisCells(ESAFramework f) {
        List<VisualizationPlugin> visPlugins = f.getVisPlugins();
        VisualizationPlugin currVP = f.getCurrVisPlugin();
        Cell[] cells = new Cell[visPlugins.size()];
        for (int i = 0; i < visPlugins.size(); i++) {
            String name = visPlugins.get(i).getPluginName();
            boolean selected = false;
            if (currVP != null) selected = name.equals(currVP.getPluginName());
            cells[i] = new Cell(i, name, selected);
        }
        return cells;
    }

    /**
     * toString() of AppState will return the string representing
     * the AppState in JSON format.
     */
    @Override
    public String toString() {
        if (this.imgPath == null) {
            return """
                    {
                        "dataCells": %s,
                        "visCells": %s,
                        "imgPath": null
                    }
                    """.formatted(Arrays.toString(this.dataCells), Arrays.toString(this.visCells));
        } else {
            return """
                    {
                        "dataCells": %s,
                        "visCells": %s,
                        "imgPath": %s
                    }
                   """.formatted(Arrays.toString(this.dataCells), Arrays.toString(this.visCells), 
                                this.imgPath);
        }
    }
    
}

class Cell {
    private final int i;
    private final String name;
    private final boolean selected;

    Cell(int i, String name, boolean selected) {
        this.i = i;
        this.name = name;
        this.selected = selected;
    }

    public int getI() {
        return i;
    }

    public String getName() {
        return this.name;
    }

    public boolean isSelected() {
        return this.selected;
    }

    @Override
    public String toString() {
        return """
            {
                "name": "%s",
                "selected": %b,
                "i": %d,
            }
            """.formatted(this.name, this.selected, this.i);
    }
}
