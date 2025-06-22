// Import the necessary Swing classes for creating a GUI (Graphical User Interface)
import javax.swing.*;

// Main class of the application
public class App {
    // Entry point of the program
    public static void main(String[] args) {

        // Define the width of the game board (the window size)
        int boardWidth = 600;

        // Set the height of the game board to be the same as the width (making a square window)
        int boardHeight = boardWidth;

        // Create a new JFrame (window) for the Snake game with the title "Snake"
        JFrame frame = getjFrame(boardWidth, boardHeight);

        // Create an instance of the SnakeGame class, passing the board width and height
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);

        // Add the SnakeGame panel to the JFrame, so it is displayed inside the window
        frame.add(snakeGame);

        // Pack the frame to make it fit the preferred size of its components (snake game)
        frame.pack();

        // Request focus for the snake game panel, so it can receive key input for controlling the snake
        snakeGame.requestFocus();
    }

    private static JFrame getjFrame(int boardWidth, int boardHeight) {
        JFrame frame = new JFrame("Snake");

        // Make the frame visible (so the window appears on the screen)
        frame.setVisible(true);

        // Set the size of the window using the width and height defined above
        frame.setSize(boardWidth, boardHeight);

        // Center the window on the screen when the game starts
        frame.setLocationRelativeTo(null);

        // Prevent the window from being resized
        frame.setResizable(false);

        // Define the default close operation: exit the program when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
}
