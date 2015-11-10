package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class leaveCommand extends Command {

	public leaveCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try{
			if(_args.size()>=2){				
				if(_args.get(1)!="true"){
					Main.bot.Leave(_args.get(1).toLowerCase(),(_args.size()>=3?true:false),true);
				}else{
					Main.bot.Leave(_sender.fromChannel,true,true);
				}
			}else{
				Main.bot.Leave(_sender.fromChannel.toLowerCase(),false,true);
			}
		}catch(Exception e){}
	}

}
