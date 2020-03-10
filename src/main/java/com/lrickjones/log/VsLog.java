package com.lrickjones.log;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import com.lrickjones.config;

/**
 * Singleton class for logger access
 * Log file is auto generated if not found
 * Sets up a rolling log, 4 logs up to 5 MBs
 * If log file fails to generate, messages are logged to console
 * File name and path set in Version class
 */
public class VsLog {
    private static final int fileSize = 1024 * 1024 * 5;
    private static Handler fileHandler = null;
    private static Logger logger;
    public static String[] levels = {"ANY","SEVERE","WARNING","INFO","DEBUG"};

    /** initialize logger using parameters in Version class */
    public static void initLogger() {
        createFileDir();
        try {
            logger = Logger.getLogger(VsLog.class.getName());
            logger.setUseParentHandlers(false);
            fileHandler = new FileHandler(System.getProperty("user.home") + Version.logFullPath, fileSize, 4, true);
            SimpleFormatter simple = new SimpleFormatter();
            fileHandler.setFormatter(simple);
            logger.addHandler(fileHandler);//adding Handler for file
            logger.setLevel(getLevelFromString(Configuration.getStringProperty("logLevel")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** create log files if not already in existence */
    private static void createFileDir() {
        File file = new File(System.getProperty("user.home") + Version.logFullPath);
        File dir = new File(System.getProperty("user.home") + Version.logPath);
        dir.mkdirs();
    }

    /**
     * return instance of logger
     * @return logger instance
     */
    public static synchronized Logger getInstance() {
        if (logger == null) initLogger();
        return logger;
    }

    /**
     * return instance of logger set to a specific logger level
     * @param level string indicating logger level (FINE, FINER, FINEST)
     * @return logger instance
     */
    public static synchronized Logger getInstance(String level) {
        if (logger == null) initLogger();
        logger.setLevel(getLevelFromString(level));
        return logger;
    }

    /**
     * convert string to equivalent level value
     * @param level level string (FINE, FINER, FINEST)
     * @return log level
     */
    public static Level getLevelFromString(String level) {
        switch (level.toLowerCase()) {
            case "fine" : return Level.FINE;
            case "finer" : return Level.FINER;
            case "finest" : return Level.FINEST;
            default: throw new IllegalStateException("Unexpected value: " + level.toLowerCase());
        }
    }
}
