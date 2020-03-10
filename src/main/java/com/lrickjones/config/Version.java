package com.lrickjones.config;

/**
 * This class will be used to manage the version number, logging info and config info for the build
 * TODO: Determine best practice to manage version number with builds
 * (most likely this unit will be generated during build)
 */
public class Version {
    public final static String version = "0.1.0";
    public static final String logPath = "/verbsurgical/log/"; //created relative to home directory
    public static final String logFile = "VerbSurgical.log";
    public static final String logFullPath = logPath + logFile;
    public static final String configPath = "/verbsurgical/config/"; //created relative to home directory
    public static final String configFile = "VerbSurgical.properties";
    public static final String configFullPath = configPath + configFile;
}
