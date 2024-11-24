package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import org.poo.fileio.Coordinates;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter
@Setter
public class Game {
	private GameInput game;
	private Table table;
	private Deck playerOneDeck;
	private Deck playerTwoDeck;
	private ArrayNode output;
	private Input input;
	private int nrGames;
	private int playerTurn;
	private static Game instance;
	private Player player1 = new Player();
	private Player player2 = new Player();
	private int currentTurn;
	private int endTurnCounter = 0;
	private int roundCounter = 1;
	private boolean gameOver = false;
	private int player1Wins = 0;
	private int player2Wins = 0;

	public Game(final Deck playerOneDeck, final Deck playerTwoDeck) {
		this.playerOneDeck = playerOneDeck;
		this.playerTwoDeck = playerTwoDeck;
	}

	public Game() { }

	/**
	 * Gets the single instance of the Game class following the singleton pattern.
	 * @return Single instance of Game
	 * */
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}

	/**
	 * Starts the game using the provided input data and prepares the output data structure
	 * @param inputData Parsed input data from the game files
	 * @param outputData Structure to hold the game output as it progresses
	 */
	public void start(final Input inputData, final ArrayNode outputData) {
		input = inputData;
		output = outputData;
		nrGames = input.getGames().size();

		for (int i = 0; i < nrGames; i++) {
			GameInput currentGame = input.getGames().get(i);
			setupGame(currentGame);
			gameOver = false;
			CardInput hero1 = currentGame.getStartGame().getPlayerOneHero();
			CardInput hero2 = currentGame.getStartGame().getPlayerTwoHero();
			setupPlayers(currentGame, hero1, hero2);

			executeGameActions(currentGame);
		}
	}


	private void setupGame(final GameInput currentGame) {
		table = new Table();
		setupDeck(currentGame.getStartGame().getPlayerOneDeckIdx(), true);
		setupDeck(currentGame.getStartGame().getPlayerTwoDeckIdx(), false);

		shuffleDecks(currentGame.getStartGame().getShuffleSeed());

		roundCounter = 1;
		endTurnCounter = 0;
	}

	private void setupDeck(final int deckIndex, final boolean isPlayerOne) {
		ArrayList<CardInput> deckInput = (isPlayerOne ? input.getPlayerOneDecks()
				: input.getPlayerTwoDecks()).getDecks().get(deckIndex);
		Deck deck = new Deck(deckInput);
		if (isPlayerOne) {
			table.setPlayer1Deck(deck);
		} else {
			table.setPlayer2Deck(deck);
		}
	}

	private void shuffleDecks(final int seed) {
		Collections.shuffle(table.getPlayer1Deck().getCards(), new Random(seed));
		Collections.shuffle(table.getPlayer2Deck().getCards(), new Random(seed));
	}

	private void setupPlayers(final GameInput currentGame, final CardInput hero1,
							  final CardInput hero2) {
		currentTurn = currentGame.getStartGame().getStartingPlayer();
		table.setHero1(new HeroCard(hero1));
		table.setHero2(new HeroCard(hero2));

		table.addCardToHand(1);
		table.addCardToHand(2);
		player1.setMana(1);
		player2.setMana(1);
	}

	private void executeGameActions(final GameInput currentGame) {
		for (ActionsInput action : currentGame.getActions()) {
			command(action);
		}
	}

	/**
	 * Processes each action provided in the game input.
	 * @param action The action to be processed.
	 */
	public void command(final ActionsInput action) {
		switch (action.getCommand()) {
			case "getPlayerDeck":
				getPlayerDeck(action);
				break;
			case "getPlayerHero":
				getPlayerHero(action);
				break;
			case "getPlayerTurn":
				getPlayerTurn();
				break;
			case "getCardsInHand":
				getCardsInHand(action);
				break;
			case "endPlayerTurn":
				endPlayerTurn();
				break;
			case "placeCard":
				placeCard(action.getHandIdx());
				break;
			case "getCardsOnTable":
				getCardsOnTable();
				break;
			case "getPlayerMana":
				getPlayerMana(action);
				break;
			case "cardUsesAttack":
				cardUsesAttack(action);
				break;
			case "getCardAtPosition":
				getCardAtPosition(action);
				break;
			case "cardUsesAbility":
				cardUsesAbility(action);
				break;
			case "useAttackHero":
				useAttackHero(action);
				break;
			case "useHeroAbility":
				useHeroAbility(action);
				break;
			case "getFrozenCardsOnTable":
				getFrozenCardsOnTable(action);
				break;
			case "getPlayerOneWins":
				getPlayerOneWins();
				break;
			case "getPlayerTwoWins":
				getPlayerTwoWins();
				break;
			case "getTotalGamesPlayed":
				getTotalGamesPlayed();
				break;
			default:
				break;
		}
	}

	private void getPlayerDeck(final ActionsInput action) {
		int playerIndex = action.getPlayerIdx();
		Deck deckToOutput = (playerIndex == 1) ? table.getPlayer1Deck()
				: table.getPlayer2Deck();

		ObjectNode newNode = output.addObject();
		newNode.put("command", action.getCommand());
		newNode.put("playerIdx", playerIndex);

		ArrayNode deckNode = newNode.putArray("output");
		for (Card card : deckToOutput.getCards()) {
			ObjectNode cardNode = deckNode.addObject();
			cardNode.put("mana", card.getCard().getMana());
			cardNode.put("attackDamage", card.getCard().getAttackDamage());
			cardNode.put("health", card.getCard().getHealth());
			cardNode.put("description", card.getCard().getDescription());
			ArrayNode colorsArray = cardNode.putArray("colors");
			for (String color : card.getCard().getColors()) {
				colorsArray.add(color);
			}
			cardNode.put("name", card.getCard().getName());
		}
	}

	private void getPlayerHero(final ActionsInput action) {
		int playerIndex = action.getPlayerIdx();
		HeroCard heroCard = (playerIndex == 1) ? table.getHero1() : table.getHero2();

		ObjectNode newNode = output.addObject();
		newNode.put("command", "getPlayerHero");
		newNode.put("playerIdx", playerIndex);

		ObjectNode heroNode = newNode.putObject("output");
		heroNode.put("mana", heroCard.getCard().getMana());
		heroNode.put("description", heroCard.getCard().getDescription());
		ArrayNode colorsArray = heroNode.putArray("colors");
		for (String color : heroCard.getCard().getColors()) {
			colorsArray.add(color);
		}
		heroNode.put("name", heroCard.getCard().getName());
		heroNode.put("health", heroCard.getCard().getHealth());

	}
	private void getPlayerTurn() {
		ObjectNode newNode = output.addObject();
		newNode.put("command", "getPlayerTurn");
		newNode.put("output", currentTurn);
	}


	private void getCardsInHand(final ActionsInput action) {
		int playerIndex = action.getPlayerIdx();
		ArrayList<Card> cardsInHand = (playerIndex == 1) ? table.getPlayer1Hand()
				: table.getPlayer2Hand();

		ObjectNode newNode = output.addObject();
		newNode.put("command", "getCardsInHand");
		newNode.put("playerIdx", playerIndex);

		ArrayNode handNode = newNode.putArray("output");
		for (Card card : cardsInHand) {
			ObjectNode cardNode = handNode.addObject();
			cardNode.put("mana", card.getCard().getMana());
			cardNode.put("attackDamage", card.getCard().getAttackDamage());
			cardNode.put("health", card.getCard().getHealth());
			cardNode.put("description", card.getCard().getDescription());
			ArrayNode colorsArray = cardNode.putArray("colors");
			for (String color : card.getCard().getColors()) {
				colorsArray.add(color);
			}
			cardNode.put("name", card.getCard().getName());
		}

	}

	/**
	 * Ends the current player's turn and checks if both players
	 * have ended their turns to potentially start a new round.
	 */
	public void endPlayerTurn() {
		// player turn
		unfreezePlayerCards(currentTurn);
		currentTurn = currentTurn == 1 ? 2 : 1;
		if (bothPlayersEndedTurns()) {
			startNewRound();
			resetCardAttacks();
		}
	}

	private void unfreezePlayerCards(final int playerID) {
		int startRow = playerID == 1 ? Table.FRONT_ROW_PLAYER1 : Table.FRONT_ROW_PLAYER2;
		int endRow = playerID == 1 ? Table.BACK_ROW_PLAYER1 : Table.BACK_ROW_PLAYER2;

		// unfreeze cards in both the front and back rows for the player
		unfreezeCardsInRow(startRow);
		unfreezeCardsInRow(endRow);
	}

	private void unfreezeCardsInRow(final int row) {
		ArrayList<Card> cards = table.getTable().get(row);
		for (Card card : cards) {
			card.setFrozen(false);
		}
	}


	private boolean bothPlayersEndedTurns() {
		endTurnCounter++;
		if (endTurnCounter == 2) {
			endTurnCounter = 0;
			return true;
		}
		return false;
	}

	private void startNewRound() {
		roundCounter++;

		player1.refreshMana(roundCounter);
		player2.refreshMana(roundCounter);

		table.addCardToHand(1);
		table.addCardToHand(2);
	}


	/**
	 * @param handIdx the index of the card in the player's hand that they intend to play
	 */
	public void placeCard(final int handIdx) {
		ArrayList<Card> currentHand = currentTurn == 1 ? table.getPlayer1Hand()
				: table.getPlayer2Hand();
		Player currentPlayer = currentTurn == 1 ? player1 : player2;

		// check mana
		if (handIdx >= currentHand.size() || currentPlayer.getMana()
				< currentHand.get(handIdx).getCard().getMana()) {
			ObjectNode responseNode = output.addObject();
			responseNode.put("command", "placeCard");
			responseNode.put("handIdx", handIdx);
			responseNode.put("error", "Not enough mana to place card on table.");
			return;
		}

		// determine the appropriate row
		Card cardToPlace = currentHand.get(handIdx);
		int row = determineRow(cardToPlace);
		if (table.isRowFull(row)) {
			ObjectNode responseNode = output.addObject();
			responseNode.put("command", "placeCard");
			responseNode.put("handIdx", handIdx);
			responseNode.put("error", "Cannot place card on table since row is full.");
			return;
		}

		// remove the card from hand and place it on the table
		currentHand.remove(handIdx);
		table.addCard(currentTurn, cardToPlace);
		currentPlayer.useMana(cardToPlace.getCard().getMana());
	}

	private int determineRow(final Card card) {
		String cardName = card.getCard().getName();
		if ("The Ripper".equals(cardName) || "Miraj".equals(cardName)
				|| "Goliath".equals(cardName) || "Warden".equals(cardName)) {
			return currentTurn == 1 ? Table.FRONT_ROW_PLAYER1 : Table.FRONT_ROW_PLAYER2;
		} else {
			return currentTurn == 1 ? Table.BACK_ROW_PLAYER1 : Table.BACK_ROW_PLAYER2;
		}
	}

	/**
	 * Retrieves and organizes the cards currently placed on the table
	 */
	public void getCardsOnTable() {
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getCardsOnTable");

		ArrayNode rowsArray = responseNode.putArray("output");

		for (ArrayList<Card> row : table.getTable()) {
			ArrayNode rowArray = rowsArray.addArray();

			for (Card card : row) {
				ObjectNode cardNode = rowArray.addObject();
				cardNode.put("mana", card.getCard().getMana());
				cardNode.put("attackDamage", card.getCard().getAttackDamage());
				cardNode.put("health", card.getCard().getHealth());
				cardNode.put("description", card.getCard().getDescription());
				ArrayNode colorsArray = cardNode.putArray("colors");
				for (String color : card.getCard().getColors()) {
					colorsArray.add(color);
				}
				cardNode.put("name", card.getCard().getName());
			}
		}
	}

	/**
	 * @param action containing details
	 */
	public void getPlayerMana(final ActionsInput action) {
		int playerIndex = action.getPlayerIdx();

		int mana = (playerIndex == 1) ? player1.getMana() : player2.getMana();

		// create the output object node for this command
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getPlayerMana");
		responseNode.put("playerIdx", playerIndex);
		responseNode.put("output", mana);
	}

	/**
	 * @param action containing coordinates of the attacker and the attacked
	 */
	public void cardUsesAttack(final ActionsInput action) {
		Coordinates attackerCoords = action.getCardAttacker();
		Coordinates attackedCoords = action.getCardAttacked();

		Card attacker = table.getCard(attackerCoords.getX(), attackerCoords.getY());
		Card attacked = table.getCard(attackedCoords.getX(), attackedCoords.getY());

		if (attacker == null || attacked == null) {
			appendError("Invalid card coordinates.");
			return;
		}

		if (!areEnemies(attacker, attacked)) {
			String error = "Attacked card does not belong to the enemy.";
			attackOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (attacker.isAttacked()) {
			String error = "Attacker card has already attacked this turn.";
			attackOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (attacker.isFrozen()) {
			String error = "Attacker card is frozen.";
			attackOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (mustTargetTank(attacked)) {
			String error = "Attacked card is not of type 'Tank'.";
			attackOutput(attackerCoords, attackedCoords, error);
			return;
		}

		attackCard(attacker, attacked, attackedCoords);
	}

	private boolean mustTargetTank(final Card attacked) {
		int attackedPlayerId = table.getOwner(attacked);
		ArrayList<Card> frontRow = table.getTable().get(attackedPlayerId == 1
				? Table.FRONT_ROW_PLAYER1 : Table.FRONT_ROW_PLAYER2);

		boolean tankExists = false;
		for (Card card : frontRow) {
			if (card.isTank()) {
				tankExists = true;
				break;
			}
		}

		return tankExists && !attacked.isTank();
	}

	private void attackOutput(final Coordinates attackerCoords,
							  final Coordinates attackedCoords,
							  final String message) {
		ObjectNode attackDetails = output.addObject();
		attackDetails.put("command", "cardUsesAttack");
		attackDetails.putObject("cardAttacker").put("x",
				attackerCoords.getX()).put("y", attackerCoords.getY());
		attackDetails.putObject("cardAttacked").put("x",
				attackedCoords.getX()).put("y", attackedCoords.getY());
		attackDetails.put("error", message);
	}

	private void appendError(final String message) {
		ObjectNode errorNode = output.addObject();
		errorNode.put("error", message);
	}

	private boolean areEnemies(final Card attacker, final Card attacked) {
		// cards are enemies based on player ownership
		return table.getOwner(attacker) != table.getOwner(attacked);
	}

	private void attackCard(final Card attacker,
							final Card attacked,
							final Coordinates attackedCoords) {
		int attackedHealth = attacked.getCard().getHealth();
		int attackerDamage = attacker.getCard().getAttackDamage();
		int newHealth = attackedHealth - attackerDamage;
		attacked.getCard().setHealth(newHealth);

		// check if the attacked card is destroyed
		if (newHealth <= 0) {
			table.removeCardAt(attackedCoords.getX(), attackedCoords.getY());
		}

		attacker.setAttacked(true);
	}
	private void resetCardAttacks() {
		for (ArrayList<Card> row : table.getTable()) {
			for (Card card : row) {
				card.setAttacked(false);
				table.getHero1().setAttacked(false);
				table.getHero2().setAttacked(false);
			}
		}
	}

	/**
	 * @param action containing the coordinates
	 */
	private void getCardAtPosition(final ActionsInput action) {
		int x = action.getX();
		int y = action.getY();

		Card card = table.getCard(x, y);
		ObjectNode cardDetails = output.addObject();
		cardDetails.put("command", "getCardAtPosition");
		cardDetails.put("x", x);
		cardDetails.put("y", y);

		if (card == null) {
			cardDetails.put("output", "No card available at that position.");
		} else {
			ObjectNode details = cardDetails.putObject("output");
			details.put("mana", card.getCard().getMana());
			details.put("attackDamage", card.getCard().getAttackDamage());
			details.put("health", card.getCard().getHealth());
			details.put("description", card.getCard().getDescription());
			ArrayNode colorsArray = details.putArray("colors");
			for (String color : card.getCard().getColors()) {
				colorsArray.add(color);
			}
			details.put("name", card.getCard().getName());
		}
	}

	/**
	 * @param action The action containing coordinates of the cards
	 */
	public void cardUsesAbility(final ActionsInput action) {
		Coordinates attackerCoords = action.getCardAttacker();
		Coordinates attackedCoords = action.getCardAttacked();

		Card attacker = table.getCard(attackerCoords.getX(), attackerCoords.getY());
		Card attacked = table.getCard(attackedCoords.getX(), attackedCoords.getY());

		if (attacker == null || attacked == null) {
			appendError("Invalid card coordinates.");
			return;
		}

		if (attacker.isFrozen()) {
			String error = "Attacker card is frozen.";
			abilityOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (attacker.isAttacked()) {
			String error = "Attacker card has already attacked this turn.";
			abilityOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (attacker.getCard().getName().equals("Disciple")
				&& areEnemies(attacker, attacked)) {
			String error = "Attacked card does not belong to the current player.";
			abilityOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if ((attacker.getCard().getName().equals("The Ripper")
				|| attacker.getCard().getName().equals("Miraj")
				|| attacker.getCard().getName().equals("The Cursed One"))
				&& !areEnemies(attacker, attacked)) {
			String error = "Attacked card does not belong to the enemy.";
			abilityOutput(attackerCoords, attackedCoords, error);
			return;
		}

		if (!attacker.getCard().getName().equals("Disciple") && mustTargetTank(attacked)) {
			String error = "Attacked card is not of type 'Tank'.";
			abilityOutput(attackerCoords, attackedCoords, error);
			return;
		}

		useAbility(attacker, attacked);
		attacker.setAttacked(true);
	}

	private void abilityOutput(final Coordinates attackerCoords,
							   final Coordinates attackedCoords,
							   final String message) {
		ObjectNode abilityDetails = output.addObject();
		abilityDetails.put("command", "cardUsesAbility");
		abilityDetails.putObject("cardAttacker").put("x",
				attackerCoords.getX()).put("y", attackerCoords.getY());
		abilityDetails.putObject("cardAttacked").put("x",
				attackedCoords.getX()).put("y", attackedCoords.getY());
		abilityDetails.put("error", message);
	}

	private void useAbility(final Card attacker, final Card attacked) {
        switch (attacker.getCard().getName()) {
            case "The Ripper" -> ((Ripper) attacker).ability((MinionCard) attacked);
            case "Miraj" -> ((Miraj) attacker).ability((MinionCard) attacker,
					(MinionCard) attacked);
            case "The Cursed One" -> ((CursedOne) attacker).ability((MinionCard) attacked, table);
            case "Disciple" -> ((Disciple) attacker).ability((MinionCard) attacked);
            default -> throw new IllegalStateException("Unhandled card type: "
					+ attacker.getCard().getName());
        }
	}

	/**
	 * @param action containing coordinates of the attacker card
	 */
	public void useAttackHero(final ActionsInput action) {
		Coordinates attackerCoords = action.getCardAttacker();

		Card attacker = table.getCard(attackerCoords.getX(), attackerCoords.getY());
		HeroCard attackedHero = currentTurn == 1 ? table.getHero2() : table.getHero1();

		if (attacker == null) {
			appendError("Invalid card coordinates.");
			return;
		}

		if (attacker.isFrozen()) {
			String error = "Attacker card is frozen.";
			attackHeroOutput(attackerCoords, error);
			return;
		}

		if (attacker.isAttacked()) {
			String error = "Attacker card has already attacked this turn.";
			attackHeroOutput(attackerCoords, error);
			return;
		}

		if (mustTargetTankBeforeHero()) {
			String error = "Attacked card is not of type 'Tank'.";
			attackHeroOutput(attackerCoords, error);
			return;
		}

		attackHero(attacker, attackedHero);
	}

	private void attackHero(final Card attacker, final HeroCard attackedHero) {
		int attackDamage = attacker.getCard().getAttackDamage();
		int newHealth = attackedHero.getCard().getHealth() - attackDamage;
		attackedHero.getCard().setHealth(newHealth);

		// check if the hero is defeated
		if (newHealth <= 0) {
			String winnerMessage = currentTurn == 1
					? "Player one killed the enemy hero."
					: "Player two killed the enemy hero.";
			endGame(winnerMessage);
			return;
		}
		attacker.setAttacked(true);
	}

	private void attackHeroOutput(final Coordinates attackerCoords, final String error) {
		ObjectNode attackDetails = output.addObject();
		attackDetails.put("command", "useAttackHero");
		attackDetails.putObject("cardAttacker").put("x",
				attackerCoords.getX()).put("y", attackerCoords.getY());
		attackDetails.put("error", error);
	}

	private boolean mustTargetTankBeforeHero() {
		int enemyPlayerId = currentTurn == 1 ? 2 : 1;
		ArrayList<Card> frontRow = table.getTable().get(enemyPlayerId == 1
				? Table.FRONT_ROW_PLAYER1 : Table.FRONT_ROW_PLAYER2);

		return frontRow.stream().anyMatch(Card::isTank);
	}

	private void endGame(final String message) {
		ObjectNode gameEndDetails = output.addObject();
		gameEndDetails.put("gameEnded", message);

		gameOver = true;
		if (currentTurn == 1) {
			player1Wins++;
		} else {
			player2Wins++;
		}
	}

	/**
	 * @param action containing affected row
	 */
	public void useHeroAbility(final ActionsInput action) {
		int affectedRow = action.getAffectedRow();
		HeroCard hero = currentTurn == 1
				? table.getHero1() : table.getHero2();
		ArrayList<Card> affectedCardsRow = table.getTable().get(affectedRow);

		if (hero.getCard().getMana() > (currentTurn == 1
				? player1.getMana() : player2.getMana())) {
			String error = "Not enough mana to use hero's ability.";
			abilityError(error, affectedRow);
			return;
		}

		if (hero.isAttacked()) {
			String error = "Hero has already attacked this turn.";
			abilityError(error, affectedRow);
			return;
		}

		if ((hero.getCard().getName().equals("Lord Royce")
				|| hero.getCard().getName().equals("Empress Thorina"))
				&& !isEnemyRow(currentTurn, affectedRow)) {
			String error = "Selected row does not belong to the enemy.";
			abilityError(error, affectedRow);
			return;
		}

		if ((hero.getCard().getName().equals("General Kocioraw")
				|| hero.getCard().getName().equals("King Mudface"))
				&& !isAllyRow(currentTurn, affectedRow)) {
			String error = "Selected row does not belong to the current player.";
			abilityError(error, affectedRow);
			return;
		}

		hero.ability(affectedCardsRow);
		hero.setAttacked(true);

		if (currentTurn == 1) {
			player1.useMana(hero.getCard().getMana());
		} else {
			player2.useMana(hero.getCard().getMana());
		}
	}


	private void abilityError(final String error, final int affectedRow) {
		ObjectNode abilityDetails = output.addObject();
		abilityDetails.put("command", "useHeroAbility");
		abilityDetails.put("affectedRow", affectedRow);
		abilityDetails.put("error", error);
	}

	private boolean isEnemyRow(final int turn, final int affectedRow) {
		return (turn == 1 && (affectedRow == Table.FRONT_ROW_PLAYER2
				|| affectedRow == Table.BACK_ROW_PLAYER2))
				|| (turn == 2 && (affectedRow == Table.FRONT_ROW_PLAYER1
				|| affectedRow == Table.BACK_ROW_PLAYER1));
	}

	private boolean isAllyRow(final int turn, final int affectedRow) {
		return (turn == 1 && (affectedRow == Table.FRONT_ROW_PLAYER1
				|| affectedRow == Table.BACK_ROW_PLAYER1))
				|| (turn == 2 && (affectedRow == Table.FRONT_ROW_PLAYER2
				|| affectedRow == Table.BACK_ROW_PLAYER2));
	}

	private void getFrozenCardsOnTable(final ActionsInput action) {
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", action.getCommand());

		ArrayNode frozenCardsNode = responseNode.putArray("output");
		for (ArrayList<Card> row : table.getTable()) {
			for (Card card : row) {
				if (card.isFrozen()) {
					ObjectNode cardNode = frozenCardsNode.addObject();
					cardNode.put("mana", card.getCard().getMana());
					cardNode.put("attackDamage",
							card.getCard().getAttackDamage());
					cardNode.put("health", card.getCard().getHealth());
					cardNode.put("description",
							card.getCard().getDescription());
					ArrayNode colorsArray = cardNode.putArray("colors");
					for (String color : card.getCard().getColors()) {
						colorsArray.add(color);
					}
					cardNode.put("name", card.getCard().getName());
				}
			}
		}
	}

	/**
	 * get how many times player1 won
	 */
	public void getPlayerOneWins() {
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getPlayerOneWins");
		responseNode.put("output", player1Wins);
	}

	/**
	 * get how many times player2 won
	 */
	public void getPlayerTwoWins() {
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getPlayerTwoWins");
		responseNode.put("output", player2Wins);
	}

	/**
	 * get how many games are
	 */
	public void getTotalGamesPlayed() {
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getTotalGamesPlayed");
		responseNode.put("output", nrGames);
	}

}
