package org.poo.main;

import lombok.Getter;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Represents a deck of cards in a game
 * This deck can include any type of cards derived from the base Card class
 */
@Getter
public class Deck {
	private final ArrayList<Card> cards =  new ArrayList<>();

	public Deck(final ArrayList<CardInput> inputCards) {
		for (CardInput inputCard: inputCards) {
			cards.add(Card.create(inputCard));
		}
	}

	/**
	 * @return The top card from the deck if the deck is not empty, null otherwise
	 */
	public Card drawCard() {
		if (!cards.isEmpty()) {
			return cards.remove(0);
		}
		return null;
	}

	/**
	 *
	 * @return true if there are no cards left in the deck, false otherwise
	 */
	public boolean isEmpty() {
		return cards.isEmpty();
	}


}
