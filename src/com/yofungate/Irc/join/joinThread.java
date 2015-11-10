package com.yofungate.Irc.join;

import java.io.IOException;

import com.yofungate.Irc.Irc;
import com.yofungate.Irc.channel;
import com.yofungate.ircBot.Main;

public class joinThread extends Thread {
	private channel chn;private long myTime;private Irc irc;
	public joinThread(channel _channel,long _myTime, Irc _irc) {
		this.chn=_channel;this.myTime=_myTime;this.irc=_irc;
	}
	public void run(){
		while(System.currentTimeMillis()<myTime){
			try {sleep(25);}catch(InterruptedException e){}
		}
		try {
			if(!irc.channels.containsKey(chn.channelName)){
				irc.getWriter().write("JOIN #"+chn.channelName.toLowerCase()+"\r\n");
				irc.getWriter().flush();
				while(!irc.channels.containsKey(chn.channelName))
					irc.channels.put(chn.channelName,chn);
				
				Main.processChannelJoin(chn);
				if(!chn.silentJoin)
					irc.sendMSG("I have joined your channel("+chn.channelName+")! Hello there.", chn.channelName);
				System.err.println("Connected to "+chn.channelName);
				irc.sendMSG("/color green",chn.channelName);
			}
		} catch (IOException e) {	}
	}
}
