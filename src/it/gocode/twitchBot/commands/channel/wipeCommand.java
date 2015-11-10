package it.gocode.twitchBot.commands.channel;

import java.util.List;

import com.yofungate.ircBot.Main;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public class wipeCommand extends ChanCommand {

	public wipeCommand(commandPerms _perms) {
		super(_perms);
	}

	public wipeCommand() {}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2){
			Main.times.put(_args.get(1), 0F);
		}else{
			Main.times.put(_sender.name, 0F);
		}
		_sender.sendMSG((_args.size()>=2?_args.get(1):_sender.name)+"'s file has been cleared", true);
	}

}
