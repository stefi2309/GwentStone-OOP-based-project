package org.poo.main;

import org.poo.fileio.CardInput;

public class HeroCard extends Card{
	public HeroCard(CardInput card) {
		super(card);
		card.setHealth(30);
	}

}
