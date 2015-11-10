package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class wisCommand extends Command {

	public wisCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=3){
			String recipient = _args.get(1);
			String message = _sender.command.split(" ",3)[2];
			Main.wis.sendMSG(message, new CommandSender(recipient,null,null,null,false,false,false));
		}
	}

}
