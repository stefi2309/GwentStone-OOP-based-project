package org.poo.main;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CardInput;

@Getter
@Setter
public abstract class Card {
	protected CardInput card;
	protected boolean frozen = false;
	protected boolean attacked = false;
	protected boolean tank = false;

	public Card(final CardInput card) {
		this.card = card;
	}

	/**
	 * Creates a Card instance based on the specified CardInput.
	 *
	 * @param cardInfo The CardInput object containing all necessary card attributes
	 * @return A new instance of a subclass of Card, specific to the type defined in CardInput
	 */
public static Card create(final CardInput cardInfo) {
	return switch (cardInfo.getName()) {
		case "Goliath" -> new Goliath(cardInfo);
		case "Sentinel" -> new Sentinel(cardInfo);
		case "Berserker" -> new Berserker(cardInfo);
		case "Warden" -> new Warden(cardInfo);
		case "The Cursed One" -> new CursedOne(cardInfo);
		case "Miraj" -> new Miraj(cardInfo);
		case "Disciple" -> new Disciple(cardInfo);
		case "Lord Royce" -> new LordRoyce(cardInfo);
		case "Empress Thorina" -> new EmpressThorina(cardInfo);
		case "King Mudface" -> new KingMudface(cardInfo);
		case "General Kocioraw" -> new GeneralKocioraw(cardInfo);
		case "The Ripper" -> new Ripper(cardInfo);
		default -> throw new IllegalStateException("Unhandled card type: "
				+ cardInfo.getName());
	};
}

}
