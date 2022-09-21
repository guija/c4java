package com.github.guija.c4java;

import com.github.guija.c4java.builder.DotBuilder;
import com.github.guija.c4java.model.Container;
import com.github.guija.c4java.model.ExternalSystem;
import com.github.guija.c4java.model.Project;
import com.github.guija.c4java.model.Sys;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ContainerViewTest {

  @Test
  public void oneSystemWithTwoContainersTest() {
    val systemA = new Sys("A", "A");
    val container1 = new Container(systemA, "1", "1");
    val container2 = new Container(systemA, "2", "2");
    systemA.add(container1, container2);
    container1.uses(container2, "TODO");
    val project = new Project();
    project.getSystems().add(systemA);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemA);
    System.out.println(dot);
    dotBuilder.createFile("oneSystemWithTwoContainersTest.png");
    assertTrue(dot.contains("1 -> 2"));
  }

  @Test
  public void twoSystemsWithContainersReferencingEachOther() {
    val systemA = new Sys("A", "A");
    val systemB = new Sys("B", "B");
    val container1 = new Container(systemA, "1", "1");
    val container2 = new Container(systemA, "2", "2");
    val container3 = new Container(systemB, "3", "3");
    val container4 = new Container(systemB, "4", "4");
    container3.uses(container1, "TODO");
    container3.uses(container4, "TODO");
    container2.uses(container4, "TODO");
    val project = new Project();
    project.getSystems().add(systemA);
    project.getSystems().add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemB);
    assertTrue(dot.contains("3 -> A"));
    assertTrue(dot.contains("3 -> 4"));
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

  @Test
  public void containerUsingExternalSystemTest() {
    val systemA = new Sys("A", "A");
    val systemB = new ExternalSystem("B", "B");
    val container = new Container(systemA, "Container", "Container");
    container.uses(systemB, "TODO");
    val project = new Project();
    project.getSystems().add(systemA);
    project.getSystems().add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemA);
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

}
