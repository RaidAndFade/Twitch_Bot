package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.yofungate.ircBot.Main;

public class beCleverCommand extends Command {

	public static ArrayList<String> cleverWith = new ArrayList<String>();
	public static ArrayList<String> globalCleverWith = new ArrayList<String>();

	public beCleverCommand(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==2){
			if(_sender.isOwner&&_sender.fromChannel==null){
				if(globalCleverWith.contains(_args.get(1).toLowerCase()))globalCleverWith.remove(_args.get(1).toLowerCase()); else globalCleverWith.add(_args.get(1).toLowerCase());
				_sender.sendMSG("@"+_sender+" user \""+_args.get(1).toLowerCase()+"\" has been "+(globalCleverWith.contains(_args.get(1).toLowerCase())?"Added":"Removed"));
			}else{
				if(_sender.fromChannel!=null){
					if(cleverWith.contains(_args.get(1).toLowerCase()+""+_sender.fromChannel.toLowerCase()))cleverWith.remove(_args.get(1).toLowerCase()+""+_sender.fromChannel.toLowerCase()); else cleverWith.add(_args.get(1).toLowerCase()+""+_sender.fromChannel.toLowerCase());
					_sender.sendMSG("@"+_sender+" user \""+_args.get(1).toLowerCase()+"\" has been "+(cleverWith.contains(_args.get(1).toLowerCase()+""+_sender.fromChannel.toLowerCase())?"Added":"Removed"));
				}else{
					_sender.sendMSG("You can't do that!");
				}
			}
		}
	}

}
