# Implementing Plugins for Team yiningd-yihey-jasonkwo-public's Framework

## Data Plugins
**JPEG Data Plugin**
- Located `hw6Backend/src/java/edu/cmu/cs214/hw6Backend/plugin/dataPlugins/JPEGDataPlugin`.
- Converts a local `.jpg` file to an `ImageData` that the framework can process and extract colors from. 
- Since it's registered, the plugin automatically loads in on start up. No extra work is needed, and the user can now input .jpeg files.

## Visualization Plugin
