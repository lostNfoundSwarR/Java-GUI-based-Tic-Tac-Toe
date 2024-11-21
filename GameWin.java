//Functional imports (utilities)
import java.util.Random;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

//Styling and event handling imports (awt)
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//GUI imports (javax.swing)
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
     * Player move logic
     * @param source       The button clicked by the player
     * @param btnArray     The array of game buttons
     * @param currentPlayer Current player's symbol
     * @param emptySpaces  The game state array
     * @param btnMap       Mapping of buttons to their indices
*/

//Player class
class Player {
     protected char chr;

     Player(char character) {
          this.chr = character;
     }

     //Move method of player
     void move(JButton source, JButton[] btnArray, char currentPlayer, char[] emptySpaces, HashMap<JButton, Integer> btnMap) {
          //Get's the index of button from the hash map
          int btnIndex = btnMap.get(source);

          if(emptySpaces[btnIndex] == ' ') {
               btnArray[btnIndex].setText(String.valueOf(currentPlayer));
               btnArray[btnIndex].setEnabled(false);
     
               emptySpaces[btnIndex] = currentPlayer;
          }
          else {
               return;
          }
     }
}

//Computer class (sub-class of Player)
class Computer extends Player {
     
     Computer(char character) {
          super(character);
     }

     //It's own move method (over-loaded method)
     void move(char currentPlayer, char[] emptySpaces, Random random, JButton[] btnArray) {
          int btnIndex;

          //Generates a random index/move until it finds an empty space
          do {
               btnIndex = random.nextInt(9);
          }
          while (emptySpaces[btnIndex] != ' '); 

          emptySpaces[btnIndex] = currentPlayer;
          btnArray[btnIndex].setText(String.valueOf(currentPlayer));
     }
}

public class GameWin extends JFrame implements ActionListener {
     //Declarations and assignments

     //GUI components
     private static JLabel statusText;
     private static JLabel playerLabel;
     private static JLabel computerLabel;

     private static JPanel btnPanel;
     private static JPanel configPanel;
     private static JPanel restartContainer;

     private static JButton[] btnArray = new JButton[9];

     private static JButton restartBtn = new JButton("Restart");

     //Non-GUI components
     private static Timer timer = new Timer();

     private static Random random = new Random();

     private static char[] players = {'O', 'X'};

     private static Player player;
     private static Computer computer;

     private static char currentPlayer;

     private static HashMap<JButton, Integer> btnMap = new HashMap<>();

