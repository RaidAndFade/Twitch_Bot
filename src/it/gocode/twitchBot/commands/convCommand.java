package it.gocode.twitchBot.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class convCommand extends Command {
	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try{
			//TODO lol
			_sender.sendMSG(milliToString(Long.parseLong(_args.get(1))));
		}catch(Exception e){}
	}

	private String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}
}
