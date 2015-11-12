package it.gocode.twitchBot.commands.wis;

import it.gocode.twitchBot.commands.CommandSender;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.yofungate.ircBot.Main;

public class statsCommand extends WisCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==3){
			if(Main.chanstats.containsKey(_args.get(1))){
				if(Main.chanstats.get(_args.get(1)).containsKey(_args.get(2)))
					_sender.sendMSG("\""+_args.get(2)+"\" has been sent "+Main.chanstats.get(_args.get(1)).get(_args.get(2))+
							" Times in this channel in #"+_args.get(1)+" in the past "
							+milliToString(System.currentTimeMillis()-Main.joinedAt.get(_args.get(1)))+
							" (That's "+
									((float)
										Main.chanstats.get(_args.get(1)).get(_args.get(2))
									/
									(float)
										((System.currentTimeMillis()-Main.joinedAt.get(_args.get(1)))/60/1000))
									+
							" per minute).");
				else
					_sender.sendMSG("\""+_args.get(2)+"\" has never been sent in this channel.");
			}else{
				_sender.sendMSG("No data about that channel! Sorry BibleThump ");
			}
		}else{
			_sender.sendMSG("Usage : !stats <search> <channel>");
		}
	}

	private String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}

}
