package com.lrickjones.config;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration parameters, manages properties file for project
 * New properties are added to defaults
 * If property file does not exist, it is generated
 * If a property is requested, but not in file, defaults are checked, if in defaults property is auto added
 * File name and path set in Version class
 * Author: Rick Jones
 */
public final class Configuration {
    private static Properties properties = null;
    // put default settings here, file will auto update when setting is used
    private static final Map<String,String> defaults = Stream.of(new String[][]{
            {"logLevel","FINEST"},
            {"updatePropertyTimeout","1500"},
            {"eventTimeout","6000"},
            {"scanRate", "1000"},
            {"reconnectInterval", "15"},
            {"telemetryDir","/home/rick/Downloads/output"},
            {"dbFileUrl","jdbc:sqlite:/home/rick/verbsurgical/repo/Service/data/system_data.db"},
            {"dbMemoryUrl", "jdbc:sqlite::memory:system_data.db"},
            {"queryArmJointTelemetry","SELECT i.id, i.path as path, f.path as filename FROM BINARY_FILE as f JOIN ITEM as i ON f.item_id=i.id;"},
            {"thingworxURL","wss://verb-surgical-prod.cloud.thingworx.com:443/Thingworx/WS"},
            {"thingworxAppKey","1c98c03f-4237-4eac-83fa-41a6be82a448"},
            {"thingworxClientName","VerbSystem"},
            {"homeDir","/home/rick/verbsurgical/robot"}
    }).collect(Collectors.toMap(p -> p[0], p -> p[1]));

    /** set properties to defaults */
    protected static void setToDefaults() {
        for (Map.Entry<String,String> entry : defaults.entrySet()) {
            properties.setProperty(entry.getKey(),entry.getValue());
        }
    }

    /** load config file, or generate new one if not found */
    public static void initConfiguration() {
        properties = new Properties();
        File file = new File(System.getProperty("user.home") + Version.configFullPath);
        if (!file.exists()) {
            try {
                File dir = new File(System.getProperty("user.home") + Version.configPath);
                dir.mkdirs();
                file.createNewFile();
                setToDefaults();
                store();
            } catch (IOException ex) {
                VsLog.getInstance().log(Level.WARNING,ex.getMessage());
            }

        }
        try {
            InputStream inputStream = new FileInputStream(System.getProperty("user.home") + Version.configFullPath);
            properties.load(inputStream);
        } catch (IOException ex) {
            VsLog.getInstance().log(Level.WARNING,ex.getMessage());
        }
    }

    /** store properties to config file */
    public static void store() {
        try {
            File file = new File(System.getProperty("user.home") + Version.configFullPath);
            OutputStream outputStream = new FileOutputStream(file);
            properties.store(outputStream,"Configuration properties for Verb Surgical Thingworx clients");
        } catch (IOException ex) {
            VsLog.getInstance().log(Level.SEVERE,ex.getMessage());
        }
        VsLog.getInstance().log(Level.INFO,"Created log file");
    }

    /**
     * If not found add default value to properties file
     * @param propName name of property
     * @return property from config file or default if not found
     */
    public static String getStringProperty(String propName) {
        if (properties == null) initConfiguration();
        String result = properties.getProperty(propName);
        if (result == null) {
            result = defaults.get(propName);
            if (result != null) {
                properties.setProperty(propName,result);
                store();
            }
        }
        return result;
    }

    /**
     * Convert property to string
     * @param propName name of property
     * @return property from config file or 0 if not found
     */
    public static int getIntProperty(String propName) {
        String strResult = getStringProperty(propName);
        try {
            int result = Integer.parseInt(strResult);
            return result;
        } catch (NumberFormatException ex) {
            VsLog.getInstance().log(Level.SEVERE,ex.getMessage());
            return 0;
        }
    }
}
