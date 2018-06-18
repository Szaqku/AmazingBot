package musicThings;

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import discordBot.Data;
import net.dv8tion.jda.core.EmbedBuilder;

public class AudioListener extends AudioEventAdapter{

	private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue<>();
	private AudioTrack previous;
	private final MusicPlayer player;
	
	public AudioListener(MusicPlayer player){
		this.player = player;
		previous = null;
	}
	
	public BlockingQueue<AudioTrack> getTracks() {
		return tracks;
	}
	public AudioTrack getPrevious(){
		return previous;
	}
	public int getTrackSize(){
		return tracks.size();
	}
	
	public void nextTrack() {
		if(tracks.isEmpty()){
			//if(player.getGuild().getAudioManager().getConnectedChannel() != null)
			//	player.getGuild().getAudioManager().closeAudioConnection();
			player.getAudioPlayer().stopTrack();
			return;
		}
		AudioTrack aT = tracks.poll();
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":arrows_counterclockwise: Nastêpny utwór.");
		eb.setDescription("Poprzedni utwór zakoñczy³‚ siê.");
		int duration = (int)aT.getInfo().length/1000;
		eb.addField("",":musical_note: `"+(int)duration/60+":"+( (duration%60 >= 10) ? duration%60 : "0"+duration%60 )+"` | "+aT.getInfo().title+"\n"+aT.getInfo().uri,false);
		eb.setFooter("Wpisz "+Data.PREFIX+"queue aby wyœwietliæ ca³¹ listê odtwarzania.","https://www.cmu.edu/about/images/icon-i.png");
		eb.setColor(new Color(97,125,213));
		player.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage(eb.build()).queue();

		player.getAudioPlayer().startTrack(aT, false);
	}
	
	@Override
	public void onTrackEnd(AudioPlayer play, AudioTrack track, AudioTrackEndReason endReason) {
		previous = track;
		if (endReason.mayStartNext && getTrackSize() >= 1){
			nextTrack();
		}
	}

	@Override
	public void onPlayerPause(AudioPlayer p) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":pause_button: Przerwa, idziemy na brejka.");
		AudioTrack aT = player.getAudioPlayer().getPlayingTrack();
		int pos = (int)aT.getPosition()/1000;
		int len = (int)aT.getInfo().length/1000;
		eb.addField("",":musical_note:"+aT.getInfo().title+"\n"+aT.getInfo().uri,false);
		eb.addField("","Czas: `"+(int)pos/60+":"+( (pos%60 >= 10) ? pos%60 : "0"+pos%60 )+"`/`"+ (int)len/60+":"+( (len%60 >= 10) ? len%60 : "0"+len%60 )+"s` ",false);
		player.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage(eb.build()).queue();
	}
	
	@Override
	public void onPlayerResume(AudioPlayer p) {
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle(":arrow_forward: Koniec przerwy..");
		AudioTrack aT = player.getAudioPlayer().getPlayingTrack();
		int pos = (int)aT.getPosition()/1000;
		int len = (int)aT.getInfo().length/1000;
		eb.addField("",":musical_note:"+aT.getInfo().title+"\n"+aT.getInfo().uri,false);
		eb.addField("","Czas: `"+(int)pos/60+":"+( (pos%60 >= 10) ? pos%60 : "0"+pos%60 )+"`/`"+ (int)len/60+":"+( (len%60 >= 10) ? len%60 : "0"+len%60 )+"s` ",false);
		player.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage(eb.build()).queue();
	}
	
	public void clearQueue(){
		tracks.clear();
	}
	
	public void queue(AudioTrack track) {
		if (!player.getAudioPlayer().startTrack(track, true)) 
			tracks.offer(track);
	}
	
	public boolean disconnect(){
		if(!player.getGuild().getAudioManager().isConnected()) 
			return false;
		if(player.getAudioPlayer().getPlayingTrack() != null)
			player.getAudioPlayer().stopTrack();
		if(player.getListener().getTrackSize()>0)
			player.getListener().clearQueue();
		
		if(previous != null)
			previous = null;
		
		player.getGuild().getTextChannelsByName("konsola", true).get(0).sendMessage(sendRandomDisconnectComment()).queue();
		player.getGuild().getAudioManager().closeAudioConnection();
		return true;
	}
	private String sendRandomDisconnectComment(){
		Random rand = new Random();
		return Data.DISCONNECT_COMMENTS[rand.nextInt(Data.DISCONNECT_COMMENTS.length)];
	}
	
}
