package com.github.guija.c4java.builder;

import com.github.guija.c4java.model.*;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

import java.io.File;
import java.net.URI;
import java.util.HashSet;

@RequiredArgsConstructor
public class DotBuilder {

  private final Project project;
  private HashSet<Component> alreadyRegisteredNodes = new HashSet<>();
  private String dot = "";

  @SneakyThrows
  public void createFile(String fileName) {
    val graphviz = Graphviz.fromString(dot);
    // graphviz.render(Format.PNG).toFile(new File(fileName));
    graphviz.render(Format.SVG).toFile(new File(fileName));
  }

  private void prependHeader() {
    this.dot =
      "digraph {\n"
        + "graph [pad=\"0.5\", nodesep=\"1\", ranksep=\"2\"];\n"
        + "splines=\"false\";\n"
        + dot;
  }

  private void appendFooter() {
    dot = dot + "}\n"; // end of digraph
  }

  private void addHeaderAndFooter() {
    prependHeader();
    appendFooter();
  }

  public String generateSystemViewDot(Project project) {
    val viewType = Component.Type.SYSTEM;
    for (val system : project.getSystems()) {
      registerNode(system);
      val childComponents = system.getAllChildComponentsWithRoot();
      for (val child : childComponents) {
        for (val usesRelation : child.getUsesRelations()) {
          val targetSystem = usesRelation.getUsed().getAncestorWithType(viewType);
          val comment = usesRelation.getComment();
          // Do not add edge if the system is the same. If you want to see edges within a system
          // the container view is the correct view, not the system view. Otherwise we would have many
          // edges from a system to itself.
          val isTargetSystemTheSameSystem = targetSystem == system;
          if (isTargetSystemTheSameSystem) {
            continue;
          }
          addEdge(system, targetSystem, comment, usesRelation.isAsync());
        }
      }
    }
    addHeaderAndFooter();
    printDotPreviewLink(dot);
    return dot;
  }

  public String generateContainerViewDot(Project project, Sys system) {
    for (val container : system.getComponents()) {
      registerNode(container);
      for (val usesRelations : container.getUsesRelations()) {
        // If the used component is from another system then we want to reference the other system, not the container of that system.
        val usedSystem = usesRelations.getUsed().getAncestorWithType(Component.Type.SYSTEM);
        val comment = usesRelations.getComment();
        if (usedSystem != system) {
          addEdge(container, usedSystem, comment, usesRelations.isAsync());
        } else {
          addEdge(container, usesRelations.getUsed(), comment, usesRelations.isAsync());
        }
      }
    }
    addHeaderAndFooter();
    printDotPreviewLink(dot);
    return dot;
  }

  private void addEdge(Component from, Component to, String comment, boolean isAsync) {
    registerNode(from);
    registerNode(to);
    val edgeStyle = getRelationStyle(isAsync);
    dot += String.format("\"%s\" -> \"%s\" [xlabel=\"%s\" style=%s fontname=Helvetica fontsize=11 color=\"#707070\"]\n", from.getId(), to.getId(), comment, edgeStyle);
  }

  private void registerNode(Component component) {
    if (!alreadyRegisteredNodes.contains(component)) {
      alreadyRegisteredNodes.add(component);
      val id = component.getId();
      val description = "This is a very very very<BR/>very long description";
      dot += String.format("\"%s\" [label=<<B>%s</B><BR/>[%s]<BR/><BR/>%s> shape=box fontname=Helvetica fontsize=12 margin=\"0.3,0.1\" fillcolor=\"#%s\" color=\"#%s\" fontcolor=white style=filled]\n",
        id,
        id,
        component.getTypeDescription(),
        description,
        getBackgroundColor(component),
        getBorderColor(component));
    }
  }

  private String getBorderColor(Component component) {
    if (component instanceof ExternalSystem) {
      return "8A8A8A";
    } else if (component instanceof Sys) {
      return "0F5EAA";
    } else {
      return "3C7FC0";
    }
  }

  private String getBackgroundColor(Component component) {
    if (component instanceof ExternalSystem) {
      return "999999";
    } else if (component instanceof Sys) {
      return "1168BD";
    } else {
      return "438DD5";
    }
  }

  private String getRelationStyle(boolean isAsync) {
    return isAsync ? "dashed" : "solid";
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
