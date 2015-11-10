package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class isModCommand extends Command {

	public isModCommand(commandPerms _perms) {
		super(_perms);
	}

	public isModCommand() {	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_sender.fromChannel!=null){
			if(_args.size()>=2)
				_sender.sendMSG(_sender+", "+(_args.get(1))+" is "+(Main.bot.channels.get(_sender.fromChannel).isMod(_args.get(1))?"":"not")+" a mod for #"+_sender.fromChannel);
			else
				_sender.sendMSG("Usage : !ismod <user>",true);
		}else{
			if(_args.size()>=3)
				if(Main.bot.channels.containsKey(_args.get(1).replace("#",""))&&Main.bot.channels.get(_args.get(1).replace("#","")).hasMods())
					_sender.sendMSG(_sender+", "+(_args.get(2))+" is "+(Main.bot.channels.get(_args.get(1).replace("#","")).isMod(_args.get(2))?"":"not")+" a mod for #"+_args.get(1).replace("#",""));
				else
					_sender.sendMSG("That channel is not recorded.",true);
			else
				_sender.sendMSG("Usage : !ismod <channel> <user>",true);
		}
	}

}
