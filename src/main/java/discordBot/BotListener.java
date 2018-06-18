package discordBot;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import commands.Command;
import commands.Tag;
import esl.EslFindLadderCommand;
import funCommands.KostkaCommand;
import funCommands.OrzelCommand;
import musicCommands.ClearCommand;
import musicCommands.PauseCommand;
// import musicCommands.PapajCommand;
import musicCommands.PlayCommand;
import musicCommands.PreviousCommand;
import musicCommands.QueueCommand;
import musicCommands.SkipCommand;
import musicCommands.VolumeCommand;
import musicCommands.WhatCommand;
import musicThings.MusicCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import utilityCommands.AnnouncementCommand;
import utilityCommands.ChangePrefix;
import utilityCommands.CleanCommand;
import utilityCommands.ColorCommand;
import utilityCommands.DisconnectCommand;
import utilityCommands.KillCommand;
import utilityCommands.PingCommand;
import utilityCommands.VersionCommand;

public class BotListener extends ListenerAdapter{
	
	public HashMap<String, Command> commands = new HashMap<String , Command>();
	public static HashMap<Long, GuildSettings> guilds = new HashMap<>();
	static{
		Path guildsTxt = Paths.get("guilds.txt");
		try {
			if(!Files.exists(guildsTxt, LinkOption.NOFOLLOW_LINKS))
				Files.createFile(guildsTxt);
			List<String> lines = Files.readAllLines(guildsTxt);
			String[] settings;
			for(String line : lines){
				settings = line.split(";");
				guilds.put(Long.valueOf(settings[0]), new GuildSettings(settings[0],settings[1]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public BotListener(){
		
		commands.put("ping", new PingCommand());
		commands.put("clean", new CleanCommand());
		commands.put("kill", new KillCommand());
		commands.put("version", new VersionCommand());
		commands.put("kostka", new KostkaCommand());		
		commands.put("moneta", new OrzelCommand());
		commands.put("disconnect", new DisconnectCommand());
		commands.put("skip", new SkipCommand());
		commands.put("clear", new ClearCommand());
		commands.put("volume", new VolumeCommand());
		commands.put("what", new WhatCommand());
		commands.put("play", new PlayCommand());
		commands.put("color", new ColorCommand());
		commands.put("announce", new AnnouncementCommand());
		commands.put("queue", new QueueCommand());
		commands.put("pause", new PauseCommand());
		commands.put("esl", new EslFindLadderCommand());
		commands.put("dc", commands.get("disconnect"));
		commands.put("previous", new PreviousCommand());
		commands.put("prefix", new ChangePrefix());
		
	}

	/*@Override
	public void onResume(ResumedEvent event) {
		if(event.getJDA().getGuilds().size() <= guilds.size())
			return;
		
		long guildId;
		
		System.out.println("onResume");
		
		for(Guild g : event.getJDA().getGuilds()){
			guildId = g.getIdLong();
			if(!guilds.containsKey(guildId))
				guilds.put(guildId,new GuildSettings(String.valueOf(guildId),"!"));
		}
	}*/
	
	@Override 
	public void onGuildJoin(GuildJoinEvent event){
		System.out.println("Dodano guild do bota.");
		guilds.putIfAbsent(event.getGuild().getIdLong(), new GuildSettings(event.getGuild().getId(),"!"));
	}
	
	@Override
	public void onShutdown(ShutdownEvent event){
		String guildsTxt = "guilds.txt";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(guildsTxt));
			for(GuildSettings gs : guilds.values()){
				bw.write(gs.toString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		if(event.getJDA().getSelfUser().equals(event.getMessage().getAuthor())) return;
		String[] receivedMessage = event.getMessage().getRawContent().split(" ");
		long guildId = event.getGuild().getIdLong();
		
		if(!guilds.containsKey(guildId))
			guilds.put(guildId, new GuildSettings(String.valueOf(guildId),"!"));
		
		String prefix = guilds.get(guildId).getPrefix();
		TextChannel konsola = event.getGuild().getTextChannelsByName("konsola", true).get(0);
		
		if(!receivedMessage[0].startsWith(prefix)) 
			return;
		
		if(!konsola.equals(event.getTextChannel())){
			event.getMessage().delete().queue();	
			event.getTextChannel().sendMessage("Jak coœ chcesz to przyjdz... a nie wo³asz mnie z drugiego koñca domu. \n"+konsola.getAsMention()).complete().delete().completeAfter(5, TimeUnit.SECONDS); 
			return;
		}
		
		String args = new String("");
		for(int i = 1; i < receivedMessage.length; i++){
			args+= receivedMessage[i]+",";
		}
		if(event.getMessage().getRawContent().equals(prefix) || event.getMessage().getRawContent().equals(prefix+"help") ){
			helpCommand(commands,event);
			
		}else if(commands.containsKey(receivedMessage[0].substring(prefix.length()))){
			commands.get(receivedMessage[0].substring(prefix.length())).action(event, args.split(","));
			
		}else if(receivedMessage[0].substring(prefix.length()).equalsIgnoreCase("help") && receivedMessage.length == 2 && commands.containsKey(receivedMessage[1])){
			commands.get(receivedMessage[1]).help(event);
			
		}else{
			event.getChannel().sendMessage(":x: ERROR. U¿yj "+prefix+"help, ¿eby wyswietliæ dostêpne komendy.").queue();
		}
		
	}
	

	@Override
	 public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		leaveIfAloneOnChannel(event);
	}
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		leaveIfAloneOnChannel(event);
	}
	@Override
	public void onException(ExceptionEvent event) {
		if(event.getCause() instanceof PermissionException){
			throw new PermissionException("I don't have permissions.");
		}
	}

	//Functions
	static void helpCommand(HashMap<String,Command> commands,MessageReceivedEvent event){
		String[] content;
		EmbedBuilder eb = new EmbedBuilder();
		for(Tag t:Tag.values()){
			if(t.equals(Tag.ESL))
				continue;
			content = commands.values().stream()
				.filter(command ->{
					return (command.checkTag(t));
				})
				.map(i -> i.name+"\t->\t"+i.getDescription())
				.toArray(String[]::new);
			eb.addField(":ballot_box_with_check: "+t.name(), ":small_orange_diamond: "+String.join("\n:small_orange_diamond: ", content), true);
		}
		eb.setColor(Color.GREEN);
		eb.addField(":slight_frown: Pomocy nie wiem jak tego u¿yc :slight_frown:","Jeœli chcesz sprawdziæ dok³adny opis dzia³ania komendy wpisz: \n"+guilds.get(event.getGuild().getIdLong()).getPrefix()+"help <`komenda`>",false);
		
		event.getTextChannel().sendMessage(eb.build()).queue();
	}
	private void leaveIfAloneOnChannel(GenericGuildVoiceEvent event){
		if(event.getGuild().getAudioManager().isConnected() 
				&& event.getGuild().getAudioManager().getConnectedChannel() != null 
				&& event.getGuild().getAudioManager().getConnectedChannel().getMembers().size() == 1){
			
			event.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage(":cry: Cya.").queue();
			MusicCommand.disconnect(event.getGuild());
		}
	}
	
}
