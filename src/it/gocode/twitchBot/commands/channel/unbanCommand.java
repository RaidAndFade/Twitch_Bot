package it.gocode.twitchBot.commands.channel;

import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class unbanCommand extends Command {

	public unbanCommand(commandPerms _perms) {
		super(_perms);
	}

	public unbanCommand() {
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()<2){_sender.sendMSG("Usage : !unban <user>");return;}
		_sender.sendMSG("/unban "+(_args.size()>=2?_args.get(1):""));
		_sender.sendMSG(_args.get(1)+" Has been unbanned by "+_sender);
	}

}
