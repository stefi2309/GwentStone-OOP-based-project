package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
public class Table {
	private ArrayList<ArrayList<Card>> table = new ArrayList<>();

	protected Deck player1Deck;
	protected Deck player2Deck;
	private ArrayList<Card> player1Hand = new ArrayList<>();
	private ArrayList<Card> player2Hand = new ArrayList<>();
	private HeroCard hero1;
	private HeroCard hero2;

	public static final int FRONT_ROW_PLAYER1 = 2;
	public static final int BACK_ROW_PLAYER1 = 3;
	public static final int FRONT_ROW_PLAYER2 = 1;
	public static final int BACK_ROW_PLAYER2 = 0;
	public static final int MAX_CARDS_PER_ROW = 5;
	public static final int MAX_ROWS = 4;

	public Table() {
		for (int i = 0; i < MAX_ROWS; i++) {
			table.add(new ArrayList<>(MAX_CARDS_PER_ROW));
		}
	}

	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public Card getCard(final int row, final int col) {
		if (row < 0 || row >= table.size()) {
			return null;
		}
		ArrayList<Card> rowList = table.get(row);
		if (col < 0 || col >= rowList.size()) {
			return null;
		}
		return table.get(row).get(col);
	}

	/**
	 *
	 * @param playerId
	 * @param card
	 */
	public void addCard(final int playerId, final Card card)  {
		if (playerId == 1) {
			if ("The Ripper".equals(card.getCard().getName())
					|| "Miraj".equals(card.getCard().getName())
					|| "Goliath".equals(card.getCard().getName())
					|| "Warden".equals(card.getCard().getName())) {
				table.get(FRONT_ROW_PLAYER1).add(card);
			} else if ("The Cursed One".equals(card.getCard().getName())
					|| "Disciple".equals(card.getCard().getName())
					|| "Sentinel".equals(card.getCard().getName())
					|| "Berserker".equals(card.getCard().getName())) {
				table.get(BACK_ROW_PLAYER1).add(card);
			}
		} else if (playerId == 2) {
			if ("The Ripper".equals(card.getCard().getName())
					|| "Miraj".equals(card.getCard().getName())
					|| "Goliath".equals(card.getCard().getName())
					|| "Warden".equals(card.getCard().getName())) {
				table.get(FRONT_ROW_PLAYER2).add(card);
			} else if ("The Cursed One".equals(card.getCard().getName())
					|| "Disciple".equals(card.getCard().getName())
					|| "Sentinel".equals(card.getCard().getName())
					|| "Berserker".equals(card.getCard().getName())) {
				table.get(BACK_ROW_PLAYER2).add(card);
			}
		}
	}

	/**
	 * @param playerId
	 */
	void addCardToHand(final int playerId) {
		if (playerId == 1 && !player1Deck.isEmpty()) {
			player1Hand.add(player1Deck.drawCard());
		} else if (playerId == 2 && !player2Deck.isEmpty()) {
			player2Hand.add(player2Deck.drawCard());
		}
	}

	/**
	 *
	 * @param card
	 */
	public void removeCard(final Card card) {
		for (ArrayList<Card> row : table) {
			if (row.contains(card)) {
				row.remove(card);
				break;
			}
		}
	}

	/**
	 *
	 * @param row
	 * @return
	 */
	public boolean isRowFull(final int row) {
		return table.get(row).size() >= MAX_CARDS_PER_ROW;
	}

	/**
	 * Determines the owner of a given card.
	 * @param card the card to check ownership for
	 * @return the player ID (1 or 2) of the card's owner
	 */
	public int getOwner(final Card card) {
		for (int i = 0; i < table.size(); i++) {
			if (table.get(i).contains(card)) {
				if (i == FRONT_ROW_PLAYER1 || i == BACK_ROW_PLAYER1) {
					return 1;
				} else if (i == FRONT_ROW_PLAYER2 || i == BACK_ROW_PLAYER2) {
					return 2;
				}
			}
		}
		return -1;
	}

	/**
	 *
	 * @param row table
	 * @param col column table
	 */
	public void removeCardAt(final int row, final int col) {
		try {
			ArrayList<Card> rowList = table.get(row);
			if (col >= 0 && col < rowList.size()) {
				rowList.remove(col);
			} else {
				System.out.println("Column index out of bounds for removal.");
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Row index out of bounds for removal.");
		}
	}

}
