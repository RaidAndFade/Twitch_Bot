package it.gocode.twitchBot.commands.group;

import java.util.ArrayList;
import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;

public class joingroupCommand extends groupCommand {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		String groupName = _sender.command.split(" ",2)[1];
		if(!groups.containsKey(groupName)){
			groups.put(groupName, new ArrayList<String>());
		}
		if(groups.get(groupName)==null||!groups.get(groupName).contains(_sender.name)){
			groups.get(groupName).add(_sender.name);
			_sender.sendMSG("@"+_sender+" you joined ' "+groupName+" '! ' "+groupName+" ' now has '"+groups.get(groupName).size()+"' members.");
		}else{
			_sender.sendMSG("@"+_sender+" you are already a member of ' "+groupName+" '");
		}
	}

}
