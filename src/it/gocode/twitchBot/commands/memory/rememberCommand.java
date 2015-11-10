package it.gocode.twitchBot.commands.memory;

import java.util.List;

import it.gocode.twitchBot.commands.CommandSender;

public class rememberCommand extends memoryCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2){
			_sender.sendMSG("Remembering "+_sender.command.split(" ",2)[1]+" for "+_sender);
			memory.put(_sender.name,_sender.command.split(" ",2)[1]);
		}else{
			_sender.sendMSG("Usage : !remember *what you want it to remember*");
		}
	}
}
