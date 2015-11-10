package com.yofungate.Irc;

import it.gocode.twitchBot.commands.CommandSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.yofungate.Irc.join.joinManager;
import com.yofungate.ircBot.Main;

public class Irc extends Thread{
	protected String server,username,password;
	public Map<String,channel> channels = new HashMap<String,channel>();
	protected  Socket socket;
	BufferedWriter writer;
	protected BufferedReader reader;
	protected joinManager joinManager;
	protected static boolean isConnected=false;
	public Irc(String _server, String _username) {
		new Irc(_server,_username,"");
	}
	public Irc(String _server,String _username,String _password){
		setName("IRC Thread");
		this.server=_server;
		this.username=_username;
		this.password=_password;
	}
	public boolean Connect() throws Exception{
        return false;
	}
	public void Join(String _channel,boolean silent, boolean isMute) throws IOException{
		_channel = _channel.toLowerCase();
		if(!channels.containsKey(_channel)){
			channel thisChannel = new channel(_channel);
			thisChannel.isMuted=isMute;
			thisChannel.silentJoin=silent;
			joinManager.Join(thisChannel);
		}
	}
	public void Leave(String _channel,boolean silent,boolean removefromARR) throws IOException{
		if(!silent)
			sendMSG("I am leaving your channel("+_channel+")! Cya.", _channel);
		getWriter().write("PART #"+_channel+"\r\n");
		getWriter().flush();
		if(removefromARR)
			channels.remove(_channel);
	}
	public void reConnect() throws Exception{
		Main.main(Main.argu);
	}
	public void run() {
		try {
		while(isConnected){
			String dline;
			final BufferedWriter tempWriter = getWriter();
			while((dline=reader.readLine())!=null){
				final String line = dline;
				try {
					//if(line!=null)System.out.println(line);	
					if(line.toLowerCase().startsWith("ping ")){
						//Respond to keep alive~
						tempWriter.write("PONG yofungate.com "+line.substring(5)+"\r\n");
						tempWriter.flush();
					}else if(line.toLowerCase().contains(" join ")){
						final String user = line.replaceFirst(":", "").split("JOIN")[0].split("@",2)[0].split("!",2)[0].trim();
						final String curchannel = line.replaceFirst(":", "").split("JOIN")[1].split(":",2)[0].replace("#", "").trim();
						Main.processJoinLeave(user, curchannel, true);
					}else if(line.toLowerCase().contains(" part ")){
						final String user = line.replaceFirst(":", "").split("PART")[0].split("@",2)[0].split("!",2)[0].trim();
						final String curchannel = line.replaceFirst(":", "").split("PART")[1].split(":",2)[0].replace("#", "").trim();
						Main.processJoinLeave(user, curchannel, false);
					}else if(line.toLowerCase().contains(" notice ")){
						final boolean isModList = line.contains("@msg-id=room_mods :tmi.twitch.tv");
						final String command = line.replaceFirst("@", "").replaceFirst(":", "").split("NOTICE")[1].split(":",2)[1];
						final String curchannel = line.replaceFirst("@", "").replaceFirst(":", "").split("NOTICE")[1].split(":",2)[0].replace("#", "").trim();
						if(isModList&&(command.contains("The moderators of this room are:")||command.contains("There are no moderators of this room."))){
							channels.get(curchannel).setMods(command.replace("The moderators of this room are: ", "").split(", "));
						}
					}else if(line.toLowerCase().contains(" whisper ")){
						final boolean isJTV = line.contains("jtv!jtv@jtv.tmi.twitch.tv");
						final String sender = line.replaceFirst("@", "").replaceFirst(":", "").split("WHISPER")[0].split(".tmi.twitch.tv",2)[0].split("@",2)[1];//Get the sender
						final String command = line.replaceFirst("@", "").replaceFirst(":", "").split("WHISPER")[1].split(":",2)[1];
						if(!isJTV){
							if(Main.isCrawling){
								if(channels.size()>=Main.crawlgoal)Main.isCrawling=false;
								try {
									Join(sender.toLowerCase(),true,true);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						Main.processWhisper(sender,command);
					}else if(line.toLowerCase().contains(" privmsg ")){
						final boolean isJTV = line.contains("jtv!jtv@jtv.tmi.twitch.tv");
						final String sender = line.replaceFirst("@", "").replaceFirst(":", "").split("PRIVMSG")[0].split(".tmi.twitch.tv",2)[0].split("@",2)[1];//Get the sender
						final String command = line.replaceFirst("@", "").replaceFirst(":", "").split("PRIVMSG")[1].split(":",2)[1];
						final String curchannel = line.replaceFirst("@", "").replaceFirst(":", "").split("PRIVMSG")[1].split(":",2)[0].replace("#", "").trim();
						if(!channels.get(curchannel).hasMods()){
							sendMSG("/mods",curchannel);
						}
						if(!isJTV){
							if(Main.isCrawling){
								if(channels.size()>=Main.crawlgoal)Main.isCrawling=false;
								try {
									Join(sender.toLowerCase(),true,true);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						
						Main.processChat(sender,command,curchannel,channels.get(curchannel).isMod(username),isJTV);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			reConnect();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void sendMSG(final String msg,final String channel){
		new Thread(){
			private long lastTime;
			public void run(){
				try {
					if(channels.get(channel).isMuted&&!msg.startsWith("/")){return;}
					if((System.currentTimeMillis()-this.lastTime)<1000){return;}
					try {
						getWriter().write("PRIVMSG #"+channel+" :"+msg+"\r\n");
						getWriter().flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {}
			}
		}.start();
	}
	public BufferedWriter getWriter() {
		return writer;
	}
	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

}

