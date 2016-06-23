package com.github.nemotec.poeditor.command;

import be.lukin.poeditor.models.Language;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Command {

    private static final String PROGRAM_NAME = "poeditor-cli";
    
    private static final String COMMAND_PULL = "pull";
    private static final String COMMAND_PUSH = "push";
    
    private static final String CONFIG_FILE = "poeditor.properties";
    
    private static String checkArgs(String[] args) {
        String error = null;
        String usage = "Usage: " + PROGRAM_NAME
                + " [" + COMMAND_PULL + " | " + COMMAND_PUSH + "]";
        
        if(args.length < 1) {
            error = "No command found! " + usage;
        }
        else if(!args[0].equals(COMMAND_PULL)
                && !args[0].equals(COMMAND_PUSH)) {
            error = "Unrecognized command `" + args[0] + "`! " + usage;
        }
        
        return error;
    }
    
    private static String checkConfig(Config.Params config) {
        String error = null;
        
        if(config == null) {
            error = "Config file `" + CONFIG_FILE + "` not found!";
        }
        else if(config.apiKey == null) {
            error = "Required value `apiKey` not found in config file!";
        }
        else if(config.projectId == null) {
            error = "Required value `projectId` not found in config file!";
        }
        return error;
    }
    
    private static void runCommand(String command, Config.Params config, Path path) throws IOException, InterruptedException {
        switch(command) {
            case COMMAND_PUSH:
                Commands.push(config.apiKey, config.projectId, config.inputFile, new Language("", config.referenceLang), path);
                break;
            case COMMAND_PULL:
                Commands.pull(config.apiKey, config.projectId, config.inputFile, config.outputFile, path);
                break;
        }
    }
        
    public static void main(String[] args) {
        
        String error = null;
        
        error = checkArgs(args);
        
        if(error != null) {
            System.err.println(error);
            System.exit(1);
        }
        
        Config.Params config = Config.load(CONFIG_FILE);
        Path cwd = Paths.get(System.getProperty("user.dir"));
        
        error = checkConfig(config);
        
        if(error != null) {
            System.err.println(error);
            System.exit(1);
        }
        
        try {
            runCommand(args[0], config, cwd);
        }
        catch(Exception e) {
            System.err.println("Error while processing command: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
