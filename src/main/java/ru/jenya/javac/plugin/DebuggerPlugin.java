package ru.jenya.javac.plugin;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.EndPosTable;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeCopier;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.Position;

import java.util.ArrayList;

public class DebuggerPlugin implements Plugin {

    public static void main(String[] args) {
        for(int i = 0;i<1000000;i++){
            int x = 2;

            x++;

        }
    }

    @Override
    public String getName() {
        return "DebuggerPlugin";
    }


    @Override
    public void init(JavacTask task, String... args) {
        Context context = ((BasicJavacTask) task).getContext();
        Names names = Names.instance(context);
        BasicJavacTask task1 = (BasicJavacTask) task;

        Trees instance = Trees.instance(task);
        task.addTaskListener(new TaskListener() {
            public void started(TaskEvent e) {

            }

            public void finished(TaskEvent e) {
                if (e.getKind() != TaskEvent.Kind.PARSE) {
                    return;
                }
                JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) e.getCompilationUnit();
                compilationUnit.accept(new TreeScannerImpl((BasicJavacTask) task, compilationUnit), null);
            }
        });
    }
}


class TreeScannerImpl extends TreeScanner<Void, Void> {

    private BasicJavacTask task;
    private Trees instance;
    private TreeMaker maker;
    private EndPosTable table;
    private JCTree.JCCompilationUnit unit;
    private Position.LineTabMapImpl lineMap;

    public TreeScannerImpl(BasicJavacTask task, JCTree.JCCompilationUnit compilationUnit) {
        this.task = task;
        instance = Trees.instance(task);
        maker = TreeMaker.instance(task.getContext());
    }

    private JCTree.JCIf buildIf(JCTree.JCStatement action, JCTree.JCExpression condition) {
//        JCTree.JCStatement action1 = (JCTree.JCStatement)action.clone();
//        JCTree.JCMethodInvocation expr = (JCTree.JCMethodInvocation) ((JCTree.JCExpressionStatement) action).expr;
//        JCTree.JCMethodInvocation expr1 = (JCTree.JCMethodInvocation) ((JCTree.JCExpressionStatement) action1).expr.clone();
//        ((JCTree.JCExpressionStatement) action1).expr = expr1;
//        JCTree.JCFieldAccess meth = (JCTree.JCFieldAccess) expr.meth;
//        expr1.meth = (JCTree.JCExpression) meth.clone();
//        JCTree.JCFieldAccess selected = (JCTree.JCFieldAccess) meth.selected;
//        ((JCTree.JCFieldAccess) expr1.meth).selected = (JCTree.JCExpression) selected.clone();
//        JCTree.JCFieldAccess selected2 = (JCTree.JCFieldAccess) meth.selected;

        TreeCopier<?> treeCopier = new TreeCopier<>(maker);
        JCTree copy = treeCopier.copy(action);
        return maker.If(condition, action, ((JCTree.JCStatement) copy));
    }


    @Override
    public Void visitMethod(MethodTree methodTree, Void aVoid) {
        JCTree.JCBlock body = (JCTree.JCBlock) methodTree.getBody();
        for (int i = 0; i < body.stats.size(); i++) {
            StatementTree statement = methodTree.getBody().getStatements().get(i);
            JCTree statement1 = (JCTree) statement;
            SourcePositions sourcePositions = instance.getSourcePositions();

            try {

                int endPos = table.getEndPos(((JCTree) statement));
                int line = unit.getLineMap().getLineNumber(endPos);

                int l1 = 26;
                if (line == l1) {
                    String condition = "pr(a) || a == 5";

                    JavacParser parser = ParserFactory.instance(task.getContext()).newParser(condition, false, false, false);
                    JCTree.JCExpression expr = parser.parseExpression();
                    int conditionLine = lineMap.getStartPosition(l1 - 1);
                    int elseLine = lineMap.getStartPosition(l1 + 1);
                    expr.accept(new PositionFiller(conditionLine), null);
                    expr.setPos(conditionLine);

                    JCTree.JCIf ifSt = buildIf((JCTree.JCStatement) statement, expr);

                    ifSt.setPos(conditionLine);
                    ifSt.thenpart.setPos(statement1.pos);
                    ifSt.thenpart.accept(new PositionFiller(statement1.pos), null);
                    ifSt.elsepart.setPos(elseLine);
                    ifSt.elsepart.accept(new PositionFiller(elseLine), null);


                    ArrayList<JCTree.JCStatement> newBody = new ArrayList<>(body.stats);
                    newBody.set(i, ifSt);
                    body.stats = com.sun.tools.javac.util.List.from(newBody);
                    System.out.println("replaced");
                }
            } catch (ArrayIndexOutOfBoundsException t) {
            } catch (Throwable e) {
                e.printStackTrace();
            }
            //System.out.println(String.format("\t%s: %s", line, statement));
        }
//        if ("main".equals(methodTree.getName().toString())) {
//            for (StatementTree statement : methodTree.getBody().getStatements()) {
////                if (statement instanceof JCTree.JCExpressionStatement) {
////                    JCTree.JCExpressionStatement st = (JCTree.JCExpressionStatement) statement;
////                    JCDiagnostic.DiagnosticPosition pos = st.pos();
////                    SourcePositions sp = instance.getSourcePositions();
////                }
//
//                System.out.println(statement);
//                int endPos = table.getEndPos(((JCTree) statement));
//                int column = unit.getLineMap().getColumnNumber(endPos);
//                int line = unit.getLineMap().getLineNumber(endPos);
//                System.out.println(String.format("%s:%s", line, column));
//            }
//        }
        return super.visitMethod(methodTree, aVoid);
    }
}

class PositionFiller extends TreeScanner<Void, Void> {

    private int newPosition;

    public PositionFiller(int newPosition) {
        this.newPosition = newPosition;
    }

    @Override
    public Void scan(Tree node, Void aVoid) {
        System.out.println(node.getClass());
        ((JCTree) node).setPos(newPosition);
        return super.scan(node, aVoid);
    }
}