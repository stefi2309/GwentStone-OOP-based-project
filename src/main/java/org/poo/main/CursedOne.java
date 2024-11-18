package org.poo.main;

import org.poo.fileio.CardInput;

public class CursedOne extends SpecialCard {
	public CursedOne(final CardInput card) {
		super(card);
	}

	/**
	 * @param enemyCard The enemy minion card that is targeted by the ability
	 * @param table     The game table on which the card interaction occurs
	 */
	public void ability(final MinionCard enemyCard, final Table table) {
		int enemyAttack = enemyCard.getCard().getAttackDamage();
		if (enemyAttack == 0) {
			table.removeCard(enemyCard);
		} else {
			int enemyLife = enemyCard.getCard().getHealth();
			enemyCard.getCard().setHealth(enemyAttack);
			enemyCard.getCard().setAttackDamage(enemyLife);
		}
	}
}
