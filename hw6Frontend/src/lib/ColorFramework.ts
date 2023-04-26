/**
 * Defines an interface for the utility methods this framework provides.
 */
interface ColorFramework {
  /**
   * Gets the string hex values from color output in the format "0#RRGGBB".
   * @returns Array of colors, in string format
   */
  getColors: () => string[];

  /**
   * Checks to see if color is too dark to use a black to write text.
   * @param hex A color's RGB hex values, in the format "0#RRGGBB"
   * @returns If the color is considered "too dark"
   */
  isDark: (hex: string) => boolean;

  /**
   * Gets the data URI from the framework.
   * @returns Data URI, if any
   */
  getUri: () => string;

  /**
   * Gets the number of colors included in the framework's analysis result.
   * @returns Number of colors in the result
   */
  getNumColors: () => number;

  /**
   * Gets the supported file types from the framework, in the form of a string.
   * Each file separated by a comma and a space.
   * E.g. ".png, .pdf"
   * @returns Supported file types, represented as 1 string
   */
  getFileTypesString: () => string;

  /**
   * Gets the supported file types from the framework, in the form of a string array.
   * E.g. [".png", "".pdf"]
   * @returns Supported file types, represented as 1 string array
   */
  getFileTypes: () => string[];

  /**
   * Stores the input JSON in the framework.
   * Should be used with caution, as this will affect all the plugins' data!
   * @param json JSON to store
   */
  setJson: (json: any) => void;

  /**
   * Gets the stored json of the framework.
   * The json has the following fields:
   * "fileTypes"
   * "dataURI"
   * "numColors"
   * "output"
   * @returns Stored JSON
   */
  getJson: () => any;
}
