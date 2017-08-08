package com.prositen.hackerrank.advanced;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.prositen.hackerrank.advanced.VisitorPattern.solve;
import static org.junit.Assert.*;

public class VisitorPatternTest {
    private Tree root;
    private final static String VISITOR_PATTERN = "hackerrank/advanced/visitor_pattern.txt";
    @org.junit.Before
    public void setupTest() {
        try {
            ClassLoader classloader = getClass().getClassLoader();
            URL resource = classloader.getResource(VISITOR_PATTERN);
            if (resource != null) {
                File f = new File(resource.getFile());
                Scanner s = new Scanner(f);
                root = solve(s);
            } else {
                throw new FileNotFoundException("Test resource not found: " + VISITOR_PATTERN);
            }
        } catch (FileNotFoundException e)  {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @org.junit.Test
    public void testFancyVisitor() {
        FancyVisitor v = new FancyVisitor();
        root.accept(v);
        assertEquals(15, v.getResult());
    }

    @org.junit.Test
    public void testProductOfRedNodesVisitor() {
        ProductOfRedNodesVisitor v = new ProductOfRedNodesVisitor();
        root.accept(v);
        assertEquals(40, v.getResult());
    }

    @org.junit.Test
    public void testSumInLeavesVisitor() {
        SumInLeavesVisitor v = new SumInLeavesVisitor();
        root.accept(v);
        assertEquals(24, v.getResult());
    }


    @org.junit.Test
    public void testTreeCorrect() {
        class DebugVisitor extends TreeVis {

            private List<Integer> nodeDFS = new ArrayList<>();

            List<Integer> getNodeDFS() {
                return nodeDFS;
            }

            @Override
            public int getResult() {
                return 0;
            }

            @Override
            public void visitNode(TreeNode node) {
                nodeDFS.add(node.getValue());
            }

            @Override
            public void visitLeaf(TreeLeaf leaf) {
                nodeDFS.add(leaf.getValue());
            }
        }
        DebugVisitor v = new DebugVisitor();
        root.accept(v);

        List<Integer> expected = Arrays.asList(4, 7, 2, 5, 12);
        assertEquals(expected, v.getNodeDFS());
    }
}