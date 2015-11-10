package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class reloadChanDataCommand extends Command {

	public reloadChanDataCommand(commandPerms _perms) {
		super(_perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		_sender.sendMSG("Reloading #"+(_sender.fromChannel!=null?_sender.fromChannel:_args.get(1))+" Data");
		if(_sender.fromChannel!=null||_args.size()>=2)
			Main.updateChannelData(_sender.fromChannel!=null?_sender.fromChannel:_args.get(1));
	}

}
