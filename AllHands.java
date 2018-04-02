import java.util.ArrayList;
import java.util.List;

public class AllHands{
	private Deck deck;
	private List<List<String>> allHands = new ArrayList<>();
	
	public AllHands(Deck deck){
		this.deck = deck;
		
		for(int i = 0; i < deck.getSize(); i++){			
			for(int j = i + 1; j < deck.getSize(); j++){
				List<String> singleHand = new ArrayList<>();
				
				singleHand.add(deck.getCard(i));
				singleHand.add(deck.getCard(j));
				
				allHands.add(singleHand);
			}
		}
	}
	
	public List<List<String>> getAllHands(){
		return this.allHands;
	}
}