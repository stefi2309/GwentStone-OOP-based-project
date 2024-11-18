package org.poo.main;

import org.poo.fileio.CardInput;

public class Miraj extends SpecialCard{
	public Miraj(CardInput card) {
		super(card);
	}

	public void ability(MinionCard myCard, MinionCard enemyCard) {
		int myCardLife = myCard.getCard().getHealth();
		myCard.getCard().setHealth(enemyCard.getCard().getHealth());
		enemyCard.getCard().setHealth(myCardLife);
	}
}
