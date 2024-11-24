package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

@Getter @Setter
 public class HeroCard extends Card {
	private final int healthHero = 30;
	public HeroCard(final CardInput card) {
		super(card);
		card.setHealth(healthHero);
	}

	/**
	 * @param row The enemy/own row targeted by this ability
	 */
	public void ability(final ArrayList<Card> row) {
        switch (card.getName()) {
            case "Empress Thorina" -> {
                Card highestHealthCard = null;
                int maxHealth = -1;
                for (Card card : row) {
                    int currentHealth = card.getCard().getHealth();
                    if (currentHealth > maxHealth) {
                        maxHealth = currentHealth;
                        highestHealthCard = card;
                    }
                }

                if (highestHealthCard != null) {
                    row.remove(highestHealthCard);
                }
            }
            case "General Kocioraw" -> row.forEach(card -> card.getCard().
                    setAttackDamage(card.getCard().getAttackDamage() + 1));
            case "King Mudface" -> row.forEach(card -> card.getCard().
                    setHealth(card.getCard().getHealth() + 1));
            case "Lord Royce" -> row.forEach(card -> card.setFrozen(true));
            default -> throw new IllegalStateException("Unhandled card type: "
                + card.getName());
        }
	}

}
