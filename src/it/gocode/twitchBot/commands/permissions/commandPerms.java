package it.gocode.twitchBot.commands.permissions;

public enum commandPerms {
	OwnerOnly,
	CHOwnerAbove,
	ModAbove,
	Anyone;
	public String toString(){
		if(this == commandPerms.OwnerOnly){
			return "Owner";
		}
		if(this == commandPerms.CHOwnerAbove){
			return "Broadcaster";
		}
		if(this == commandPerms.ModAbove){
			return "Mod";
		}
		return "Anyone";
	}
}
