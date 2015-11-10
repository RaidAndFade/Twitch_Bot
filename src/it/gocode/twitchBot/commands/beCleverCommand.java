package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yofungate.ircBot.Main;

public class beCleverCommand extends Command {

	public static ArrayList<String> cleverWith = new ArrayList<String>();

	public beCleverCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==2){
			if(_sender.isOwner){
				if(cleverWith.contains(_args.get(1)))cleverWith.remove(_args.get(1)); else cleverWith.add(_args.get(1));
				_sender.sendMSG("@"+_sender+" user \""+_args.get(1)+"\" has been "+(cleverWith.contains(_args.get(1))?"Added":"Removed"));
			}else{
				if(_sender.fromChannel!=null){
					if(cleverWith.contains(_args.get(1)+""+_sender.fromChannel))cleverWith.remove(_args.get(1)+""+_sender.fromChannel); else cleverWith.add(_args.get(1)+""+_sender.fromChannel);
					_sender.sendMSG("@"+_sender+" user \""+_args.get(1)+"\" has been "+(cleverWith.contains(_args.get(1)+""+_sender.fromChannel)?"Added":"Removed"));
				}else{
					_sender.sendMSG("You can't do that!");
				}
			}
		}
	}

}
