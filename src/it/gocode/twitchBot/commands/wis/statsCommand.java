package it.gocode.twitchBot.commands.wis;

import it.gocode.twitchBot.commands.CommandSender;

import java.util.List;

import com.yofungate.ircBot.Main;

public class statsCommand extends WisCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==3){
			if(Main.chanstats.containsKey(_args.get(1))){
				if(Main.chanstats.get(_args.get(2)).containsKey(_args.get(1)))
					_sender.sendMSG("\""+_args.get(1)+"\" has been sent "+Main.chanstats.get(_args.get(2)).get(_args.get(1))+" Times in this channel.");
				else
					_sender.sendMSG("\""+_args.get(1)+"\" has never been sent in this channel.");
			}else{
				_sender.sendMSG("No data about that channel! Sorry BibleThump ");
			}
		}else{
			_sender.sendMSG("Usage : !stats <search> <channel>");
		}
	}

}
