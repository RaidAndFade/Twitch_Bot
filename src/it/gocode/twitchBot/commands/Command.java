package it.gocode.twitchBot.commands;

import it.gocode.twitchBot.commands.channel.raffleCommandHandler;
import it.gocode.twitchBot.commands.custom.customCommand;
import it.gocode.twitchBot.commands.custom.customCommandHandler;
import static it.gocode.twitchBot.commands.custom.togglableCustomCommand.togglableCustomCommandExecutor;
import static it.gocode.twitchBot.commands.custom.togglableCustomCommand.togglableCustomCommandModifier;
import it.gocode.twitchBot.commands.group.joingroupCommand;
import it.gocode.twitchBot.commands.group.leavegroupCommand;
import it.gocode.twitchBot.commands.memory.rememberCommand;
import it.gocode.twitchBot.commands.memory.remindCommand;
import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.*;

public abstract class Command {
	public static Map<String,Command> SpecialCommands = new HashMap<String,Command>();
	
	public static Map<String,Command> commandList = new HashMap<String,Command>();
	public static Map<String, Command> chanCommandList = new HashMap<String,Command>();
	public static Map<String, Command> wispCommandList = new HashMap<String,Command>();
	
	public commandPerms perms = commandPerms.Anyone;
	
	public Command(commandPerms _perms){
		perms = _perms;
	}
	
	public Command(){}
	
	public static void initCommands(){
		addMiscCommand("conv",new convCommand()); //Done
		addMiscCommand("math",new mathCommand()); //Done
		addMiscCommand("beclever",new beCleverCommand(commandPerms.ModAbove)); //Done
		addMiscCommand("mejoin",new joinCommand()); //Done
		addMiscCommand("meleave",new leaveCommand(commandPerms.CHOwnerAbove)); //Done
		addMiscCommand("mcrawl",new mcrawlCommand(commandPerms.OwnerOnly)); //Done
		addMiscCommand("muptime", new chanStatsCommand("%user%, This channel %uptime%.","%user%, %channel% %uptime%.")); //Done
		addMiscCommand("chanstats", new chanStatsCommand("%user%, This channel has %viewers% viewers, and %uptime% %game%.","%user%, %channel% has %viewers% viewers, and %uptime% %game%.")); //Done
		addMiscCommand("relchan",new reloadChanDataCommand(commandPerms.CHOwnerAbove)); //Done
		addMiscCommand("coin",new tossCoinCommand()); //Done
		addMiscCommand("rand",new randomNumCommand()); //Done
		addMiscCommand("kirby",new customCommand("%user%, <(^.^<) <(^.^)> (>^.^)>")); //Done
		addMiscCommand("dance",new customCommand("/me breaks into dance. ^.^")); //Done
		addMiscCommand("remember",new rememberCommand()); //Done
		addMiscCommand("remind",new remindCommand()); //Done
		addMiscCommand("joinGroup",new joingroupCommand()); //Done
		addMiscCommand("leaveGroup",new leavegroupCommand()); //Done
		addMiscCommand("orgasm",new customCommand("%user% just had such an extreme orgasm that he is no longer able to speak. Kreygasm ")); //Done
		addTogglableMiscCommand("hug","/me hugs %user% passionately. BibleThump","%user%, No. Fuck you. Kappa"); //Done
		addTogglableMiscCommand("kiss","/me kisses %user% gently. <3 <3","/me slaps %user% in the face. EW! DISGUSTING!"); //Done
		addTogglableMiscCommand("fuck","@%user%, Where, When. Kappa","@%user%, DansGame You're a pervert. Fuck off. This D is for someone else."); //Done
		addMiscCommand("loveme", new customCommand("%user%, You are loved. <3")); //Done
		addMiscCommand("allstats", new allStatsCommand(commandPerms.OwnerOnly)); //Done
		addMiscCommand("clearallstats", new clearAllStatsCommand(commandPerms.OwnerOnly)); //Done
		addMiscCommand("s", new sayCommand(commandPerms.CHOwnerAbove)); //Done
		addMiscCommand("w", new wisCommand(commandPerms.OwnerOnly)); //Done
		addMiscCommand("fiteme", new customCommand("{[{isOwner?I can't fight my owner m8.:Ready to fite bich, Kappa}]}")); //Done
		addMiscCommand("bless", new customCommand("%user% has blessed %{touser:you all!}%!")); //Done
		addMiscCommand("ismod", new isModCommand(commandPerms.CHOwnerAbove)); //Done
		addMiscCommand("mmute", new mmuteCommand(commandPerms.CHOwnerAbove)); //Done
		
		addMiscCommand("mcom",new customCommandHandler(commandPerms.ModAbove)); //Done

		addChanCommand("raffle",new it.gocode.twitchBot.commands.channel.raffleCommandHandler(commandPerms.CHOwnerAbove)); //Done
		addChanCommand("clearstats", new it.gocode.twitchBot.commands.channel.clearStatscommand(commandPerms.CHOwnerAbove)); //Done
		addChanCommand("stats", new it.gocode.twitchBot.commands.channel.statsCommand()); //Done
		addChanCommand("unban", new it.gocode.twitchBot.commands.channel.unbanCommand(commandPerms.ModAbove));
		addChanCommand("wipefile", new it.gocode.twitchBot.commands.channel.wipeCommand(commandPerms.CHOwnerAbove)); //Done
		addChanCommand("rejoin", new it.gocode.twitchBot.commands.channel.rejoinCommand(commandPerms.CHOwnerAbove)); //Done 
		addChanCommand("slap", new customCommand(commandPerms.ModAbove,"%user% has slapped %{touser:themself}% around with a large trout.")); //Done
		addChanCommand("listmods",new it.gocode.twitchBot.commands.channel.listModsCommand(commandPerms.CHOwnerAbove)); //Done
		
		addWisCommand("raffle", new it.gocode.twitchBot.commands.wis.raffleSetupHandler());
		addWisCommand("stats", new it.gocode.twitchBot.commands.wis.statsCommand()); //Done
		addWisCommand("listmods",new it.gocode.twitchBot.commands.wis.listModsCommand()); //Done
	}
	
