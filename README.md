# Othello
Othello game

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D array (integer)

  - The game board has 8 rows and 8 columns.
  - Like two points(positions) are identical when x and y are equal, two cells are identical when they are located
  at the same row and column.
  - I used a list of Cells of 2D game board for ValidMove.
  - By iterating through entries of 2D array, each cell's element of the game board are saved,
    and players can re-load the state of the game board.

  2. Collections and Maps
  *Feedback: This seems like it's storing the same state as the 2D array.

  -ArrayList : I used this to store 'Cells' that are valid choices for current player.
  A mover should click one of Cells in validMove list. The game board will show
  those valid cells with yellow boxes. If a mover clicks a Cell and that is valid,
  game model will search for flips and update valid moves for next player.
  If the validMove list is empty, a mover's turn should be skipped.

  **I chose both LinkedList and TreeMap for managing the game record because I had to implement a method to re-arrange
  and store both name(String) and score(Integer) of top 10 highest record in order. If I had only used one treemap that has
  names as a key and score as a value, I would not have been able to keep track of ranking efficiently because
  the treemap would be sorted in alphabetical order. Also, If I had only used one treemap
  that has scores as a key and name as a value, I would not have been able to store all the players who have
  the same score.**

  -LinkedList: I used this to re-arrange and store at most 10 players in the order of ranking. For example, the name stored
  in index 0 is the first place. I chose LinkedList because I can add object(player's name) to the index I want
  to put in and other players' places(index of the list) will be automatically replaced, and it is resizable.
  If the player gets top 9 highest score and added to index 8 of the list, a player who used to be in 9th place will
  be replaced automatically from index 8 to index 9.

  -TreeMap: I used this to update and store players' name as a key and their scores as a value. Although players are not stored
  in order of ranking, I chose this to keep track of their scores. By iterating this map, I can easily find a certain
  player's score by using getKey method. Also, put(KEY,VALUE) method is useful for both updating player's scores and
  adding a new player's score.

  3. File I/O
  *Feedback: A good implementation of this concept would be if you have a "save game" button which stores
  all the mentioned info in a file which then can be reloaded

  -I used I/O to store top 10 highest scores and the name of those players and to save and re-load the game states.

  -A game state such as game board element, players' name, players' score, the number of turns, the number of skips, and
   current player can be saved when a player clicks 'SAVE' button by writing to a text file (saved_moves.txt).
  -The game state can be re-loaded by reading a text file (saved_moves.txt)
  -When other player presses save button, the previously saved game state will be removed (not appending) and a new game
  state will be written and stored.

  - When a new game starts or the game is re-loaded, text files will be read.
  - If there is no previous record, meaning text files do not exist, the game will start with empty rank and userScores.
    In this case, text files(saved_rank.txt and saved_scores.txt) will be automatically created after the game ends.
  -Every time a game ends, updated ranking and scores will be stored in a text file (saved_rank.txt and saved_scores.txt).

  4. JUnit Testable Component
  *Feedback: You can't really check if you're showing instruction since it's part of the GUI but the rest looks
  good as long as it's implemented through JUnit tests.

  (game logic and method test)
    playTurn method:
                    1. flipping discs (checking all 8 directions)
                        --> location: corner, side, middle of the game board
                    2. updating scores after each move
                    3. changing the current player

    updateValidChoices method : updating valid choices for the current player

    checkWinner method: comparing players' score at the end of the game

    restart method: restarting the game

    skip method : skips turn

  (Edge cases)
  1. When a player has to skip his or her turn (= When a play does not have any valid moves)
  2. When both players don't have valid move
  3. When all the elements of the game board become non-zero
  4. When clicking a cell that is not valid
  5. When one player's score becomes zero although there are still empty cells in the game board

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  1. RunStartMenu : The first screen that shows when you run Game.

    	‘How to Play’ button  Instruction window shows up, menu window does not disappear

    	‘Load game’  button 	If there is no saved move  error message
                            	If there is a saved game state, load the latest saved game state,
                                and menu window disappears.

    	‘Start’ button  If players did not type their names -> error message “Enter name!”
                        If players input the same name -> error message “Do not enter the same name”
                        Input names will be upper-cased, be shown at the game board,
                       and be used when saving the score and rank to text files.

  2. OthelloPanel
     Status label  who's turn? who is the winner? did the game end?

     Score label  It shows updated scores after each turn.

     ‘MENU’ button  going back to menu screen (i.e. to change player’s name and start a new game).
                     This won’t automatically save the game state.

     ‘RESTART’ button  starting a new game with the same players
                        If the player presses this button before the game finishes,
                       the score won’t be saved automatically

     ‘INSTRUCTION’ button  Instruction window shows independently, the game board will not be changed.

     ‘RECORDS’ button   A leaderboard window shows independently, the game board will not be changed.

     ‘SAVE’ button  To save the game state. The player should press this button if they want to continue later.
                     If the other player presses this button after, the previous saved game state will be removed.
     ‘QUIT’  To close the window. The game won’t be automatically saved.

  3. GameBoard

  	Yellow boxes are to tell which cells are valid.
  	Black discs are for player 1 and white discs are for player 2.
  	Game begins with four discs placed in the center.

  4. Othello (Game logic/ Write and read game state and score record/ Get and set methods for JUnit test)

    Othello.writeToFile() & Othello.readFile() : Methods to save and load game using I/O.

    Game logic -> Which discs are flipped? Which cells are valid choices?
                    What happens after a player clicks a valid move?
                    When will the player's turn be skipped?
                    When does the game end?

    Update record -> Othello.rankRearrange() method: The ranking will be updated after the game ends.
                   -> Othello.writeToFile() & Othello.readFile() : keeping and updating the leaderboard

    Get and set methods to test the game model with private(encapsulated) Othello fields.

  5. Cell

    (override) equals method compares if two cells have the same x(integer) and y(integer).
   If x and y are equal, two objects are indicating the same cell.

  6. Instruction

   A frame(window) with instruction image
   'Back' button  closes the instruction screen

  7. Records

   shows previous top 10 ranking with names and scores.
   If no one has played(finished) the game yet, it will show a label, "no records".
   If the game ends and players' scores are higher than previous top 10 highest scores,
    their scores will be stored in a text file and updated to the leaderboard.

  8. GameTest  JUnit test (testing game logic and edge cases)

  9. Game  class that has main method run to start and start menu screen

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

1. Analyzing the game logic: Implementing method that searches for flips and skips a player's
turn without affecting other game states was tricky.

2. Choosing a type of collection for the game record(ranking): First, I thought saving players' score as a key and their name
as a value in TreeMap is appropriate because keys for a treemap will be sorted. But then I realized that if several
players have the same scores, only the latest player's record will be stored in the map because map cannot have
repeated keys.
The next idea was to use two different TreeMaps, one storing rank(integer) as a key and name as a value and
the other storing rank(integer) as a key and the score as a value. However, I realized that this idea is also not good.
Ths is because if a current player gets 5th highest score, the previous players under 4th place should
be replaced to one place lower but using 'put' method like map.put(8,"rank7 before") for every player seemed
too complicated.

3. Implementing Load method: It was difficult to find a way to separate a method to start a new game and a method
to re-load the previously saved game. I designed the game so that game board shows players' name that players
had input before starting the game. Thus, finding a way for model to use players' name saved in a text file
when Othello.loadGame() method is called was difficult. I was able to implement load method
by adding a parameter, 'isNewGame' to GameBoard and OthelloPanel classes.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think there is a good separation of functionality, because GUI parts and game logic are in the separate classes.
Also, I have built instruction and record frames in a separate class so that they won't affect other classes such as
Menu screen and Othello (game board) screen.

I think I have encapsulated private states like map, set, list, and other fields well. I implemented
get and set methods for JUnit tests that check the game model having private states.

What I would like to improve are Othello.searchForFlip() method and Othello.updateValidity() method.
Since other commands are similar except for arguments like the number of column and row, I tried to
find some helper function that would make the implementation shorter or to build separate classes,
but I was not able to do that.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  1. Instruction text : Ultra Board Games, "Othello Game Rules", https://www.ultraboardgames.com/othello/game-rules.php
