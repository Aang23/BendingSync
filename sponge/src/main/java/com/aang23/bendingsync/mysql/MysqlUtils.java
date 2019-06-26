package com.aang23.bendingsync.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.aang23.bendingsync.storage.BendingDataStorage;

import java.sql.ResultSet;

public class MysqlUtils {

    public static void setup() {
        MysqlConnect mysqlConnect = new MysqlConnect();

        String sql = "CREATE TABLE IF NOT EXISTS benders (bender_uuid VARCHAR(100), data VARCHAR(10000));";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }

        sql = "CREATE TABLE IF NOT EXISTS swordsmans (swordsman_uuid VARCHAR(100), data VARCHAR(10000));";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }

        sql = "CREATE TABLE IF NOT EXISTS metadata (option VARCHAR(100), value VARCHAR(10000));";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }

        if (!isMetatdataPresent("avatar_uuid"))
            setMetadataValue("avatar_uuid", "none");
        if (!isMetatdataPresent("avatar_bending_data"))
            setMetadataValue("avatar_bending_data", new BendingDataStorage().toJsonString());
    }

    public static void setMetadataValue(String option, String value) {
        if (isMetatdataPresent(option))
            delMetadata(option);
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "INSERT INTO metadata (option, value) VALUES('" + option + "','" + value + "');";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static String getMedataValue(String option) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String pass = null;
        String sql = "SELECT * FROM metadata WHERE option='" + option + "';";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            pass = rs.getString("data");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return pass;
    }

    private static boolean isMetatdataPresent(String option) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        boolean result = false;
        String sql = "SELECT EXISTS(SELECT * FROM metadata WHERE option='" + option + "');";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return result;
    }

    private static void delMetadata(String option) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "DELETE FROM metadata WHERE option='" + option + "'";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static boolean doesBenderExists(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        boolean result = false;
        String sql = "SELECT EXISTS(SELECT * FROM benders WHERE bender_uuid='" + uuid + "');";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return result;
    }

    public static boolean doesSwordsmanExists(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        boolean result = false;
        String sql = "SELECT EXISTS(SELECT * FROM swordsmans WHERE swordsman_uuid='" + uuid + "');";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0)
                result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return result;
    }

    public static void addBender(String uuid, String data) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "INSERT INTO benders (bender_uuid, data) VALUES('" + uuid + "', '" + data + "' );";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static void addSwordsman(String uuid, String data) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "INSERT INTO swordsmans (swordsman_uuid, data) VALUES('" + uuid + "', '" + data + "' );";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static void delBender(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "DELETE FROM benders WHERE bender_uuid='" + uuid + "'";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static void delSwordsman(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String sql = "DELETE FROM swordsmans WHERE swordsman_uuid='" + uuid + "'";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
    }

    public static String getBendingData(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String pass = null;
        String sql = "SELECT * FROM benders WHERE bender_uuid='" + uuid + "';";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            pass = rs.getString("data");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return pass;
    }

    public static String getDssData(String uuid) {
        MysqlConnect mysqlConnect = new MysqlConnect();
        String pass = null;
        String sql = "SELECT * FROM swordsmans WHERE swordsman_uuid='" + uuid + "';";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            rs.next();
            pass = rs.getString("data");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }
        return pass;
    }
}