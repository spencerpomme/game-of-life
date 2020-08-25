package life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.*;

public class Main {

}

class Universe {
    int N;  // side size
    long S; // seed
    int M;  // number of generations
    Random random;
    List<List<Boolean>> universe = new ArrayList<>();

    public Universe(int N, long S, int M) {
        this.N = N;
        this.S = S;
        this.M = M;
        this.initUniverse(this.S);
    }

    public Universe(int N, int M) {
        this.N = N;
        this.M = M;
        this.initUniverse();
    }

    private void initUniverse(long seed) {
        this.random = new Random(S);
        for (int i = 0; i < this.getN(); i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < this.getN(); j++) {
                row.add(this.random.nextBoolean());
            }
            this.universe.add(row);
        }
    }

    private void initUniverse() {
        for (int i = 0; i < this.getN(); i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < this.getN(); j++) {
                row.add(false);
            }
            this.universe.add(row);
        }
    }

    public void showUniverse() {
        for (var row : this.getUniverse()) {
            for (var state : row) {
                System.out.print(state? 'O': ' ');
            }
            System.out.println();
        }
    }

    public void generate() {
        Algorithm evolve = new Evolve();
        for (int i = 0; i < this.getM(); i++) {
            System.out.println("Generation #" + (i+1));

            this.universe = evolve.update(this).getUniverse();
            System.out.println("Alive: " + this.countAlive());
            this.showUniverse();
        }
    }

    public boolean isAlive(int i, int j) {
        return this.universe.get(i).get(j);
    }

    public int getN() {
        return N;
    }

    public long getS() {
        return S;
    }

    public int getM() {
        return M;
    }

    public List<List<Boolean>> getUniverse() {
        return universe;
    }

    public int countAlive() {
        int count = 0;
        for (int i = 0; i < this.getN(); i++) {
            for (int j = 0; j < this.getN(); j++) {
                if (this.getUniverse().get(i).get(j)) {
                    count++;
                }
            }
        }
        return count;
    }
}

interface Algorithm {
    Universe update(Universe before);
}


class Evolve implements Algorithm {

    @Override
    public Universe update(Universe before) {
        Universe after = new Universe(before.getN(), before.getM());
        int side = before.getN();
        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                int n = checkSurrounding(i, j, before.getUniverse());
                if (before.isAlive(i, j)) {
                    if (n < 2 || n > 3) {
                        // die boredom or overpopulation
                        after.getUniverse().get(i).set(j, false);
                    } else {
                        after.getUniverse().get(i).set(j, true);
                    }
                } else {
                    if (n == 3) {
                        // resurrect current cell
                        after.getUniverse().get(i).set(j, true);
                    } else {
                        after.getUniverse().get(i).set(j, false);
                    }
                }
            }
        }
        return after;
    }

    // count surrounding cells
    private int checkSurrounding(int i, int j, List<List<Boolean>> oldUniverse) {
        int neighbors = 0;
        int side = oldUniverse.size();
        // eight neighbors:
        // ui -> upper i | di -> down i | lj -> left j | rj -> right j
        int ui = i-1 < 0? side+i-1: i-1;
        int di = i+1 > side-1? i+1-side: i+1;
        int lj = j-1 < 0? side+j-1: j-1;
        int rj = j+1 > side-1? j+1-side: j+1;

        neighbors += oldUniverse.get(ui).get(lj) ? 1: 0;
        neighbors += oldUniverse.get(ui).get(j) ? 1: 0;
        neighbors += oldUniverse.get(ui).get(rj) ? 1: 0;
        neighbors += oldUniverse.get(i).get(lj) ? 1: 0;
        neighbors += oldUniverse.get(i).get(rj) ? 1: 0;
        neighbors += oldUniverse.get(di).get(lj) ? 1: 0;
        neighbors += oldUniverse.get(di).get(j) ? 1: 0;
        neighbors += oldUniverse.get(di).get(rj) ? 1: 0;
        // System.out.println(neighbors);
        return neighbors;
    }
}

