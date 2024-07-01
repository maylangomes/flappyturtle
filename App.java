import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 543;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Turtle");
		frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlappyTurtle flappyTurtle = new FlappyTurtle();
        frame.add(flappyTurtle);
        frame.pack();
        flappyTurtle.requestFocus();
        frame.setVisible(true);
    }
}
