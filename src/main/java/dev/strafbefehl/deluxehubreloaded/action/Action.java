package dev.strafbefehl.deluxehubreloaded.action;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import org.bukkit.entity.Player;

public interface Action {

	String getIdentifier();

	void execute(DeluxeHubPlugin plugin, Player player, String data);

}
