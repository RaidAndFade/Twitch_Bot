package it.gocode.twitchBot.commands.channel;

import static it.gocode.twitchBot.commands.channel.raffleCommandHandler.curRaffles;
import static it.gocode.twitchBot.commands.channel.raffleCommandHandler.waitingPrize;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.channel.raffles.Raffle;
import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.yofungate.Irc.Irc;

public class raffleCommandHandler extends ChanCommand {
	
	public static Map<String,Raffle> curRaffles = new HashMap<String,Raffle>();
	public static ArrayList<String> waitingPrize = new ArrayList<String>();

	public raffleCommandHandler(commandPerms perms) {
		super(perms);
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()>=2){
			String subCommand = _args.get(1);
			switch(subCommand){
				case "setup" :
					setupRaffle(_sender,_args);
					break;
				case "start" :
					startRaffle(_sender);
					break;
				case "end" :
					endRaffle(_sender);
					break;
			}
		}else{
			if(curRaffles.containsKey(_sender.fromChannel))
				_sender.sendMSG("!raffle <start/end>", true);
			else
				_sender.sendMSG("!raffle setup [UserClaimRequired] [FollowerOnly]", true);
		}
	}

	private void startRaffle(CommandSender _sender) {
		if(curRaffles.containsKey(_sender.fromChannel)){
			if(curRaffles.get(_sender.fromChannel).prize!=null){
				curRaffles.get(_sender.fromChannel).accepting=true;
				//TODO
				new CommandSender("",_sender.fromChannel,"",_sender.fromIrc,false,false,false).sendMSG("A raffle has been started! Type '1' to join. "+(curRaffles.get(_sender.fromChannel).follower?"Only followers may compete in this raffle.":""));
			}else{
				_sender.sendMSG("You need to set the prize first. Whisper !raffle prize <message> to me. Message being what you want to send to the winner.", true);
			}
		}else{
			_sender.sendMSG("You need to setup the raffle first. Type '!raffle setup' in your channel.",true);
		}
	}
	private void setupRaffle(final CommandSender _sender,List<String> _args) {
		if(!curRaffles.containsKey(_sender.fromChannel)){
			if(_args.size()>=3){
				curRaffles.put(_sender.fromChannel, new Raffle(_args.get(2).toLowerCase().startsWith("y"),false,_sender.fromChannel));
				_sender.sendMSG("You need to set the prize first. Whisper !raffle prize <message> to me. Message being what you want to send to the winner.", true);
			}else{
				//_sender.sendMSG("Usage : !raffle setup [UserClaimRequired] [FollowerOnly] <-- both of these are Yes/No", true);
				_sender.sendMSG("Usage : !raffle setup [UserClaimRequired]<-- Yes/No", true);
			}
		}else if(waitingPrize.contains(_sender.fromChannel)){
			_sender.sendMSG("Set your raffle's prize, Whisper !raffle prize to me for help.", true);
		}else{
			_sender.sendMSG("There is already a raffle going on.", true);
		}
	}
	private void endRaffle(final CommandSender _sender) {
		if(curRaffles.containsKey(_sender.fromChannel)){
			new Thread("Re-assign winner-"+_sender.fromChannel){
				public void run(){
					try {
						while(curRaffles.containsKey(_sender.fromChannel)&&(curRaffles.get(_sender.fromChannel).awaitingClaim||curRaffles.get(_sender.fromChannel).accepting)){
							if(curRaffles.get(_sender.fromChannel).winner!=""){
								curRaffles.get(_sender.fromChannel).remove(curRaffles.get(_sender.fromChannel).winner);
							}
							_sender.sendMSG("A winner has been selected! "+curRaffles.get(_sender.fromChannel).selectWinner()+" You have 30 seconds to claim your prize, type something in chat.");
							Thread.sleep(30000);
						}
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				}
			}.start();
		}else{
			_sender.sendMSG("There is no raffle going on right now.", true);
		}
	}

}
