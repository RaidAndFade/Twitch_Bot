package it.gocode.twitchBot.commands.custom;

import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class customCommandHandler extends Command {

	public customCommandHandler(commandPerms modabove) {
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2){
			String subCommand = _args.get(1);
			switch(subCommand){
			case "add" :
				add(_sender,_args);
				break;
			case "del" :
				del(_sender,_args);
				break;
			case "perm" :
				perm(_sender,_args);
				break;
			case "info" :
				info(_sender,_args);
				break;
			default : 
				_sender.sendMSG("Usage : !maddcom <add/del/perm/info>",true);break;
			}
		}else{
			_sender.sendMSG("Usage : !maddcom <add/del/perm/info>",true);
		}
	}

	private void info(CommandSender _sender, List<String> _args) {
	}

	private void perm(CommandSender _sender, List<String> _args) {
	}

	private void del(CommandSender _sender, List<String> _args) {
	}

	private void add(CommandSender _sender, List<String> _args){
		if(_args.size()>=3){
			String ccommand = _args.get(1).toLowerCase().replace("!", "");
			if(_sender.isOwner){
				if(!Command.SpecialCommands.containsKey(ccommand)){
					if(_args.get(1).contains(":"))
						Command.addCustomChanCommand(ccommand.split(":",2)[0],ccommand.split(":",2)[1],_sender.command.split(" ",3)[2]);
					else
						Command.addCustomGlobalCommand(ccommand,_sender.command.split(" ",3)[2]);
					_sender.sendMSG("Command added! '!"+ccommand+"'",true);
				}else{
					_sender.sendMSG("That command already exists!",true);
				}
			}else if(_sender.isMod||_sender.isCHOwner){
				if(!Command.SpecialCommands.containsKey(_sender.fromChannel+":"+ccommand)){
					Command.addCustomChanCommand(_sender.fromChannel,ccommand,_sender.command.split(" ",3)[2]);
					_sender.sendMSG("Command added! '!"+ccommand+"'",true);
				}else{
					_sender.sendMSG("That command already exists!",true);
				}
			}
		}else{
			_sender.sendMSG("Usage : !maddcom <command> <response>",true);
		}
	}
}
