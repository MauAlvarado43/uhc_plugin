package vch.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;

public class AdvancementListener extends BaseListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {

        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        Advancement advancement = event.getAdvancement();
        String advancementKey = advancement.getKey().getKey();
        
        if (advancementKey.startsWith("recipes/")) {
            return;
        }

        Player player = event.getPlayer();
        vch.uhc.models.Player uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
        
        if (uhcPlayer == null) {
            return;
        }

        String advancementTitle = advancementKey;
        if (advancement.getDisplay() != null && advancement.getDisplay().title() != null) {
            advancementTitle = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(advancement.getDisplay().title());
        }

        String randomName = uhcPlayer.getRandomName();
        String message = Messages.ADVANCEMENT_MADE(randomName, advancementTitle);
        
        Bukkit.getScheduler().runTask(UHC.getPlugin(), () -> {
            Bukkit.getServer().broadcast(net.kyori.adventure.text.Component.text(message));
        });

    }

}
