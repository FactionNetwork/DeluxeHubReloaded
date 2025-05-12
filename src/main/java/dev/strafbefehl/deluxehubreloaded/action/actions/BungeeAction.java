package dev.strafbefehl.deluxehubreloaded.action.actions;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.strafbefehl.deluxehubreloaded.DeluxeHubPlugin;
import dev.strafbefehl.deluxehubreloaded.action.Action;
import org.bukkit.entity.Player;

@Deprecated
public class BungeeAction implements Action {

	@Override
	public String getIdentifier() {
		return "BUNGEE";
	}

	@Override
	public void execute(DeluxeHubPlugin plugin, Player player, String data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(player.getName());
		out.writeUTF(data);
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
}
