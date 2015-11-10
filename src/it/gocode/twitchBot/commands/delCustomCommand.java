package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

public class delCustomCommand extends Command {

	public delCustomCommand(commandPerms _perms) {
		super(_perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2){
			String ccommand = _args.get(1).toLowerCase().replace("!", "");
			if(_sender.isOwner){
				if(Command.SpecialCommands.containsKey("!"+ccommand)){
					if(ccommand.contains(":")){
						Command.SpecialCommands.remove(ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]);
						_sender.sendMSG("!"+ccommand.split(":",2)[0]+":!"+ccommand.split(":",2)[1]+"' has been removed.", true);
					}else{
						Command.SpecialCommands.remove("!"+ccommand);
						_sender.sendMSG("'!"+ccommand+"' has been removed.", true);
					}
				}else{
					_sender.sendMSG("That command is not set!");
				}
			}else if(_sender.isCHOwner||_sender.isMod){
				if(Command.SpecialCommands.containsKey(_sender.fromChannel+":!"+ccommand)){
					Command.SpecialCommands.remove(_sender.fromChannel+":!"+ccommand);
					_sender.sendMSG("'!"+ccommand+"' has been removed.", true);
				}else{
					_sender.sendMSG("That command does not exist!", true);
				}
			}
		}
	}

}
