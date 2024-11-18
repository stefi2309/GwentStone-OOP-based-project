package org.poo.main;

import org.poo.fileio.CardInput;

public class Ripper extends SpecialCard {

	public Ripper(final CardInput card) {
		super(card);
	}

	/**
	 *
	 * @param enemyCard
	 */
	public void ability(final MinionCard enemyCard) {
		int enemyAttack = enemyCard.getCard().getAttackDamage();
		if (enemyAttack < 2) {
			enemyCard.getCard().setAttackDamage(0);
		} else {
		enemyCard.getCard().setAttackDamage(enemyAttack - 2);
		}
	}
}
