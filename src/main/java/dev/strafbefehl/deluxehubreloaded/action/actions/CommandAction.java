package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import org.bukkit.entity.Player;

public class CommandAction implements Action {

	@Override
	public String getIdentifier() {
		return "COMMAND";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		player.chat(data.contains("/") ? data : "/" + data);
	}
}
