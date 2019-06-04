package com.aang23.bendingsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BendingDataStorage {
    public String userId;
    public List<String> bendings = new ArrayList<String>();
    public Map<String, String> xps = new HashMap<String, String>();
    public Map<String, String> levels = new HashMap<String, String>();
    public Map<String, String> paths = new HashMap<String, String>();
    public float chiAvailable;
    public float chiMax;
    public float chiTotal;

    public String toJsonString() {
        JSONObject data = new JSONObject();

        data.put("uuid", userId);
        data.put("bendings", bendings);
        data.put("xps", xps);
        data.put("levels", levels);
        data.put("paths", paths);
        data.put("chiAvailable", chiAvailable);
        data.put("chiMax", chiMax);
        data.put("chiTotal", chiTotal);

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
        bendings = (List<String>) data.get("bendings");
        xps = (Map<String, String>) data.get("xps");
        levels = (Map<String, String>) data.get("levels");
        paths = (Map<String, String>) data.get("paths");
        chiAvailable = ((Double) data.get("chiAvailable")).floatValue();
        chiMax = ((Double) data.get("chiMax")).floatValue();
        chiTotal = ((Double) data.get("chiTotal")).floatValue();
    }
}