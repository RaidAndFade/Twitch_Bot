package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.ArrayList;
import java.util.List;

public class copyCatCommand extends Command {
	public static ArrayList<String> copyCat = new ArrayList<String>();

	public copyCatCommand(commandPerms _perms) {
		super(_perms);
	}

	public copyCatCommand() {}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		
	}

}
