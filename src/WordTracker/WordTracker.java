package WordTracker;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * WordTracker.java
 * 
 * A class responsible for processing text files, tracking word occurrences, 
 * and generating reports based on specific criteria. The class uses a Binary
 * Search Tree (BSTree) to store and manage WordInfo objects, which track
 * the occurrences of words in different files and line numbers.
 * 
 * The class provides functionalities for:
 * - Loading and saving word occurrences to/from a repository file.
 * - Processing files to extract words and their locations.
 * - Generating reports on word occurrences, either by file, by line, or by occurrence count.
 * 
 * The class relies on the following data structures:
 * - A Binary Search Tree (BSTree) to store WordInfo objects.
 * - WordInfo objects that encapsulate information about each word's occurrences.
 * 
 * @param <E> The type of elements held in the internal data structures.
 */
public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser"; 
    private BSTree<WordInfo> wordTree;  

    /**
     * Constructs a WordTracker instance, loading the word occurrences repository
     * if it exists.
     */
    public WordTracker() {
        loadRepository();
    }

    /**
     * Loads the word occurrences from a serialized repository file, if it exists.
     * If loading fails or the file does not exist, a new BSTree is created.
     */
    private void loadRepository() {
        if (Files.exists(Paths.get(REPOSITORY_FILE))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REPOSITORY_FILE))) {
                wordTree = (BSTree<WordInfo>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading repository: " + e.getMessage());
                wordTree = new BSTree<>();
            }
        } else {
            wordTree = new BSTree<>();
        }
    }

    /**
     * Saves the current word occurrences to a serialized repository file.
     */
    private void saveRepository() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
        } catch (IOException e) {
            System.err.println("Error saving repository: " + e.getMessage());
        }
    }

    /**
     * Processes a given file, extracting words and storing their occurrences
     * (filename and line number) in the wordTree.
     *
     * @param filename The name of the file to process.
     */
    public void processFile(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            Pattern wordPattern = Pattern.compile("[^a-zA-Z]+");  

            for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String[] words = wordPattern.split(lines.get(lineNum).toLowerCase());
                for (String word : words) {
                    if (!word.isEmpty()) {
                        WordInfo wordInfo = new WordInfo(word);
                        BSTreeNode<WordInfo> existingNode = wordTree.search(wordInfo);
                        
                        if (existingNode == null) {
                            wordTree.add(wordInfo);  
                            existingNode = wordTree.search(wordInfo);
                        }
                        
                        existingNode.getElement().addLocation(filename, lineNum + 1);  
                    }
                }
            }
            saveRepository();  
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    /**
     * Generates a report based on the specified report type, and writes it to
     * an output file or prints it to the console.
     *
     * @param reportType The type of report to generate (-pf, -pl, or -po).
     * @param outputFile The file to write the report to (or null for console output).
     */
    public void generateReport(String reportType, String outputFile) {
        final PrintStream output;
        try {
            output = outputFile != null ? new PrintStream(new FileOutputStream(outputFile)) : System.out;

            Iterator<WordInfo> iterator = wordTree.inorderIterator();  // In-order iteration of wordTree
            while (iterator.hasNext()) {
                WordInfo info = iterator.next();
                switch (reportType) {
                    case "-pf":
                        output.printf("Word: %s, Files: %s%n", 
                            info.getWord(), 
                            String.join(", ", info.getFileLocations().keySet()));
                        break;
                    case "-pl":
                        output.printf("Word: %s%n", info.getWord());
                        info.getFileLocations().forEach((file, lines) -> 
                            output.printf("  File: %s, Lines: %s%n", 
                                file, lines.toString()));
                        break;
                    case "-po":
                        output.printf("Word: %s, Occurrences: %d%n", 
                            info.getWord(), 
                            info.getOccurrences());
                        info.getFileLocations().forEach((file, lines) -> 
                            output.printf("  File: %s, Lines: %s%n", 
                                file, lines.toString()));
                        break;
                }
            }
            
            if (output != System.out) {
                output.close();  // Close the output file if it was used
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error creating output file: " + e.getMessage());
        }
    }

    /**
     * The main method that processes the command-line arguments and invokes
     * the corresponding methods to process files and generate reports.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        WordTracker tracker = new WordTracker();
        tracker.processFile(args[0]); 

        String reportType = args[1];  
        String outputFile = args.length > 3 && args[2].equals("-f") ? args[3] : null;  // Get output file if specified
        
        tracker.generateReport(reportType, outputFile); 
    }
}
