package com.github.guija.c4java.util;

import lombok.val;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TextUtil {

  public static String splitIntoMultipleLinesMaintainingWordBoundaries(String str, int maxLineLength, String lineSeparator) {
    String[] words = str.split(" ");
    ArrayList<String> lines = new ArrayList<>();
    ArrayList<String> wordsInCurrentLine = new ArrayList<>();
    for (val word : words) {
      val currentLine = String.join(" ", wordsInCurrentLine);
      val futureLineLength = currentLine.length() + word.length() + 1; // +1 because of one space
      if (futureLineLength > maxLineLength) {
        lines.add(String.join(" ", wordsInCurrentLine));
        wordsInCurrentLine = new ArrayList<>();
      }
      wordsInCurrentLine.add(word);
    }
    if (wordsInCurrentLine.size() > 0) {
      lines.add(String.join(" ", wordsInCurrentLine));
    }
    return lines.stream().filter(line -> line.length() > 0).collect(Collectors.joining(lineSeparator));
  }

}
