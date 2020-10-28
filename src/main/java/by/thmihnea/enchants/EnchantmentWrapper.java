package by.thmihnea.enchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EnchantmentWrapper {

    public static final Enchantment LUCKY = new LuckyEnchantment(100, "Lucky", 50);
    public static final Enchantment TOKENGREED = new TokenGreedEnchantment(101, "TokenGreed", 1000);
    public static final Enchantment FORTUNE = new FortuneEnchantment(102, "Fortune", 2000);
    public static final Enchantment GREED = new GreedEnchantment(103, "Greed", 500);
    public static final Enchantment EXPERIENCED = new ExperiencedEnchantment(104, "Experienced", 25);
    public static final Enchantment LAZY = new LazyEnchantment(105, "Lazy", 10);
    public static final Enchantment SPEED = new SpeedEnchantment(106, "Speed", 3);

    public static void register() {
        boolean registeredLucky = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LUCKY);
        boolean registeredTokenGreed = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(TOKENGREED);
        boolean registeredFortune = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(FORTUNE);
        boolean registeredGreed = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(GREED);
        boolean registeredExperienced = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(EXPERIENCED);
        boolean registeredLazy = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(LAZY);
        boolean registeredSpeed = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(SPEED);

        if (!registeredLucky) registerEnchantments(LUCKY);
        if (!registeredTokenGreed) registerEnchantments(TOKENGREED);
        if (!registeredFortune) registerEnchantments(FORTUNE);
        if (!registeredGreed) registerEnchantments(GREED);
        if (!registeredExperienced) registerEnchantments(EXPERIENCED);
        if (!registeredLazy) registerEnchantments(LAZY);
        if (!registeredSpeed) registerEnchantments(SPEED);
    }

    public static void registerEnchantments(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            e.printStackTrace();
        }
        if (registered) {
            System.out.println("[Prison Core] - Enchantment " + enchantment.getName() + " successfully registered!");
        }
    }
}
