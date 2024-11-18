package org.poo.main;

import org.poo.fileio.CardInput;

public class CursedOne extends SpecialCard{
	public CursedOne(CardInput card) {
		super(card);
	}

	public void ability(MinionCard enemyCard, Table table) {
		int enemyAttack = enemyCard.getCard().getAttackDamage();
		if(enemyAttack == 0) {
			table.removeCard(enemyCard);
		} else {
			int enemyLife = enemyCard.getCard().getHealth();
			enemyCard.getCard().setHealth(enemyAttack);
			enemyCard.getCard().setAttackDamage(enemyLife);
		}


	}
}
