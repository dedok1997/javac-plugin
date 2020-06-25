package ru.jenya.javac.plugin;

import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;

import java.util.*;

public class LineAggregator extends TreeScanner<Void, Void> {

    public Map<Integer, List<JCTree>> lines = new TreeMap<>();
    private final Set<Tree> used = new HashSet<>();
    private final JCTree.JCCompilationUnit compilationUnit;
    private final Set<Integer> lineToCatch;

    public LineAggregator(JCTree.JCCompilationUnit compilationUnit, Set<Integer> lineToCatch) {
        this.compilationUnit = compilationUnit;
        this.lineToCatch = lineToCatch;
    }

    @Override
    public Void scan(Tree node, Void aVoid) {

        try {
            int endPos = compilationUnit.endPositions.getEndPos(((JCTree) node));
            int line = compilationUnit.getLineMap().getLineNumber(endPos);
            if (node != null)
                System.out.println(node.getClass() + " // " + line);
            if (lineToCatch.contains(line)) {
                if (!used.contains(node)) {
                    used.add(node);
                    node.accept(new CatchInner(), null);
                    if (lines.containsKey(line)) {
                        lines.get(line).add(((JCTree) node));
                    } else {
                        List<JCTree> l = new ArrayList<>();
                        l.add((JCTree) node);
                        lines.put(line, l);
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {

        }


        return super.scan(node, aVoid);
    }

    private class CatchInner extends TreeScanner<Void, Void> {

        @Override
        public Void scan(Tree node, Void aVoid) {
            used.add(node);
            return super.scan(node, aVoid);
        }
    }


}
