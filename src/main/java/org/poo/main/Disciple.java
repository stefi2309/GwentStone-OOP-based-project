package org.poo.main;

import org.poo.fileio.CardInput;

public class Disciple extends SpecialCard {
	public Disciple(final CardInput card) {
		super(card);
	}

	/**
	 * @param allyCard The minion card whose health will be increased
	 */
	public void ability(final MinionCard allyCard) {
		int life = allyCard.getCard().getHealth();
		allyCard.getCard().setHealth(life + 2);
	}
}
