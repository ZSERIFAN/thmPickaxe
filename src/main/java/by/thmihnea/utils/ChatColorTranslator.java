package by.thmihnea.utils;

import net.md_5.bungee.api.ChatColor;

public class ChatColorTranslator {
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
