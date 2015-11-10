package it.gocode.twitchBot.commands;

import java.io.IOException;
import java.util.List;

import com.yofungate.ircBot.Main;

public class joinCommand extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try {
		boolean isSilent=(_args.size()>=3&&_args.get(2).equalsIgnoreCase("true")),isMute=(_args.size()>=4&&_args.get(3).equalsIgnoreCase("true"));
		isSilent=_sender.isOwner&&isSilent;
		if(_args.size()>=2 && _sender.isOwner){
			if(_args.get(1)!="true"){
				for(String channel : _args.get(1).split(",")){
					Main.bot.Join(channel.toLowerCase(),isSilent,isMute);
				}
			}else{
				Main.bot.Join(_args.get(1),isSilent,isMute);
			}
		}else{
			Main.bot.Join(_sender.name.toLowerCase(),isSilent,isMute);
		}
		} catch (IOException e) {}
	}

}
