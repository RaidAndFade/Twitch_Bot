package it.gocode.twitchBot.commands;

import com.yofungate.Irc.Irc;
import com.yofungate.ircBot.Main;

public class CommandSender {
	public String name;
	public String fromChannel;
	public String command;
	public boolean isOwner;
	public boolean isCHOwner;
	public boolean isMod;
	public Irc fromIrc;
	public boolean isFollower;
	public CommandSender(String _name, String _channel, String _comm, Irc _from, boolean _isOwner, boolean _isCHOwner, boolean _isMod){
		this(_name,_channel,_comm,_from,_isOwner,_isCHOwner,_isMod,false);
	}
	public CommandSender(String _name, String _channel, String _comm, Irc _from, boolean _isOwner, boolean _isCHOwner, boolean _isMod, boolean _isFollower){
		name = _name.toLowerCase();fromChannel = _channel;fromIrc=_from;isOwner = _isOwner;isCHOwner = _isCHOwner;isMod = _isMod;command = _comm;isFollower = _isFollower;
	}
	public void sendMSG(String _msg,boolean whisperPref){
		if(this.fromChannel==null||whisperPref){
			sendRespMSG(_msg);
		}else{
			sendChanMSG(_msg);
		}
	}
	public void sendMSG(String _msg){
		sendMSG(_msg,false);
	}
	public void sendRespMSG(String _msg){
		Main.wis.sendMSG(_msg, this);
	}
	public void sendChanMSG(String _msg) {
		if(fromChannel!=null)
			Main.bot.sendMSG(_msg, this);
	}
	@Override
	public String toString(){
		return this.name;
	}
}
