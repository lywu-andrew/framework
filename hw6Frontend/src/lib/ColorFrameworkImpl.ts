class ColorFrameworkImpl implements ColorFramework {
  /**
   * This json has the following fields:
   * "fileTypes"
   * "dataURI"
   * "numColors"
   * "output"
   */
  json = null;
  
  /**
   * Checks to see if color is too dark to use a black to write text.
   * @param hex A color's RGB hex values, in the format "0#RRGGBB"
   * @returns If the color is considered "too dark"
   */
  getColors(): string[] {
    if (!this.json) return [];
    let color_list: number[] = this.json["output"];
    let hex_colors: string[] = [];

    // Pad and convert colors to hex string
    const pad = (color: string) => {
      while (color.length < 8) color = "0" + color;
      let color_format: String = color.substring(2, 8);
      return "#" + color_format;
    }
    for (let i = 0; i < color_list.length; i++) {
      let hex: string = (color_list[i] >>> 0).toString(16);
      hex_colors.push(pad(String(hex.toUpperCase())));
    }

    return hex_colors;
    }
  
  /**
   * Checks to see if color is too dark to use a black to write text.
   * @param hex A color's RGB hex values, in the format "0#RRGGBB"
   * @returns If the color is considered "too dark"
   */
  isDark(hex: string): boolean {
    let color: String = hex.substring(1);
    let red: number = parseInt(color.substring(0, 2), 16);
    let green: number = parseInt(color.substring(2, 4), 16);
    let blue: number = parseInt(color.substring(4, 6), 16);
    return red * 0.299 + green * 0.587 + blue * 0.114 < 186;
  }
  
  /**
   * Gets the data URI from the framework.
   * @returns Data URI, if any
   */
  getUri(): string {
    if (!this.json) return "";
    return this.json["dataURI"];
  }
  
  /**
   * Gets the number of colors included in the framework's analysis result.
   * @returns Number of colors in the result
   */
  getNumColors(): number {
    if (!this.json) return 0;
    return this.json["numColors"];
  }
  
  /**
   * Gets the supported file types from the framework, in the form of a string.
   * Each file separated by a comma and a space.
   * E.g. ".png, .pdf"
   * @returns Supported file types, represented as 1 string
   */
  getFileTypesString(): string {
    if (!this.json) return "";
    return this.json["fileTypes"].join(", ");
  }
  
  /**
   * Gets the supported file types from the framework, in the form of a string array.
   * E.g. [".png", "".pdf"]
   * @returns Supported file types, represented as 1 string array
   */
  getFileTypes(): string[] {
    if (!this.json) return [];
    return JSON.parse(JSON.stringify(this.json))["fileTypes"];
  }
  
  /**
   * Stores the input JSON in the framework.
   * Should be used with caution, as this will affect all the plugins' data!
   * @param json JSON to store
   */
  setJson(json: any): void {
    this.json = JSON.parse(JSON.stringify(json));
  }
    
  /**
   * Gets the stored json of the framework.
   * The json has the following fields:
   * "fileTypes"
   * "dataURI"
   * "numColors"
   * "output"
   * @returns Stored JSON
   */
  getJson(): any {
    return JSON.parse(JSON.stringify(this.json));
  }
}

// Shared instance of ColorFramework for all plugins.
let framework: ColorFramework = new ColorFrameworkImpl();
  
export { framework };
  