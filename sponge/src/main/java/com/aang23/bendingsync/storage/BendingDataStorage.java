package com.aang23.bendingsync.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.aang23.bendingsync.BendingSync;
import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.BendingStyles;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.minecraft.entity.player.EntityPlayer;

public class BendingDataStorage {
    public String userId;
    public List<String> bendings = new ArrayList<String>();
    public Map<String, String> xps = new HashMap<String, String>();
    public Map<String, String> levels = new HashMap<String, String>();
    public Map<String, String> paths = new HashMap<String, String>();
    public float chiAvailable;
    public float chiMax;
    public float chiTotal;

    /**
     * Serialize this object to a JSON string
     * 
     * @return
     */
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

    /**
     * Restorage this object's content from a JSON string
     * 
     * @param in
     */
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

    /**
     * Return a BendingDataStorage filled with datas from the specified Bender
     * 
     * @param player
     * @return
     */
    public static BendingDataStorage getDataStorageFromBender(EntityPlayer player) {
        Bender bender = Bender.get(player);

        BendingDataStorage toreturn = new BendingDataStorage();

        toreturn.userId = bender.getInfo().getId().toString();

        toreturn.chiAvailable = bender.getData().chi().getAvailableChi();
        toreturn.chiMax = bender.getData().chi().getMaxChi();
        toreturn.chiTotal = bender.getData().chi().getTotalChi();

        for (BendingStyle style : bender.getData().getAllBending())
            toreturn.bendings.add(style.getName());

        for (Entry<String, AbilityData> data : bender.getData().getAbilityDataMap().entrySet()) {
            toreturn.xps.put(data.getKey(), String.valueOf(data.getValue().getXp()));
            toreturn.levels.put(data.getKey(), String.valueOf(data.getValue().getLevel()));
            toreturn.paths.put(data.getKey(), data.getValue().getPath().name());
        }

        return toreturn;
    }

    /**
     * Restore BedingData from a storage to a Bender
     * 
     * @param player
     * @param storage
     */
    public static void setDataDromBendingStorage(EntityPlayer player, BendingDataStorage storage) {
        Bender bender = Bender.get(player);

        if (bender.getInfo().getId().toString().equals(storage.userId)) {
            bender.getData().clearAbilityData();
            bender.getData().clearBending();

            bender.getData().chi().setAvailableChi(storage.chiAvailable);
            bender.getData().chi().setMaxChi(storage.chiMax);
            bender.getData().chi().setTotalChi(storage.chiTotal);

            for (String bendingStyle : storage.bendings)
                bender.getData().addBending(BendingStyles.get(bendingStyle));

            for (Entry<String, String> xp : storage.xps.entrySet())
                bender.getData().getAbilityData(xp.getKey()).setXp(Float.parseFloat(xp.getValue()));

            for (Entry<String, String> level : storage.levels.entrySet())
                bender.getData().getAbilityData(level.getKey()).setLevel(Integer.parseInt(level.getValue()));

            for (Entry<String, String> path : storage.paths.entrySet())
                bender.getData().getAbilityData(path.getKey()).setPath(AbilityTreePath.valueOf(path.getValue()));
        } else {
            BendingSync.logger.error("Tried to restore BendingData from wrong UUID !");
        }
        bender.getData().saveAll();
    }
}