     private static char[] emptySpaces = {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

     //Winning conditions
     private static int[][] winConditions = 
     {
          //Rows
          {0, 1, 2},
          {3, 4, 5},
          {6, 7 ,8},

          //Columns
          {0, 3, 6},
          {1, 4, 7},
          {2, 5, 8},

          //Diagonals
          {0, 4, 8}, //Principle diagonal
          {2, 4, 6}  //Secondary diagonal
     };

     GameWin() {
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          this.setSize(new Dimension(800, 800));
          this.setResizable(false);
          this.setLayout(new BorderLayout());

          styleWindow();
          initializeBtns();
          initialize();

          this.setVisible(true);
     }

     //Initialize the game
     void initialize() {

          //Selects player character randomly
          player = new Player(players[random.nextInt(2)]);
          computer = new Computer((player.chr == 'O') ? 'X' : 'O');
     
          //Randomly selects the current player
          currentPlayer = players[random.nextInt(2)];

          playerLabel.setText("Player: " + player.chr);
          computerLabel.setText("Computer: " + computer.chr);

          //Executes the computer move method if the current player is the computer
          if(currentPlayer == computer.chr) {
               statusText.setText("The computer is thinking...");
               executeComMove();
               disableBtns();
          }
          else {
               statusText.setText("Your turn");
          }
     }

     //Styles the window
     void styleWindow() {
          statusText = new JLabel();
          statusText.setFont(new Font("", Font.ITALIC, 60));
          statusText.setHorizontalAlignment(JLabel.CENTER);
          statusText.setBorder(new EmptyBorder(0, 0, 0, 0));
          this.add(statusText, BorderLayout.NORTH);

          btnPanel = new JPanel();
          btnPanel.setLayout(new GridLayout(3, 3, 10, 10));
          btnPanel.setBorder(new EmptyBorder(0, 20, 10, 20));

          this.add(btnPanel, BorderLayout.CENTER);

          playerLabel = new JLabel();
          playerLabel.setFont(new Font("", Font.ITALIC, 30));
          playerLabel.setHorizontalAlignment(JLabel.CENTER);

          computerLabel = new JLabel();
          computerLabel.setFont(new Font("", Font.ITALIC, 30));
          computerLabel.setHorizontalAlignment(JLabel.CENTER);

          restartBtn.setFont(new Font("", Font.ITALIC, 35));
          restartBtn.setFocusable(false);
          restartBtn.addActionListener(this);

          restartContainer = new JPanel();
          restartContainer.add(restartBtn);

          configPanel = new JPanel();
          configPanel.setLayout(new GridLayout(3, 1, 10, 10));
          configPanel.setSize(new Dimension(70, 30));
          configPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
          configPanel.add(playerLabel);
          configPanel.add(computerLabel);
          configPanel.add(restartContainer);

          this.add(configPanel, BorderLayout.SOUTH);
     }
     
     //Initializes buttons
     void initializeBtns() {
          //Set's up the buttons logic dynamically
          for (int i = 0; i < 9; i++) {
               JButton button = new JButton();
               btnArray[i] = button;
               btnMap.put(button, i);
               styleBtn(button);
               button.addActionListener(this);
               btnPanel.add(button);
          }
     }

     //Styles the buttons
     static void styleBtn(JButton btn) {
          btn.setSize(new Dimension(55, 55));
          btn.setFont(new Font("", Font.BOLD, 70));
          btn.setBackground(Color.WHITE);
          btn.setFocusable(false);
          btnPanel.add(btn);
     }

     //Changes the current player
     static void changePlayer() {
          currentPlayer = (currentPlayer == 'O') ? 'X' : 'O';
          
          if(currentPlayer == player.chr) {
               statusText.setText("Your turn");
          }
          else {
               statusText.setText("The computer is thinking..");
          }
     };

     //Disables all buttons
     static void disableBtns() {
          for(JButton btn : btnArray) {
               btn.setEnabled(false);
          }
     }

     //Enables all buttons
     static void enableBtns() {
          for(JButton btn : btnArray) {
               btn.setEnabled(true);
          }
     }

     //Checks for the winner
     static char checkWinner() {
          char winner = ' ';
     
          // Check all winning conditions
          for (int[] condition : winConditions) {
               char boxA = emptySpaces[condition[0]];
               char boxB = emptySpaces[condition[1]];
               char boxC = emptySpaces[condition[2]];
     
               // If all three boxes in the condition are the same and not empty, declare a winner
               if (boxA == boxB && boxB == boxC && boxA != ' ') {
                    btnArray[condition[0]].setBackground(Color.GREEN);
                    btnArray[condition[1]].setBackground(Color.GREEN);
                    btnArray[condition[2]].setBackground(Color.GREEN);

                    winner = boxA;
                    break;
               }
          }
     
          // If no winner and board is full, it's a draw
          if (winner == ' ' && isBoardFull()) {
               statusText.setText("It's a draw!");
               disableBtns(); // Disable all buttons
          }
     
          return winner;
     }
     
     // Helper method to check if the board is full
     static boolean isBoardFull() {
          for (char space : emptySpaces) {
               if (space == ' ') { // If any space is empty, return false
                    return false;
               }
          }
          return true; // All spaces are filled
     }
 
     //Executes computer move
     static void executeComMove() {
          //A timer task is used here to make it look like the computer is actually thinking

          TimerTask task = new TimerTask() {
               @Override
               public void run() {
                    computer.move(currentPlayer, emptySpaces, random, btnArray);

                    if(checkWinner() != ' ') {
                         disableBtns();

                         char winner = checkWinner();

                         if(winner == player.chr) {
                              statusText.setText("You won");
                         }
                         else {
                              statusText.setText("Computer won");
                         }

                         return;
                    }

                    changePlayer();
                    enableBtns();
               }
          };

          timer.schedule(task, 2000);
     }

     void restartGame() {
          //Sets the button text and space value to empty
          for(int i = 0; i < emptySpaces.length; i++) {
               emptySpaces[i] = ' ';
               btnArray[i].setText("");
               btnArray[i].setBackground(Color.WHITE);
          }

          enableBtns();

          //Initializes the game again
          initialize();
     }

     @Override
     public void actionPerformed(ActionEvent event) {
          //Gets the action source
          JButton source = (JButton) (event.getSource());

          if(source != restartBtn) {
               player.move(source, btnArray, currentPlayer, emptySpaces, btnMap);
               
               if(checkWinner() != ' ') {
                    disableBtns();

                    char winner = checkWinner();

                    if(winner == player.chr) {
                         statusText.setText("You won");
                    }
                    else {
                         statusText.setText("Computer won");
                    }

                    return;
               }

               changePlayer();

               executeComMove();
               disableBtns(); 
          }
          else {
               restartGame();
          }
     }
}