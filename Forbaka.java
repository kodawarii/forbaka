import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

public class Forbaka{
	
	public static void main(String[] fucking){
		
		Scanner sc = new Scanner(System.in);
		
		Deck deck = new Deck();
		List<String> flop = new ArrayList<>();
		
		while(sc.hasNext()){
			flop.add(sc.next());
		}
		
			System.out.println("");
			System.out.println("");
		System.out.println("// FLOP:");
		System.out.println(flop);
			System.out.println("");
			System.out.println("");
			
		for(int i = 0; i < flop.size(); i++){
			deck.removeCards(flop.get(i));
		}
		
		AllHands allHandsFlop = new AllHands(deck); // All Pocket 2-card Hands
		
		List<List<String>> everyFuckingCombinations = new ArrayList<>(); // All (Pocket + Flop) 5-card Hands
		
		for(List<String> hand: allHandsFlop.getAllHands()){
			List<String> omaha = new ArrayList<>();
			
			for(String flopcard: flop){
				omaha.add(flopcard);
			}			
			for(String handcard: hand){
				omaha.add(handcard);
			}
			
			everyFuckingCombinations.add(omaha);
		}
		
		Set<List<String>> listOfStraightFlushHands = new HashSet<>();
		Set<List<String>> listOf4KindHands = new HashSet<>();
		Set<List<String>> listOfFullHouseHands = new HashSet<>();
		Set<List<String>> listOfFlushHands = new HashSet<>();
		Set<List<String>> listOfStraightHands = new HashSet<>();
		Set<List<String>> listOf3KindHands = new HashSet<>();
		Set<List<String>> listOf2PairHands = new HashSet<>();
		Set<List<String>> listOf1PairHands = new HashSet<>();
		
		// for one pairs, kickers etc "the specifics", do later on - focus on basics for now
		
		for(List<String> oneHand: everyFuckingCombinations){
			
			boolean straightFlush = false;
			boolean fourKind = false;
			boolean fullHouse = false;
			boolean flush = false;
			boolean straight = false;
			boolean threeKind = false;
			boolean twoPair = false;
			boolean onePair = false; // checker for full house - if(onePair && threeKind)
			
			HashMap<String, Integer> rankFreq = new HashMap<String, Integer>(); // {Card Rank: Frequency of occurrence in Omaha}
			HashMap<String, Integer> suiteFreq = new HashMap<String, Integer>();
			Set<Integer> ranksSet = new HashSet<>();
			List<Integer> ranksSortedList;
			
			/* Getting frequencies cards e.g. AAAA -> A:4	 KKK45 -> K:3, 4:1, 5:1  etc */
			/* Also getting frequencies of Suites e.g. As Ah 5h 4h 3h -> s:1 h:4 */
			for(String s: oneHand){
				String suuji = String.valueOf(s.charAt(0));
				String suite = String.valueOf(s.charAt(1));
				
				if(!rankFreq.containsKey(suuji)){
					rankFreq.put(suuji, 1);
				}
				else{
					rankFreq.put(suuji, rankFreq.get(suuji) + 1);
				}
				
				if(!suiteFreq.containsKey(suite)){
					suiteFreq.put(suite, 1);
				}
				else{
					suiteFreq.put(suite, suiteFreq.get(suite) + 1);
				}
				
				/* Adding Suuji to HashSet to check for Straights*/
				if(suuji.equals("A")){
					ranksSet.add(14); // how do we account for BOTH : A 2 3 4 5 , T J Q K A ?
					ranksSet.add(1); // just add 1?
				}
				else if(suuji.equals("K")){
					ranksSet.add(13);
				}
				else if(suuji.equals("Q")){
					ranksSet.add(12);
				}
				else if(suuji.equals("J")){
					ranksSet.add(11);
				}
				else if(suuji.equals("T")){
					ranksSet.add(10);
				}
				else{
					ranksSet.add(Integer.parseInt(suuji));
				}
			}
			
			/* check for straights */
			ranksSortedList = new ArrayList<>(ranksSet);
			Collections.sort(ranksSortedList);
			
			List<String> ranksSortedListStringList = new ArrayList<>();
			/* Adding that Sorted List to ListOfStraightHands if it makes a Straight*/
			if(ranksSortedList.size() >= 5){

				// focus on only flop for now, later on check for len=7, len=6, len=5 etc
				
				int first = ranksSortedList.get(0);
				String first_string = String.valueOf(first);
				ranksSortedListStringList.add(first_string);
				
				int count = 1;
				for(int i = 1; i < 5; i++){
					int item_i = ranksSortedList.get(i);
					ranksSortedListStringList.add(String.valueOf(item_i));
					if(item_i != ++first){
						break;
					}
					
					count++;
				}
				
				if(count >= 5){
					listOfStraightHands.add(ranksSortedListStringList);
					straight = true;
				}
			}
			
			/* check for Quads, Trips, 1-Pair */
			int twoPairCounter = 0;
			for(Map.Entry<String, Integer> entry : rankFreq.entrySet()) {
				String key = entry.getKey();
				Integer value = entry.getValue();
				
				if(value == 4){
					listOf4KindHands.add(oneHand);
					fourKind = true;
				}
				
				if(value == 3){
					List<String> temporary = new ArrayList<>();
					temporary.add(key);
					temporary.add(key);
					temporary.add(key);
					listOf3KindHands.add(temporary);
					threeKind = true;
				}
				
				if(value == 2){
					List<String> temporary = new ArrayList<>();
					temporary.add(key);
					temporary.add(key);
					listOf1PairHands.add(temporary);
					onePair = true;
					
					/* Handle 2-pair (see sheet, 'x-files') */
					twoPairCounter++;
					if(twoPairCounter > 1){
						List<String> temporary2 = new ArrayList<>();
						for(String s: oneHand){
							temporary2.add(String.valueOf(s.charAt(0)));
						}
						listOf2PairHands.add(temporary2);
					}
				}
				
				/* Check for Full House */
				if(onePair && threeKind){
					List<String> temporary3 = new ArrayList<>();
					for(String s: oneHand){
						temporary3.add(String.valueOf(s.charAt(0)));
					}
					listOfFullHouseHands.add(temporary3);
					fullHouse = true;
				}
			}			
			
			/* check for flush */
			for(Map.Entry<String, Integer> entry: suiteFreq.entrySet()){
				String key = entry.getKey();
				Integer value = entry.getValue();
				
				if(value == 5){
					listOfFlushHands.add(oneHand);
					flush = true;
				}
			}
			
			/* check for Straight Flush */
			if(flush && straight){
				straightFlush = true;
				listOfStraightFlushHands.add(oneHand);
			}
		}
		
		// debugging section
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Straight Flush Hands: ");
		System.out.println("");
		for(List<String> hand : listOfStraightFlushHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Four of Kind Hands: ");
		System.out.println("");
		for(List<String> hand : listOf4KindHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Full House Hands: ");
		System.out.println("");
		for(List<String> hand : listOfFullHouseHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Flush Hands: ");
		System.out.println("");
		for(List<String> hand : listOfFlushHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Straight Hands: ");
		System.out.println("");
		for(List<String> hand : listOfStraightHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Three of Kind Hands: ");
		System.out.println("");
		for(List<String> hand : listOf3KindHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("Two-Pair Hands: ");
		System.out.println("");
		for(List<String> hand : listOf2PairHands){
			System.out.println(hand);
		}
		
		System.out.println("");
		System.out.println("------------------------------------------------------------");
		System.out.println("");
		System.out.println("One-Pair Hands: ");
		System.out.println("");
		for(List<String> hand : listOf1PairHands){
			System.out.println(hand);
		}
	}
}