package by.thmihnea.utils;

import org.bukkit.enchantments.Enchantment;

public class EnchantUtil {
    public static String enchantmentName(String arg) {
        switch (Enchantment.getByName(arg).getName()) {
            case "ARROW_DAMAGE":
                return "Power";
            case "ARROW_FIRE":
                return "Flame";
            case "ARROW_INFINITE":
                return "Infinity";
            case "ARROW_KNOCKBACK":
                return "Punch";
            case "BINDING_CURSE":
                return "Curse of Binding";
            case "DAMAGE_ALL":
                return "Sharpness";
            case "DAMAGE_ARTHROPODS":
                return "Bane of Arthropods";
            case "DAMAGE_UNDEAD":
                return "Smite";
            case "DEPTH_STRIDER":
                return "Depth Strider";
            case "DIG_SPEED":
                return "Efficiency";
            case "DURABILITY":
                return "Unbreaking";
            case "FIRE_ASPECT":
                return "Fire Aspect";
            case "FROST_WALKER":
                return "Frost Walker";
            case "KNOCKBACK":
                return "Knockback";
            case "LOOT_BONUS_BLOCKS":
                return "Fortune";
            case "LOOT_BONUS_MOBS":
                return "Looting";
            case "LUCK":
                return "Luck of the Sea";
            case "LURE":
                return "Lure";
            case "MENDING":
                return "Mending";
            case "OXYGEN":
                return "Respiration";
            case "PROTECTION_ENVIRONMENTAL    ":
                return "Protection";
            case "PROTECTION_EXPLOSIONS":
                return "Blast Protection";
            case "PROTECTION_FALL":
                return "Feather Falling";
            case "PROTECTION_FIRE":
                return "Fire Protection";
            case "PROTECTION_PROJECTILE":
                return "Projectile Protection";
            case "SILK_TOUCH":
                return "Silk Touch";
            case "SWEEPING_EDGE":
                return "Sweeping Edge";
            case "THORNS":
                return "Thorns";
            case "VANISHING_CURSE":
                return "Cure of Vanishing";
            case "WATER_WORKER":
                return "Aqua Affinity";
            case "Lucky":
                return "Lucky";
            case "TokenGreed":
                return "Token Greed";
            case "Fortune":
                return "Fortune";
            case "Greed":
                return "Greed";
            case "Experienced":
                return "Experienced";
            case "Lazy":
                return "Lazy";
            case "Speed":
                return "Speed";
            default:
                return "Unknown";
        }
    }
}
