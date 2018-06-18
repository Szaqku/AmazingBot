package esl;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;


public class EslLadder {
	Ladder ladder;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		EslLadder el;
		try {
			el = new EslLadder(tournament.GO4PALADINS,6);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	public EslLadder(tournament tr, int num) throws IOException{
		ladder = getLadderFromLinkWithNumber(tr.url,num);
	}
	
	public Ladder getLadderFromLinkWithNumber(String url,int num) throws IOException{
		url = url.replace("*", String.valueOf(num));
		Document document = Jsoup.connect(url).get();
		
		Ladder ladder = new Ladder(document);
		
		return ladder;	
	}
	
	public final class Ladder{
		
		private ArrayList<Round> bracket;
		
		public Round getRound(int i) {
			if(i<bracket.size())
				return bracket.get(i);
			else 
				return null;
		}
		
		public ArrayList<Round> getRoundsList(){
			return this.bracket;
		}
		
		public int getRoundsListSize(){
			return bracket.size();
		}
		public Ladder(Document document) {
			bracket = new ArrayList<Round>();
			Elements rounds = document.select(".tourney-brackets .round");
			
			for(Element r : rounds){
				Round round = new Round();
				for(Element g : r.select(".group")){
					Group group = new Group();
					for(Element p : g.select(".pairing")){
						Pair pair = new Pair();
						for(Element player : p.select(".contestant")){
							pair.addTeamToList(player.text());
						}
						group.addPairToList(pair);
					}
					round.addGroupToList(group);
				}
				bracket.add(round);
			}
	}
}	
	public final class Round{
		private ArrayList<Group> groups;
		
		public Round(){
			groups = new ArrayList<Group>();
		}
		
		public void addGroupToList(Group group){
			groups.add(group);
		}
		
		public Group getGroup(int i) {
			return groups.get(i);
		}
		
		public int getGroupsSize() {
			return groups.size();
		}
		public ArrayList<Group> getGroupsList(){
			return this.groups;
		}
		
	}
	public final class Group{
		Pair[] pairs;
		
		public Group(){
			pairs = new Pair[2];
		}
		
		public void addPairToList(Pair pair){
			if(pairs[0] == null)
				pairs[0] = pair;
			else
				pairs[1] = pair;
		}
		public Pair getPair(int i){
			return pairs[i];
		}
		public Pair[] getPairsTable(){
			return this.pairs;
		}
		public Group getGroup(){
			return this;
		}
		
	}
	public final class Pair{
		private String[] teams;
		
		public Pair(){
			teams = new String[2];
		}
		
		public void addTeamToList(String team){
			if(team.length()<= 2)
				team +=" EMPTY SLOT";
			
			if(teams[0] == null)
				teams[0] = team;
			else
				teams[1] = team;
		}
		
		public String getTeam(int i){
			return teams[i];
		}
		
		public String[] getPair(){
			return this.teams;
		}
		
		@Override
		public String toString(){
			if(teams[0] != null && teams[1] != null)
				return teams[0] + " vs "+ teams[1];
			else
				return "";
		}
	}
		
}
