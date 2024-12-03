package WordTracker;

import java.io.Serializable;
import java.util.*;

public class WordInfo implements Comparable<WordInfo>, Serializable {
    private static final long serialVersionUID = 1L;
    private String word;
    private Map<String, Set<Integer>> fileLocations;

    public WordInfo(String word) {
        this.word = word;
        this.fileLocations = new HashMap<>();
    }

    public void addLocation(String filename, int lineNumber) {
        fileLocations.computeIfAbsent(filename, k -> new TreeSet<>()).add(lineNumber);
    }

    public String getWord() { return word; }
    public Map<String, Set<Integer>> getFileLocations() { return fileLocations; }
    
    @Override
    public int compareTo(WordInfo other) {
        return this.word.compareTo(other.word);
    }

    public int getOccurrences() {
        return fileLocations.values().stream()
            .mapToInt(Set::size)
            .sum();
    }
}