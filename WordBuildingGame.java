import java.util.*;
import java.util.logging.Logger;

public class WordBuildingGame {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Set<String> dictionary = new HashSet<>();
    private static int player1Score = 0;
    private static int player2Score = 0;
    private static final Logger logger = Logger.getLogger(WordBuildingGame.class.getName());
    private static final String PLAYER1_COLOR = "\u001B[34m"; // Blue
    private static final String PLAYER2_COLOR = "\u001B[31m"; // Red
    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m"; // Yellow

    public static void main(String[] args) {
        try {
            // Basic logging setup without external file
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(java.util.logging.Level.INFO);
            
            // Custom formatter that only shows the message
            java.util.logging.SimpleFormatter formatter = new java.util.logging.SimpleFormatter() {
                @Override
                public String format(java.util.logging.LogRecord record) {
                    return record.getMessage() + "\n";
                }
            };
            
            rootLogger.getHandlers()[0].setFormatter(formatter);
        } catch (Exception e) {
            logger.severe("Could not configure logging: " + e.getMessage());
        }

        initializeDictionary();
        logger.info("Welcome to Word Building Game!");
        playGame("");
    }

    private static void initializeDictionary() {
        // Add some sample words - in practice, load from file
        String[] words = {"cat", "cats", "catch", "catches", "dog", "dogs", "do", "doing"};
        dictionary.addAll(Arrays.asList(words));
    }

    private static void playGame(String currentString) {
        //logger.info("\nCurrent string: " + (currentString.isEmpty() ? "[empty]" : currentString));
        
        for (int player = 1; player <= 2; player++) {
            String newString = takeTurn(currentString, player);
            if (newString == null) {
                announceWinner();
                askToPlayAgain();
                return;
            }
            currentString = newString;
        }
        
        playGame(currentString);
    }
    
    private static String takeTurn(String currentString, int player) {
        String playerColor = (player == 1) ? PLAYER1_COLOR : PLAYER2_COLOR;
        logger.info(YELLOW + "\nCurrent string: " + (currentString.isEmpty() ? "[empty]" : currentString) + RESET   );
        logger.info(playerColor + String.format("Player %d's turn", player) + RESET);
        logger.info(playerColor + "Add a letter (or 'quit' to end): " + RESET);
        //logger.info("Current string: " + YELLOW + currentString + RESET);
        String input = scanner.nextLine().toLowerCase();
        
        if (input.equals("quit")) {
            return null;
        }
    
        if (input.length() != 1) {
            logger.warning("Please enter exactly one letter!");
            return takeTurn(currentString, player);
        }
    
        String newString = currentString + input;
        
        if (dictionary.contains(newString)) {
            if (player == 1) player1Score++; else player2Score++;
            String playerColorFound = (player == 1) ? PLAYER1_COLOR : PLAYER2_COLOR;
            logger.info(playerColorFound + String.format("Word '%s' found! Player %d scores a point!", newString, player) + RESET);
          //  logger.info("Current string: " + YELLOW + newString + RESET);
            dictionary.remove(newString); // Remove the word so it can't be used again
            return ""; // Reset the string after finding a word
        }
    
        return newString;
    }

    private static void announceWinner() {
        logger.info("\nGame Over!");
        logger.info(PLAYER1_COLOR + String.format("Player 1: %d points", player1Score) + RESET);
        logger.info(PLAYER2_COLOR + String.format("Player 2: %d points", player2Score) + RESET);
        if (player1Score > player2Score) {
            logger.info(PLAYER1_COLOR + "Player 1 wins!" + RESET);
        } else if (player2Score > player1Score) {
            logger.info(PLAYER2_COLOR + "Player 2 wins!" + RESET);
        } else {
            logger.info("It's a tie!");
        }
    }

    private static void askToPlayAgain() {
        logger.info("Play again? (y/n): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            player1Score = 0;
            player2Score = 0;
            playGame("");
        } else {
            logger.info("Thanks for playing!");
        }
    }
}
