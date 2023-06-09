package co.com.pragma.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath;

    public ConfigFileReader(String filePath){
        propertyFilePath = filePath;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();

            try {
                properties.load(reader);
            }
            catch (IOException e) {

                e.printStackTrace(); }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }
        finally {
            try {
                if(reader != null)
                    reader.close();
            }
            catch (IOException ignore) {

            }
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPropertyByKey(String key) {
        String p = properties.getProperty(key);
        if (p != null){
            if (p.contains("${")) {
                String envVarValueInProp= p.substring(p.indexOf("{")+1,p.indexOf(":"));
                String envVarValue = System.getenv(envVarValueInProp);
                if(envVarValue != null){
                    p=envVarValue;
                }else{
                    p= p.substring(p.indexOf(":")+1,p.indexOf("}"));
                }
            }
        return p;
        }else
            throw new RuntimeException("Property not specified in the " + propertyFilePath + " file for the Key: " + key);
    }

    public static void changeProperty(String filename, String key, String value) throws IOException {
        Properties prop =new Properties();
        prop.load(new FileInputStream(filename));
        prop.setProperty(key, value);
        prop.store(new FileOutputStream(filename),null);
    }

    public void setProperties(String key, String value) throws IOException {
        properties.setProperty(key, value);
        //properties.store(new FileOutputStream(filename),null);
    }
}
