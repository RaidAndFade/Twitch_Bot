package com.yofungate.ircBot;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yofungate.Irc.ChannelIRC;
import com.yofungate.Irc.Irc;
import com.yofungate.Irc.WhisperIRC;
import com.yofungate.Irc.channel;
import com.yofungate.misc.misc;

import static com.yofungate.misc.misc.*;
import static com.yofungate.ircBot.LogManager.*;

public class Main {
	public static ChannelIRC bot;
	public static WhisperIRC wis;
	public static int crawlgoal;
	public static String[] argu;
	public static boolean isCrawling = false,isLogging = true,crawlStarted = false;
	public static Set<Entry<String, channel>> notCrawled;
	public static String[] bannedWords = {"dudeclever","nigger","nigga","bitch","***","anal","ass","fuck","fck","tard","retard"};
	public static Map<String, Float> times = new HashMap<String,Float>();
	public static Map<String, Integer> allstats = new HashMap<String,Integer>();	
	public static Map<String, Map<String, Integer>> chanstats = new HashMap<String, Map<String, Integer>>();
	public static Map<String, JsonObject> chandata = new HashMap<String, JsonObject>();
	private static Map<String, Long> startedAt = new HashMap<String, Long>();
	public static long botStarted;
	private static ChatterBotSession cbot;
	private static JEP j;
	private static String logURL = "api.gocode.it/irclog.php";
	public static void main(String... args){
		argu = args;
		Config.botOwner=args[2];Config.startingChannel=Config.botOwner;
		try {
			
			JsonArray wisServers = new Gson().fromJson(returnGet("http://tmi.twitch.tv/servers?cluster=group"), JsonObject.class).get("servers").getAsJsonArray();
			int wisServersSize = wisServers.size()-1;
			wis = new WhisperIRC(wisServers.get(randInt(0,wisServersSize)).getAsString(),args[0],args[1]);
			wis.Connect();
			wis.start();
			

			bot = new ChannelIRC(Config.server,args[0],args[1]);
			j = new JEP();j.addStandardConstants();j.addStandardFunctions();j.addComplex();j.setAllowUndeclared(true);j.setAllowAssignment(true);j.setImplicitMul(true);
			cbot = new ChatterBotFactory().create(ChatterBotType.CLEVERBOT).createSession();
			System.out.println(bot.Connect()?"Connected":"Could Not Connect");
			bot.Join(Config.startingChannel.toLowerCase(),false,false);
			bot.Join(args[0], true, false);
			//getChannels();
			botStarted = System.currentTimeMillis();
			bot.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Command.initCommands();
	}
	private static void getChannels() throws Exception {
		BufferedReader in = getGET("http://api.gocode.it/irclog.php?type=prejoin");
		for(String channel : in.readLine().split(",")){
			Main.bot.Join(channel.toLowerCase(),true,true);
		}
		for(String channel : in.readLine().split(",")){
			Main.bot.Join(channel.toLowerCase(),true,false);
		}
		for(String channel : in.readLine().split(",")){
			Main.bot.Join(channel.toLowerCase(),false,false);
		}
	}
	private static void beClever(final String _sender,final String _message,final String _channel){
		new RThread(){
			public void run(){
				String response;
				try {
					response = cbot.think(_message);
					//bot.sendMSG("@"+_sender+","+response, _channel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.rename("CleverBot Think").start();
	}
	public static void command(final String _sender,final String _command,final String _channel,final boolean isMod) throws Exception {
		final List<String> args = new ArrayList<String>();
		boolean isOwner = _sender.equalsIgnoreCase(Config.botOwner),isCHOwner = _sender.equalsIgnoreCase(_channel);
		args.addAll(Arrays.asList(_command.trim().split(" ")));
		//for(String copyc : copyThese){
		//	if(_sender.equalsIgnoreCase(copyc)){bot.sendMSG(_command.toLowerCase().replaceAll(" i ", _sender).replaceAll(" i ", _sender).replaceAll(Config.botOwner.toLowerCase(), _sender), _channel);return;}
		//}
		//for(String cleverc : cleverWith){
		//	if((_sender+""+_channel).equalsIgnoreCase(cleverc))if(!_command.startsWith("!"))beClever(_sender,_command,_channel);
		//}
		CommandSender CS = new CommandSender(_sender,_channel,_command,bot,isOwner,isCHOwner,isMod);
		String command = args.get(0).toLowerCase();
		if(Command.commandList.containsKey(command)){
			if(Command.commandList.get(command).hasPerms(CS)){
				Command.commandList.get(command).execute(CS, args);	
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
		if(Command.chanCommandList.containsKey(command)){
			if(Command.chanCommandList.get(command).hasPerms(CS)){
				Command.chanCommandList.get(command).execute(CS, args);	
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
		if(Command.SpecialCommands.containsKey(_channel+":"+command)){
			if(Command.SpecialCommands.get(_channel+":"+command).hasPerms(CS)){
				Command.SpecialCommands.get(_channel+":"+command).execute(CS, args);
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}else if(Command.SpecialCommands.containsKey(command)){
			if(Command.SpecialCommands.get(command).hasPerms(CS)){
				Command.SpecialCommands.get(command).execute(CS, args);
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
	}
	public static void command(final String _sender,final String _command) throws Exception {
		final List<String> args = new ArrayList<String>();
		boolean isOwner = _sender.equalsIgnoreCase(Config.botOwner);
		args.addAll(Arrays.asList(_command.trim().split(" ")));
		//for(String copyc : copyThese){
		//	if(_sender.equalsIgnoreCase(copyc)){bot.sendMSG(_command.toLowerCase().replaceAll(" i ", _sender).replaceAll(" i ", _sender).replaceAll(Config.botOwner.toLowerCase(), _sender), _channel);return;}
		//}
		//for(String cleverc : cleverWith){
		//	if((_sender+""+_channel).equalsIgnoreCase(cleverc))if(!_command.startsWith("!"))beClever(_sender,_command,_channel);
		//}
		CommandSender CS = new CommandSender(_sender,null,_command,wis,isOwner,false,false);
		String command = args.get(0);
		if(Command.commandList.containsKey(command)){
			if(Command.commandList.get(command).hasPerms(CS)){
				Command.commandList.get(command).execute(CS, args);	
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
		if(Command.wispCommandList.containsKey(command)){
			if(Command.wispCommandList.get(command).hasPerms(CS)){
				Command.wispCommandList.get(command).execute(CS, args);	
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
		if(Command.SpecialCommands.containsKey(command)){
			if(Command.SpecialCommands.get(command).hasPerms(CS)){
				Command.SpecialCommands.get(command).execute(CS, args);
			}else{
				CS.sendMSG("You are not allowed to use that command!",true); // You could also not send anything.
			}
		}
	}
	public static String[] getWords(String sentance){
		return sentance.split(" ");
	}
	public static void addToStats(final String _sentance,final String _channel) {
		if(_sentance.startsWith("!")){
			return;
		}
		new RThread(){
			public void run(){
				String[] words = getWords(_sentance);
				for(int i=0;i<words.length;i++){
					if(!chanstats.containsKey(_channel))
						chanstats.put(_channel, new HashMap<String,Integer>());
					if(chanstats.get(_channel).containsKey(words[i]))
						chanstats.get(_channel).put(words[i], chanstats.get(_channel).get(words[i])+1);
					else
						chanstats.get(_channel).put(words[i], 1);
					if(allstats.containsKey(words[i]))
						allstats.put(words[i],allstats.get(words[i])+1);
					else
						allstats.put(words[i],1);
				}
			}
		}.rename("Adding Words To Stats").start();
	}
	public static void profanityCheck(final String _sender,final String _message,final String _channel,boolean isMod){
		int spaceCount = 0;for(char chara : _message.toCharArray()){if(chara==' ')spaceCount++;}float spacePercent = (float)spaceCount/(float)_message.length();
		if(isMod&&!bot.channels.get(_channel).isMod(_sender)){
			if(_message.toLowerCase().contains("dudeclever")){if(isMod)tOUser(_sender,50F,_channel,"Youre So Clever Kappa.");}
			for(String bannedWord : bannedWords){if(_message.toLowerCase().contains(bannedWord.toLowerCase())){if(isMod)tOUser(_sender,1F,_channel,"Dont say bad words. BibleThump");}}
			if(spacePercent<0.06&&_message.length()>20){if(isMod)tOUser(_sender,0.1F,_channel,"Dont forget to use spaces. ^.^");}
			if(_message.length()>150){if(isMod)tOUser(_sender,0.2F,_channel,"Dont Spam Please. BibleThump");}
		}
	}
	public static void tOUser(final String _sender, float Multiplier, final String _channel,String Message){
		times.put(_sender, ((times.get(_sender)==null?1F:times.get(_sender)+Multiplier)));
		//bot.sendMSG(_sender+", "+Message, _channel);
		//bot.sendMSG("/timeout "+_sender+" "+(Math.round(Math.pow(times.get(_sender),2))), _channel);
		new RThread(){
			public void run(){
				try {this.sleep(2000);} catch (InterruptedException e) {}
				//bot.sendMSG("/timeout "+_sender+" "+(Math.round(Math.pow(times.get(_sender),2))), _channel);
			}
		}.start();
	}
	public static void updateChannelData(String _channel) {
		System.out.println(_channel);
		try{
			String jayson = returnGet("https://api.twitch.tv/kraken/streams?channel="+_channel);
			System.out.println(jayson);
			chandata.put(_channel, new Gson().fromJson(jayson,JsonObject.class));
		} catch (Exception e) {e.printStackTrace();}
	}
	public static void processChannelJoin(channel _Channel){
		logChannel(_Channel);
	}
	public static void processWhisper(final String sender,final String command) {
		new RThread(){
			public void run(){
				try {
					command(sender,command);
				} catch (Exception e) { e.printStackTrace(); }
			}

		}.rename("Processing-WH-"+sender).start();
	}
	public static void processChat(final String sender, final String command, final String curchannel, final boolean _isMod,final boolean _isJTV) throws Exception {
		new RThread(){
			public void run(){
				try {
					if(!_isJTV)profanityCheck(sender,command,curchannel,_isMod);
					if(!_isJTV)command(sender,command,curchannel,_isMod);
					if(!_isJTV)addToStats(command,curchannel);
				} catch (Exception e) {  e.printStackTrace(); }
			}

		}.rename("Processing-CH-"+sender).start();
		
		if(isLogging)logChat(sender,!_isJTV?command:command.replaceAll(",", "-"),curchannel);
	}
	public static void processJoinLeave(final String _user,final String _channel,final boolean _joined){
		new RThread(){
			public void run(){
				try {
					if(isLogging)logJoinLeave(_user,_channel,_joined);
				} catch (Exception e) {}
			}
		}.rename("Processing-JL-"+_user).start();
	}
}

