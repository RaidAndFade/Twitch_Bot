package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

public class permCustomCommand extends Command {

	public permCustomCommand(commandPerms _perms) {
		super(_perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=3){
			String ccommand = _args.get(1).toLowerCase().replace("!", "");
			if(_sender.isOwner){
				if(Command.SpecialCommands.containsKey("!"+ccommand)){
					if(ccommand.contains(":"))
						Command.SpecialCommands.get(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]).perms = strToPerms(_args.get(2));
					else
						Command.SpecialCommands.get("!"+ccommand).perms = strToPerms(_args.get(2));
					_sender.sendMSG("!"+ccommand+"'s perm is now "+_args.get(2));
				}else{
					_sender.sendMSG("That command is not set!");
				}
			}else if(_sender.isCHOwner||_sender.isMod){
				if(Command.SpecialCommands.containsKey(_sender.fromChannel+":!"+ccommand)){
					Command.SpecialCommands.get(_sender.fromChannel+":!"+ccommand).perms = strToPerms(_args.get(2));
					_sender.sendMSG("!"+ccommand+"'s perm is now "+_args.get(2));
				}else{
					if(Command.SpecialCommands.containsKey("!"+ccommand)){
						Command.SpecialCommands.put(_sender.fromChannel+":!"+ccommand, Command.SpecialCommands.get("!"+ccommand));
						Command.SpecialCommands.get(_sender.fromChannel+":!"+ccommand).perms = strToPerms(_args.get(2));
						_sender.sendMSG("!"+ccommand+"'s perm is now "+_args.get(2));
					}else
						_sender.sendMSG("That command does not exist!");
				}
			}
		}
	}

	private commandPerms strToPerms(String input) {
		switch(input){
		case "o":return commandPerms.OwnerOnly;
		case "h+":return commandPerms.CHOwnerAbove;
		case "m+":return commandPerms.ModAbove;
		default: return commandPerms.Anyone;
		}
	}
}
