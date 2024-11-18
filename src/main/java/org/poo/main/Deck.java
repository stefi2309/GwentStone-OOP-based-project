package org.poo.main;

import lombok.Getter;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

@Getter
public class Deck {
	private final ArrayList<Card> cards =  new ArrayList<>();

	public Deck(ArrayList<CardInput> inputCards) {
		for(CardInput inputCard: inputCards){
			cards.add(Card.create(inputCard) );
		}
	}

	public void addCard(Card card) {
		cards.add(card);
	}


	public Card drawCard() {
		if (!cards.isEmpty()) {
			return cards.remove(0);
		}
		return null;
	}

	public int getDeckSize() {
		return cards.size();
	}
	public boolean isEmpty(){
		return cards.isEmpty();
	}


}
