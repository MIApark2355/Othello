package Othello;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Othello class holds the game logic, functions to write and read game states and game records,
 * and methods useful for JUnit test
 */

public class Othello {

    /*--2D array--*/
    //game board (8 rows and 8 columns)
    private int[][] board;
    private int numTurns;
    private boolean player1;
    private String name1;
    private String name2;
    private int score1;
    private int score2;
    private boolean gameOver;

    // If a player has no valid move, the player have to skip turn.
    // If both players continuously have no valid move, the game ends.
    private int skip;

    /*--Collections--*/
    //1. ArrayList stores current player's valid moves after each turn.
    //2. Map is to match between players and their score.
    //3. LinkedList store players' name in the order of their score. (high to low)
    private ArrayList<Cell> validMove = new ArrayList<>();
    private Map<String, Integer> userScores;
    private LinkedList<String> rank = new LinkedList<>();

    static final String PATH_TO_SAVE_SCORES  = "files/game_Scores.txt";
    static final String PATH_TO_SAVE_RANK = "files/game_Rank.txt";
    static final String PATH_TO_SAVE_MOVES = "files/saved_moves.txt";

    /**
     *
     * @param player1 player who plays with black discs
     * @param player2 player who plays with white discs
     * @param isNewGame true if players have chosen to start a new game
     */
    public Othello(String player1, String player2, boolean isNewGame) {

        this.name1 = player1;
        this.name2 = player2;

        userScores = new TreeMap<>();

        if (isNewGame) {
            restart();
        } else {
            this.board = new int[8][8];
            loadGame();
        }
        // When starting a game, the model reads a text file only if it exits.
        // If there is no previous record, collections of userScores and rank will be empty.
        if (((new File(PATH_TO_SAVE_RANK).exists()))
                && ((new File(PATH_TO_SAVE_SCORES).exists()))) {
            readFile(PATH_TO_SAVE_RANK);
            readFile(PATH_TO_SAVE_SCORES);
        }
    }

    /**
     * Setting the game to initial state
     */
    public void restart() {
        //setting up the game
        board = new int[8][8];
        numTurns = 0;
        player1 = true; //starts with player1
        score1 = 2;
        score2 = 2;
        gameOver = false;
        skip = 0;

        // Both players begin the game with two pieces
        // on the board in the four centre squares.
        // There should be no same color connected vertically or horizontally.
        board[3][3] = 2;
        board[3][4] = 1;
        board[4][3] = 1;
        board[4][4] = 2;

        resetValidity(); //empty the validMove list

        updateValidity(3,4);//update validMove for player1
        updateValidity(4,3);//update validMove for player1
    }

    /**
     * loading the game from the file and setting
     * the game state based on the saved game states.
     */
    public void loadGame() {
        if (new File(PATH_TO_SAVE_MOVES).exists()) {
            readFile(PATH_TO_SAVE_MOVES);
            gameOver = false;
            updateValidChoices();
        }
    }

    /**
     * A leaderboard should be updated after the end of each game.
     */
    public void endGame() {
        rankRearrange(name1, score1);
        rankRearrange(name2, score2);
    }

