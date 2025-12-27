package vch.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import vch.uhc.UHC;
import vch.uhc.managers.PlayerManager;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.GameState;
import vch.uhc.models.UHCTeam;

public class ChatListener extends BaseListener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        e.setCancelled(true);

        Player sender = e.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(e.message());
        PlayerManager playerManager = UHC.getPlugin().getPlayerManager();
        UHCTeam senderTeam = playerManager.getPlayerByUUID(sender.getUniqueId()).getTeam();

        if (message.startsWith("!")) {

            String globalMessage = Messages.CHAT_GLOBAL_PREFIX(
                playerManager.getPlayerByUUID(sender.getUniqueId()).getName(),
                message.substring(1)
            );
            Bukkit.getServer().broadcast(net.kyori.adventure.text.Component.text(globalMessage));

        } else {

            if (senderTeam == null) {
                sender.sendMessage(message);
                return;
            }

            String teamMessage = Messages.CHAT_TEAM_PREFIX(sender.getName(), message);
            for (vch.uhc.models.UHCPlayer member : senderTeam.getMembers()) {
                Player player = Bukkit.getPlayer(member.getUuid());
                if (player != null) {
                    player.sendMessage(teamMessage);
                }
            }

        }

    }

}
