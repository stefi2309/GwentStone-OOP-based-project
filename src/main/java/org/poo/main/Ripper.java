package org.poo.main;

import org.poo.fileio.CardInput;

public class Ripper extends SpecialCard{

	public Ripper(CardInput card) {
		super(card);
	}

	public void ability(MinionCard enemyCard) {
		int enemyAttack = enemyCard.getCard().getAttackDamage();
		if(enemyAttack < 2) {
			enemyCard.getCard().setAttackDamage(0);
		} else {
		enemyCard.getCard().setAttackDamage(enemyAttack - 2);
		}
	}
}
