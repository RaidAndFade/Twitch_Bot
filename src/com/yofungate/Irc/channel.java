package com.yofungate.Irc;

import java.util.ArrayList;
import java.util.Arrays;

public class channel {
	private ArrayList<String> mods = new ArrayList<String>();
	public String channelName;
	public boolean isMuted;
	public boolean silentJoin;
	public channel(String _channel) {
		this.channelName=_channel;
	}	
	public void setMods(String[] _mods){
		this.mods.addAll(Arrays.asList(_mods));
		this.mods.add(channelName);
	}
	public Object[] getMods(){
		return this.mods.toArray();
	}
	public boolean isMod(String _username){
		for(String mod : this.mods){
			if(_username.equalsIgnoreCase(mod)){return true;}
		}
		return false;
	}
	public String listMods() {
		System.out.println(""+Arrays.toString(this.mods.toArray()));
		return Arrays.toString(this.mods.toArray());
	}
	public boolean hasMods(){
		return !mods.isEmpty();
	}
}
