package training.MarkovChain;

import analysis.harmonic.ChordDegree;
import training.tools.Pair;

import java.util.HashMap;
import java.util.Random;

public class MarkovDegree {

    // Using two ChordDegree parameter instead of a Pair one because
    // Values cannot be changed in a Javafx pair ...
    // https://docs.oracle.com/javafx/2/api/javafx/util/Pair.html
    private ChordDegree depth2_;
    private ChordDegree depth1_;
    private HashMap<String, HashMap<ChordDegree, Double>> transitions_; // The string is Pair.toString();

    /**
     * Constructor for the MarkovDegree class.
     * Takes no parameter so transition matrix should be
     */
    public MarkovDegree() {
        depth1_ = null;
        depth2_ = null;
        transitions_ = new HashMap<>();
    }

    // ---------- PUBLIC FUNCTIONS ----------

    /**
     * This function add a degree to fill the markov transition matrix.
     * @param chordDegree
     */
    public void addDegree(ChordDegree chordDegree) {
        Pair<ChordDegree, ChordDegree> line = new Pair<>(depth2_, depth1_);
        addLineEntry(line, chordDegree);
        updateDepth(chordDegree);
    }

    /**
     * This function is called once every degree of a genre has been added into the matrix.
     * Then each line is divided by the sum of every value of the line. Creating the transition
     * probability matrix which lines equal 1.
     */
    public void closeLearning() {
        for(String key : transitions_.keySet()) {
            HashMap<ChordDegree, Double> column = transitions_.get(key);
            double sum = 0;
            for (ChordDegree key2 : column.keySet())
                sum += column.get(key2);
            for (ChordDegree key2 : column.keySet())
                column.put(key2, column.get(key2) / sum);
        }
    }

    /**
     * This functions generate a degree based on the transition matrix using
     * two ChordDegree as input. It generate a random number between 0 and 1
     * and use it two choose the according degree among the ones from the corresponding column.
     * @param depth1
     * @param depth2
     * @return ChordDegree.
     */
    public ChordDegree getDegree(ChordDegree depth1, ChordDegree depth2) {
        Random generator = new Random();
        double getDegree =  generator.nextDouble(); // Generate double between 0 and 1.
        Pair<ChordDegree, ChordDegree> key = new Pair<>(depth1, depth2);
        HashMap<ChordDegree, Double> column = transitions_.get(key);
        double sum = 0;
        for (ChordDegree key2 : column.keySet()) {
            if (column.get(key2) + sum > getDegree)
                return key2;
            else
                sum += column.get(key2);
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("               ");
        for (String key1 : transitions_.keySet()) {
            sb.append("          ");
            HashMap<ChordDegree, Double> columns = transitions_.get(key1);
            for (ChordDegree key2 : columns.keySet()) {
                sb.append(key2.toString()).append("     ");
            }
            sb.append("   Number = " + columns.size()).append("\n");
            sb.append(key1.toString()).append("        ");
            for (ChordDegree key2 : columns.keySet()) {
                sb.append(columns.get(key2)).append("         ");
            }
            sb.append("\n").append("               ");
        }
        return sb.toString();
    }

    // --------- PRIVATE FUNCTIONS ----------

    /**
     * This function add a new column in the Markov Transition Matrix when the given
     * ChordDegree doesn't match a key of the column. Otherwise, it increment the integer
     * value of the existing key.
     * @param column
     * @param key
     */
    private void addColumnEntry(HashMap<ChordDegree, Double> column, ChordDegree key) {
        if (column.get(key) == null)
            column.put(key, 1.0);
        else
            column.put(key, column.get(key) + 1.0);
    }

    /**
     * This function add a new line in the Markov Transition matrix when
     * the combinaison of values of depth1_ and depth2_ is new.
     * @param lineKey
     * @param columnKey
     */
    private void addLineEntry(Pair<ChordDegree, ChordDegree> lineKey, ChordDegree columnKey) {
        HashMap<ChordDegree, Double> column = transitions_.get(lineKey.toString());
        if (column == null) {
            column = new HashMap<>();
            addColumnEntry(column, columnKey);
            transitions_.put(lineKey.toString(), column);
        }
        else
            addColumnEntry(column, columnKey);
    }

    /**
     * This function update the depth the transition matrix by replacing
     * the depth2 value by the depth1 and the depth1 value by the newly added
     * ChordDegree.
     * @param chordDegree
     */
    private void updateDepth(ChordDegree chordDegree) {
        depth2_ = depth1_;
        depth1_ = chordDegree;
    }

}
