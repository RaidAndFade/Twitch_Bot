package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class clearAllStatsCommand extends Command {

	public clearAllStatsCommand(commandPerms _perms) {
		super(_perms);
	}

	public clearAllStatsCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		Main.allstats.clear();
		_sender.sendMSG("Cleared all stats.");
	}

}
