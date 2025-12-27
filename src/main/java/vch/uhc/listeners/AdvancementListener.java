package vch.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.GameState;
import vch.uhc.models.UHCPlayer;

public class AdvancementListener extends BaseListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        Advancement advancement = event.getAdvancement();
        String advancementKey = advancement.getKey().getKey();
        
        if (advancementKey.startsWith("recipes/")) {
            return;
        }

        Player player = event.getPlayer();
        UHCPlayer uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        
        if (uhcPlayer == null) {
            return;
        }

        String advancementTitle = advancementKey;
        AdvancementDisplay display = advancement.getDisplay();
        if (display != null) {
            Component title = display.title();
            if (title != null) {
                advancementTitle = PlainTextComponentSerializer.plainText().serialize(title);
            }
        }

        String randomName = uhcPlayer.getRandomName();
        String message = Messages.ADVANCEMENT_MADE(randomName, advancementTitle);
        
        Bukkit.getScheduler().runTask(UHC.getPlugin(), () -> {
            Bukkit.getServer().broadcast(Component.text(message));
        });

    }

}
