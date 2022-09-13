package com.github.guija.c4java.builder;

import com.github.guija.c4java.model.Component;
import com.github.guija.c4java.model.Project;
import com.github.guija.c4java.model.Sys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

import java.net.URI;

@RequiredArgsConstructor
public class DotBuilder {

  private String prependHeader(Project project, String dot) {
    return "digraph {\n" + dot;
  }

  private String appendFooter(Project project, String dot) {
    return dot + "}\n";
  }

  private String addHeaderAndFooter(Project project, String dot) {
    return prependHeader(project, appendFooter(project, dot));
  }

  public String generateSystemViewDot(Project project) {
    val viewType = Component.Type.SYSTEM;
    String dot = "";
    for (val system : project.getSystems()) {
      for (val used : system.getUses()) {
        val targetSystem = used.getAncestorWithType(viewType);
        dot = addEdge(dot, system, targetSystem);
      }
    }
    dot = addHeaderAndFooter(project, dot);
    printDotPreviewLink(dot);
    return dot;
  }

  public String generateContainerViewDot(Project project, Sys system) {
    // Gehe ueber System und Container und finde alle anderen Systeme oder container die diese Systeme / Container verwenden.
    String dot = "";
    for (val container : system.getComponents()) {
      for (val used : container.getUses()) {
        // If the used component is from another system then we want to reference the other system, not the container of that system.
        val usedSystem = used.getAncestorWithType(Component.Type.SYSTEM);
        if (usedSystem != system) {
          dot = addEdge(dot, container, usedSystem);
        } else {
          dot = addEdge(dot, container, used);
        }
      }
    }
    dot = addHeaderAndFooter(project, dot);
    printDotPreviewLink(dot);
    return dot;
  }

  private String addEdge(String dot, Component from, Component to) {
    String newDot = new String(dot);
    newDot += String.format("%s -> %s\n", from.getId(), to.getId());
    return newDot;
  }

  @SneakyThrows
  public void printDotPreviewLink(String input) {
    var uri = new URI(
      "https",
      "dreampuf.github.io",
      "/GraphvizOnline/#" + input,
      null);
    System.out.println(uri.toURL().toString().replace("/%23", "#"));
  }

}
