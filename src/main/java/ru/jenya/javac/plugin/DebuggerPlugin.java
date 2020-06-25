package ru.jenya.javac.plugin;

import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.tree.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DebuggerPlugin implements Plugin {


    @Override
    public String getName() {
        return "DebuggerPlugin";
    }

    // ru.jenya.javac.plugin.Example:9

    @Override
    public void init(JavacTask task, String... args) {
        Map<String, List<DebugArgument>> grouped = Arrays.stream(args).map(DebugArgument::parse)
                .collect(Collectors.groupingBy(a -> a.className));
        task.addTaskListener(new TaskListener() {
                                 public void started(TaskEvent e) {

                                 }

                                 public void finished(TaskEvent e) {
                                     if (e.getKind() != TaskEvent.Kind.PARSE) {
                                         return;
                                     }
                                     JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) e.getCompilationUnit();
                                     String[] path = compilationUnit.sourcefile.toUri().normalize().getPath().split("/");
                                     String name = path[path.length - 1];
                                     String fullName = compilationUnit.getPackageName() + "." + name;
                                     System.out.println(fullName);
                                     try {
                                         Files.write(Paths.get("/Users/evgenijmartyn/study/javac-plugin/g"), fullName.getBytes());
                                     } catch (IOException ioException) {
                                         ioException.printStackTrace();
                                     }
                                     if (!grouped.containsKey(fullName)) return;
                                     List<DebugArgument> lines = grouped.get(fullName);
                                     Set<Integer> catched = lines.stream().map(l -> l.line).collect(Collectors.toSet());

                                     LineAggregator lineAggregator = new LineAggregator(compilationUnit, catched);
                                     compilationUnit.accept(lineAggregator, null);
                                     BasicJavacTask task1 = (BasicJavacTask) task;
                                     LineTranslator translator = new LineTranslator(lineAggregator.lines,
                                             TreeMaker.instance(task1.getContext()),
                                             task1,
                                             compilationUnit,
                                             lines.stream().collect(Collectors.groupingBy(d -> d.line)));
                                     compilationUnit.accept(translator);

//                String condition = "i == 999999";
//                BasicJavacTask task1 = (BasicJavacTask) task;
//                Position.LineMap lineMap = compilationUnit.getLineMap();
//                for (Map.Entry<Integer, List<JCTree>> entry : lineAggregator.lines.entrySet()) {
//                    int lineNumb = entry.getKey();
//                    List<JCTree> list = entry.getValue();
//                    JavacParser parser = ParserFactory.instance(task1.getContext()).newParser(condition, false, false, false);
//                    JCTree.JCExpression expr = parser.parseExpression();
//                    int conditionLine = lineMap.getStartPosition(lineNumb - 1);
//                    int elseLine = lineMap.getStartPosition(lineNumb + 1);
//                    int currentPos = lineMap.getStartPosition(lineNumb);
//                    expr.accept(new PositionFiller(conditionLine), null);
//                    expr.setPos(conditionLine);
//
//                    JCTree.JCIf ifSt = buildIf((JCTree.JCStatement) statement, expr);
//
//                    ifSt.setPos(conditionLine);
//                    ifSt.thenpart.setPos(currentPos);
//                    ifSt.thenpart.accept(new PositionFiller(currentPos), null);
//                    ifSt.elsepart.setPos(elseLine);
//                    ifSt.elsepart.accept(new PositionFiller(elseLine), null);
//
//eNumb);
////                    expr.accept(new PositionFiller(conditionLine), null);
////                    expr.setPos(conditionLine);
////
////                    JCTree.JCIf ifSt = buildIf((JCTree.JCStatement) statement, expr);
////
////                    ifSt.setPos(conditionLine);
////                    ifSt.thenpart.setPos(currentPos);
////                    ifSt.thenpart.accept(new PositionFiller(currentPos), null);
////                    ifSt.elsepart.setPos(elseLine);
////                    ifSt.elsepart.accept(new PositionFiller(elseLine), null);
////
////
////                    ArrayList<JCTree.JCStatement> newBody = new ArrayList<>(body.stats);
////                    newBody.set(i, ifSt);
////                    body.stats = com.sun.tools.javac.util.List.from(newBody);
////                    System.out.println("replaced");
////                }
//                //compilationUnit.accept(new TreeScannerImpl((BasicJavacTask) task, compilationUnit), null);
//            }
//        });
//    }
//}
//
//
//class TreeScannerImpl extends
//                    ArrayList<JCTree.JCStatement> newBody = new ArrayList<>(body.stats);
//                    newBody.set(i, ifSt);
//                    body.stats = com.sun.tools.javac.util.List.from(newBody);
//                    System.out.println("replaced");
//                }
                //compilationUnit.accept(new TreeScannerImpl((BasicJavacTask) task, compilationUnit), null);
            }
        });
    }
}