	private static void addTogglableMiscCommand(String command, String able, String unable) {
		togglableCustomCommandExecutor tempTCCE = new togglableCustomCommandExecutor(able,unable);
		addMiscCommand(command+"me",tempTCCE);
		addMiscCommand("can"+command,new togglableCustomCommandModifier(commandPerms.OwnerOnly,tempTCCE,command+"able"));
	}

	public abstract void execute(CommandSender _sender, List<String> _args);
	
	public static void addChanCommand(String commandName, Command commandClass){
		chanCommandList.put("!"+commandName,commandClass);
	}
	public static void addMiscCommand(String commandName, Command commandClass){
		commandList.put("!"+commandName, commandClass);
	}
	public static void addWisCommand(String commandName, Command commandClass){
		wispCommandList.put("!"+commandName,commandClass);
	}

	public boolean hasPerms(CommandSender _CS) {
		switch(perms){
			case OwnerOnly:
				return _CS.isOwner;
			case ModAbove:
				return _CS.isMod||_CS.isCHOwner||_CS.isOwner;
			case CHOwnerAbove:
				return _CS.isCHOwner||_CS.isOwner;
			case Anyone:
			default:
				return true;
		}
	}

	public static void addCustomChanCommand(String channelName, String commandName, String commandResponse) {
		System.out.println("!"+channelName+":"+commandName+","+commandResponse);
		SpecialCommands.put(channelName.toLowerCase()+":!"+commandName.toLowerCase(), new customCommand(commandResponse));
		System.out.println("!"+channelName+":"+commandName+","+commandResponse);
	}

	public static void addCustomGlobalCommand(String commandName, String commandResponse) {
		System.out.println("!"+commandName+","+commandResponse);
		SpecialCommands.put("!"+commandName.toLowerCase(), new customCommand(commandResponse));
		System.out.println("!"+commandName+","+commandResponse);
	}
	
	public static void delCustomCommand(String commandName){
		SpecialCommands.remove("!"+commandName);
	}
}
