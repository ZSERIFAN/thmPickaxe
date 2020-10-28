package by.thmihnea.utils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Random;

public class FortuneUtil {

    public static int calc(int level) {
        Random rand = new Random();
        int def = rand.nextInt(3);
        if (def == 0) def = 1;
        def += (int) (0.01D * (double) level);
        if (level >= 1) {
            while (level - 10 > 0) {
                def += 3;
                level -= 10;
            }
        }
        return def;
    }
}
