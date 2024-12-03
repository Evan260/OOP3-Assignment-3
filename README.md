# WordTracker

A Java program that tracks word occurrences across multiple text files using a Binary Search Tree implementation.

## Features

- Tracks unique words and their locations in files
- Persists data between runs using serialization
- Generates three types of reports:
  - Words and files (-pf)
  - Words, files, and line numbers (-pl)
  - Words, files, line numbers, and occurrence count (-po)

## Requirements

- Java 8+
- Command line interface

## Usage

```bash
java -jar WordTracker.jar <input.txt> [-pf|-pl|-po] [-f <output.txt>]
```

### Options

- `<input.txt>`: Input file to process
- `-pf`: List files containing each word
- `-pl`: Show line numbers for each word occurrence
- `-po`: Show occurrence count and line numbers
- `-f <output.txt>`: Optional output to file

### Examples

```bash
# Print files and lines for words in example1.txt
java -jar WordTracker.jar example1.txt -pl

# Print word frequencies with output to results.txt
java -jar WordTracker.jar example1.txt -po -f results.txt
```
