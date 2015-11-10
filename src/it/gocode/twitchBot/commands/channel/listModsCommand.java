package it.gocode.twitchBot.commands.channel;

import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;

import com.yofungate.ircBot.Main;

public class listModsCommand extends ChanCommand {

	public listModsCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2&&Main.bot.channels.containsKey(_args.get(1))){
			_sender.sendMSG("The moderators of #"+_args.get(1)+" are :"+Main.bot.channels.get(_args.get(1)).listMods());
		}else{
			if(_sender.fromChannel!=null){
				_sender.sendMSG("The moderators of this channel are :"+Main.bot.channels.get(_sender.fromChannel).listMods());
			}
		}
	}

}
