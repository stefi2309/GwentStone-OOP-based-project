package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;

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

			//currentTurn = currentGame.getStartGame().getStartingPlayer();
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

		// These calls are now safe to perform after shuffling
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
		currentTurn = currentTurn == 1 ? 2 : 1;
		if (bothPlayersEndedTurns()) {
			startNewRound();
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
		// Refresh mana for both players and draw new cards to hand
		player1.refreshMana(roundCounter);
		player2.refreshMana(roundCounter);

		table.addCardToHand(1);
		table.addCardToHand(2);

	}

	/**
	 * Attempts to place a card from the player's hand onto the table based on the hand index
	 * @param handIdx the index of the card in the player's hand that they intend to play
	 */
	public void placeCard(final int handIdx) {
		ArrayList<Card> currentHand = currentTurn == 1 ? table.getPlayer1Hand()
				: table.getPlayer2Hand();
		Player currentPlayer = currentTurn == 1 ? player1 : player2;

		// Check mana
		if (handIdx >= currentHand.size() || currentPlayer.getMana()
				< currentHand.get(handIdx).getCard().getMana()) {
			ObjectNode responseNode = output.addObject();
			responseNode.put("command", "placeCard");
			responseNode.put("handIdx", handIdx);
			responseNode.put("error", "Not enough mana to place card on table.");
			return;
		}

		// Determine the appropriate row
		Card cardToPlace = currentHand.get(handIdx);
		int row = determineRow(cardToPlace);
		if (table.isRowFull(row)) {
			ObjectNode responseNode = output.addObject();
			responseNode.put("command", "placeCard");
			responseNode.put("handIdx", handIdx);
			responseNode.put("error", "Cannot place card on table since row is full.");
			return;
		}

		// Remove the card from hand and place it on the table
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

		// Create the main output array node for all rows
		ArrayNode rowsArray = responseNode.putArray("output");

		for (ArrayList<Card> row : table.getTable()) {
			// Create an array node for each row to store card details
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
	 * @param action The action input containing details such as which player's mana to retrieve
	 */
	public void getPlayerMana(final ActionsInput action) {
		int playerIndex = action.getPlayerIdx();

		int mana = (playerIndex == 1) ? player1.getMana() : player2.getMana();

		// Create the output object node for this command
		ObjectNode responseNode = output.addObject();
		responseNode.put("command", "getPlayerMana");
		responseNode.put("playerIdx", playerIndex);
		responseNode.put("output", mana);
	}



}
