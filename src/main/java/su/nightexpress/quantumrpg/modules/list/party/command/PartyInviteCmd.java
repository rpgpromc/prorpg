package su.nightexpress.quantumrpg.modules.list.party.command;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import mc.promcteam.engine.utils.PlayerUT;
import su.nightexpress.quantumrpg.Perms;
import su.nightexpress.quantumrpg.modules.command.MCmd;
import su.nightexpress.quantumrpg.modules.list.party.PartyManager;

public class PartyInviteCmd extends MCmd<PartyManager> {

	public PartyInviteCmd(@NotNull PartyManager m) {
		super(m, new String[] {"invite"}, Perms.PARTY_CMD_INVITE);
	}

	@Override
	@NotNull
	public String usage() {
		return plugin.lang().Party_Cmd_Invite_Usage.getMsg();
	}
	
	@Override
	@NotNull
	public List<String> getTab(@NotNull Player p, int i, @NotNull String[] args) {
		if (i == 1) {
			return PlayerUT.getPlayerNames();
		}
		return super.getTab(p, i, args);
	}

	@Override
	@NotNull
	public String description() {
		return plugin.lang().Party_Cmd_Invite_Desc.getDefaultMsg();
	}
	
	@Override
	public boolean playersOnly() {
		return true;
	}
	
	@Override
	public void perform(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;
		if (args.length != 2) {
			this.printUsage(sender);
			return;
		}
		
		Player invite = plugin.getServer().getPlayer(args[1]);
		module.invitePlayer(player, invite);
	}
}
