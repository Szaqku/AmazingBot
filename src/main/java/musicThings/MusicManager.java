package musicThings;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

public class MusicManager {

	private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
	private final Map<String, MusicPlayer> players = new HashMap<>();
	
	public MusicManager(){
		AudioSourceManagers.registerRemoteSources(manager);
		AudioSourceManagers.registerLocalSource(manager);
	}
	
	public synchronized MusicPlayer getPlayer(Guild guild){
		if(!players.containsKey(guild.getId())) 
			players.put(guild.getId(), new MusicPlayer(manager.createPlayer(), guild));
		return players.get(guild.getId());
	}
	public void loadTrackFromYT(final TextChannel channel, final String source){
		MusicPlayer player = getPlayer(channel.getGuild());
		
		channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());
		manager.loadItem("ytsearch: "+source, new FunctionalResultHandler(null, playlist -> {
			AudioTrack track = playlist.getTracks().get(0);
			
			sendInfoAboutLoadedTrack(track.getInfo(),channel);
			
			player.playTrack(track);
		}, null, null));
		
	}
	
	public void loadTrack(final TextChannel channel, final String source){
		MusicPlayer player = getPlayer(channel.getGuild());
		
		channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());
		
		manager.loadItemOrdered(player, source, new AudioLoadResultHandler(){
			
			@Override
			public void trackLoaded(AudioTrack track) {
				sendInfoAboutLoadedTrack(track.getInfo(),channel);
				player.playTrack(track);
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle(":white_check_mark: Pomyœlnie dodano listê odtwarzania.");
				AudioTrack track;
				String content = new String("");
				for(int i = 0; i < playlist.getTracks().size() && i < 5; i++){
					track = playlist.getTracks().get(i);
					content += buildLoadedTrackInfo(track.getInfo().length,track.getInfo().title);
				}
				eb.appendDescription("Za³adowane do "+player.getGuild().getJDA().getSelfUser().getAsMention()+"\n\n");
				eb.appendDescription(":notes: **"+playlist.getName()+"**\n\n"+content);
				
				for(int i = 0; i < playlist.getTracks().size(); i++){
					player.playTrack(playlist.getTracks().get(i)); 
				}
				eb.appendDescription("\noraz **"+(playlist.getTracks().size()-5)+"** wiêcej.\n_");
				
				eb.setFooter("Wpisz "+Data.PREFIX+"queue aby wyœwietliæ ca³¹ listê odtwarzania.", "https://www.cmu.edu/about/images/icon-i.png");
				
				eb.setColor(new Color(100,170,70));
				channel.sendMessage(eb.build()).queue();
			}
			
			
			@Override
			public void noMatches() {
				channel.sendMessage("Nie znaleziono. " + source + "").queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Niepowodzenie. (Powód: " + exception.getMessage()+")").queue();
			}
		});
	}
	private String buildLoadedTrackInfo(long duration,String title){
		duration = duration/1000;
		return ":musical_note: `"+(int)duration/60+":"+( (duration%60 >= 10) ? duration%60 : "0"+duration%60 )+"` | "+title+"\n";
	};

	private void sendInfoAboutLoadedTrack(AudioTrackInfo trackInfo,TextChannel channel){
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":white_check_mark: Utwór dodany pomyœlnie.");
		eb.appendDescription("Za³adowane do "+channel.getGuild().getJDA().getSelfUser().getAsMention()+"\n\n"+buildLoadedTrackInfo(trackInfo.length,trackInfo.title)+"");
		eb.setFooter("Wpisz "+Data.PREFIX+"queue aby wyœwietliæ ca³¹ listê odtwarzania.","https://www.cmu.edu/about/images/icon-i.png");
		eb.setColor(new Color(100,170,70));
		
		channel.sendMessage(eb.build()).queue();
	}
	
}
