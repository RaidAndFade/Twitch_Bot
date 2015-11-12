package it.gocode.twitchBot.channel.raffles;

import it.gocode.twitchBot.commands.CommandSender;

import java.util.ArrayList;

import com.yofungate.ircBot.Main;
import com.yofungate.misc.misc;

public class Raffle {
	
	public ArrayList<String> contestants;
	public String prize;
	public String channel;
	public String winner;
	
	public boolean accepting = false;
	public boolean mustClaim = true;
	public boolean follower = true;
	
	public boolean awaitingClaim = false;
	
	public Raffle(boolean _claim, boolean _follower, String _channel){
		mustClaim = _claim;follower = _follower;channel = _channel;
		contestants = new ArrayList<String>();
	}	
	
	public void setPrize(String _prize) {
		this.prize = _prize;
	}
	
	public void addContestant(String _user){
		contestants.add(_user.toLowerCase());
	}
	
	public String selectWinner(){
		awaitingClaim=mustClaim;
		accepting=false;
		winner = contestants.get(misc.randInt(0, contestants.size()-1)).toLowerCase();
		return winner;
	}
	
	public void sendPrize(String _winner){
		new CommandSender(_winner,channel,"",Main.wis,false,false,false).sendMSG(channel+" : "+prize,true);
		awaitingClaim=false;
		
	}

	public void remove(String _user) {
		contestants.remove(_user);
	}
}
