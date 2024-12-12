package WordTracker;

import java.io.Serializable;
import java.util.*;

/**
 * WordInfo.java
 * 
 * A class that tracks information about a specific word, including its
 * occurrences across different files and line numbers. It implements the 
 * Comparable interface to allow sorting of WordInfo objects based on the
 * word itself. The class is Serializable, allowing instances of it to be
 * written to and read from storage.
 * 
 * The class contains:
 * - A word field to store the word being tracked.
 * - A map (fileLocations) to store the filenames as keys, with sets of line
 *   numbers as values, to track where the word appears in different files.
 * 
 * The class provides functionality for:
 * - Adding occurrences of the word in specific files and line numbers.
 * - Retrieving the word and its file locations.
 * - Comparing WordInfo objects by word for sorting.
 * - Counting the total occurrences of the word across all files.
 * 
 * @param <E> The type of elements held in the internal data structures.
 */
public class WordInfo implements Comparable<WordInfo>, Serializable {
    private static final long serialVersionUID = 1L;
    private String word;                             // The word being tracked
    private Map<String, Set<Integer>> fileLocations; // A map of filenames to line numbers where the word appears

    /**
     * Constructs a new WordInfo object for a given word.
     *
     * @param word The word to track.
     */
    public WordInfo(String word) {
        this.word = word;
        this.fileLocations = new HashMap<>();
    }

    /**
     * Adds a location (filename and line number) where the word occurs.
     *
     * @param filename The name of the file.
     * @param lineNumber The line number in the file where the word appears.
     */
    public void addLocation(String filename, int lineNumber) {
        fileLocations.computeIfAbsent(filename, k -> new TreeSet<>()).add(lineNumber);
    }

    /**
     * Gets the word being tracked.
     *
     * @return The word.
     */
    public String getWord() { 
        return word; 
    }

    /**
     * Gets the map of file locations where the word appears.
     *
     * @return A map of filenames to sets of line numbers.
     */
    public Map<String, Set<Integer>> getFileLocations() { 
        return fileLocations; 
    }

    /**
     * Compares this WordInfo object with another WordInfo object based on the word.
     * 
     * @param other The other WordInfo object to compare with.
     * @return A negative integer, zero, or a positive integer as this word is
     *         less than, equal to, or greater than the other word.
     */
    @Override
    public int compareTo(WordInfo other) {
        return this.word.compareTo(other.word);
    }

    /**
     * Calculates the total number of occurrences of the word across all files.
     * 
     * @return The total number of occurrences.
     */
    public int getOccurrences() {
        return fileLocations.values().stream()
            .mapToInt(Set::size)  // Count the occurrences in each file
            .sum();  // Sum up the counts for all files
    }
}
