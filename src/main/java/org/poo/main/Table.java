package org.poo.main;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
public class Table {
	private ArrayList<ArrayList<Card>> table = new ArrayList<>();

	@Getter @Setter
	protected Deck player1Deck;
	@Getter @Setter
	protected Deck player2Deck;
	@Getter @Setter
	ArrayList<Card> player1Hand = new ArrayList<>();
	@Getter @Setter
	ArrayList<Card> player2Hand = new ArrayList<>();
	@Getter @Setter
	HeroCard hero1;
	@Getter @Setter
	HeroCard hero2;

	public static final int FRONT_ROW_PLAYER1 = 2;
	public static final int BACK_ROW_PLAYER1 = 3;
	public static final int FRONT_ROW_PLAYER2 = 1;
	public static final int BACK_ROW_PLAYER2 = 0;
	public static final int MAX_CARDS_PER_ROW = 5;

	public Table() {
		for (int i = 0; i < 4; i++) {
			table.add(new ArrayList<>(5));
		}
	}
	public Card getCard(int row, int col) {
		return table.get(row).get(col);
	}

	public void addCard(int playerId, Card card)  {
		if (playerId == 1) {
			if ("The Ripper".equals(card.getCard().getName()) || "Miraj".equals(card.getCard().getName()) ||
					"Goliath".equals(card.getCard().getName()) || "Warden".equals(card.getCard().getName())) {
				table.get(2).add(card);  // Front row for player 1
			} else if ("The Cursed One".equals(card.getCard().getName()) || "Disciple".equals(card.getCard().getName())
					|| "Sentinel".equals(card.getCard().getName()) || "Berserker".equals(card.getCard().getName())) {
				table.get(3).add(card);  // Back row for player 1
			}
		} else if (playerId == 2) {
			if ("The Ripper".equals(card.getCard().getName()) || "Miraj".equals(card.getCard().getName())||
					"Goliath".equals(card.getCard().getName()) || "Warden".equals(card.getCard().getName())) {
				table.get(1).add(card);  // Front row for player 2
			} else if ("The Cursed One".equals(card.getCard().getName()) || "Disciple".equals(card.getCard().getName())
					|| "Sentinel".equals(card.getCard().getName()) || "Berserker".equals(card.getCard().getName())) {
				table.get(0).add(card);  // Back row for player 2
			}
		}
	}

	void addCardToHand(int playerId){
		if(playerId == 1 && !player1Deck.isEmpty()){
			//System.out.println("carte in mana jucator 1:" + player1Deck.drawCard() + "\n");
			player1Hand.add(player1Deck.drawCard());
		}
		else if(playerId == 2 && !player2Deck.isEmpty()){
			//System.out.println("carte in mana jucator 2:" + player2Deck.drawCard() + "\n");
			player2Hand.add(player2Deck.drawCard());
		}

	}

	public void removeCard(Card card) {
		for (ArrayList<Card> row : table) {
			if (row.contains(card)) {
				row.remove(card);
				break;
			}
		}
	}
	public boolean isRowFull(int row) {
		return table.get(row).size() >= MAX_CARDS_PER_ROW;
	}
}
