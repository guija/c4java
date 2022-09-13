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
    val systemA = new Sys("A");
    val systemB = new Sys("B");
    systemA.uses(systemB);
    val container1 = new Container("1");
    val container2 = new Container("2");
    val container3 = new Container("3");
    val container4 = new Container("4");
    systemA.add(container1);
    systemA.add(container2);
    systemB.add(container3);
    systemB.add(container4);
    val project = new Project();
    project.add(systemA);
    project.add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateSystemViewDot(project);
    assertTrue(dot.contains("A -> B"));
  }

  @Test
  public void containerUsesSystemTest() {
    val systemA = new Sys("A");
    val systemB = new Sys("B");
    val container1 = new Container("1");
    systemB.add(container1);
    systemA.uses(container1);
    val project = new Project();
    project.getSystems().add(systemA);
    project.getSystems().add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateSystemViewDot(project);
    assertTrue(dot.contains("A -> B"));
  }

}
