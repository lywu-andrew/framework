# Design

Design decisions and structures for Homework 6: Data Visualization Framework

---

## Domain

Our domain is in image processing. The idea is to take an image and create color themes based on the image.
The framework extracts different colors from different sources (provided by data plugins) and outputs a list of colors in
different formats (using visualization plugins). The core of the framework takes a list of colors that represents the colors of the input
image and outputs a list of specific colors that represents the theme of the image. This allows for reuse and extensibility.
Any plugin just needs to convert any source into a list of colors and the input it into the framework. Plugins that manage
visuals only need to process a list of colors that represent the theme of the input colors.

Data plugins that could convert sources into colors could include:

- Converts an image format like .png/jpeg into a list of colors to input into the framework core
- Gets an image from a URL and converts it into a list of colors to input into the framework core
- Converts and image in a non-standard format like .pdf into a list of colors to input into the framework core

Visualization plugins that could process the output of the framework core could include:

- Visualize the list of colors on the color wheel with hex and rgb values
- A template website that uses the list of colors from the framework core
- Generates an image with the given colors as a theme using a ML algorithm like stable diffusion

---

## Generality vs. Specificity

### Generality:

The extensibility of the framework is very high. The only thing that people need to add onto the framework is to turn input sources
into a list of colors. This could be from images, videos, even sound. If there is a creative way of turning something into a list of
colors, then the framework is able to interpret it and output a theme of colors. The default of the framework however is to take an
image and convert it into a list of colors which can easily be done either manually or by using APIs.

The generality of the framework is very high. It can theoretically take any input (that can be turned into a list of colors through plugins)
and output a theme related to it. Even though this can be done, the results may not be meaningful.

### Specificity:

Even though the generality is really high, the results of the vast and broad areas of inputs that are allowed through plugins may not be
logical or meaningful. It would be equivalent to putting a laptop in a cooking pan. Even though you can theoretically do it, the results
from it wouldn't be meaningful. The specificity of our framework is specifically dealing with images or even sound and text. Anything that
can logically be turned into a list of colors can be used for our framework.

Even though the framework's generality is high, it's specificity may be even higher. We are dealing specifically with image processing or
theme processing, information that can generate a color theme.

---

## Project Structure

The data plugins are backend plugins, implemented in Java. The visualization plugins are frontend plugins, implemented in Svelte, which contains both TypeScript and HTML.

### Root directory
```
hw6-analytics-framework-yiningd-yihey-jasonkwo
```
This is also the root directory of this Git repository.

### Backend logic organization
```
hw6Backend
    | edu.cmu.cs214.hw6Backend
        | App.java
        | framework
            | core
                | ColorFramework.java
                | ColorFrameworkImpl.java
                | ColorOutput.java
                | DataPlugin.java
                | ImageData.java
                | RgbPixel.java
            | gui
                | ColorFrameworkState.java
                | ColorOutputValues.java
        | plugin
            | dataPlugins
                | <Sample data plugin files> 
```
Classes related to the framework will be located at the `edu.cmu.cs214.hw6Backend.framework` package.

- The core framework structure is located at `edu.cmu.cs214.hw6Backend.framework.core`.
- The framework interface is defined in `edu.cmu.cs214.hw6Backend.framework.core`.
- The data plugin interface is defined in `edu.cmu.cs214.hw6Backend.framework.core`.
- The helper classes for the UI are defined in `edu.cmu.cs214.hw6Backend.framework.gui`.

Data plugins will be located at `edu.cmu.cs214.hw6Backend.plugin.dataPlugins`.

### Frontend logic organization
```
hw6Frontend
    | public
    | index.html
    | <package and config files>
    | src
        | app.css
        | App.svelte
        | main.ts
        | vite-env.d.ts
        | assets
            | <SVG assets for display purposes>
        | plugins
            | <Sample visualization plugin files>
        | lib
            | ColorFramework.ts
            | ColorFrameworkImpl.ts
            | Data.svelte
            | Theme.svelte
```
The core framework structure is located at `hw6Frontend/src` and `hw6Frontend/src/lib`.

There is no defined visualization plugin interface, but the plugin themselves should be placed `hw6Frontend/src/plugins`.

### Object Model

**Backend**

![object-model-backend](https://user-images.githubusercontent.com/72016126/232850940-4ae5f9e4-76fd-4b4a-973e-991192ddd232.png)

**Communication with the Frontend**

The backend will send a JSON to the frontend:

```
{
    "dataURI": "<data URI generated by data plugin>",
    "fileTypes": ["<supported type of data plugin 1>", "<supported type of data plugin 2>"],
    "numColors": 10,
    "output": <string version of ColorOutputValue>
}
```

**Frontend**

<img width="283" alt="Screenshot 2023-04-18 at 12 48 35" src="https://user-images.githubusercontent.com/72016126/232851549-0dadef3d-26aa-4020-9924-68a7dc911fcb.png">

---

## Plugin Interfaces

### Data Plugins

The DisplayPlugin interface contains six methods: onRegister(), isValidFileType(), convertFileToDataURI(), convertFileToImage(), getSupportedFileType(), and getPluginName(). These are the only methods the framework will attempt to call.

<img width="591" alt="plugin-interface" src="https://user-images.githubusercontent.com/72016126/232850862-c2fec099-c95f-46cb-95b1-6b830a333eff.png">

**1. onRegister(framework: ColorFramework): void**
- The main purpose of this method is to grant the concrete plugin implementation a reference to the framework.
- This is the only opportunity for the DataPlugin to get access to the framework. Save the framework as a private field if your plugin plans on using the framework's public functions in the future.

**2. isValidFileType(filePath: String): boolean**
- This function is used for checking whether a certain plugin is suitable for the input file. The check should be based on the file's extension name and the type of file this plugin can handle. For example, a PNGDataPlugin should only return `true` for files with extension name ".png".
- The input "filePath" is a string of the absolute path of this file. User can call the framework's static method `ColorFramework.getFileExtension(filePath)` to parse the string and get the extension name.

**3. convertFileToDataURI(filePath: String): String**
- This function takes in the file's absolute path and should return a dataURI string which contains the image the user want to send back to the frontend for display.
- The resulting dataURI is in the form `"data:<file type>;base64,<data in base 64>"` (e.g. `"data:image/jpeg;base64,<data in base 64>"`). The contents in `<>` are the image bytes in base 64 string.

**4. convertFileToImage(filePath: String): ImageData**
- This function takes in the file's absolute path and should return an object of type `ImageData`.
- The user should create an `ImageData` instance based on the file located at the input file path.

**5. getSupportedFileType(): String**
- This function should return the standard extension name string this data plugin supports. For example, ".png", ".jpg", etc. 
- The return string MUST be in the format ".xxx" or ".xxxx", as it will be used in the plugin selection process when a file is uploaded.
- This string must be unique to the plugin, and it shouldn't be changed during execution.

**6. getPluginName(): String**
- This function should return a name string related to this data plugin, like "PNG Data Plugin".
- This name must be unique to the plugin, and it shouldn't be changed during execution.

### Visualization Plugins

There's no defined interface for a visualization plugin.

The only requirements are:

1. The plugin should be a `.svelte` file. This file should contain both the script (implemented in ts) and the styling information (HTML elements) for the plugin.

2. The plugin's file name should be the desired plugin name. For example, a visualization plugin for a bar chart should be named `BarChart.svelte`. Such a plugin will show up on the web page's upper right corner as a tab named "BarChart".
