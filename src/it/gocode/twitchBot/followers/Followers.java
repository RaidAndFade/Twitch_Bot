package it.gocode.twitchBot.followers;

import it.gocode.twitchBot.commands.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.*;
import com.yofungate.Irc.Irc;
import com.yofungate.Irc.channel;
import com.yofungate.ircBot.Main;
import com.yofungate.misc.misc;

public class Followers {
	public static followerCounter fC;
	public static Map<String, ArrayList<String>> followers = new HashMap<String, ArrayList<String>>();
	
	public static void initThread(){
		fC = new followerCounter("Follower notifier");
		fC.start();
	}
	
	public static class followerCounter extends Thread{
		public followerCounter(String name) {
			super(name);
		}
		public void run(){
			while(true){
				try {
					for(Entry<String, channel> _c : Main.bot.channels.entrySet()){
						if(!_c.getValue().isMod(Main.bot.getUsername()))continue;
						if(!isOnline(_c.getKey()))continue;
						ArrayList<String> chanFollowers = new ArrayList<String>();
						String followerData = misc.returnGet("https://api.twitch.tv/kraken/channels/"+_c.getKey()+"/follows");
						JsonObject followerJData = new Gson().fromJson(followerData, JsonObject.class);
						JsonArray followsJData = followerJData.getAsJsonArray("follows");
						for(int i=0;i<followsJData.size();i++){
							chanFollowers.add(followsJData.get(i).getAsJsonObject().get("user").getAsJsonObject().get("display_name").getAsString());
						}
						if(followers.containsKey(_c.getKey())){
							ArrayList<String> oldChanFollowers = followers.get(_c.getKey());
							ArrayList<String> newChanFollowers = (ArrayList<String>) chanFollowers.clone();
							newChanFollowers.removeAll(oldChanFollowers);
							if(newChanFollowers.size()>0){
								String newFollowers = "";
								for(String follower : newChanFollowers){
									newFollowers += follower +",";
								}
								newFollowers = newFollowers.substring(0, newFollowers.length()-1);
								new CommandSender("",_c.getKey(),"",Main.bot,false,false,false).sendMSG("Thank you for following : "+newFollowers);
							}
						}
						
						followers.put(_c.getKey(), chanFollowers);
						//System.out.println(Arrays.toString(chanFollowers.toArray()));
						//Get the user's followers from the https://api.twitch.tv/kraken/channels/CHANNEL/follows url
						//store into temporary arraylist & put into followers map when done.
					}
					Thread.sleep(60000);
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}

	public static boolean isOnline(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		return Main.chandata.get(_channel).get("streams").getAsJsonArray().size()>=1;
	}
}
