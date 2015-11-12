package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class mmuteCommand extends Command {

	public mmuteCommand(commandPerms _perms) {
		super(_perms);
	}

	public mmuteCommand() {	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_sender.isOwner&&_args.size()>=2){
			String cchannel = _args.get(1);
			if(Main.bot.channels.containsKey(cchannel)){
				Main.bot.channels.get(cchannel).isMuted = !Main.bot.channels.get(cchannel).isMuted;
				_sender.sendMSG("Bot "+(Main.bot.channels.get(cchannel).isMuted?"":"un")+"muted in #"+cchannel+".", true);
			}else{
				_sender.sendMSG("I am not in that channel",true);
			}
		}else if(_sender.fromChannel!=null){
			Main.bot.channels.get(_sender.fromChannel).isMuted = !Main.bot.channels.get(_sender.fromChannel).isMuted;
			_sender.sendMSG("Bot "+(Main.bot.channels.get(_sender.fromChannel).isMuted?"":"un")+"muted!", true);
		}else{
			_sender.sendMSG("Usage : !mmute <channel>");
		}
	}

}
