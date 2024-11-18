package org.poo.main;

import org.poo.fileio.CardInput;

public class Disciple extends SpecialCard{
	public Disciple(CardInput card) {
		super(card);
	}
	public void ability(MinionCard allyCard) {
		int life = allyCard.getCard().getHealth();
		allyCard.getCard().setHealth(life + 2);
	}
}
