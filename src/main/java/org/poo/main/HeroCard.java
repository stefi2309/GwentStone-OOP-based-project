package org.poo.main;

import lombok.Getter;
import org.poo.fileio.CardInput;

@Getter
public class HeroCard extends Card {
	private final int healthHero = 30;
	public HeroCard(final CardInput card) {
		super(card);
		card.setHealth(healthHero);
	}

}
