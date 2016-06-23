package com.nemotec.poeditor.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Config {

    private static final String DEFAULT_LANG = "en";
    private static final String DEFAULT_INPUT = "Bundle.properties";
    private static final String DEFAULT_OUTPUT = "Bundle_{0}.properties";
        
    public static class Params {
        public String apiKey;
        public String projectId;
        public String referenceLang;
        public String inputFile;
        public String outputFile;
    }

    public static Params load(String filename) {
        Params params = null;
        Properties properties = new Properties();
        InputStream stream = null;

        try {
            File f = new File(filename);
            stream = new FileInputStream(f);
            properties.load(stream);
        }
        catch (Exception e) {
            return null;
        }

        params = new Params();
        params.apiKey = properties.getProperty("apiKey", null);
        params.projectId = properties.getProperty("projectId", null);
        params.referenceLang = properties.getProperty("referenceLang", DEFAULT_LANG);
        params.inputFile = properties.getProperty("inputFile", DEFAULT_INPUT);
        params.outputFile = properties.getProperty("outputFile", DEFAULT_OUTPUT);
        return params;
    }
    
    public static File save(Params params, String filename) {
        File f = null;
        try {
            Properties properties = new Properties();
            properties.setProperty("apiKey", params.apiKey);
            properties.setProperty("projectId", params.projectId);
            properties.setProperty("referenceLang", params.referenceLang);
            properties.setProperty("inputFile", params.inputFile);
            properties.setProperty("outputFile", params.inputFile);

            f = new File(filename);
            OutputStream out = new FileOutputStream(f);
            properties.store(out, "Created by POEditor-Command");
        }
        catch (Exception e) {
            return null;
        }
        return f;
    }
}
