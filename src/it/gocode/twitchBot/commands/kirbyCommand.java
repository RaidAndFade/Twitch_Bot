package it.gocode.twitchBot.commands;

import java.util.List;

public class kirbyCommand extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		_sender.sendMSG(_sender.name+", <(^.^<) <(^.^)> (>^.^)>");
	}

}
