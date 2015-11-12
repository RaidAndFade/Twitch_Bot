package com.yofungate.Irc;

import it.gocode.twitchBot.commands.CommandSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.yofungate.Irc.join.joinManager;

public class ChannelIRC extends Irc {

	protected boolean isWhisperClient=false;
	
	public ChannelIRC(String _server, String _username) {
		super(_server, _username);
		setName("Channels Thread");
	}

	public ChannelIRC(String _server, String _username, String _password) {
		super(_server, _username, _password);
		setName("Channels Thread");
	}
	
	public boolean Connect() throws Exception{
		socket = new Socket(server,6667);//Connect to the IRC Server
		setWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));//Get an Output stream (Send)
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Get an Input Stream (Recieve)
		writer.write("PASS "+password+"\r\n"); // Send OAuth Key (for twitch)
		writer.write("NICK "+username.toLowerCase()+"\r\n"); // Send NickName
		writer.write("USER "+username.toLowerCase()+" 8 * : MemanBot v0.1 \r\n"); // Send Username 
		writer.write("CAP REQ :twitch.tv/membership \r\n");
		writer.write("CAP REQ :twitch.tv/commands \r\n");
		writer.write("CAP REQ :twitch.tv/tags \r\n");
		writer.flush(); //Send it all
		this.joinManager=new joinManager(this);
		String line = "";
		while ((line = reader.readLine( )) != null) {
			System.out.println(line);
            if (line.indexOf("004") >= 0) {
            	isConnected=true;
            	return true;
            }
        }
		return false;
	}
	public long lastTime;
	public void sendMSG(final String msg,final CommandSender _sender){
		final String channel = _sender.fromChannel;
		new Thread("Sending Message to "+(_sender.fromChannel)){
			public void run(){
				try {
					if(channels.get(channel).isMuted&&!msg.startsWith("/")){return;}
					while((System.currentTimeMillis()-lastTime)<1550){Thread.sleep(System.currentTimeMillis()-lastTime);}
					try {
						writer.write("PRIVMSG #"+channel+" :"+msg+"\r\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					lastTime = System.currentTimeMillis();
				} catch (Exception e) {e.printStackTrace();}
			}
		}.start();
	}
}
