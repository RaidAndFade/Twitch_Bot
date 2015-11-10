package it.gocode.twitchBot.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.yofungate.ircBot.Main;

public class muptimeCommand extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		if(_args.size()==1 && _sender.fromChannel!=null){
			_sender.sendMSG(_sender+", This channel has been up for "+getUptime(_sender.fromChannel));
		}else if(_args.size()==2){
			String _tchannel = _args.get(1);
			_sender.sendMSG(_sender+", "+_tchannel+" channel has been up for "+getUptime(_tchannel));	
		}
	}

	private String getUptime(String _channel) {
		if(!Main.chandata.containsKey(_channel)){
			Main.updateChannelData(_channel);
		}
		String created = Main.chandata.get(_channel).get("streams").getAsJsonArray().get(0).getAsJsonObject().get("created_at").getAsString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date d1 = null;
		try {
			d1 = format.parse(created);
		} catch (ParseException e) {
			return "NULL";
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
		return milliToString(diff);
	}
	private String milliToString(long input) {
		long diffsecs = TimeUnit.MILLISECONDS.toSeconds(input)%60;
		long diffmins = TimeUnit.MILLISECONDS.toMinutes(input)%60;
		long diffhour = TimeUnit.MILLISECONDS.toHours(input);
		return (diffhour!=0?diffhour+"h ":"") + (diffmins!=0?diffmins+"m ":"")+(diffsecs!=0?diffsecs+"s ":"");
	}
}
