package it.gocode.twitchBot.commands.wis;

import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;

import static it.gocode.twitchBot.commands.channel.raffleCommandHandler.*;

public class raffleSetupHandler extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(!curRaffles.containsKey(_sender.name)){
			_sender.sendMSG("You need to !raffle setup in your channel first", true);
			return;
		}
		if(_args.size()>=2){
			String subCommand = _args.get(1);
			switch(subCommand){
				//TODO raffle quickstart whisper
				case "award" :
					awardPrize(_sender,_args);
					break;
				case "prize" :
					setPrize(_sender,_args);
					break;
				}
		}else{
			if(curRaffles.containsKey(_sender.name)){
				_sender.sendMSG("!raffle "+(curRaffles.get(_sender.name).prize==null?"prize <winner's message>":curRaffles.get(_sender.name).accepting?"award <user>":""), true);
			}
		}
	}
	private void setPrize(CommandSender _sender, List<String> _args) {
		if(curRaffles.containsKey(_sender.name)){
			curRaffles.get(_sender.name).setPrize(_sender.command.split(" ",3)[2]);
			_sender.sendMSG("You set the prize message to ' "+_sender.command.split(" ",3)[2]+" ', If this is not correct, use the same command to re-set it. Otherwise type '!raffle start' in your channel",true);
		}else{
			_sender.sendMSG("You need to setup the raffle first. Type '!raffle setup' in your channel",true);
		}
	}
	
	private void awardPrize(CommandSender _sender, List<String> _args) {
		if(curRaffles.containsKey(_sender.name)){
			//TODO
		}else{
			_sender.sendMSG("There is no raffle going on right now.", true);
		}
	}
}
