package me.relow.relow;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final File configFile = new File(RELOW.getPluginFile(), "config.yml");
    private static final YamlConfiguration configYML = new YamlConfiguration();

    //消息
    private static String prefix;
    private static String close;
    private static String money1Not;
    private static String money1Yes;
    private static String level1Yes;
    private static String level1Not;
    private static String points1Yes;
    private static String points1Not;

    private static String money2Not;
    private static String money2Yes;
    private static String level2Yes;
    private static String level2Not;
    private static String points2Yes;
    private static String points2Not;

    private static String fenliFail;
    private static String washFail;
    private static String notAllow;
    private static String Lore;

    private static List<String> noRemove;

    private static int R_success_rate;
    private static int T_success_rate;

    private static boolean money;
    private static boolean money2;
    private static boolean level;
    private static boolean level2;
    private static boolean points;
    private static boolean points2;

    private static double money_amount;
    private static double money_amount2;
    private static int level_amount;
    private static int level_amount2;
    private static int points_amount;
    private static int points_amount2;

    private static List<permissions> permissionsList = new ArrayList<>();
    private static int defaultNum;

    public static void loadConfig() {
        //载入config.yml
        try {
            configYML.load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        //从config.yml获得每个值
        prefix = configYML.getString("prefix");
        close = configYML.getString("close");
        money1Not = configYML.getString("money1Not");
        money1Yes = configYML.getString("money1Yes");
        level1Yes = configYML.getString("level1Yes");
        level1Not = configYML.getString("level1Not");
        points1Not = configYML.getString("points1Not");
        points1Yes = configYML.getString("points1Yes");

        money2Not = configYML.getString("money2Not");
        money2Yes = configYML.getString("money2Yes");
        level2Yes = configYML.getString("level2Yes");
        level2Not = configYML.getString("level2Not");
        points2Not = configYML.getString("points2Not");
        points2Yes = configYML.getString("points2Yes");

        noRemove = configYML.getStringList("noRemove");

        fenliFail = configYML.getString("fenliFail");
        washFail = configYML.getString("washFail");
        notAllow = configYML.getString("notAllow");
        Lore = configYML.getString("Lore");



        R_success_rate = configYML.getInt("R_success_rate");
        T_success_rate = configYML.getInt("T_success_rate");

        money = configYML.getBoolean("money");
        money2 = configYML.getBoolean("money2");
        level = configYML.getBoolean("level");
        level2 = configYML.getBoolean("level2");
        points = configYML.getBoolean("points");
        points2 = configYML.getBoolean("points2");

        money_amount = configYML.getDouble("money_amount");
        money_amount2 = configYML.getDouble("money_amount2");
        level_amount = configYML.getInt("level_amount");
        level_amount2 = configYML.getInt("level_amount2");
        points_amount = configYML.getInt("points_amount");
        points_amount2 = configYML.getInt("points_amount2");
        defaultNum = configYML.getInt("default");



        loadPermissions();
    }

    public static void loadPermissions(){
        String prefix = "permissions.";
        int i = 1;
        String index = "p" + i;
        String key = prefix + index;
        while (configYML.getString(key + ".name") != null){
            permissions p = new permissions(configYML.getString(key + ".name"),configYML.getInt(key + ".num"),
                    configYML.getDouble(key + ".money1"),configYML.getInt(key + ".points1"),configYML.getInt(key + ".level1"),
                    configYML.getDouble(key + ".money2"),configYML.getInt(key + ".points2"),configYML.getInt(key + ".level2"),
                    configYML.getInt(key + ".rsuccess"),configYML.getInt(key + ".wsuccess"));
            System.out.println("[RELOW]已加载自定义权限节点:" + p.getName() + "-限制数量:" + p.getNum());
            System.out.println("[RELOW]money1:" + p.getMoney1() + " points1:" + p.getPoints1() + "level1:" + p.getPoints1());
            System.out.println("[RELOW]money2:" + p.getMoney2() + " points2:" + p.getPoints2() + "level2:" + p.getPoints2());
            System.out.println("[RELOW]rsuccess:" + p.getRsuccess() + " wsuccess:" + p.getWsuccess());
            permissionsList.add(p);
            i++;
            index = "p" + i;
            key = prefix + index;
        }

    }


    public static List<String> getNoRemove() {
        return noRemove;
    }

    public static File getConfigFile() {
        return configFile;
    }

    public static YamlConfiguration getConfigYML() {
        return configYML;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getClose() {
        return close;
    }


    public static String getFenliFail() {
        return fenliFail;
    }

    public static String getWashFail() {
        return washFail;
    }

    public static String getNotAllow() {
        return notAllow;
    }

    public static int getR_success_rate() {
        return R_success_rate;
    }

    public static int getT_success_rate() {
        return T_success_rate;
    }

    public static boolean isMoney() {
        return money;
    }

    public static boolean isMoney2() {
        return money2;
    }

    public static boolean isLevel() {
        return level;
    }

    public static boolean isLevel2() {
        return level2;
    }

    public static double getMoney_amount() {
        return money_amount;
    }

    public static double getMoney_amount2() {
        return money_amount2;
    }

    public static int getLevel_amount() {
        return level_amount;
    }

    public static int getLevel_amount2() {
        return level_amount2;
    }

    public static String getLore() {
        return Lore;
    }

    public static String getMoney1Not() {
        return money1Not;
    }

    public static String getMoney1Yes() {
        return money1Yes;
    }

    public static String getLevel1Yes() {
        return level1Yes;
    }

    public static String getLevel1Not() {
        return level1Not;
    }

    public static String getMoney2Not() {
        return money2Not;
    }

    public static String getMoney2Yes() {
        return money2Yes;
    }

    public static String getLevel2Yes() {
        return level2Yes;
    }

    public static String getLevel2Not() {
        return level2Not;
    }

    public static List<permissions> getPermissionsList() {
        return permissionsList;
    }

    public static int getDefaultNum() {
        return defaultNum;
    }

    public static boolean isPoints() {
        return points;
    }

    public static boolean isPoints2() {
        return points2;
    }

    public static int getPoints_amount() {
        return points_amount;
    }

    public static int getPoints_amount2() {
        return points_amount2;
    }

    public static String getPoints1Yes() {
        return points1Yes;
    }

    public static String getPoints1Not() {
        return points1Not;
    }

    public static String getPoints2Yes() {
        return points2Yes;
    }

    public static String getPoints2Not() {
        return points2Not;
    }
}
