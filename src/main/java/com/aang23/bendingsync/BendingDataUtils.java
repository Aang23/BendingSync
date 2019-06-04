package com.aang23.bendingsync;

import java.util.Map.Entry;

import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.BendingStyles;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;

public class BendingDataUtils {
    public static BendingDataStorage getDataStorageFromBender(Bender bender) {
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

    public static void setDataDromBendingStorage(Bender bender, BendingDataStorage storage) {
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