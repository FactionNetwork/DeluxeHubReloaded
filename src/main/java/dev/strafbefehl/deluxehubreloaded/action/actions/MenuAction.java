package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import dev.strafbefehl.deluxehubreloaded.inventory.AbstractInventory;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

	@Override
	public String getIdentifier() {
		return "MENU";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		AbstractInventory inventory = plugin.getInventoryManager().getInventory(data);

		if (inventory != null) {
			inventory.openInventory(player);
		} else {
			plugin.getLogger().warning("[MENU] Action Failed: Menu '" + data + "' not found.");
		}
	}
}
