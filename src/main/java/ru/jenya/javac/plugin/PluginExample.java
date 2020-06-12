//package ru.jenya.javac.plugin;
//
//
//import com.sun.source.tree.MethodTree;
//import com.sun.source.tree.VariableTree;
//import com.sun.source.util.*;
//import com.sun.tools.javac.api.BasicJavacTask;
//import com.sun.tools.javac.code.TypeTag;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.util.Context;
//import com.sun.tools.javac.util.Name;
//import com.sun.tools.javac.util.Names;
//
//import java.lang.annotation.*;
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.sun.tools.javac.util.List.nil;
//
///*
//
//TaskEvent e.getKind():
//    PARSE – builds an Abstract Syntax Tree (AST)
//    ENTER – source code imports are resolved
//    ANALYZE – parser output (an AST) is analyzed for errors
//    GENERATE – generating binaries for the target source file
//
//
//JavacTask
// */
//public class PluginExample implements Plugin {
//
//    @Override
//    public String getName() {
//        return "MyPlugin";
//    }
//
//
//
//    @Override
//    public void init(JavacTask task, String... args) {
//        Context context = ((BasicJavacTask) task).getContext();
//        task.addTaskListener(new TaskListener() {
//            public void started(TaskEvent e) {
//            }
//
//            public void finished(TaskEvent e) {
//                if (e.getKind() != TaskEvent.Kind.PARSE) {
//                    return;
//                }
//
//                e.getCompilationUnit().accept(new TreeScanner<Void, Void>() {
//                    @Override
//                    public Void visitMethod(MethodTree method, Void v) {
//                        List<VariableTree> parametersToInstrument
//                                = method.getParameters().stream()
//                                .filter(PluginExample.this::shouldInstrument)
//                                .collect(Collectors.toList());
//
//                        if (!parametersToInstrument.isEmpty()) {
//                            Collections.reverse(parametersToInstrument);
//                            parametersToInstrument.forEach(p -> addCheck(method, p, context));
//                        }
//                        return super.visitMethod(method, v);
//                    }
//
//
//                }, null);
//            }
//        });
//    }
//
//    private static JCTree.JCIf createCheck(VariableTree parameter, Context context) {
//        TreeMaker factory = TreeMaker.instance(context);
//        Names symbolsTable = Names.instance(context);
//
//        return factory.at(((JCTree) parameter).pos)
//                .If(factory.Parens(createIfCondition(factory, symbolsTable, parameter)),
//                        createIfBlock(factory, symbolsTable, parameter),
//                        null);
//    }
//
//    private static JCTree.JCBinary createIfCondition(TreeMaker factory,
//                                                     Names symbolsTable, VariableTree parameter) {
//        Name parameterId = symbolsTable.fromString(parameter.getName().toString());
//        return factory.Binary(JCTree.Tag.LE,
//                factory.Ident(parameterId),
//                factory.Literal(TypeTag.INT, 0));
//    }
//
//    private void addCheck(MethodTree method, VariableTree parameter, Context context) {
//        JCTree.JCIf check = createCheck(parameter, context);
//        JCTree.JCBlock body = (JCTree.JCBlock) method.getBody();
//        body.stats = body.stats.prepend(check);
//    }
//
//    private boolean shouldInstrument(VariableTree parameter) {
//        return true;
//    }
//
//    private static JCTree.JCBlock createIfBlock(TreeMaker factory,
//                                                Names symbolsTable, VariableTree parameter) {
//        String parameterName = parameter.getName().toString();
//        Name parameterId = symbolsTable.fromString(parameterName);
//
//        String errorMessagePrefix = String.format(
//                "Argument '%s' of type %s is marked by @%s but got '",
//                parameterName, parameter.getType(), Positive.class.getSimpleName());
//        String errorMessageSuffix = "' for it";
//
//        return factory.Block(0, com.sun.tools.javac.util.List.of(
//                factory.Throw(
//                        factory.NewClass(null, nil(),
//                                factory.Ident(symbolsTable.fromString(
//                                        IllegalArgumentException.class.getSimpleName())),
//                                com.sun.tools.javac.util.List.of(factory.Binary(JCTree.Tag.PLUS,
//                                        factory.Binary(JCTree.Tag.PLUS,
//                                                factory.Literal(TypeTag.CLASS, errorMessagePrefix),
//                                                factory.Ident(parameterId)),
//                                        factory.Literal(TypeTag.CLASS, errorMessageSuffix))), null))));
//    }
//
//
//}
//
//@Documented
//@Retention(RetentionPolicy.CLASS)
//@Target({ElementType.PARAMETER})
//@interface Positive {
//}