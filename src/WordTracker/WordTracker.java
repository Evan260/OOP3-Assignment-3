package WordTracker;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser";
    private BSTree<WordInfo> wordTree;

    public WordTracker() {
        loadRepository();
    }

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

    private void saveRepository() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
        } catch (IOException e) {
            System.err.println("Error saving repository: " + e.getMessage());
        }
    }

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

    public void generateReport(String reportType, String outputFile) {
        final PrintStream output;
        try {
            output = outputFile != null ? new PrintStream(new FileOutputStream(outputFile)) : System.out;

            Iterator<WordInfo> iterator = wordTree.inorderIterator();
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
                output.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error creating output file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f <output.txt>]");
            return;
        }

        WordTracker tracker = new WordTracker();
        tracker.processFile(args[0]);

        String reportType = args[1];
        String outputFile = args.length > 3 && args[2].equals("-f") ? args[3] : null;
        
        tracker.generateReport(reportType, outputFile);
    }
}