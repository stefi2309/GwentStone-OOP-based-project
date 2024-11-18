package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
public class Player {
	private int id;
	private ArrayList<Deck> decks;
	private HeroCard hero;
	@Getter @Setter
	private int mana = 1;
	private final int maxMana = 10;
	private int wonGames = 0;
	private int nrGames = 0;
	public Player(){ }
	public Player(ArrayList<Deck> decks) {
		this.decks = decks;
	}

	public Deck getDeck(int index) {
		return decks.get(index);
	}
	public void refreshMana(int value) {
		if (value < maxMana) {
			mana += value;
		} else {
			mana += maxMana;
		}
	}
	public void useMana(int amount) {
		mana -= amount;
	}

}
