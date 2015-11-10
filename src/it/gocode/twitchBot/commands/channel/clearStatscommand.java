package it.gocode.twitchBot.commands.channel;

import java.util.List;

import com.yofungate.ircBot.Main;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class clearStatscommand extends ChanCommand {

	public clearStatscommand(commandPerms _perms) {
		super(_perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		Main.chanstats.get(_sender.fromChannel).clear();
		_sender.sendMSG("This chat's stats has been cleared.");
	}

}
