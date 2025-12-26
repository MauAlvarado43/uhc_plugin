package vch.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import vch.uhc.UHC;
import vch.uhc.managers.PlayerManager;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.models.Team;

public class ChatListener extends BaseListener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        e.setCancelled(true);

        Player sender = e.getPlayer();
        String message = e.getMessage();
        PlayerManager playerManager = UHC.getPlugin().getPlayerManager();
        Team senderTeam = playerManager.getPlayerByUUID(sender.getUniqueId()).getTeam();

        if (message.startsWith("!")) {

            String globalMessage = Messages.CHAT_GLOBAL_PREFIX(
                playerManager.getPlayerByUUID(sender.getUniqueId()).getName(),
                message.substring(1)
            );
            Bukkit.broadcastMessage(globalMessage);

        } else {

            if (senderTeam == null) {
                sender.sendMessage(message);
                return;
            }

            String teamMessage = Messages.CHAT_TEAM_PREFIX(sender.getName(), message.substring(1));
            for (vch.uhc.models.Player member : senderTeam.getMembers()) {
                Player player = Bukkit.getPlayer(member.getUuid());
                if (player != null) {
                    player.sendMessage(teamMessage);
                }
            }

        }

    }

}
