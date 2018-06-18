package esl;

public enum tournament{
	GO4PALADINS("https://play.eslgaming.com/paladins/global/paladins/major/go4paladins-europe/cup-*/rankings/"),
	COMUNITYCUP5V5("https://play.eslgaming.com/paladins/global/paladins/open/5on5-community-cup-europe-*/rankings/");
	
	String url;
	tournament(String url){
		this.url = url;
	}
};
