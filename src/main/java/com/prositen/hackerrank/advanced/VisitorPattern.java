package com.prositen.hackerrank.advanced;

// Code below is from HackerRank, don't touch
import java.util.ArrayList;
import java.io.*;
import java.util.*;

import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}

// Code above is from HackerRank, don't touch

class SumInLeavesVisitor extends TreeVis {
    private int result = 0;
    public int getResult() {
        return result;
    }

    public void visitNode(TreeNode node) {

    }

    public void visitLeaf(TreeLeaf leaf) {
        result += leaf.getValue();
    }
}

class ProductOfRedNodesVisitor extends TreeVis {
    private long result = 1;
    private long modulo = 1000000007;
    public int getResult() {
        return (int) result;
    }

    public void visitNode(TreeNode node) {
        if (node.getColor() == Color.RED) {
            result *= node.getValue();
            result %= modulo;
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.RED) {
            result *= leaf.getValue();
            result %= modulo;
        }
    }
}

class FancyVisitor extends TreeVis {
    private int evenDepthNonLeaves = 0;
    private int greenLeaves = 0;
    public int getResult() {
        return Math.abs(evenDepthNonLeaves - greenLeaves);
    }

    public void visitNode(TreeNode node) {
        if (node.getDepth() % 2 == 0) {
            evenDepthNonLeaves += node.getValue();
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.GREEN) {
            greenLeaves += leaf.getValue();
        }
    }
}

public class VisitorPattern {


    public static Tree solve() {
        Scanner scanner = new Scanner(System.in);
        return solve(scanner);
    }


    public static Tree solve(Scanner scanner) {

        int nodes = scanner.nextInt();

        int[] values = new int[nodes + 1];
        Color[] colors = new Color[nodes + 1];

        Map<Integer, HashSet<Integer>> children = new HashMap<>();
        for (int n = 1; n <= nodes; n++) {
            children.put(n, new HashSet<>());
            values[n] = scanner.nextInt();
        }

        for (int n = 1; n <= nodes; n++) {
            colors[n] = scanner.nextInt() == 0 ? Color.RED : Color.GREEN;
        }

        while (scanner.hasNext()) {
            int from = scanner.nextInt();
            int to = scanner.nextInt();
            children.get(from).add(to);
            children.get(to).add(from);
        }


        boolean[] edgeFound = new boolean[nodes+1];

        TreeNode root = new TreeNode(values[1], colors[1], 0);

        List<Map.Entry<Integer, TreeNode>> bfsQueue = new ArrayList<>();
        bfsQueue.add(new AbstractMap.SimpleEntry<>(1, root));

        while (bfsQueue.size() > 0) {
            Map.Entry<Integer, TreeNode> entry = bfsQueue.remove(0);
            Integer id = entry.getKey();
            TreeNode node = entry.getValue();
            edgeFound[id] = true;
            for(Integer childId: children.get(id)) {
                if (edgeFound[childId]) {
                    continue;
                }
                HashSet<Integer> grandchildren = children.get(childId);
                if (grandchildren.size() == 1) { // Actually its parent
                    node.addChild(new TreeLeaf(values[childId], colors[childId], node.getDepth() + 1));
                } else {
                    TreeNode child = new TreeNode(values[childId], colors[childId], node.getDepth() + 1);
                    bfsQueue.add(new AbstractMap.SimpleEntry<>(childId, child));
                    node.addChild(child);
                }
            }
        }

        return root;
    }

    // Code below is from HackerRank; don't touch

    public static void main(String[] args) {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);


        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
}