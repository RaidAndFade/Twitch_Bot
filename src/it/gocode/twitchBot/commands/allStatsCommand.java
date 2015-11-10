package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.yofungate.ircBot.Main;

public class allStatsCommand extends Command {

	public allStatsCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(Main.allstats.containsKey(_args.get(1)))
			_sender.sendMSG("\""+_args.get(1)+"\" has been sent "+Main.allstats.get(_args.get(1))+" Times in "+Main.bot.channels.size()+" channels in the past "+milliToString(System.currentTimeMillis()-Main.botStarted)+" (That's "+((float)Main.allstats.get(_args.get(1))/(float)(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-Main.botStarted)))+" per minute).");
		else
			_sender.sendMSG("\""+_args.get(1)+"\" has never been sent in "+Main.bot.channels.size()+" channels.");
	}

	private String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}

}
