package minispark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import minispark.Common.DependencyType;

/**
 * Created by lzb on 4/8/17.
 */

class Node {
  public Rdd rdd;
  public boolean processed;

  Node(Rdd _rdd) {
    this(_rdd, false);
  }

  Node(Rdd _rdd, boolean _processed) {
    this.rdd = _rdd;
    this.processed = _processed;
  }
}

public class Dag {
  public Node root;
  public List<Node> nodeList;
  public HashMap<Node, ArrayList<Node>> edgeMap;

  public Dag() {
    this.root = null;
    this.nodeList = new ArrayList<Node>();
    this.edgeMap = new HashMap<Node, ArrayList<Node>>();
  }

  public static void buildDag(Rdd rdd, Dag dag, Node parent) {
    if (rdd.isTarget) {
      Node targetNode = new Node(rdd);
      dag.root = targetNode;
      parent = targetNode;
      dag.nodeList.add(targetNode);
    }

    if (rdd.dependencyType == DependencyType.Wide) {

    } else {
      if (rdd.parentRdd != null) {
        buildDag(rdd.parentRdd, dag, parent);
      }
    }
  }

  public static Dag buildDagFromRdd(Rdd rdd) {
    Dag dag = new Dag();
    buildDag(rdd, dag, null);
    return dag;
  }
}

