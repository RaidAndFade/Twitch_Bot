package com.yofungate.ircBot;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;

import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.yofungate.Irc.Irc;
import com.yofungate.Irc.channel;
import static com.yofungate.misc.misc.*;

public class Main {
	public static Irc bot;
	public static int crawlgoal;
	public static String[] argu;
	public static boolean isCrawling = false,isLogging = true,crawlStarted = false;
	public static Set<Entry<String, channel>> notCrawled;
	public static ArrayList<String> hugables = new ArrayList<String>();
	public static ArrayList<String> kissables = new ArrayList<String>();
	public static ArrayList<String> fuckables = new ArrayList<String>();
	public static ArrayList<String> copyThese = new ArrayList<String>();
	public static ArrayList<String> cleverWith = new ArrayList<String>();
	public static String[] bannedWords = {"dudeclever","nigger","nigga","bitch","***","anal","ass","fuck","fck","tard","retard"};
	private static Map<String, String> memory = new HashMap<String,String>();
	private static Map<String, String> commands = new HashMap<String,String>();
	private static Map<String, String> commandPerms = new HashMap<String,String>();
	private static Map<String, String> groups = new HashMap<String,String>();
	private static Map<String, Integer> groupcount = new HashMap<String,Integer>();
	private static Map<String, Float> times = new HashMap<String,Float>();
	private static ChatterBotSession cbot;
	private static JEP j;
	public static void main(String... args){
		argu = args;
		Config.botOwner=args[2];Config.startingChannel=Config.botOwner;
		bot = new Irc(Config.server,args[0],args[1]);
		try {
			j = new JEP();j.addStandardConstants();j.addStandardFunctions();j.addComplex();j.setAllowUndeclared(true);j.setAllowAssignment(true);j.setImplicitMul(true);
			cbot = new ChatterBotFactory().create(ChatterBotType.JABBERWACKY).createSession();
			System.out.println(bot.Connect()?"Connected":"Could Not Connect");
			bot.Join(Config.startingChannel.toLowerCase(),false,false);
			bot.Join(args[0], true, false);
			getChannels();
			bot.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void getChannels() throws Exception {
		BufferedReader in = getGET("Http://192.168.5.55/api/irclog.php?type=prejoin");
		command(Config.botOwner,"!mejoin "+in.readLine()+" true true",Config.botOwner,false);
		command(Config.botOwner,"!mejoin "+in.readLine()+" true",Config.botOwner,false);
		command(Config.botOwner,"!mejoin "+in.readLine()+"",Config.botOwner,false);
	}
	private static void beClever(final String _sender,final String _message,final String _channel){
		new RThread(){
			public void run(){
				String response;
				try {
					response = cbot.think(_message);
					System.out.println(_message + ":" + response );
					bot.sendMSG("@"+_sender+","+response, _channel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.rename("CleverBot Think").start();
	}
	public static void command(final String _sender,final String _command,final String _channel,final boolean isMod) throws Exception {
		final ArrayList<String> args = new ArrayList<String>();
		boolean isOwner = _sender.equalsIgnoreCase(Config.botOwner),isCHOwner = _sender.equalsIgnoreCase(_channel);
		args.addAll(Arrays.asList(_command.trim().split(" ")));
		for(String copyc : copyThese){
			if(_sender.equalsIgnoreCase(copyc)){bot.sendMSG(_command, _channel);return;}
		}
		for(String cleverc : cleverWith){
			if((_sender+""+_channel).equalsIgnoreCase(cleverc))if(!_command.startsWith("!"))beClever(_sender,_command,_channel);
		}
		switch(args.get(0)){
		case "!math" :
			if(args.size()>=2){
				doMath(_sender,args.get(1),_channel);
			}
			break;
		case "!beclever":
			if(isOwner&&args.size()==2){
				if(cleverWith.contains(args.get(1)+""+_channel))cleverWith.remove(args.get(1)+""+_channel); else cleverWith.add(args.get(1)+""+_channel);
				bot.sendMSG("@"+_sender+" user \""+args.get(1)+"\" has been "+(cleverWith.contains(args.get(1)+""+_channel)?"Added":"Removed"), _channel);
			}
			break;
		case "!mejoin" : 
			boolean isSilent=(args.size()>=3&&args.get(2).equalsIgnoreCase("true")),isMute=(args.size()>=4&&args.get(3).equalsIgnoreCase("true"));
			if(args.size()>=2){
				if(args.get(1)!="true"){
					for(String channel : args.get(1).split(",")){
						bot.Join(channel.toLowerCase(),isSilent,isMute);
					}
				}else{
					bot.Join(args.get(1),isSilent,isMute);
				}
			}else{
				bot.Join(_sender.toLowerCase(),isSilent,isMute);
			}
			break;
		case "!meleave" : 
			if(isCHOwner||isOwner){
				if(args.size()>=2){				
					if(args.get(1)!="true"){
						bot.Leave(args.get(1).toLowerCase(),(args.size()>=3?true:false),true);
					}else{
						bot.Leave(_sender.toLowerCase(),true,true);
					}
				}else{
					bot.Leave(_channel.toLowerCase(),false,true);
				}
			}
			break;
		case "!mcrawl" :
			if(args.size()>=2&&isOwner){
				switch(args.get(1)){
				case "begin" :
					if(!crawlStarted){
						notCrawled=bot.channels.entrySet();
						crawlStarted=true;
					}
					isCrawling=true;
					break;
				case "end" :
					if(crawlStarted){
						isCrawling=false;crawlStarted=true;
						for(Entry<String, channel> chan : bot.channels.entrySet()){
							bot.Leave(chan.getValue().channelName,true,false);
						}
						bot.channels.clear();
						for(Entry<String, channel> _c : notCrawled){
							bot.Join(_c.getKey(),true,_c.getValue().isMuted);
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
						crawlgoal = args.get(2)!=null?Integer.parseInt(args.get(2))!=0?Integer.parseInt(args.get(2)):0:0;
					}catch(NumberFormatException e){
						bot.sendMSG(_sender+", usage of this command : !mcrawl goal [goal]", _channel);
					}
					break;
				case "count" :
					bot.sendMSG(_sender+", I am inside "+bot.channels.size()+" chat(s) so far ^.^", _channel);
					break;
				}
			}
			break;
		case "!coin" : 
			bot.sendMSG(_sender+", you tossed a coin, It landed on "+(randInt(0, 100)>50?"Heads":"Tails"), _channel);
			break;
		case "!rand" : 
			try{
			bot.sendMSG(_sender+", your number is "+randInt((args.size()>=3?Integer.parseInt(args.get(2)):0), (args.size()>=2?Integer.parseInt(args.get(1)):100)), _channel);
			}catch(NumberFormatException e){
				bot.sendMSG(_sender+", usage of this command : !rand [max] [min]", _channel);
			}
			break;
		case "!listmods" :
			if((isCHOwner||isOwner)){
				if(args.size()>=2&&bot.channels.containsKey(_command.split(" ")[1].replace("#", ""))){
					bot.sendMSG("The moderators of #"+_command.split(" ")[1].replace("#", "")+" are :"+bot.channels.get(_command.split(" ")[1].replace("#", "")).listMods() , _channel);
				}else{
					bot.sendMSG("The moderators of this channel are :"+bot.channels.get(_channel).listMods() , _channel);
				}
			}
			break;
		case "!kirby" :
			bot.sendMSG(_sender+", <(^.^<) <(^.^)> (>^.^)>", _channel);
			break;
		case "!slap" :
			bot.sendMSG(_sender+" has slapped "+(args.size()>=2&&_sender!="popsterfang"?args.get(1):"Themself"), _channel);
			break;
		case "!dance" :
			bot.sendMSG("/me breaks into dance. ^.^", _channel);
			break;
		case "!remember" :
			if(args.size()>=2){
				bot.sendMSG("Remembering "+_command.split(" ",2)[1]+" for "+_sender, _channel);
				memory.put(_sender,_command.split(" ",2)[1]);
			}else{
				bot.sendMSG("Usage : !remember ", _channel);
			}
			break;
		case "!remind" :
			bot.sendMSG(memory.get(_sender)!=null?"You said \""+memory.get(_sender)+"\" to me @"+_sender+".":"I don't have anything for you. @"+_sender, _channel);
			break;
		case "!joingroup" :
			if(groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")==null||!groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic").contains(_sender)){
				groups.put(args.size()>=2?_command.split(" ",2)[1]:"Basic", groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")!=null?groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")+_sender+" ":_sender+" ");
				groupcount.put(args.size()>=2?_command.split(" ",2)[1]:"Basic", groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")==null?1:groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")+1);
				bot.sendMSG("@"+_sender+" you have joined '"+(args.size()>=2?_command.split(" ",2)[1]:"Basic")+"'. this group now has "+groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")+" member"+(groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")!=1?"s":""), _channel);
			}else{
				bot.sendMSG("@"+_sender+" you are already a member of '"+(args.size()>=2?_command.split(" ",2)[1]:"Basic")+"'", _channel);
			}
			break;
		case "!leavegroup" :
			try{
			if(groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic").contains(_sender)){
				groups.put(args.size()>=2?_command.split(" ",2)[1]:"Basic", groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")!=null?groups.get(args.size()>=2?_command.split(" ",2)[1]:"Basic").replace(_sender+" ",""):"");
				groupcount.put(args.size()>=2?_command.split(" ",2)[1]:"Basic", groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")==null?0:groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")-1);
				bot.sendMSG("@"+_sender+" you have left '"+(args.size()>=2?_command.split(" ",2)[1]:"Basic")+"'. this group now has "+groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")+" member"+(groupcount.get(args.size()>=2?_command.split(" ",2)[1]:"Basic")!=1?"s":""), _channel);
			}else{
				bot.sendMSG("@"+_sender+" you were never a member of '"+(args.size()>=2?_command.split(" ",2)[1]:"Basic")+"'", _channel);
			}
			}catch(NullPointerException e){
				bot.sendMSG("@"+_sender+" the group '"+(args.size()>=2?_command.split(" ",2)[1]:"Basic")+"' does not exist", _channel);
			}
			break;
		case "!orgasm" :
			bot.sendMSG(_sender+" Just had such an extreme orgasm that he is no longer able to speak.", _channel);
			break;
		case "!hugme" :
			boolean hugable=false;
			for(String hugablep : hugables){
				if(_sender.equalsIgnoreCase(hugablep))hugable=true;
			}
			bot.sendMSG((_sender.equals(Config.botOwner.toLowerCase())||hugable?"/me hugs "+_sender+" passionately. BibleThump":_sender+", No. Fuck you. Kappa"), _channel);
			break;
		case "!loveme" :
			bot.sendMSG((_sender.equalsIgnoreCase("laurynsynyshyn")?_sender+"I love you more than I love myself ~ meman":_sender+",You are loved. <3"), _channel);
			break;
		case "!kissme" :
			boolean kissable=false;
			for(String kissablep : kissables){
				if(_sender.equalsIgnoreCase(kissablep))kissable=true;
			}
			bot.sendMSG((_sender.equals(Config.botOwner.toLowerCase())||kissable?"/me kisses "+_sender+" gently. <3 <3":"/me slaps "+_sender+" in the face. EW! DISGUSTING!"), _channel);
			break;
		case "!fuckme" :
			boolean fuckable=false;
			for(String fuckablep : fuckables){
				if(_sender.equalsIgnoreCase(fuckablep))fuckable=true;
			}
			bot.sendMSG((_sender.equals(Config.botOwner.toLowerCase())||fuckable?"@"+_sender+", Where, When. Kappa":"@"+_sender+", DansGame You're a pervert. Fuck off. This D is for someone else."), _channel);
			break;
		case "!rejoin" :
			channel ch = bot.channels.get(_channel);
			bot.Leave(_channel, true, true);
			bot.Join(_channel,true,ch.isMuted);
			break;
		case "!wipefile" :
			if(isMod){
				if(args.size()>=2){
					times.put(args.get(1), 0F);
				}else{
					times.put(_sender, 0F);
				}
				bot.sendMSG((args.size()>=2?args.get(1):_sender)+"'s file has been cleared", _channel);
			}
			break;
		case "!unban" :
			if(isMod){
				if(args.size()<2){bot.sendMSG("Usage : !unban <user>", _channel);break;}
				bot.sendMSG("/unban "+(args.size()>=2?args.get(1):""), _channel);
				bot.sendMSG(args.get(1)+" Has been unbanned by "+_sender, _channel);
			}
			break;
		case "!s" :
			if(isOwner&&args.size()>=2){
				if(_command.split(" ")[1].startsWith("#")){
					if(bot.channels.containsKey(_command.split(" ")[1].replace("#", ""))){
						if(bot.channels.get(_command.split(" ")[1].replace("#", "")).isMuted){
							bot.sendMSG(_sender+", I am muted in that channel",  _channel);
						}else{
							bot.sendMSG(_command.split(" ",3)[2], _command.split(" ")[1].replace("#", ""));
						}
					}else{
						bot.sendMSG(_command.split(" ",2)[1], _channel);
					}
				}else{
					bot.sendMSG(_command.split(" ",2)[1], _channel);
				}
			}
			break;
		case "!canhug" :
			if(isOwner&&args.size()==2){
				if(hugables.contains(args.get(1)))hugables.remove(args.get(1)); else hugables.add(args.get(1));
				bot.sendMSG("@"+_sender+" user \""+args.get(1)+"\" has been "+(hugables.contains(args.get(1))?"Added":"Removed"), _channel);
			}
			break;
		case "!cankiss" :
			if(isOwner&&args.size()==2){
				if(kissables.contains(args.get(1)))kissables.remove(args.get(1)); else kissables.add(args.get(1));
				bot.sendMSG("@"+_sender+" user \""+args.get(1)+"\" has been "+(kissables.contains(args.get(1))?"Added":"Removed"), _channel);
			}
			break;
		case "!canfuck" :
			if(isOwner&&args.size()==2){
				if(fuckables.contains(args.get(1)))fuckables.remove(args.get(1)); else fuckables.add(args.get(1));
				bot.sendMSG("@"+_sender+" user \""+args.get(1)+"\" has been "+(fuckables.contains(args.get(1))?"Added":"Removed"), _channel);
			}
			break;
		case "!copycat" :
			if(isOwner&&args.size()==2){
				if(copyThese.contains(args.get(1)))copyThese.remove(args.get(1)); else copyThese.add(args.get(1));
				bot.sendMSG("@"+_sender+" user \""+args.get(1)+"\" has been "+(copyThese.contains(args.get(1))?"Added":"Removed"), _channel);
			}
			break;
		case "!fiteme":
			bot.sendMSG(_sender+","+(isOwner?" I cant fight my owner m8":"Ready to fite bich. Kappa"), _channel);
			break;
		case "!bless":
			bot.sendMSG(_sender+" Has Blessed "+(args.size()>=2?args.get(1):"Everyone"), _channel);
			break;
		case "!mmute":
			if(isCHOwner||isOwner){
				if(args.size()>=2){			
					if(args.get(1)!="true"){
						bot.sendMSG("I am "+(!bot.channels.get(args.get(1)).isMuted?"now":"no longer")+" muted in "+args.get(1)+" chat", _channel);
						bot.channels.get(args.get(1)).isMuted=!bot.channels.get(args.get(1)).isMuted;
					}else{
						bot.channels.get(_channel).isMuted=!bot.channels.get(_channel).isMuted;
					}
				}else{
					bot.channels.get(_channel).isMuted=!bot.channels.get(_channel).isMuted;
				}
			}
			break;
		case "!ismod":
			if(args.size()>=2&&(isMod||isOwner)){
				bot.sendMSG(_sender+", "+(args.get(1))+" is "+(bot.channels.get(_channel).isMod(args.get(1))?"":"not")+" a mod for #"+_channel, _channel);
			}
			break;
		case "!maddcom":
			if(args.size()>=2&&(isMod||isOwner)){
				if(commands.containsKey(args.get(1))){
					bot.sendMSG(_sender+", That command already exists", _channel);
				}else{
					commands.put(args.get(1),_command.split(args.get(1)+" ",2)[1]);
					bot.sendMSG(_sender+", Command "+args.get(1)+" has been added", _channel);
				}
			}
			break;
		case "!mpermcom":
			if(args.size()>=2&&(isMod||isOwner)){
				if(commands.containsKey(args.get(1).toLowerCase())){
					bot.sendMSG(_sender+", Command Permission changed to +", _channel);
				}else{
					commands.put(args.get(1).toLowerCase(),_command.split(args.get(1).toLowerCase()+" ",2)[1]);
					bot.sendMSG(_sender+", That Command does not exist", _channel);
				}
			}
			break;
		case "!mdelcom":
			if(args.size()>=2&&(isMod||isOwner)){
				if(commands.containsKey(args.get(1).toLowerCase())){
					commands.remove(args.get(1).toLowerCase());
					bot.sendMSG(_sender+", Command !"+args.get(1).replace("!", "").toLowerCase()+" has been removed", _channel);
				}else{
					bot.sendMSG(_sender+", That command does not exist", _channel);
				}
			}
			break;
		default:
			if(commands.containsKey(args.get(0).toLowerCase())){
			//	if(commandPerms.containsKey()){
					bot.sendMSG(commands.get(args.get(0).toLowerCase()).replace("%touser%",(args.size()>=2?args.get(1):_sender)).replace("%user%", _sender).replace("%broadcaster%", _channel), _channel);
			//	}
			}
		}
	}
	private static void doMath(final String _sender,final String _math,final String _channel) {
		new RThread(){
			public void run(){
				try{
					bot.sendMSG(_sender+" : "+j.evaluate(j.parse(_math)), _channel);
				}catch(Exception e){}
			}
		}.rename("Math-"+_sender).start();
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
		bot.sendMSG(_sender+", "+Message, _channel);
		bot.sendMSG("/timeout "+_sender+" "+(Math.round(Math.pow(times.get(_sender),2))), _channel);
		new RThread(){
			public void run(){
				try {this.sleep(2000);} catch (InterruptedException e) {}
				bot.sendMSG("/timeout "+_sender+" 1", _channel);
			}
		}.start();
	}
	public static void processChannelJoin(channel _Channel){
		logChannel(_Channel);
	}
	public static void processChat(final String sender, final String command, final String curchannel, final boolean _isMod,final boolean _isJTV) throws Exception {
		new RThread(){
			public void run(){
				try {
					if(!_isJTV)profanityCheck(sender,command,curchannel,_isMod);
					if(!_isJTV)command(sender.toLowerCase(),command,curchannel,_isMod);
				} catch (Exception e) {}
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
	private static void logChat(final String _sender, final String _text, final String _curchannel) {
			new RThread(){
				public void run(){
					try {
						String query = escapeHTML("type=chat&msgText="+_text+"&msgChannel="+_curchannel+"&msgSender="+_sender);
						sendPOST("Http://192.168.5.55/api/irclog.php",query);
					} catch (Exception e) {e.printStackTrace();}
				}
			}.rename("Logging:"+_sender+"-"+_curchannel+"-"+_text.substring(0, 3)).start();
	}
	private static void logJoinLeave(final String _user,final String _channel,final boolean _joined){
		new RThread(){
			public void run(){
				try {
					String query = escapeHTML("type=chat&msgSender="+_user+"&msgChannel="+_channel+"&msgText="+(_joined?"|Joined|":"|Left|"));
					sendPOST("Http://192.168.5.55/api/irclog.php",query);
				} catch (Exception e) {}
			}
		}.rename("Logging:"+_user+"-"+_channel+"-"+(_joined?"Join":"Leave")).start();
	}
	private static void logChannel(final channel _Channel){
		new RThread(){
			public void run(){
				try {
					String query = escapeHTML("type=channel&chN="+_Channel.channelName);
				//	sendPOST("Http://192.168.5.55/api/irclog.php",query);
				} catch (Exception e) {}
			}
		}.rename("LogChannel:"+_Channel.channelName).start();
	}
}

