package ru.jenya.javac.plugin;

import com.sun.source.tree.Tree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.tree.JCTree;

class PositionFiller extends TreeScanner<Void, Void> {

    private int newPosition;

    public PositionFiller(int newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public Void scan(Tree node, Void aVoid) {
        ((JCTree) node).setPos(newPosition);
        return super.scan(node, aVoid);
    }
}
