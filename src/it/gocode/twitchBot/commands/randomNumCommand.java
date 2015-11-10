package it.gocode.twitchBot.commands;

import java.util.List;
import static com.yofungate.misc.misc.*;

public class randomNumCommand extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try{
			_sender.sendMSG(_sender+", your number is "+randInt((_args.size()>=3?Integer.parseInt(_args.get(1)):0), (_args.size()>=2?Integer.parseInt(_args.get(2)):100)));
		}catch(NumberFormatException e){
			_sender.sendMSG(_sender+", usage of this command : !rand [max] [min]");
		}
		
	}

}
