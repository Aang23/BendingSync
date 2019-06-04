package com.aang23.bendingsync;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReSkillableDataStorage {
    public String userId;
    public Map<String, String> levels = new HashMap<String, String>();

    public String toJsonString() {
        JSONObject data = new JSONObject();

        data.put("uuid", userId);
        data.put("levels", levels);

        return data.toJSONString();
    }

    public void fromJsonString(String in) {

        JSONObject data = null;
        try {
            data = (JSONObject) new JSONParser().parse(in);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        userId = (String) data.get("uuid");
        levels = (Map<String, String>) data.get("levels");
    }
}