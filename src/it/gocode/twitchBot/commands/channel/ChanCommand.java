package it.gocode.twitchBot.commands.channel;

import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public abstract class ChanCommand extends Command {

	public ChanCommand(commandPerms perms){
		super(perms);
	}
	
	public ChanCommand(){}
	
	@Override
	public abstract void execute(CommandSender _sender, List<String> _args);

}
