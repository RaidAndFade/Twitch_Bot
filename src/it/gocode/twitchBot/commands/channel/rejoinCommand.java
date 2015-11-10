package it.gocode.twitchBot.commands.channel;

import java.io.IOException;
import java.util.List;

import com.yofungate.Irc.channel;
import com.yofungate.ircBot.Main;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class rejoinCommand extends Command {

	public rejoinCommand(commandPerms _perms) {
		super(_perms);
	}

	public rejoinCommand() {
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try {
			channel ch = Main.bot.channels.get(_sender.fromChannel);
			Main.bot.Leave(_sender.fromChannel, true, true);
			Main.bot.Join(_sender.fromChannel,true,ch.isMuted);
		} catch (IOException e) {}
	}

}
