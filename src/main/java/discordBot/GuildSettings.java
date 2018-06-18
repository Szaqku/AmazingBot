package discordBot;

public class GuildSettings {
	
	public static int numberOfGuilds = 0;
	
	
	private String guildId;
	private String prefix;
	
	public GuildSettings(String guild,String prefix){
		this.setPrefix(prefix);
		this.guildId = guild;
		numberOfGuilds++;
	}
	

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public String toString(){
		return this.guildId+";"+this.prefix;
	}


}
