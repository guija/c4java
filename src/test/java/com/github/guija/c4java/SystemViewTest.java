package com.github.guija.c4java;

import com.github.guija.c4java.builder.DotBuilder;
import com.github.guija.c4java.model.Project;
import com.github.guija.c4java.model.Sys;
import com.github.guija.c4java.model.Container;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SystemViewTest {

  @Test
  public void systemUsesSystemTest() {
    val project = new Project();
    val systemA = new Sys(project, "A", "A");
    val systemB = new Sys(project, "B", "B");
    systemA.uses(systemB, "TODO");
    val container1 = new Container(systemA, "1", "1");
    val container2 = new Container(systemA, "2", "2");
    val container3 = new Container(systemB, "3", "3");
    val container4 = new Container(systemB, "4", "4");
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateSystemViewDot();
    assertTrue(dot.contains("\"A\" -> \"B\""));
  }

  @Test
  public void containerUsesSystemTest() {
    val project = new Project();
    val systemA = new Sys(project, "A", "A");
    val systemB = new Sys(project, "B", "B");
    val container1 = new Container(systemB, "1", "1");
    systemA.uses(container1, "TODO");
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateSystemViewDot();
    assertTrue(dot.contains("\"A\" -> \"B\""));
  }

}
