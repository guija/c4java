package com.github.guija.c4java.builder;

import com.github.guija.c4java.model.*;
import com.github.guija.c4java.util.TextUtil;
import guru.nidi.graphviz.engine.Engine;
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

  private static final String LINE_SEPARATOR = "<BR/>";
  private static final int MAX_LINE_LENGTH = 30;

  private final Project project;
  private HashSet<Component> alreadyRegisteredNodes = new HashSet<>();
  private String dot = "";

  @SneakyThrows
  public void createFile(String fileName) {
    val engine = Engine.DOT;
    val graphviz = Graphviz.fromString(dot).engine(engine);
    val renderer = graphviz.render(Format.SVG);
    // graphviz.render(Format.PNG).toFile(new File(fileName));
    renderer.toFile(new File(fileName));
  }

  private void prependHeader() {
    this.dot =
      "digraph {\n"
        + "graph [pad=\"0.5\", nodesep=\"2\", ranksep=\"2\"];\n"
        + "splines=\"true\";\n"
        // + "mindist=1"
        + "splines=ortho;\n"
        + dot;
  }

  private void appendFooter() {
    dot = dot + "}\n"; // end of digraph
  }

  private void addHeaderAndFooter() {
    prependHeader();
    appendFooter();
  }

  public String generateSystemViewDot() {

    val viewType = Component.Type.SYSTEM;

    for (val system : project.getSystems()) {

      registerNode(system);

      val childComponents = system.getAllChildComponentsWithRoot();
      for (val child : childComponents) {
        for (val usesRelation : child.getUsedAndUses()) {
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

  public String generateContainerViewDot(Sys system) {
    for (val container : system.getComponents()) {

      registerNode(container);

      for (val usesRelations : container.getUsedAndUses()) {
        // If the used component is from another system then we want to reference the other system, not the container of that system.
        val user = usesRelations.getUser();
        val used = usesRelations.getUsed();
        val userSystem = user.getAncestorWithType(Component.Type.SYSTEM);
        val usedSystem = used.getAncestorWithType(Component.Type.SYSTEM);
        val isSameSystem = userSystem == usedSystem;
        val comment = usesRelations.getComment();
        if (isSameSystem) {
          addEdge(container, usesRelations.getUsed(), comment, usesRelations.isAsync());
        } else {
          // We want to normalize the systems to that if there is an edge to an external component or from an external component
          // into this system we always want to show the system not the container for that system.
          val effectiveUser = userSystem != system ? userSystem : user;
          val effectiveUsed = usedSystem != system ? usedSystem : used;
          addEdge(effectiveUser, effectiveUsed, comment, usesRelations.isAsync());
        }
      }

    }
    addHeaderAndFooter();
    printDotPreviewLink(dot);
    return dot;
  }

  private void addEdge(Component from, Component to, String comment, boolean isAsync) {
    // Avoid self references
    if (from == to) {
      return;
    }
    registerNode(from);
    registerNode(to);
    val edgeStyle = getRelationStyle(isAsync);
    val commentFormatted = TextUtil.splitIntoMultipleLinesMaintainingWordBoundaries(comment, MAX_LINE_LENGTH, "\n");
    dot += String.format("\"%s\" -> \"%s\" [xlabel=\"%s\" style=%s fontname=Helvetica fontsize=11 color=\"#707070\"]\n", from.getId(), to.getId(), commentFormatted, edgeStyle);
  }

  private void registerNode(Component component) {
    if (!alreadyRegisteredNodes.contains(component)) {
      alreadyRegisteredNodes.add(component);
      val id = component.getId();
      val shape = getShape(component);
      val description = TextUtil.splitIntoMultipleLinesMaintainingWordBoundaries(component.getDescription(), MAX_LINE_LENGTH, LINE_SEPARATOR);
      val href = component instanceof Sys && !((Sys)component).isExternal() ? "href=\"system_"+id+".svg\"" : "";
      dot += String.format("\"%s\" [label=<<B>%s</B><BR/>[%s]<BR/><BR/>%s> shape=%s fontname=Helvetica fontsize=12 margin=\"0.3,0.1\" fillcolor=\"#%s\" color=\"#%s\" fontcolor=white style=filled %s]\n",
        id,
        component.getName(),
        component.getTypeDescription(),
        description,
        shape,
        getBackgroundColor(component),
        getBorderColor(component),
        href);
    }
  }

  private String getShape(Component component) {
    if (component instanceof Database) {
      return "cylinder";
    } else if (component instanceof Queue) {
      return "cds";
    } else {
      return "box";
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
