package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import org.bukkit.entity.Player;

public class CloseInventoryAction implements Action {

	@Override
	public String getIdentifier() {
		return "CLOSE";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		player.closeInventory();
	}
}
