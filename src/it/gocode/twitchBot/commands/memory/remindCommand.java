package it.gocode.twitchBot.commands.memory;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;

import java.util.List;

public class remindCommand extends memoryCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		_sender.sendMSG(memory.get(_sender.name)!=null?"You said \""+memory.get(_sender.name)+"\" to me @"+_sender+".":"I don't have anything for you. @"+_sender);
	}

}
