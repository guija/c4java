package com.github.guija.c4java;

import com.github.guija.c4java.builder.DotBuilder;
import com.github.guija.c4java.model.Container;
import com.github.guija.c4java.model.ExternalSystem;
import com.github.guija.c4java.model.Project;
import com.github.guija.c4java.model.Sys;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContainerViewTest {

  @Test
  public void oneSystemWithTwoContainersTest() {
    val project = new Project();
    val systemA = new Sys(project, "A", "A");
    val container1 = new Container(systemA, "1", "1");
    val container2 = new Container(systemA, "2", "2");
    container1.uses(container2, "TODO");
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemA);
    System.out.println(dot);
    dotBuilder.createFile("oneSystemWithTwoContainersTest.png");
    assertTrue(dot.contains("\"1\" -> \"2\""));
  }

  @Test
  public void twoSystemsWithContainersReferencingEachOther() {
    val project = new Project();
    val systemA = new Sys(project, "A", "A");
    val systemB = new Sys(project, "B", "B");
    val container1 = new Container(systemA, "1", "1");
    val container2 = new Container(systemA, "2", "2");
    val container3 = new Container(systemB, "3", "3");
    val container4 = new Container(systemB, "4", "4");
    container3.uses(container1, "TODO");
    container3.uses(container4, "TODO");
    container2.uses(container4, "TODO");
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemB);
    assertTrue(dot.contains("\"3\" -> \"A\""));
    assertTrue(dot.contains("\"3\" -> \"4\""));
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

  @Test
  public void containerUsingExternalSystemTest() {
    val project = new Project();
    val systemA = new Sys(project, "A", "A");
    val systemB = new ExternalSystem(project, "B", "B");
    val container = new Container(systemA, "Container", "Container");
    container.uses(systemB, "TODO");
    val dotBuilder = new DotBuilder(project);
    val dot = dotBuilder.generateContainerViewDot(systemA);
    dotBuilder.createFile("twoSystemsWithContainersReferencingEachOther.png");
  }

  @Test
  public void internalContainerIsUsedByExternalSystemTest() {
    val project = new Project();
    val systemInternal = new Sys(project, "InternalSystem", "description");
    val internalContainer = new Container(systemInternal, "InternalContainer", "description");
    val externalSystem = new ExternalSystem(project, "ExternalSystem", "description");
    externalSystem.uses(internalContainer, "usage");
    val dotBuilder = new DotBuilder(project);
    val containerViewDot = dotBuilder.generateContainerViewDot(systemInternal);
    assertTrue(containerViewDot.contains("\"ExternalSystem\" -> \"InternalContainer\""));
  }

  @Test
  public void twoInternalContainersAreCalledByTheSameExternalSystemTest() {
    // A internal container and an external system are using two other internal containers
    // There was a bug with self references of the internal containers which we want to avoid.
    val project = new Project();
    val systemInternal = new Sys(project, "InternalSystem", "description");
    val internalContainer = new Container(systemInternal, "InternalContainer", "description");
    val internalContainer2 = new Container(systemInternal, "InternalContainer2", "description");
    val internalContainer3 = new Container(systemInternal, "InternalContainer3", "description");
    val externalSystem = new ExternalSystem(project, "ExternalSystem", "description");
    externalSystem.uses(internalContainer, "usage1");
    externalSystem.uses(internalContainer2, "usage2");
    internalContainer3.uses(internalContainer, "usage31");
    internalContainer3.uses(internalContainer2, "usage32");
    val dotBuilder = new DotBuilder(project);
    val containerViewDot = dotBuilder.generateContainerViewDot(systemInternal);
    assertTrue(containerViewDot.contains("\"ExternalSystem\" -> \"InternalContainer\""));
  }

  public void internalContainerIsUsedByOtherContainerTest() {
    assertTrue("to be implemented", true);
  }

  public void internalSystemIsUsedByOtherContainerTest() {
    assertTrue("to be implemented", true);
  }

}
