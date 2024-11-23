package org.poo.main;

import org.poo.fileio.CardInput;

public class Warden extends MinionCard {
	public Warden(final CardInput card) {
		super(card);
		this.setTank(true);
	}
}
