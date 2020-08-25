package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GameOfLife extends JFrame {
    private final JLabel generationLabel = new JLabel("0");
    private final JLabel aliveLabel = new JLabel("0");
    private final JLabel testLabel = new JLabel("test");
    private boolean play = true;
    private boolean over = false;

    private final ArrayList<JPanel> squares = new ArrayList<>();
    private int size;

    public void setGeneration(int value) {
        generationLabel.setText(String.valueOf(value));
    }

    public void setAlive(int value) {
        aliveLabel.setText(String.valueOf(value));
    }

    public void setSize(int s) {
        size = s;
    }

    public boolean isPlay() {
        return play;
    }

    public boolean isOver() {
        return over;
    }

    public static void main(String[] args) {

        int side = 50;
        int seed = 0;
        int gen = 500;

        GameOfLife view = new GameOfLife();
        view.setSize(side);
        Universe world = new Universe(side, seed, gen);
        Algorithm evolve = new Evolve();

        for (int i = 0; i < world.getM(); i++) {
            System.out.println("Generation #" + (i+1));
            view.setGeneration(i+1);
            view.setAlive(world.countAlive());

            if (view.isPlay()) {
                world.universe = evolve.update(world).getUniverse();
                view.refresh(world.universe);
            } else {
                i = i-1;
                continue;
            }
            if (view.isOver()) {
                i = 0;
                continue;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void play() {
        this.over = false;
        this.play = true;
    }

    public void pause() {
        this.play = false;
    }

    public void reset() {
        this.play = false;
        this.over = true;
    }

    public GameOfLife() {

        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int width = 1250;
        int height = 990;
        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(null);
        setVisible(true);
        setResizable(false);

        JPanel captionPanel = new JPanel();
        captionPanel.setBounds(30,50,250, 100);
        captionPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        add(captionPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setBounds(40, 180, 200, 100);
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
        add(controlPanel);

        generationLabel.setName("GenerationLabel");
        aliveLabel.setName("AliveLabel");
        testLabel.setName("test");
        captionPanel.add(new JLabel("Generation #"));
        captionPanel.add(generationLabel);
        captionPanel.add(new JLabel("Alive: "));
        captionPanel.add(aliveLabel);
        captionPanel.add(testLabel);

        JToggleButton playToggleButton = new JToggleButton("Play/Pause");
        JButton resetButton = new JButton("Reset");
        playToggleButton.setName("PlayToggleButton");
        resetButton.setName("ResetButton");

        playToggleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (play) {
                    testLabel.setText("play");
                    play();
                } else {
                    testLabel.setText("pause");
                    pause();
                }

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        controlPanel.add(playToggleButton);
        controlPanel.add(resetButton);

        LeftPaneBox p = new LeftPaneBox();
        DrawGrid panel = new DrawGrid();

        add(p);
        add(panel);

    }

    public class DrawGrid extends JPanel {
        private final int w = 900;
        int h = 900;
        int p = 20;
        public DrawGrid() {
            setBounds(300 + p, p, w, h);
        }
        public void paintComponent(Graphics g) {
            // size is the number of cells in one dimension
            for (int i = 0; i <= size; i++) {
                int x = (w-1) * i / size;
                g.drawLine(x, 0, x, h);
            }
            for (int i = 0; i <= size; i++) {
                int y = (h-1) * i / size;
                g.drawLine(0, y, w, y);
            }
        }
    }

    public class LeftPaneBox extends JPanel {
        private final int w = 100;
        int h = 900;
        int p = 20;
        public LeftPaneBox() {
            setBounds(0, 0, 300, h+p);
        }
        public void paintComponent(Graphics g) {
            g.drawRect(p,p,270,899);
        }
    }

    public void refresh(List<List<Boolean>> f) {
        for (JPanel jPanel: squares) {
            remove(jPanel);
        }
        squares.clear();
        for (int i = 0; i < f.size(); i++) {
            for (int j = 0; j < f.get(i).size(); j++) {
                if (f.get(i).get(j)) {
                    JPanel square = new Square(j, i);
                    add(square);
                    squares.add(square);
                }
            }
        }
        repaint();
    }

    private class Square extends JPanel {

        int ws = (int) Math.ceil((double) 900 / size);
        int hs = (int) Math.ceil((double) 900 / size);
        public Square(int i, int j) {
            int x = 900 * i / size;
            int y = 900 * j / size;
            setBounds(319 + x, 20 + y, ws, hs);
        }
        @Override
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLACK);
            g2.fillRect(0,0, ws, ws);
        }
    }
}
