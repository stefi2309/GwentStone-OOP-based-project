package org.poo.main;

import org.poo.fileio.CardInput;

public class Miraj extends SpecialCard {
	public Miraj(final CardInput card) {
		super(card);
	}

	/**
	 *
	 * @param myCard
	 * @param enemyCard
	 */
	public void ability(final MinionCard myCard, final MinionCard enemyCard) {
		int myCardLife = myCard.getCard().getHealth();
		myCard.getCard().setHealth(enemyCard.getCard().getHealth());
		enemyCard.getCard().setHealth(myCardLife);
	}
}
