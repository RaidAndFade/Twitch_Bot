package it.gocode.twitchBot.commands.channel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.yofungate.ircBot.Main;

import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class statsCommand extends ChanCommand {

	public statsCommand(commandPerms perms) {
		super(perms);
		// TODO Auto-generated constructor stub
	}

	public statsCommand() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==2){
			if(Main.chanstats.get(_sender.fromChannel).containsKey(_args.get(1)))
				_sender.sendMSG("\""+_args.get(1)+"\" has been sent "+Main.chanstats.get(_sender.fromChannel).get(_args.get(1))+" Times in this channel in the past "+milliToString(System.currentTimeMillis()-Main.joinedAt.get(_sender.fromChannel))+" (That's "+Math.round((float)Main.chanstats.get(_sender.fromChannel).get(_args.get(1))/(float)(TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()-Main.joinedAt.get(_sender.fromChannel))))+" per minute).");
			else
				_sender.sendMSG("\""+_args.get(1)+"\" has never been sent in this channel.");
		}else{
			_sender.sendMSG("Usage : !stats <search>",true);
		}
		
	}

	private String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}

}
