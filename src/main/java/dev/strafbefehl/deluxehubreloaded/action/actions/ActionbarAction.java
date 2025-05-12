package dev.strafbefehl.deluxehubreloaded.action.actions;

import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import dev.strafbefehl.deluxehubreloaded.utility.TextUtil;
import dev.strafbefehl.deluxehubreloaded.utility.reflection.ActionBar;
import org.bukkit.entity.Player;

public class ActionbarAction implements Action {

	@Override
	public String getIdentifier() {
		return "ACTIONBAR";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		ActionBar.sendActionBar(player, TextUtil.color(data));
	}
}
