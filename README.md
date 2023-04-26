# Using the Framework

## Setting up data plugins

Data plugins should implement the `DataPlugin` interface, located at `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/framework/core`.

Place implementation(s) of data plugin in the directory `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins`.

To register your plugin with the framework, add the fully-qualified class name of your plugin to the `edu.cmu.cs214.hw6Backend.framework.core.DataPlugin` file in the `src/main/resources/META-INF/services/` directory.

## Starting the backend

Go to the `hw6Backend` folder and run in terminal

```
mvn exec:exec
```

This will start the Java server at http://localhost:8080.

## Setting up visualization plugins

Visualization plugins are loaded in the frontend.

The front-end uses [Svelte](https://svelte.dev/) to render HTML and reactive data.
In order for you to create a visualization plugin, you will need to create a `.svelte` file in the `hw6Frontend/src/plugins` directory.
The file name will be used to create a button on the website: `Plugin.svelte` will create a button plugin labeled `Plugin`.
The framework will automatically register your file and load the visual plugin into the website.
You can read more about [Svelte](https://svelte.dev/) at their website.

Once you **successfully upload a file**, you will be able to click on the tab and view your plugin.

## Starting the frontend

Go to the `hw6Frontend` folder and run in another terminal window

```
npm install
npm run dev
```

This will start the front-end server at http://localhost:5173.

---

# Framework Documentation

## General idea

Our domain is in color theme processing. The idea is to take in an image and create color themes based on the image. The framework extracts different colors from different sources (provided by data plugins) and outputs a list of colors in different formats (using visualization plugins). The core of the framework takes a list of colors that represents the colors of the input image and outputs a list of specific colors that represents the theme of the image. This allows for reuse and extensibility. Any plugin just needs to convert any source into a list of colors and the input it into the framework. Plugins that manage visuals only need to process a list of colors that represent the theme of the input colors.

In implementation, our color framework works like this: when the user drag a image file into the box of the browser, the front end will this image to backend and the backend will save it under the path `src/main/java/edu/cmu/cs214/hw6Backend/files`. The data plugin will read this file and do data processing on it to make it compatible with our framework. Then the framework will use the extracted color information and display different forms of visual effects with display plugins. There's also a scroll bar in the browser for the user to set the number of distinct colors they want to extract from the original image.

Data plugins that could convert sources into colors could include(our samples):

- Converts an image format like .png into a list of colors to input into the framework core
- Converts the image in a non-standard format like .pdf into a list of colors to input into the framework core

Visualization plugins that could process the output of the framework core could include(our samples):

- Visualize the list of colors on the color wheel with hex and rgb values
- Draws a recursion fractal tree using the colors extracted from the data source

## Plugin Interfaces

### Data Plugin

---

These following functions are the ones that the framework attempts to call in its lifecycle. User only needs to implement these methods to add a new data plugin.

<img width="591" alt="plugin-interface" src="https://user-images.githubusercontent.com/72016126/232851812-8be14560-d914-4f1e-adc6-f7e3a5501d08.png">

**1. onRegister(framework: ColorFramework): void**

- This method grants the concrete plugin implementation a reference to the framework.
- This is the only opportunity for the DataPlugin to get access to the framework. Save the framework as a private field if your plugin plans on using the framework's public functions in the future.

**2. isValidFileType(filePath: String): boolean**

- This function is used for checking whether a certain plugin is suitable for the input file. The check should be based on the file's extension name and the type of file this plugin can handle. For example, a PNGDataPlugin should only return `true` for files with extension name ".png".
- The input "filePath" is a string of the absolute path of this file. User can call the framework's static method `ColorFramework.getFileExtension(filePath)` to parse the string and get the extension name.
- DO NOT throw exceptions! Return false if an error occurs.

**3. convertFileToDataURI(filePath: String): String**

- This function takes in the file's absolute path and should return a dataURI string which contains the image the user want to send back to the frontend for display.
- The dataURI should take the form `"data:<file type>;base64,<data in base 64>"` (e.g. `"data:image/jpeg;base64,<data in base 64>"`). The contents in `<>` are the image bytes in base 64 string.
- DO NOT throw exceptions! Return an empty string if an error occurs.

**4. convertFileToImage(filePath: String): ImageData**

- This function takes in the file's absolute path and should return an object of type `ImageData`.
- The user should create an `ImageData` instance based on the file located at the input file path. Details about `ImageData` are listed below in the **_"related data structures"_** section.
- DO NOT throw exceptions! Return an empty object using the default ImageData constructor (`new ImageData()`) if an error occurs.

**5. getSupportedFileType(): String**

- This function should return the standard extension name string this data plugin supports. For example, ".png", ".jpg", etc.
- The return string MUST be in the format ".xxx" or ".xxxx", as it will be used in the plugin selection process when a file is uploaded.
- This string must be unique to the plugin, and it shouldn't be changed during execution.

**6. getPluginName(): String**

- This function should return a name string related to this data plugin, like "PNG Data Plugin".
- This name must be unique to the plugin, and it shouldn't be changed during execution.

### Visualization Plugin

---

There's no defined interface for a visualization plugin.

The only requirements are:

1. The plugin should be a `.svelte` file. This file should contain both the script (implemented in `js` but can use `ts` if you include an anntation: `<script lang='ts'>`)
   and the styling information (HTML + CSS) for the plugin. `NOTE:` Visual plugins support [`TailwindCSS`](https://tailwindcss.com/)

2. The plugin's file name should be the desired plugin name. For example, a visualization plugin for a bar chart should be named `BarChart.svelte`. Such a plugin will show up on the web page's upper right corner as a tab named "BarChart".

To obtain color information from the framework, use helper functions located in the interface `hw6Frontend/src/lib/ColorFramework.ts`. These are made available through a concrete ColorFramework implementation, which you can import into your plugin using

```
import { framework } from "../lib/ColorFrameworkImpl";

// Then you can use the framework helpers
framework.getColors();
framework.getUri();
framework.getNumColors();
```

## Related Data Structures

### ColorFramework (backend)

The concrete framework implements the `ColorFramework` interface, which provides a few helper methods for the data plugins to use.

<img width="597" alt="Screenshot 2023-04-18 at 13 05 44" src="https://user-images.githubusercontent.com/72016126/232851957-2230010f-8614-4dc7-af00-28fe2e2a8a2d.png">

**1. getFileExtension(): String**

- A static method that can be called without instantiating an `ColorFramework`.
- Gets the file extension, excluding the "." from a file name. For example, it will return "png" for "file.png".

**2. registerDataPlugin(plugin: DataPlugin): boolean**

- Registers a data plugin with the framework.
- If it returns false, either the DataPlugin instance is invalid, or it has already been registered.

**3. getNumColors(): int**

- This method returns the number of colors that the analysis result will contain.

**5. getFilePath(): string**

- This method returns the absolute file path stored in the framework.
- If no file path has been set, it will return `null`.

### ImageData (backend)

The framework will use `ImageData` store image data. This class contains three fields: `width: int`, the width of the image in number of pixels (px); `height: int`, the height of the image in number of pixels (px), and `colors: RgbPixel[]`. Detailed information of `RgbPixel` can be found below.

The data plugin may use the following constructors to create an `ImageData` instance:

**1. ImageData()**

- Default constructor which can be called when the user want to handle internal errors.
- It will construct an empty image with `width = 0`, `height = 0`, and `data = []`.

**2. ImageData(width: int, height: int, data: DataBufferInt)**

- `width` is the width, in pixels (px), of original image.
- `height` is the height, in pixels (px), of the original image.
- `data` is a `DataBufferInt` type object. To obtain this information, translate the original image to a `BufferedImage` instance, with the image type set to `BufferedImage.TYPE_INT_ARGB`. Then, you can get the image data using `(DataBufferInt) image.getRaster().getDataBuffer()`.

**3. ImageData(width: int, height: int, data: DataBufferByte data, hasAlpha: boolean)**

- `width` is the width, in pixels (px), of original image.
- `height` is the height, in pixels (px), of the original image.
- `data` is a `DataBufferByte` type object. To obtain this information, translate the original image to a `BufferedImage` instance, with the image type set to `BufferedImage.TYPE_3BYTE_BGR` or `BufferedImage.TYPE_4BYTE_ABGR`. Then, you can get the image data using `(DataBufferByte) image.getRaster().getDataBuffer()`.
- `hasAlpha` is a boolean value indicating whether this image uses alpha channel value. You can obtain this information from the `BufferedImage` instance as well: `image.getAlphaRaster() != null`.

**4. ImageData(width: int, height: int, colors: RgbPixel[])**

- `width` is the width, in pixels (px), of original image.
- `height` is the height, in pixels (px), of the original image.
- `colors` is a list of `RgbPixel`. If the you want to create a `ImageData` instance from existing list of `RgbPixel`, you should use this constructor.

### RgbPixel (backend)

The framework uses this immutable wrapper class to represent an ARGB pixel in a image.

Each instance has four int values `red`, `green`, `blue` and `alpha`, which all take the range from `0` to `255` or `0x00` to `0xff` in hex. **Important note: if you attempt to set one of these values to a value below `0` or above `255`, you will automatically receive a pure white pixel!**

<img width="434" alt="Screenshot 2023-04-18 at 12 25 49" src="https://user-images.githubusercontent.com/72016126/232852156-cd0729a9-6fec-4018-b302-f363db4ce311.png">
  
A `RgbPixel` can be constructed from the following constructors:

**1. RgbPixel()**

- Default constructor which can be called when the user want to handle internal errors.
- It will create a white pixel, with `red`, `green`, `blue` and `alpha` set to `255`.

**2. RgbPixel(red: int, green: int, blue: int)**

- This constructor will create a pixel with the input RGB values.
- The `alpha` value will be initialized to `255`, indicating a solid color.

**3. RgbPixel(red: int, green: int, blue: int, alpha: int)**

- This constructor will create a pixel with the input ARGB values.

The data plugin may want to use the following methods to get information about and/or manipulate on existing pixels.

**1. mixPixels(pixels: RgbPixel[]): RgbPixel**

- A static method that can be called without instantiating an `RgbPixel`.
- It takes in an array of `RgbPixel` and returns a new "RgbPixel". This new `RgbPixel` will be a mixture of the original pixels, which means it has the average values of the four channels of the original pixels.

**2. getPixelFromHex(hex: int): RgbPixel**

- A static method that can be called without instantiating an `RgbPixel`.
- It takes a int value (in ARGB format like `0xffff0000`) and returns the corresponding `RgbPixel` object.

**3. getColorHex(): int**

- It returns a int value representing the four channels of this pixel.
- For example, a white pixel (red: 255, green: 255, blue: 255, alpha: 255) is represented as `-1` (`0xffffffff`).

**4. getColorHexString(): String**

- It returns the string in hexadecimal format of the int value mentioned above. Like red, the returned string should be "ffff0000".

### ColorFramework (frontend)

The framework's frontend uses an instance of this class transport JSON files between web pages. Pages can get display information from the backend using this instance.

 <img width="283" alt="Screenshot 2023-04-18 at 12 48 35" src="https://user-images.githubusercontent.com/72016126/232852225-e64cd1e0-2278-4be7-85a6-6fbc06c9e7ec.png">
  
The following methods willl fetch the necessary information for the web pages and visualization plugins:

**1. getColors(): string[]**

- If an image has successfully been uploaded, this method returns a list of output colors as strings, in the format `"0#RRGGBB"`.
- If no image has been uploaded, this method returns an empty array.

**2. isDark(hex: string): boolean**

- This helper method can be used to determine if a color is to dark to be used with black texts.
- The input string should be a color, in the format `"0#RRGGBB"`.

**3. getUri(): string**

- If an image has successfully been uploaded, this method returns a data URI (in the format `"data:<file type>;base64,<data in base 64>"`) representing the image to be displayed.
- If no image has been uploaded, this method returns an empty string.

**4. getNumColors(): number**

- This method returns the number of colors that the analysis result will contain.
- If an internal error occurred, this method will return 0.

**5. getFileTypesString(): string**

- This method returns the types of file extensions the data plugins support, in the form of a string. Each file extension is separated by a comma and a space.
- If an internal error occurred, this method will return "".

**6. getFileTypes(): string[]**

- This method returns the types of file extensions the data plugins support, in the form of a string array.
- If an internal error occurred, this method will return an empty array.


## Sample Plugins

### Data Plugins

**PNG Data Plugin**

- Located `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins/PNGDataPlugin`.
- Converts a local `.png` file to an `ImageData` that the framework can process and extract colors from. 

**PDF Data Plugin**

- Located at `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins/PDFDataPlugin`.
- Reads a saved `.pdf` file, converts it to a JPEG, and then to an `ImageData` instance.
- To ensure that the computation is fast enough, this plugin only process the first 3 pages of a PDF.

### Visualization Plugins

**Fractal Tree**

- Located `hw6Frontend/src/plugins/Tree.svelte`.
- Draws a recursive fractal tree using the colors the framework outputs.
- A slider controls how "bushy" the tree is. 

**Color Wheel**

- Located at `hw6Frontend/src/plugins/Wheel.svelte`.
- Generates a color wheel from the colors the framework outputs.
- Provides a preview of the uploaded image on the side.
