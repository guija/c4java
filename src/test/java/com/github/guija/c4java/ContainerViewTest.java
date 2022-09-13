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
    val systemA = new Sys("A");
    val container1 = new Container("1");
    val container2 = new Container("2");
    systemA.add(container1, container2);
    container1.uses(container2);
    val project = new Project();
    project.getSystems().add(systemA);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(project, systemA);
    System.out.println(dot);
    dotBuilder.createFile("oneSystemWithTwoContainersTest.png");
    assertTrue(dot.contains("1 -> 2"));
  }

  @Test
  public void twoSystemsWithContainersReferencingEachOther() {
    val systemA = new Sys("A");
    val systemB = new Sys("B");
    val container1 = new Container("1");
    val container2 = new Container("2");
    val container3 = new Container("3");
    val container4 = new Container("4");
    systemA.add(container1, container2);
    systemB.add(container3, container4);
    container3.uses(container1);
    container3.uses(container4);
    container2.uses(container4);
    val project = new Project();
    project.getSystems().add(systemA);
    project.getSystems().add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(project, systemB);
    assertTrue(dot.contains("3 -> A"));
    assertTrue(dot.contains("3 -> 4"));
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

  @Test
  public void containerUsingExternalSystemTest() {
    val systemA = new Sys("A");
    val systemB = new ExternalSystem("B");
    val container = new Container("Container");
    systemA.add(container);
    container.uses(systemB);
    val project = new Project();
    project.getSystems().add(systemA);
    project.getSystems().add(systemB);
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(project, systemA);
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

}
