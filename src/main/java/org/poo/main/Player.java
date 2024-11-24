package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
public class Player {
	private ArrayList<Deck> decks;
	@Getter @Setter
	private int mana = 1;
	private final int maxMana = 10;
	private int wonGames = 0;
	private int nrGames = 0;
	public Player() { }
	public Player(final ArrayList<Deck> decks) {
		this.decks = decks;
	}

	/**
	 *
	 * @param index
	 * @return
	 */
	public Deck getDeck(final int index) {
		return decks.get(index);
	}

	/**
	 *
	 * @param value
	 */
	public void refreshMana(final int value) {
		if (value < maxMana) {
			mana += value;
		} else {
			mana += maxMana;
		}
	}

	/**
	 *
	 * @param amount
	 */
	public void useMana(final int amount) {
		mana -= amount;
	}

}
