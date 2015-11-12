package it.gocode.twitchBot.commands.wis;

import java.util.List;

import com.yofungate.ircBot.Main;

import it.gocode.twitchBot.commands.CommandSender;

public class listModsCommand extends WisCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2&&Main.bot.channels.containsKey(_args.get(1))){
			_sender.sendMSG("The moderators of #"+_args.get(1)+" are :"+Main.bot.channels.get(_args.get(1)).listMods());
		}else{
			_sender.sendMSG("I have no data for that channel!");
		}
	}

}
