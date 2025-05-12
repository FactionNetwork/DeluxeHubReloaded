package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandAction implements Action {

	@Override
	public String getIdentifier() {
		return "CONSOLE";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
	}
}
