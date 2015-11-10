package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

public class addCustomCommand extends Command {

	public addCustomCommand(commandPerms _perms) {
		super(_perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
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
