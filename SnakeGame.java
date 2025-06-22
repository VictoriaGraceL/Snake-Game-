// Import necessary classes from the AWT (Abstract Window Toolkit) and Swing libraries for GUI and event handling
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// Main class representing the Snake Game, extends JPanel to display the game on the screen
public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    // Tile class to represent each part of the snake and food in the grid (x, y coordinates)
    private static class Tile {
        int x;  // x-coordinate of the tile
        int y;  // y-coordinate of the tile

        Tile(int x, int y) {  // Constructor to initialize a tile at (x, y)
            this.x = x;
            this.y = y;
        }
    }

    // Declare the dimensions of the board and tile size
    int boardWidth;  // Width of the game board
    int boardHeight;  // Height of the game board
    int tileSize = 25;  // Size of each tile (square cell in the grid)

    // Declare variables for the snake
    Tile snakeHead;  // Represents the snake's head position
    ArrayList<Tile> snakeBody;  // List of body parts of the snake

    // Declare variables for the food
    Tile food;  // Represents the food's position on the grid
    Random random;  // Random generator to place food at random locations

    // Game logic variables
    int velocityX;  // Snake's velocity in the x-direction (1 for right, -1 for left, 0 for no movement)
    int velocityY;  // Snake's velocity in the y-direction (1 for down, -1 for up, 0 for no movement)
    Timer gameLoop;  // Timer to control the game loop, triggering movement and screen updates

    boolean gameOver = false;  // Flag to track if the game is over

    // Constructor to initialize the game
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Set the preferred size of the panel and background color
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);

        // Add key listener for player input (arrow keys)
        addKeyListener(this);
        setFocusable(true);  // Allow the panel to receive keyboard focus

        // Initialize the snake's starting position
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();  // Initialize the snake body as an empty list

        // Initialize the food at a random position
        food = new Tile(10, 10);
        random = new Random();
        placeFood();  // Place the first food on the board

        // Set initial snake velocity (moving right)
        velocityX = 1;
        velocityY = 0;

        // Create a timer for the game loop, setting the interval to 100ms (game updates every 100ms)
        gameLoop = new Timer(100, this);
        gameLoop.start();  // Start the game loop
    }

    // Override the paintComponent method to draw the game elements (snake, food, grid)
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);  // Call the draw method to render the game
    }

    // Method to handle drawing the game elements
    public void draw(Graphics g) {
        // Draw grid lines for the game board
        for (int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);  // Vertical lines
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);  // Horizontal lines
        }

        // Draw the food on the board (red square)
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);  // 3D effect for food

        // Draw the snake's head (green square)
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);  // 3D effect for head

        // Draw each part of the snake's body (green squares)
        for (Tile snakePart : snakeBody) {
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);  // 3D effect for body parts
        }

        // Display the score (the length of the snake body)
        g.setFont(new Font("Arial", Font.PLAIN, 16));  // Set font for the score text
        if (gameOver) {
            g.setColor(Color.red);  // Set color to red if the game is over
            g.drawString("Game Over: " + snakeBody.size(), tileSize - 16, tileSize);  // Display game over message
        } else {
            g.drawString("Score: " + snakeBody.size(), tileSize - 16, tileSize);  // Display current score
        }
    }

    // Method to place food at a random position on the board
    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);  // Random x-coordinate
        food.y = random.nextInt(boardHeight / tileSize);  // Random y-coordinate
    }

    // Method to handle snake movement and collision logic
    public void move() {
        // Check if the snake eats the food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));  // Add a new body part to the snake
            placeFood();  // Place a new food item at a random position
        }

        // Move the snake's body (move each part towards the previous part)
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {  // For the first body part (the one right before the head)
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {  // For the rest of the body
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        // Move the snake's head in the current direction
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Check for collisions with the snake's body (game over)
        for (Tile snakePart : snakeBody) {
            if (collision(snakeHead, snakePart)) {
                gameOver = true;  // End the game if the head collides with the body
            }
        }

        // Check if the snake goes out of bounds (game over)
        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;  // End the game if the head moves out of bounds
        }
    }

    // Method to check if two tiles are colliding (i.e., they occupy the same position)
    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;  // Check if both x and y coordinates match
    }

    // Action performed method is called every x milliseconds by the gameLoop timer
    @Override
    public void actionPerformed(ActionEvent e) {
        move();  // Move the snake
        repaint();  // Redraw the game screen
        if (gameOver) {
            gameLoop.stop();  // Stop the game loop if the game is over
        }
    }

    // Method to handle key presses for controlling the snake
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;  // Move up
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;  // Move down
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;  // Move left
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;  // Move right
        }
    }

    // These methods are not used but need to be implemented as part of the KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
