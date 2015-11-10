package it.gocode.twitchBot.commands.custom;

import java.util.ArrayList;
import java.util.List;

import it.gocode.twitchBot.commands.Command;
import it.gocode.twitchBot.commands.CommandSender;
import it.gocode.twitchBot.commands.permissions.commandPerms;

public abstract class togglableCustomCommand extends customCommand{
	public List<String> ables = new ArrayList<String>();
	
	public togglableCustomCommand(commandPerms perms) {
		super(perms);
	}
	@Override
	public abstract void execute(CommandSender _sender, List<String> _args);
	public static class togglableCustomCommandExecutor extends togglableCustomCommand{
		private String able,unable;
		
		public togglableCustomCommandExecutor(String _able, String _unable) {
			super(commandPerms.Anyone);
			able = _able;unable = _unable;
		}

		@Override
		public void execute(CommandSender _sender, List<String> _args) {
			if(ables.contains(_sender.name)){
				_sender.sendMSG(parseVars(able,_sender,_args));
			}else{
				_sender.sendMSG(parseVars(unable,_sender,_args));
			}
		}
	}
	public static class togglableCustomCommandModifier extends togglableCustomCommand{
		togglableCustomCommandExecutor Slave;
		String statement;
		
		public togglableCustomCommandModifier(commandPerms perms, togglableCustomCommandExecutor _slave, String _statement) {
			super(perms);
			Slave = _slave;
			statement = _statement;
		}
		@Override
		public void execute(CommandSender _sender, List<String> _args) {
			if(_args.size()>1){
				if(Slave.ables.contains(_args.get(1))){
					Slave.ables.remove(_args.get(1));
				}else{
					Slave.ables.add(_args.get(1));
				}
				_sender.sendMSG(_args.get(1)+" is"+(!Slave.ables.contains(_args.get(1))?" no longer":" now")+" "+statement);
			}else{
				
			}
		}
		
	}
}
