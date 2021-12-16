package common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for reading application properties
 * @author Gareth Edwards
 * @version 0.0.1
 */
public class AppProperties {
    private final Properties properties;
    private String propsFile;

    /**
     * Constructor
     * @param propsFile Properties file name
     */
    public AppProperties(String propsFile) {
        properties = new Properties();
        this.propsFile = propsFile;

        try {
            properties.load(propStream());
        }
        catch (IOException e) {
            System.err.println("Error loading application properties: " + e.getMessage());
        }
    }

    /**
     * Get a resource stream for the properties file
     * @return
     */
    private InputStream propStream() {
        return getClass().getClassLoader().getResourceAsStream(propsFile);
    }

    /**
     * Read an individual property
     * @param name  Property name
     * @return
     */
    public String getProperty(String name) {
        return properties.getProperty(name, "Property not found");
    }

    //region Set and Get Methods
    public String getPropsFile() {
        return propsFile;
    }

    public void setPropsFile(String propsFile) {
        this.propsFile = propsFile;
    }
    //endregion
}
