package it.gocode.twitchBot.commands.custom;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

import java.util.Arrays;
import java.util.List;

import static com.yofungate.misc.misc.*;

public class customCommand extends Command {

	public String response;

	protected customCommand(commandPerms perms) {
		super(perms);
	}
	public customCommand(String _resp) {
		super();
		response = _resp;
	}
	public customCommand(commandPerms perms, String _resp) {
		super(perms);
		response = _resp;
	}

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		_sender.sendMSG(parseVars(response,_sender,_args));
	}
	
	public String parseVars(String _parsable,CommandSender _sender,List<String> _args){
		String p = _parsable;
		String user = _sender.name;
		String touser = _args.size()>=2?_args.get(1):user;
		
		p = p.replace("%user%", user).replace("%touser%", touser);
		
		p = parseConditionals(p,_sender,_args);
		
		return p;
	}
	private String parseConditionals(String p, CommandSender _sender,	List<String> _args) {
		p=" "+p;
		String[] conds = p.split("\\{\\[\\{");
		for(int x=1;x<conds.length;x++){
			String cond = conds[x];
			String statement = cond.split("\\}\\]\\}",2)[0];
			String conditional = statement.split("\\?",2)[0];
			String option1 = statement.split("\\?",2)[1].split("\\:",2)[0];
			String option2 = statement.split("\\?",2)[1].split("\\:",2)[1];
			boolean condTrue = false;
			switch(conditional){
				case "isOwner":condTrue = _sender.isOwner;break;
				case "isCHOwner":condTrue = _sender.isCHOwner;break;
				case "isMod":condTrue = _sender.isMod;break;
			}
			conds[x] = condTrue?option1:option2;
		}
		p = unsplit(conds,"");
		
		conds = p.split("\\%\\{");
		for(int x=1;x<conds.length;x++){
			String cond = conds[x];
			String var = cond.split("\\}\\%",2)[0].split(":",2)[0];
			String elser = cond.split("\\}\\%",2)[0].split(":",2)[1];
			String result = "";
			switch(var){
				case "touser":result=_args.size()>=2?_args.get(1):elser;
			}
			conds[x] = cond.replace(cond, result).replace("\\}\\%", "");
		}
		System.out.println(p);
		p = unsplit(conds,"");
		System.out.println(p);
		return p;
	}

}
