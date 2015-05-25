package com.yofungate.Irc;

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
	private String server,username,password;
	public Map<String,channel> channels = new HashMap<String,channel>();
	private  Socket socket;public BufferedWriter writer;private BufferedReader reader;
	private joinManager joinManager;
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
	public boolean Connect() throws UnknownHostException, IOException{
		socket = new Socket(server,6667);//Connect to the IRC Server
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//Get an Output stream (Send)
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Get an Input Stream (Recieve)
		writer.write("PASS "+password+"\r\n"); // Send OAuth Key (for twitch)
		writer.write("NICK "+username.toLowerCase()+"\r\n"); // Send NickName
		writer.write("USER "+username.toLowerCase()+" 8 * : MemanBot v0.1 \r\n"); // Send Username 
		writer.write("TWITCHCLIENT 3 \r\n");
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
	public void Join(String _channel,boolean silent, boolean isMute) throws IOException{
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
		writer.write("PART #"+_channel+"\r\n");
		writer.flush();
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
			final BufferedWriter tempWriter = writer;
			while((dline=reader.readLine())!=null){
				final String line = dline;
				try {
					if(line!=null)System.out.println(line);	
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
					}else if(line.toLowerCase().contains(" privmsg ")){
						final boolean isJTV = line.contains("jtv!jtv@jtv.tmi.twitch.tv");
						final String sender = line.replaceFirst(":", "").split("PRIVMSG")[0].split("@",2)[0].split("!",2)[0].trim();//Get the sender
						final String command = line.replaceFirst(":", "").split("PRIVMSG")[1].split(":",2)[1];
						final String curchannel = line.replaceFirst(":", "").split("PRIVMSG")[1].split(":",2)[0].replace("#", "").trim();
						if(!channels.get(curchannel).hasMods()){
							sendMSG("/mods",curchannel);
						}
						if(isJTV&&(command.contains("The moderators of this room are:")||command.contains("There are no moderators of this room."))){
							channels.get(curchannel).setMods(command.replace("The moderators of this room are: ", "").split(", "));
						}
						if(!isJTV){
							if(Main.isCrawling){
								if(Main.crawlgoal<=channels.size())Main.isCrawling=false;
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
			public void run(){
				try {
					if(channels.get(channel).isMuted&&!msg.startsWith("/")){return;}
					try {
						writer.write("PRIVMSG #"+channel+" :"+msg+"\r\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (Exception e) {}
			}
		}.start();
	}
}

