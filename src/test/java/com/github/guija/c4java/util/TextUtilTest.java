package com.github.guija.c4java.util;

import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextUtilTest {

  @Test
  public void splitIntoMultipleLinesMaintainingWordBoundariesTest() {
    val text = "a b c";
    val splitText = TextUtil.splitIntoMultipleLinesMaintainingWordBoundaries(text, 2, "\n");
    assertEquals("a\nb\nc", splitText);
  }

  @Test
  public void splitIntoMultipleLinesMaintainingWordBoundariesTestOneLongWordTest() {
    val text = "abc";
    val splitText = TextUtil.splitIntoMultipleLinesMaintainingWordBoundaries(text, 2, "\n");
    assertEquals("abc", splitText);
  }

}
