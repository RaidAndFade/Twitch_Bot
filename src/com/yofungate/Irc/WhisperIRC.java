package com.yofungate.Irc;

import it.gocode.twitchBot.commands.CommandSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.yofungate.Irc.join.joinManager;

public class WhisperIRC extends Irc {
	private boolean isWhisperClient = true;
	
	public WhisperIRC(String _server, String _username) {
		super(_server, _username);
		setName("Whispers Thread");
	}

	public WhisperIRC(String _server, String _username, String _password) {
		super(_server, _username, _password);
		setName("Whispers Thread");
	}
	
	public boolean Connect() throws Exception{
		socket = new Socket(server.split(":")[0],443);//Connect to the IRC Server
		setWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));//Get an Output stream (Send)
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Get an Input Stream (Recieve)
		getWriter().write("PASS "+password+"\r\n"); // Send OAuth Key (for twitch)
		getWriter().write("NICK "+username.toLowerCase()+"\r\n"); // Send NickName
		getWriter().write("USER "+username.toLowerCase()+" 8 * : MemanBot v0.1 \r\n"); // Send Username 
		getWriter().write("CAP REQ :twitch.tv/commands \r\n");
		getWriter().write("CAP REQ :twitch.tv/tags \r\n");
		getWriter().flush(); //Send it all
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
		new Thread("Sending Message to "+_sender.name){
			private long lastTime;
			public void run(){
				try {
					if(System.currentTimeMillis()-lastTime<1000){return;}
					System.out.println("PRIVMSG #jtv :/w "+_sender.name+" "+msg+" \r\n");
					writer.write("PRIVMSG #jtv :/w "+_sender.name+" "+msg+" \r\n");
					writer.flush();
				} catch (Exception e) {}
			}
		}.start();
		lastTime = System.currentTimeMillis();
	}
}