//
//class TreeScannerImpl extends TreeScanner<Void, Void> {
//
//    private final BasicJavacTask task;
//    private final Trees instance;
//    private final TreeMaker maker;
//    private final EndPosTable table;
//    private final JCTree.JCCompilationUnit unit;
//    private final Position.LineTabMapImpl lineMap;
//
//    public TreeScannerImpl(BasicJavacTask task, JCTree.JCCompilationUnit compilationUnit) {
//        this.task = task;
//        instance = Trees.instance(task);
//        maker = TreeMaker.instance(task.getContext());
//        table = compilationUnit.endPositions;
//        unit = compilationUnit;
//        lineMap = ((Position.LineTabMapImpl) compilationUnit.lineMap);
//
//    }
//
//    private JCTree.JCIf buildIf(JCTree.JCStatement action, JCTree.JCExpression condition) {
//        TreeCopier<?> treeCopier = new TreeCopier<>(maker);
//        JCTree copy = treeCopier.copy(action);
//        return maker.If(condition, action, ((JCTree.JCStatement) copy));
//    }
//
//
//    public Void scan(Tree node, Void aVoid) {
//        int endPos = table.getEndPos(((JCTree) node));
//        int line = unit.getLineMap().getLineNumber(endPos);
//        if(line == 26){
//            System.out.println(node);
//        }
//        return super.scan(node, aVoid);
//    }
//
//
//    @Override
//    public Void visitMethod(MethodTree methodTree, Void aVoid) {
//        JCTree.JCBlock body = (JCTree.JCBlock) methodTree.getBody();
//        for (int i = 0; i < body.stats.size(); i++) {
//            StatementTree statement = methodTree.getBody().getStatements().get(i);
//            JCTree statement1 = (JCTree) statement;
//
//            try {
//
//                int endPos = table.getEndPos(((JCTree) statement));
//                int line = unit.getLineMap().getLineNumber(endPos);
//
//                int l1 = 26;
//                if (line == l1) {
//                    String condition = "i == 999999";
//
//                    JavacParser parser = ParserFactory.instance(task.getContext()).newParser(condition, false, false, false);
//                    JCTree.JCExpression expr = parser.parseExpression();
//                    int conditionLine = lineMap.getStartPosition(l1 - 1);
//                    int elseLine = lineMap.getStartPosition(l1 + 1);
//                    expr.accept(new PositionFiller(conditionLine), null);
//                    expr.setPos(conditionLine);
//
//                    JCTree.JCIf ifSt = buildIf((JCTree.JCStatement) statement, expr);
//
//                    ifSt.setPos(conditionLine);
//                    ifSt.thenpart.setPos(statement1.pos);
//                    ifSt.thenpart.accept(new PositionFiller(statement1.pos), null);
//                    ifSt.elsepart.setPos(elseLine);
//                    ifSt.elsepart.accept(new PositionFiller(elseLine), null);
//
//
//                    ArrayList<JCTree.JCStatement> newBody = new ArrayList<>(body.stats);
//                    newBody.set(i, ifSt);
//                    body.stats = com.sun.tools.javac.util.List.from(newBody);
//                    System.out.println("replaced");
//                }
//            } catch (ArrayIndexOutOfBoundsException t) {
//
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//            //System.out.println(String.format("\t%s: %s", line, statement));
//        }
////        if ("main".equals(methodTree.getName().toString())) {
////            for (StatementTree statement : methodTree.getBody().getStatements()) {
//////                if (statement instanceof JCTree.JCExpressionStatement) {
//////                    JCTree.JCExpressionStatement st = (JCTree.JCExpressionStatement) statement;
//////                    JCDiagnostic.DiagnosticPosition pos = st.pos();
//////                    SourcePositions sp = instance.getSourcePositions();
//////                }
////
////                System.out.println(statement);
////                int endPos = table.getEndPos(((JCTree) statement));
////                int column = unit.getLineMap().getColumnNumber(endPos);
////                int line = unit.getLineMap().getLineNumber(endPos);
////                System.out.println(String.format("%s:%s", line, column));
////            }
////        }
//        return super.visitMethod(methodTree, aVoid);
//    }
//}