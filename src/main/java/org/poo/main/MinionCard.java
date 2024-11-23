package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CardInput;

@Getter @Setter
public class MinionCard extends Card {
	public MinionCard(final CardInput card) {
		super(card);
	}
}
