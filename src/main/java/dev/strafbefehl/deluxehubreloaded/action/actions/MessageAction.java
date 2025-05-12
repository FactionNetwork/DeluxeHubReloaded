package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import dev.strafbefehl.deluxehubreloaded.utility.TextUtil;
import org.bukkit.entity.Player;

public class MessageAction implements Action {

	@Override
	public String getIdentifier() {
		return "MESSAGE";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		if (data.contains("<center>") && data.contains("</center>")) data = TextUtil.getCenteredMessage(data);
		player.sendMessage(TextUtil.color(data));
	}
}
