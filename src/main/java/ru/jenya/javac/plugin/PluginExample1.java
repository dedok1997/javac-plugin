package ru.jenya.javac.plugin;


import com.sun.source.tree.*;
import com.sun.source.util.*;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.*;

import javax.tools.JavaFileObject;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sun.tools.javac.util.List.nil;

/*

TaskEvent e.getKind():
    PARSE – builds an Abstract Syntax Tree (AST)
    ENTER – source code imports are resolved
    ANALYZE – parser output (an AST) is analyzed for errors
    GENERATE – generating binaries for the target source file


JavacTask -
 */
public class PluginExample1 implements Plugin {


    public static void main(String[] args) {
        int a = 2 + 3;
        System.out.println(a);
    }

    @Override
    public String getName() {
        return "PluginExample1";
    }



    @Override
    public void init(JavacTask task, String... args) {
        Context context = ((BasicJavacTask) task).getContext();
        Trees instance = Trees.instance(task);
        task.addTaskListener(new TaskListener() {
            public void started(TaskEvent e) {

            }

            public void finished(TaskEvent e) {
                if (e.getKind() != TaskEvent.Kind.PARSE) {
                    return;
                }
                e.getCompilationUnit().accept(new TreeScannerImpl(((BasicJavacTask) task)), null);
            }
        });
    }
}


class TreeScannerImpl extends TreeScanner<Void, Void>{



    private BasicJavacTask task;

    @Override
    public Void scan(Tree tree, Void aVoid) {
        //Log.instance(task.getContext()).printRawLines(Log.WriterKind.ERROR, tree.getClass().getCanonicalName());
        return super.scan(tree, aVoid);
    }

    public TreeScannerImpl(BasicJavacTask context) {
        this.task = context;
    }

    @Override
    public Void visitMethod(MethodTree methodTree, Void aVoid) {
//        JCTree.JCBlock body = (JCTree.JCBlock) methodTree.getBody();
//        TreeMaker factory = TreeMaker.instance(task.getContext());
//        Names symbolsTable = Names.instance(task.getContext());
//
//        factory.App(factory.Ident(symbolsTable.fromString("System")))
//        body.stats.prepend()
        if("main".equals(methodTree.getName().toString())){
            for (StatementTree statement : methodTree.getBody().getStatements()) {
                if(statement instanceof JCTree.JCExpressionStatement){
                    JCTree.JCExpressionStatement st = (JCTree.JCExpressionStatement) statement;

                }

            }
        }

        return super.visitMethod(methodTree, aVoid);
    }
}

class TaskListenerImpl implements TaskListener{
    @Override
    public void started(TaskEvent taskEvent) {

    }

    @Override
    public void finished(TaskEvent taskEvent) {

    }
}