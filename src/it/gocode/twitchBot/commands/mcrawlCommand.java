package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.yofungate.Irc.channel;
import com.yofungate.ircBot.Main;

public class mcrawlCommand extends Command {
	public static int crawlgoal;
	public static boolean isCrawling = false,isLogging = true,crawlStarted = false;
	public static Set<Entry<String, channel>> notCrawled;

	
	public mcrawlCommand(commandPerms perms) {
		super(perms);
	}


	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		try{
			if(_args.size()>=2){
				switch(_args.get(1)){
				case "begin" :
					if(!crawlStarted){
						notCrawled=Main.bot.channels.entrySet();
						crawlStarted=true;
					}
					isCrawling=true;
					break;
				case "end" :
					if(crawlStarted){
						isCrawling=false;crawlStarted=true;
						for(Entry<String, channel> chan : Main.bot.channels.entrySet()){
							Main.bot.Leave(chan.getValue().channelName,true,false);
						}
						Main.bot.channels.clear();
						for(Entry<String, channel> _c : notCrawled){
							Main.bot.Join(_c.getKey(),true,_c.getValue().isMuted);
						}
					}
					break;
				case "stop" :
					isCrawling=false;
					break;
				case "resume" :
					isCrawling=true;
					break;
				case "goal" :
					try{
						crawlgoal = _args.get(2)!=null?Integer.parseInt(_args.get(2))!=0?Integer.parseInt(_args.get(2)):0:0;
					}catch(NumberFormatException e){
						_sender.sendMSG(_sender+", usage of this command : !mcrawl goal [goal]");
					}
					break;
				case "count" :
					_sender.sendMSG(_sender+", I am inside "+Main.bot.channels.size()+" chat(s) so far ^.^");
					break;
				}
			}
		}catch(Exception e){}
	}

}
