package com.aang23.bendingsync;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A class to handle the .json config file
 * 
 * @author Aang23
 */
public class ConfigManager {
    private static final Path config_path = new File("config").toPath();

    public static JSONObject config = null;

    public static String address;
    public static String username;
    public static String password;
    public static String database;
    public static String port;
    public static String redis_address;

    /**
     * Setup the config as a whole. (Write defaults & load)
     */
    public static void setupConfig() {
        if (!config_path.toFile().exists())
            config_path.toFile().mkdirs();
        if (!new File(config_path.toString() + "/bendingsync.json").exists()) {
            try {
                writeInitialConfig();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        Object configobj = null;
        try {
            configobj = new JSONParser().parse(new FileReader(config_path.toString() + "/bendingsync.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        config = (JSONObject) configobj;

        address = (String) config.get("address");
        username = (String) config.get("username");
        password = (String) config.get("password");
        database = (String) config.get("database");
        port = (String) config.get("port");
        redis_address = (String) config.get("redis_address");
    }

    /**
     * Writes the default config file
     * 
     * @throws FileNotFoundException
     */
    private static void writeInitialConfig() throws FileNotFoundException {
        JSONObject configfile = new JSONObject();

        configfile.put("address", "localhost");
        configfile.put("username", "bendingsync");
        configfile.put("password", "minecraft");
        configfile.put("database", "bendingsync");
        configfile.put("port", "3306");
        configfile.put("redis_address", "localhost");

        PrintWriter pw = new PrintWriter(config_path.toString() + "/bendingsync.json");
        pw.write(configfile.toJSONString());

        pw.flush();
        pw.close();
    }
}