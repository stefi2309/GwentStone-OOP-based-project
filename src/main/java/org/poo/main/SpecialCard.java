package org.poo.main;

import org.poo.fileio.CardInput;

public class SpecialCard extends Card {

	public SpecialCard(final CardInput card) {
		super(card);
	}

	/**
	 *
	 * @param enemyCard The enemy minion card targeted by this ability
	 */
	public void ability(final MinionCard enemyCard) { }
}
