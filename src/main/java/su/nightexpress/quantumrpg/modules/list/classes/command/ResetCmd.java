package su.nightexpress.quantumrpg.modules.list.classes.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import mc.promcteam.engine.utils.PlayerUT;
import su.nightexpress.quantumrpg.Perms;
import su.nightexpress.quantumrpg.modules.command.MCmd;
import su.nightexpress.quantumrpg.modules.list.classes.ClassManager;

public class ResetCmd extends MCmd<ClassManager> {

	public ResetCmd(@NotNull ClassManager module) {
		super(module, new String[] {"reset"}, Perms.CLASS_CMD_RESET);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Classes_Cmd_Reset_Desc.getMsg();
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Classes_Cmd_Reset_Usage.getMsg();
	}

	@Override
	public boolean playersOnly() {
		return false;
	}

	@Override
	@NotNull
	public List<String> getTab(@NotNull Player player, int i, @NotNull String[] args) {
		if (i == 1) {
			return PlayerUT.getPlayerNames();
		}
		return super.getTab(player, i, args);
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		if (args.length != 2) {
			this.printUsage(sender);
			return;
		}
		
		Player player = plugin.getServer().getPlayer(args[1]);
		if (player == null) {
			this.errPlayer(sender);
			return;
		}
		
		this.module.resetClassData(player);
		
		plugin.lang().Classes_Cmd_Reset_Done
			.replace("%player%", player.getName())
			.send(sender);
	}
}
