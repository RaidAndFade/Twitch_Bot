package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class sayCommand extends Command {

	public sayCommand(commandPerms _perms) {
		super(_perms);
	}

	public sayCommand() {
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try{
		if(_args.size()>=2){
			if(_sender.command.split(" ",2)[1].startsWith("#")){
				if(Main.bot.channels.containsKey(_sender.command.split(" ")[1].replace("#", ""))){
					if(Main.bot.channels.get(_sender.command.split(" ")[1].replace("#", "")).isMuted){
						_sender.sendMSG(_sender+", I am muted in that channel",true);
					}else{
						Main.bot.sendMSG(_sender.command.split(" ",3)[2], _sender.command.split(" ")[1].replace("#", ""));
					}
				}else{
					_sender.sendMSG(_sender.command.split(" ",2)[1]);
				}
			}else if(_sender.fromChannel!=null){
				_sender.sendMSG(_sender.command.split(" ",2)[1]);
			}else{
				_sender.sendMSG("Whisper Usage : !s <channel> <message>");
			}
		}else{
			if(_sender.fromChannel!=null){
				_sender.sendMSG("Usage : !s [channel] <message>");
			}else{
				_sender.sendMSG("Whisper Usage : !s <channel> <message>");
			}
		}
		}catch(Exception e){e.printStackTrace();}
	}

}
