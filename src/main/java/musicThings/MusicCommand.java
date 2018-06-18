package musicThings;

import java.awt.Color;
import java.util.stream.Collectors;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;

public class MusicCommand {

	private final static MusicManager manager = new MusicManager();
	
	public static void play(Guild guild, TextChannel textChannel, User user, String[] args){
		
		if(guild == null) return;
		
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
			VoiceChannel voiceChannel = guild.getMember(user).getVoiceState().getChannel();
			if(voiceChannel == null){
				textChannel.sendMessage("Gdzie jesteœ ? Do³¹cz do jakiegœ kana³u.").queue();
				return;
			}
			guild.getAudioManager().openAudioConnection(voiceChannel);
		}
		if(args[0] == null || args[0].equals("")){
			textChannel.sendMessage("Ale wpisz tu coœ pajacu...").queue();
			return;
		}
		
		
		if(args.length == 1 && args[0].startsWith("http"))
		{
			StringBuilder editedMessage = new StringBuilder();
			editedMessage.append("<"+args[0]+">");
			textChannel.deleteMessageById(textChannel.getLatestMessageId()).queue();
			textChannel.sendMessage("```"+Data.PREFIX+"play "+editedMessage+"\nDodane przez "+user.getName()+"```").queue();
			manager.loadTrack(textChannel, args[0]);
		}else{
			String source = String.join(" ", args);
			manager.loadTrackFromYT(textChannel, source);
		}
	}
 
	public static void play(Guild guild, TextChannel textChannel, VoiceChannel voiceChannel){
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
			guild.getAudioManager().openAudioConnection(voiceChannel);
		}
		manager.loadTrack(textChannel, "https://www.youtube.com/watch?v=JmCxwraaPRM");
	}
	
	public static void skip(Guild guild, TextChannel textChannel) {
		if(!guild.getAudioManager().isConnected() && !guild.getAudioManager().isAttemptingToConnect()){
			return;
		}
		
		manager.getPlayer(guild).skipTrack();
	}
	
	public static void clear(TextChannel textChannel){
		MusicPlayer player = manager.getPlayer(textChannel.getGuild());
		
		if(player.getListener().getTracks().isEmpty()){
			textChannel.sendMessage("Cicho.. g³ucho.. i pusto...").queue();
			return;
		}
		
		player.getListener().getTracks().clear();
		textChannel.sendMessage("Kolejka zosta³a wyczyszczona.").queue();
	}
	
	public static void volume(Guild guild,TextChannel textChannel,String vol){
		
		if(vol.equals("") || vol == null){
			textChannel.sendMessage("Volume: "+manager.getPlayer(guild).getAudioPlayer().getVolume()+". ").queue();
			return;
		}

		try{
			int volume = Integer.parseInt(vol.trim()); 
			if(volume > 200 || volume < 0) {
				textChannel.sendMessage(":x: Nieprawid³owa wartoœæ.").queue();
				return;
			}
			manager.getPlayer(guild).getAudioPlayer().setVolume(volume);
			textChannel.sendMessage("G³oœnoœæ ustawiona na: `"+volume+"%`.").queue();
			
		}catch(Exception ex){
			textChannel.sendMessage("Nieprawid³owa wartoœæ.").queue();
		}
	}
	
	public static void what(Guild guild, TextChannel textChannel,int i){
		if(manager.getPlayer(guild).getAudioPlayer().getPlayingTrack() == null ){
			textChannel.sendMessage(":x: Nic nie jest aktualnie grane.").queue();
			return;
		}
		if(i > manager.getPlayer(guild).getListener().getTrackSize() || i < 0){
			textChannel.sendMessage(":x: a Wie¿a Eiffla ma 324m. Podaj poprawny numer.").queue();
			return;
		}
		AudioTrack ati;
		if(i == 0){
			ati = manager.getPlayer(guild).getAudioPlayer().getPlayingTrack();
		}else{
			ati = manager.getPlayer(guild).getListener().getTracks().stream().collect(Collectors.toList()).get(i-1);
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GREEN);
		eb.setTitle("Aktualnie grany utwór: \n");
		eb.appendDescription("Tytu³: `"+ (ati.getInfo().title+"` \n"));
		eb.appendDescription("Link: "+ (ati.getInfo().uri+" \n"));
		int len = (int)ati.getInfo().length/1000;
		int pos = (int)ati.getPosition()/1000;
		if(i == 0)
			eb.appendDescription("Czas: `"+(int)pos/60+":"+( (pos%60 >= 10) ? pos%60 : "0"+pos%60 )+"`/`"+ (int)len/60+":"+( (len%60 >= 10) ? len%60 : "0"+len%60 )+"s` ");
		else
			eb.appendDescription("Czas: `"+ (int)len/60+":"+( (len%60 >= 10) ? len%60 : "0"+len%60 )+"s` ");
		
		textChannel.sendMessage(eb.build()).queue();	
	}
	
	public static void disconnect(Guild guild, TextChannel textChannel, User user){
		if(manager.getPlayer(guild).getListener().disconnect()){
			textChannel.sendMessage("Roz³¹czony przez "+user.getAsMention()).queue();
		}
	}
	public static void disconnect(Guild guild){
		manager.getPlayer(guild).getListener().disconnect();
	}
	
	public static void queue(Guild guild){
		AudioTrack[] list =  manager.getPlayer(guild).getListener().getTracks().stream().toArray(AudioTrack[]::new);
		if(list.length == 0){
			guild.getTextChannelsByName("konsola", true).get(0).sendMessage("Kolejka jest pusta.").queue();	
			return;
		}
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":clipboard: Lista utworów w kolejce.");
		eb.appendDescription("W kolejce jest **"+list.length+"** utworow.\n\n");
		int duration;
		for(AudioTrack track: list){
			duration = (int)track.getInfo().length/1000;
			eb.appendDescription(":musical_note: `"+(int)duration/60+":"+( (duration%60 >= 10) ? duration%60 : "0"+duration%60 )+"` | "+track.getInfo().title+"\n");
			if(eb.getDescriptionBuilder().length()>1900){
				eb.appendDescription("\n.. i **kilka** innych.");
				break;
			}
		}
		eb.setFooter("Aby wyœwietliæ dok³adny opis aktualnie granego utworu, wpisz "+Data.PREFIX+"what.","https://www.cmu.edu/about/images/icon-i.png");
		guild.getTextChannelsByName("konsola", true).get(0).sendMessage(eb.build()).queue();
	}
	public static void pause(Guild guild){
		AudioPlayer aP = manager.getPlayer(guild).getAudioPlayer();
		if(aP.getPlayingTrack() != null){
			if(aP.isPaused())
				aP.setPaused(false);
			else
				aP.setPaused(true);
		}
	}
	public static void previous(Guild guild){
		MusicPlayer mP  = manager.getPlayer(guild);
		if(mP.getListener().getPrevious() != null){
			manager.loadTrack(guild.getTextChannelsByName("konsola", true).get(0),mP.getListener().getPrevious().getInfo().uri);
		}
	}
}
