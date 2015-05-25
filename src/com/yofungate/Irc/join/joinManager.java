package com.yofungate.Irc.join;

import com.yofungate.Irc.Irc;
import com.yofungate.Irc.channel;

public class joinManager {
	public long lastJoin;
	public Irc irc;
	public joinManager(Irc _irc){
		this.irc = _irc;
		this.lastJoin=System.currentTimeMillis();
	}
	int joinCount=0;
	public void Join(channel _channel) {
		if(joinCount>=5){joinCount=0;lastJoin+=500;}else{joinCount++;}
		joinThread thread = new joinThread(_channel,lastJoin,irc);
		thread.setName("Joining "+_channel.channelName);
		thread.start();
	}
}
