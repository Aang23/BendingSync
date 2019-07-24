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
 * Class to handle the .json config file
 * 
 * @author Aang23
 */
public class ConfigManager {
    public static JSONObject config = null;

    public static String discord_token;
    public static String channelid;
    public static String redis_address;

    /**
     * Setup the config : Write default & load
     * 
     * @param config_path
     */
    public static void setupConfig(Path config_path) {
        if (!config_path.toFile().exists())
            config_path.toFile().mkdirs();
        if (!new File(config_path.toString() + "/bendingsync.json").exists()) {
            try {
                writeInitialConfig(config_path);
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

        discord_token = (String) config.get("discord_token");
        channelid = (String) config.get("channelid");
        redis_address = (String) config.get("redis_address");
    }

    /**
     * Write default config file
     * 
     * @param config_path
     * @throws FileNotFoundException
     */
    private static void writeInitialConfig(Path config_path) throws FileNotFoundException {
        JSONObject configfile = new JSONObject();

        configfile.put("discord_token", "tokenhere");
        configfile.put("channelid", "00000000000000");
        configfile.put("redis_address", "localhost");

        PrintWriter pw = new PrintWriter(config_path.toString() + "/bendingsync.json");
        pw.write(configfile.toJSONString());

        pw.flush();
        pw.close();
    }
}