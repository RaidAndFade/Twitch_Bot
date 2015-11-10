package com.yofungate.ircBot;

import static com.yofungate.misc.misc.*;

import com.yofungate.Irc.channel;

public class LogManager {

	public static void logChat(final String _sender, final String _text, final String _curchannel) {
		new RThread(){
			public void run(){
				try {
					String query = escapeHTML("type=chat&msgText="+_text+"&msgChannel="+_curchannel+"&msgSender="+_sender);
					sendPOST("http://api.gocode.it/irclog.php",query);
				} catch (Exception e) {}
			}
		}.rename("Logging:"+_sender+"-"+_curchannel+"-"+(_text+"  ").substring(0, 3)).start();
	}
	public static void logJoinLeave(final String _user,final String _channel,final boolean _joined){
		new RThread(){
			public void run(){
				try {
					String query = escapeHTML("type=chat&msgSender="+_user+"&msgChannel="+_channel+"&msgText="+(_joined?"|Joined|":"|Left|"));
					sendPOST("http://api.gocode.it/irclog.php",query);
				} catch (Exception e) {}
			}
		}.rename("Logging:"+_user+"-"+_channel+"-"+(_joined?"Join":"Leave")).start();
	}
	public static void logChannel(final channel _Channel){
		new RThread(){
			public void run(){
				try {
					String query = escapeHTML("type=channel&chN="+_Channel.channelName);
				//	sendPOST("http://api.gocode.it/irclog.php",query);
				} catch (Exception e) {}
			}
		}.rename("LogChannel:"+_Channel.channelName).start();
	}
}