    /** A method to update validMove(ArrayList) for current player.
    * If any line of 8 directions has opponent player's discs connected and
     * followed by current player's disc,then the empty cell before opponent
     * player discs' sequence is a valid move.
     * (i.e. if the current player is 1, then the first cell for 02222211 is a valid move)
    */
    public void updateValidChoices() {
        validMove = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == getCurrentPlayer()) {
                    updateValidity(j,i); // a reference cell that we will check
                }
            }
        }
        //If the current player has no valid move, the next(opponent) player takes turn.
        //If the player has valid moves right after one skip of opponent player,
        //the number of skip is re-set to zero
        if (validMove.isEmpty()) {
            skip();
        } else {
            skip = 0;
        }
    }

    //Method to skip current player's turn
    public void skip() {
        skip ++;
        player1 = !player1;
    }

    /**
     * A method to add valid moves for current player to ArrayList ValidMove.
     * This will look through 8 directions and check opponent player's discs would be flipped
     * between which empty cell and a reference cell when the empty cell is filled with
     * current player's disc. Those empty cells will be added to ValidMove.
     *
     * @param c column index for a reference cell
     * @param r row index for a reference cell
     */
    public void updateValidity(int c, int r) {
        //EAST
        //Local variable, opponentExist tells you that
        // there is at least one opponent color after a reference cell.
        boolean opponentExist = false;
        for (int i = 1; ((c + i) < 8); i++) {
            if (board[r][c + i] == getCurrentPlayer()) {
                break;
            }
            if (board[r][c + i] != getCurrentPlayer()) {
                if (board[r][c + i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c + i, r));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //SOUTHEAST
        opponentExist = false;
        for (int i = 1; ((c + i) < 8) && ((r + i) < 8); i++) {
            if (board[r + i][c + i] == getCurrentPlayer()) {
                break;
            }
            if (board[r + i][c + i] != getCurrentPlayer()) {
                if (board[r + i][c + i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c + i, r + i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //SOUTH
        opponentExist = false;
        for (int i = 1; ((r + i) < 8); i++) {
            if (board[r + i][c] == getCurrentPlayer()) {
                break;
            }
            if (board[r + i][c] != getCurrentPlayer()) {
                if (board[r + i][c] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c, r + i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //SOUTHWEST
        opponentExist = false;
        for (int i = 1; ((c - i) > -1) && ((r + i) < 8); i++) {
            if (board[r + i][c - i] == getCurrentPlayer()) {
                break;
            }
            if (board[r + i][c - i] != getCurrentPlayer()) {
                if (board[r + i][c - i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c - i, r + i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //WEST
        opponentExist = false;
        for (int i = 1; ((c - i) > -1); i++) {
            if (board[r][c - i] == getCurrentPlayer()) {
                break;
            }
            if (board[r][c - i] != getCurrentPlayer()) {
                if (board[r][c - i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c - i, r));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //NORTHWEST
        opponentExist = false;
        for (int i = 1; ((c - i) > -1) && ((r - i) > -1); i++) {
            if (board[r - i][c - i] == getCurrentPlayer()) {
                break;
            }
            if (board[r - i][c - i] != getCurrentPlayer()) {
                if (board[r - i][c - i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c - i, r - i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
        //NORTH
        opponentExist = false;
        for (int i = 1; (r - i) > -1; i++) {
            if (board[r - i][c] == getCurrentPlayer()) {
                break;
            }
            if (board[r - i][c] != getCurrentPlayer()) {
                if (board[r - i][c] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c, r - i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }

        //NORTHEAST
        opponentExist = false;
        for (int i = 1; ((c + i) < 8) && ((r - i) > -1); i++) {
            if (board[r - i][c + i] == getCurrentPlayer()) {
                break;
            }
            if (board[r - i][c + i] != getCurrentPlayer()) {
                if (board[r - i][c + i] == 0) { //empty cell
                    if (opponentExist) {
                        validMove.add(new Cell(c + i, r - i));
                    }
                    break;
                } else {
                    opponentExist = true;
                }
            }
        }
    }


    /**
     *
     * @param c column index
     * @param r row index
     * @return true if (c,r) is a valid move. Otherwise, returns false.
     */
    public boolean isValid(int c, int r) {
        for (Cell cell : validMove) {
            if (cell.equals(new Cell(c,r))) {
                return true;
            }
        }
        return false;
    }


    /**
     * A method that flips all the discs that should be flipped
     * regarding the game logic.
     @parameter c column
     @parameter r row
     **/
    public void searchForFlip(int c, int r) {
        //EAST
        boolean flipPossible = false; //local variable
        ArrayList<Cell> possibleFlip = new ArrayList<>(); //local variable
        for (int i = 1; (c + i) < 8; i++) {
            if (board[r][c + i] == 0) {
                break;
            }
            if (board[r][c + i] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c + i,r));
                flipPossible = true;

            }
            if (board[r][c + i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //SOUTHEAST
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((c + i) < 8) && ((r + i) < 8); i++) {
            if (board[r + i][c + i] == 0) {
                break;
            }
            if (board[r + i][c + i] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c + i,r + i));
                flipPossible = true;

            }
            if (board[r + i][c + i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }

        //SOUTH
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((r + i) < 8); i++) {
            if (board[r + i][c] == 0) {
                break;
            }
            if (board[r + i][c] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c,r + i));
                flipPossible = true;

            }
            if (board[r + i][c] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //NORTHWEST
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((c - i) > -1) && ((r - i) > -1); i++) {
            if (board[r - i][c - i] == 0) {
                break;
            }
            if (board[r - i][c - i] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c - i,r - i));
                flipPossible = true;

            }
            if (board[r - i][c - i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //WEST
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((c - i) > -1); i++) {
            if (board[r][c - i] == 0) {
                break;
            }
            if (board[r][c - i] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c - i,r));
                flipPossible = true;
            }
            if (board[r][c - i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //SOUTHWEST
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((c - i) > -1) && ((r + i) < 8); i++) {
            if (board[r + i][c - i] == 0) {
                break;
            }
            //storing opponent blocks that are connected
            if (board[r + i][c - i] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c - i,r + i));
                flipPossible = true;
            }
            if (board[r + i][c - i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //NORTH
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; (r - i) > -1; i++) {
            if (board[r - i][c] == 0) {
                break;
            }
            if (board[r - i][c] != getCurrentPlayer()) {
                possibleFlip.add(new Cell(c,r - i));
                flipPossible = true;
            }
            if (board[r - i][c] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
        //NORTHEAST
        flipPossible = false;
        possibleFlip = new ArrayList<>();
        for (int i = 1; ((c + i) < 8) && ((r - i) > -1); i++) {
            if (board[r - i][c + i] == 0) {
                break;
            }
            if (board[r - i][c + i] != getCurrentPlayer()) {
                flipPossible = true;
                possibleFlip.add(new Cell(c + i,r - i));
            }
            if (board[r - i][c + i] == getCurrentPlayer()) {
                if (flipPossible) {
                    for (Cell cell : possibleFlip) {
                        flip(cell);
                    }
                }
                break;
            }
        }
    }

    /**
     * A method to flip black(1) to white(2) and white(2) to black(1)
     * If a black disc is flipped to the white color,
     * player 1 loses one point and player 2 gets plus one point.
     *
     * @param cell cell that should be flipped
     */
    public void flip(Cell cell) {
        int r = cell.getY();
        int c = cell.getX();
        if (board[r][c] == 1) {
            board[r][c] = 2;
            score1 --;
            score2 ++;
        } else if (board[r][c] == 2) {
            board[r][c] = 1;
            score2 --;
            score1 ++;
        }
    }

    /**
     * If the choice of current player is empty and is a valid move,
     * the cell's state will be updated to current player's number, and
     * the current player will get plus one point.
     *
     * Also, opponent player's discs between the chosen cell and some cell will be flipped.
     * Thus, the opponent will lose points as the current player gets points.
     *
     * After discs are flipped, next player takes turn and numTurn increases by 1.
     *
     * @param c column index of a cell that is clicked by mouse
     * @param r row index of a cell that is clicked by mouse
     */
    public void playTurn(int c, int r) {
        if ((board[r][c] == 0) && (isValid(c, r)) && (!gameOver)) {
            if (player1) {
                board[r][c] = 1;
                score1 ++;
            } else {
                board[r][c] = 2;
                score2 ++;
            }
            searchForFlip(c,r);
            player1 = !player1;
            numTurns++;
        }
    }

    /**
     * If numTurns is more than 60 or there has been two skips, the game ends.
     *
     * @return the winner's player number. If the scores are same, it returns 3. If the game has not
     * ended, it returns 0.
     */
    public int checkWinner() {
        if (numTurns >= 60 || (skip >= 2) || score1 == 0 || score2 == 0) {
            gameOver = true;
            if (score1 > score2) {
                return 1;
            }
            if (score2 > score1) {
                return 2;
            } else {
                return 3;
            }
        }
        return 0;
    }

    /**
     * helper method for rankRearrange method
     * @param player player's name
     * @param newScore player's score after the end of game
     * @return 1 if there is no record for player. If a player has a record in
     * userScores Map and got a new high score, it returns 3. If a player got lower score than
     * before, it returns 2.
     */
    public int playerType(String player, int newScore) {
        //if the player had played the game before and is in top 10 records.
        if (userScores.containsKey(player)) {
            int lastScore = userScores.get(player);
            if (lastScore >= newScore) {
                return 2; //should keep the previous record
            } else {
                return 3; //player's new score is higher than before
            }
        } else { //player not in the record
            return 1; //new player
        }
    }

    /**
     * helper method for rankRearrange method
     * @param name player's name
     * @return a player's score by mapping player's name as a key in userScores Map.
     */
    public int getScoreByName(String name) {
        for (Map.Entry<String,Integer> e : userScores.entrySet()) {
            if (name.equals(e.getKey())) {
                return e.getValue();
            }
        }
        return -1; // no such name
    }

    /**
     * PlayerType 1: There is no previous record to consider
     * PlayerType 2: Since the last score is higher than
     * current score, record should not be updated.
     * PlayerType 3: The record should be updated to the new score
     * which is higher than the last score.
     *
     * After updating the record, top 10 highest scores and the name of those
     * players will be written on a text file.
     *
     * @param name player's name
     * @param score player's score at the end of game
     */
    public void rankRearrange(String name, Integer score) {
        int playerType = playerType(name, score);
        boolean smallest = true;
        if (userScores.isEmpty()) {
            rank.add(name);
            userScores.put(name, score);
        //if the current player had a better score before, nothing will be updated
        } else if (playerType != 2) {
            if (playerType == 3) {
                rank.remove(name); // remove previous record to update the ranking and score
            }
            //searching for the right place
            for (int i = 0; i < rank.size(); i++) {
                //For example, if player 1's score is higher than
                //1st place player's (index 0 for linkedList rank) score,
                //player 1's name should be added at the index 0.
                if (getScoreByName(rank.get(i)) <= score) {
                    smallest = false;
                    rank.add(i, name);
                    userScores.put(name, score); //score will be updated for the same name
                    if (rank.size() > 10) {
                        String toRemove = rank.get(10);// 11th place
                        rank.removeLast();
                        userScores.remove(toRemove);
                    }
                    break;
                }
            }
            //if there is enough place to store current player's score which is lowest
            if (rank.size() < 10 && smallest) {
                userScores.put(name, score);
                rank.addLast(name);
            }
        }

        writeToFile(PATH_TO_SAVE_RANK);
        writeToFile(PATH_TO_SAVE_SCORES);
    }


    public Map<String, Integer> getUserScores() {
        return userScores;
    }

    public void resetValidity() {
        validMove = new ArrayList<>();
    }

    /*--Concept (I/0)--*/
    //1.Writing
    public void writeToFile(String fileName) {
        File file = null;

        try {
            file = Paths.get(fileName).toFile();
        } catch (InvalidPathException e) {
            file = new File(fileName); //create if it does not exist
            System.out.println("file created");
        }

        BufferedWriter bw = null;

        try {
            FileWriter writer = new FileWriter(fileName, false);
            bw = new BufferedWriter(writer);

            if (fileName.equals(PATH_TO_SAVE_RANK)) {
                file.delete();
                //writing at most 10 players who got the top 10 highest score
                for (int i = 0; (i < rank.size()) && (i < 10) ; i++) {
                    bw.write(rank.get(i));
                    bw.newLine();
                }
            } else if (fileName.equals(PATH_TO_SAVE_SCORES)) {
                int i = 0;
                for (Map.Entry<String,Integer> entry : userScores.entrySet()) {
                    i++;
                    bw.write(entry.getKey());//writing score first
                    bw.newLine();
                    bw.write(String.valueOf(entry.getValue()));//writing name after
                    bw.newLine();
                    if (i == 10) {
                        break;
                    }
                }
            } else if (fileName.equals(PATH_TO_SAVE_MOVES)) {
                //writing the game board state
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        bw.write(String.valueOf(board[i][j]));
                        if (j < 7) {
                            bw.write(" ");
                        }
                    }
                    bw.newLine();
                }
                //writing the states for important fields
                bw.write(name1);
                bw.newLine();
                bw.write(String.valueOf(score1));
                bw.newLine();
                bw.write(name2);
                bw.newLine();
                bw.write(String.valueOf(score2));
                bw.newLine();
                bw.write(String.valueOf(numTurns));
                bw.newLine();
                bw.write(String.valueOf(getCurrentPlayer()));
                bw.newLine();
                bw.write(String.valueOf(skip));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //When 'SAVE' button is clicked from the OthelloScreen window
    public void save() {
        writeToFile(PATH_TO_SAVE_MOVES);
    }

    //Reading
    public void readFile(String fileName) {

        BufferedReader br = null;
        File file;

        try {
            file = new File(fileName);
            br = new BufferedReader(new FileReader(file));

            if (fileName.equals(PATH_TO_SAVE_RANK)) {
                String rankName;
                //make sure only top 10 records are loaded to rank LinkedList
                int i = 0;
                while (((rankName = br.readLine()) != null) && (i < 11)) {
                    rank.add(rankName);
                    i++;
                }
            } else if (fileName.equals(PATH_TO_SAVE_SCORES)) {
                String nameToPut;
                String scoreToPut;
                while ((nameToPut = br.readLine()) != null) {
                    scoreToPut = br.readLine();
                    //putting top 10 highest scores as values for TreeMap
                    //Scores are not in numerical order in the userScores Map
                    userScores.put(nameToPut, Integer.parseInt(scoreToPut));
                }
            } else if (fileName.equals(PATH_TO_SAVE_MOVES)) {
                //When 'Load Game' button is clicked from the menu screen,
                //the game state will be set as the saved state.
                for (int i = 0; i < 8; i++) {
                    String row = br.readLine();
                    if (row != null) {
                        String[] tem = row.split(" ");
                        for (int j = 0; j < tem.length; j++) {
                            board[i][j] = Integer.parseInt(tem[j]);
                        }
                    }
                }
                String next = br.readLine();
                if (next != null) {
                    name1 = next;
                    score1 = Integer.parseInt(br.readLine());
                    name2 = br.readLine();
                    score2 = Integer.parseInt(br.readLine());
                    numTurns = Integer.parseInt(br.readLine());
                    int playerToStart = Integer.parseInt(br.readLine());
                    player1 = (playerToStart == 1);
                    skip = Integer.parseInt(br.readLine());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*--get and set methods for JUnit Test--*/
    /**
     *
     * @param board game board that each cell is 0,1,or 2
     * @param currentPlayer1 true if a player to play turn is player 1
     * @param skip the number of continuous skips
     */
    public void setBoard(int[] board, boolean currentPlayer1, int skip) {
        restart();
        for (int i = 0 ; i < board.length ; i++) {
            if (board[i] != 0) {
                numTurns++;
                if (board[i] == 1) {
                    score1 ++;
                } else {
                    score2 ++;
                }
            }
        }
        numTurns -= 4; // center four cells begins with 1 or 2 (non-zero element)
        score1 -= 2; //players have 2 points when the game begins
        score2 -= 2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8 ; j++) {
                this.board[i][j] = board[(i * 8) + j];
            }
        }
        player1 = currentPlayer1;
        this.skip = skip;
    }

    /**
     * This method changes 2D array to a list.
     * A row is added next to the last element(last column cell) of previous row.
     *
     * @return a list of integer(0,1,or 2)
     */
    public int[] getBoard() {
        int[] res = new int[64];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                res[(i * 8) + j] = board[i][j];
            }
        }
        return res;
    }

    public int getCurrentPlayer() {
        if (player1) {
            return 1;
        } else {
            return 2;
        }
    }

    public int getCell(int c, int r) {
        return board[r][c];
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public boolean skipped() {
        if (skip >= 1) {
            return true;
        }
        return false;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasValidMove() {
        return !validMove.isEmpty();
    }


    public LinkedList getRank() {
        return rank;
    }

    public void printGameState() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 7) {
                    System.out.print(" ");
                }
            }
            if (i < 7) {
                System.out.println("\n");
            } else if (i == 7) {
                System.out.println("\n------END------");
            }
        }
    }

}