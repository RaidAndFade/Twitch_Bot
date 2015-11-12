package it.gocode.twitchBot.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.yofungate.ircBot.Main;

public class chanStatsCommand extends Command {

	String outputStringSelf = "";
	String outputStringOther = "";
	
	public chanStatsCommand(String _outSelf,String _outOther){
		outputStringSelf = _outSelf;
		outputStringOther = _outOther;
	}
	
	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		String reqChannel = "";
		if(_args.size()==1 && _sender.fromChannel!=null){
			reqChannel = _sender.fromChannel;
		}else if(_args.size()>=2){
			reqChannel = _args.get(1);
		}
		if(reqChannel == ""){
			_sender.sendMSG("You must state a channel.",true);
			return;
		}
		if(!isUp(reqChannel)){
			_sender.sendMSG("That channel is not online right now!");
			return;
		}
		_sender.sendMSG(parseOutput(reqChannel==_sender.fromChannel?outputStringSelf:outputStringOther,reqChannel).replace("%user%", _sender.name));
		
	}
	
	private String parseOutput(String out, String _channel) {
		String _o = out;
		_o = _o.replace("%channel%", _channel).replace("%uptime%", getUptime(_channel)).replace("%viewers%", getViewers(_channel)).replace("%game%", getGame(_channel)).replace("%fps%", getFPS(_channel));
		return _o;
	}

	private CharSequence getFPS(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		String fps = Main.chandata.get(_channel).get("streams").getAsJsonArray().get(0).getAsJsonObject().get("average_fps").getAsString();
		return fps;
	}

	private String getGame(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		String game = Main.chandata.get(_channel).get("streams").getAsJsonArray().get(0).getAsJsonObject().get("game").getAsString();
		return "playing "+game;
	}

	private String getViewers(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		String viewers = Main.chandata.get(_channel).get("streams").getAsJsonArray().get(0).getAsJsonObject().get("viewers").getAsString();
		return viewers;
	}

	public static String getUptime(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		String created = Main.chandata.get(_channel).get("streams").getAsJsonArray().get(0).getAsJsonObject().get("created_at").getAsString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date d1 = null;
		try {
			d1 = format.parse(created);
		} catch (ParseException e) {
			return "is not up right now.";
		}
		final Date currentTime = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date d2 = null;
		try {
			d2 = format.parse(sdf.format(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long diff = d2.getTime() - d1.getTime();
		return "has been up for "+milliToString(diff);
	}
	
	private static boolean isUp(String _channel){
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		return Main.chandata.get(_channel).get("streams").getAsJsonArray().size()>=1;
	}
	
	private static String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}
}
