package it.gocode.twitchBot.commands;

import java.util.List;
import static com.yofungate.misc.misc.*;

public class tossCoinCommand extends Command {

	@Override
	public void execute(CommandSender _sender, List<String> _args) {
		_sender.sendMSG(_sender+", you tossed a coin, It landed on "+(randInt(0, 100)>50?"Heads":"Tails"));
	}
}
