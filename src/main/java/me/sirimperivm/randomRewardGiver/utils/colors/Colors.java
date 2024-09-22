package me.sirimperivm.randomRewardGiver.utils.colors;

import cz.foresttech.api.ColorAPI;
import me.sirimperivm.randomRewardGiver.Main;

@SuppressWarnings("all")
public class Colors {

    private Main plugin;

    public Colors(Main plugin) {
        this.plugin = plugin;
    }

    public String translatedString(String t) {
        return ColorAPI.colorize(t);
    }

    public static String translateString(String t) {
        return ColorAPI.colorize(t);
    }
}
