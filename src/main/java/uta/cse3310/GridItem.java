package uta.cse3310;

import java.util.ArrayList;

public class GridItem {
    public char letter;
    public transient int wordCount;
    public ArrayList<Integer> selectedBy = new ArrayList<Integer>();
    public ArrayList<Integer> foundBy = new ArrayList<Integer>();

    public GridItem(char letter) {
        this.letter = letter;
    }
}
