import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class FlappyTurtle extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 543;
    int boardHeight = 640;

    Image backgroundImg;
    Image turtleImg;
    Image topPipeImg;
    Image bottomPipeImg;

    int turtleX = boardWidth / 8;
    int turtleY = boardWidth / 2;
    int turtleWidth = 34;
    int turtleHeight = 24;

    class Turtle {
        int x = turtleX;
        int y = turtleY;
        int width = turtleWidth;
        int height = turtleHeight;
        Image img;

        Turtle(Image img) {
            this.img = img;
        }
    }

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    Turtle turtle;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;

    FlappyTurtle() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("./ocean.png")).getImage();
        turtleImg = new ImageIcon(getClass().getResource("./turtle.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        turtle = new Turtle(turtleImg);
        pipes = new ArrayList<>();

        placePipeTimer = new Timer(1500, e -> placePipes());
        placePipeTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        g.drawImage(turtleImg, turtle.x, turtle.y, turtle.width, turtle.height, null);

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over : " + (int) score, 10, 35);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Jump -> Space", 10, 70);
            g.drawString("Super Jump -> Z", 10, 100);
            g.drawString("Back -> Q", 10, 130);
            g.drawString("Forward -> D", 10, 160);
            g.drawString("Restart -> Space", 10, 190);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        velocityY += gravity;
        turtle.y += velocityY;
        turtle.y = Math.max(turtle.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && turtle.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(turtle, pipe)) {
                gameOver = true;
            }
        }

        if (turtle.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Turtle a, Pipe b) {
        return a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                turtle.y = turtleY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            turtle.x = Math.max(0, turtle.x - 10);
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            turtle.x = Math.min(boardWidth - turtle.width, turtle.x + 10);
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            velocityY = -18;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
