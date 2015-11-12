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
				_sender.sendMSG("Usage : !mcom <add/del/perm/info>",true);break;
			}
		}else{
			_sender.sendMSG("Usage : !mcom <add/del/perm/info>",true);
		}
	}

	private void info(CommandSender _sender, List<String> _args) {
		if(_args.size()>=3){
			String ccommand = _args.get(2).toLowerCase().replace("!", "");
			customCommand command = null;
			if(_sender.isOwner){
				if(ccommand.contains(":")){
					if(Command.SpecialCommands.containsKey(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1])){
						command = (customCommand) Command.SpecialCommands.get(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}else{
					if(Command.SpecialCommands.containsKey("!"+ccommand)){
						command = (customCommand) Command.SpecialCommands.get("!"+ccommand);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}
			}else if(_sender.isMod||_sender.isCHOwner){
				if(Command.SpecialCommands.containsKey(_sender.fromChannel+":"+ccommand)){
					command = (customCommand) Command.SpecialCommands.get(_sender.fromChannel+":"+ccommand);
				}else{
					_sender.sendMSG("That command is not set!",true);
				}
			}
			if(command!=null){
				_sender.sendMSG("!" + ccommand+" | Output : "+command.response+" | Perms = "+command.perms.toString(),true);
			}
		}else{
			_sender.sendMSG("Usage : !mcom info <command>",true);
		}
	}

	private void perm(CommandSender _sender, List<String> _args) {
		if(_args.size()>=4){
			String ccommand = _args.get(2).toLowerCase().replace("!", "");
			String modifier = _args.get(3).toLowerCase();
			if(_sender.isOwner){
				if(ccommand.contains(":")){
					if(Command.SpecialCommands.containsKey(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1])){
						Command.SpecialCommands.get(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]).perms = strToPerms(_sender,modifier);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}else{
					if(Command.SpecialCommands.containsKey("!"+ccommand)){
						Command.SpecialCommands.get("!"+ccommand).perms = strToPerms(_sender,modifier);
						_sender.sendMSG("!"+ccommand+"'s perm is now "+Command.SpecialCommands.get("!"+ccommand).perms.toString(),true);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}
			}else if(_sender.isMod||_sender.isCHOwner){
				if(Command.SpecialCommands.containsKey(_sender.fromChannel+":!"+ccommand)){
					Command.SpecialCommands.get(_sender.fromChannel+":!"+ccommand).perms = strToPerms(_sender,modifier);
					_sender.sendMSG("!"+ccommand+"'s perm is now "+Command.SpecialCommands.get("!"+ccommand).perms.toString(),true);
				}else{
					if(Command.SpecialCommands.containsKey("!"+ccommand)){
						Command.SpecialCommands.put(_sender.fromChannel+":!"+ccommand, Command.SpecialCommands.get("!"+ccommand));
						Command.SpecialCommands.get(_sender.fromChannel+":!"+ccommand).perms = strToPerms(_sender,modifier);
						_sender.sendMSG("!"+ccommand+"'s perm is now "+Command.SpecialCommands.get("!"+ccommand).perms.toString(),true);
					}else
						_sender.sendMSG("That command does not exist!",true);
				}
			}
		}else{
			_sender.sendMSG("Usage : !mcom perm <command> <"+(_sender.isOwner?"o/":"")+(_sender.isCHOwner?"b/":"")+"m/a>",true);
		}
	}

	private void del(CommandSender _sender, List<String> _args) {
		if(_args.size()>=3){
			String ccommand = _args.get(2).toLowerCase().replace("!", "");
			if(_sender.isOwner){
				if(ccommand.contains(":")){
					if(Command.SpecialCommands.containsKey(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1])){
						Command.SpecialCommands.remove(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]);
						_sender.sendMSG("!"+ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]+"' has been removed.", true);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}else{
					if(Command.SpecialCommands.containsKey("!"+ccommand)){
						Command.SpecialCommands.remove("!"+ccommand);
						_sender.sendMSG("'!"+ccommand+"' has been removed.", true);
					}else{
						_sender.sendMSG("That command is not set!",true);
					}
				}
			}else if(_sender.isMod||_sender.isCHOwner){
				if(Command.SpecialCommands.containsKey(_sender.fromChannel+":!"+ccommand)){
					if(Command.SpecialCommands.get(_sender.fromChannel+":!"+ccommand).perms!=commandPerms.ModAbove){
						if(!(_sender.isCHOwner||_sender.isOwner)){
							_sender.sendMSG("You don't have permission to do that.");
						}
					}
					Command.SpecialCommands.remove(_sender.fromChannel+":!"+ccommand);
					_sender.sendMSG("'!"+ccommand+"' has been removed.", true);
				}else{
					_sender.sendMSG("That command does not exist!", true);
				}
			}
		}else{
			_sender.sendMSG("Usage : !mcom del <command>",true);
		}
	}

	private void add(CommandSender _sender, List<String> _args){
		if(_args.size()>=3){
			String ccommand = _args.get(2).toLowerCase().replace("!", "");
			if(_sender.isOwner){
				if(ccommand.contains(":")){
					if(!Command.SpecialCommands.containsKey(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1])){
						Command.addCustomChanCommand(ccommand.split(":",2)[0],ccommand.split(":",2)[1],_sender.command.split(" ",4)[3]);
						_sender.sendMSG("Command added! '!"+ccommand+"'",true);
					}else{
						_sender.sendMSG("That command already exists!",true);
					}
				}else{
					if(!Command.SpecialCommands.containsKey("!"+ccommand)){
						Command.addCustomGlobalCommand(ccommand,_sender.command.split(" ",4)[3]);
						_sender.sendMSG("'!"+ccommand+"' has been added.", true);
					}else{
						_sender.sendMSG("That command already exists!",true);
					}
				}
			}else if(_sender.isMod||_sender.isCHOwner){
				if(!Command.SpecialCommands.containsKey(_sender.fromChannel+":"+ccommand)){
					Command.addCustomChanCommand(_sender.fromChannel,ccommand,_sender.command.split(" ",4)[3]);
					_sender.sendMSG("Command added! '!"+ccommand+"'",true);
				}else{
					_sender.sendMSG("That command already exists!",true);
				}
			}
		}else{
			_sender.sendMSG("Usage : !mcom add <command> <response>",true);
		}
	}

	private commandPerms strToPerms(CommandSender _sender, String input) {
		commandPerms output = commandPerms.Anyone;
		switch(input){
		case "o":output = commandPerms.OwnerOnly;break;
		case "b":output = commandPerms.CHOwnerAbove;break;
		case "m":output = commandPerms.ModAbove;break;
		}
		if(!_sender.isOwner&&output==commandPerms.OwnerOnly){
			output = commandPerms.CHOwnerAbove;
		}
		if(!_sender.isCHOwner&&output==commandPerms.CHOwnerAbove){
			output = commandPerms.ModAbove;
		}
		return output;
	}
}
