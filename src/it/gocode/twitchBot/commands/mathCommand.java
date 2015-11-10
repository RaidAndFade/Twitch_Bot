package it.gocode.twitchBot.commands;

import java.util.Arrays;
import java.util.List;

import org.nfunk.jep.JEP;

import com.yofungate.misc.misc.RThread;

public class mathCommand extends Command {
	
	private static JEP j;

	public mathCommand(){
		j = new JEP();j.addStandardConstants();j.addStandardFunctions();j.addComplex();j.setAllowUndeclared(true);j.setAllowAssignment(true);j.setImplicitMul(true);
	}
	
	@Override
	public void execute(CommandSender _sender, List<String> args) {
		if(args.size()>=2){
			String query = "";
			for(int i = 1; i < args.size(); i++){
				query += args.get(i);
			}
			doMath(_sender,query);
		}
	}

	private void doMath(final CommandSender _sender, final String query) {
		new RThread(){
			public void run(){
				try{
					_sender.sendMSG(_sender+" : "+j.evaluate(j.parse(query)));
				}catch(Exception e){}
			}
		}.rename("Math-"+_sender).start();
	}

}
