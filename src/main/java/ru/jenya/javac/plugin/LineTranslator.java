package ru.jenya.javac.plugin;

import com.sun.source.util.Trees;
import com.sun.tools.javac.api.BasicJavacTask;
import com.sun.tools.javac.parser.JavacParser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.Position;

import java.util.*;
import java.util.stream.Collectors;

public class LineTranslator extends TreeTranslator {

    private final Map<Integer, List<JCTree>> lines;
    private final Map<JCTree, Integer> m = new HashMap<>();
    private final TreeMaker maker;
    private final BasicJavacTask task;
    private final Position.LineTabMapImpl lineMap;
    private final Map<Integer, List<DebugArgument>> breakpoints;

    public LineTranslator(Map<Integer, List<JCTree>> lines,
                          TreeMaker maker,
                          BasicJavacTask task,
                          JCTree.JCCompilationUnit compilationUnit,
                          Map<Integer, List<DebugArgument>> breakpoints) {
        this.lines = lines;
        this.maker = maker;
        this.task = task;
        this.breakpoints = breakpoints;
        lineMap = ((Position.LineTabMapImpl) compilationUnit.lineMap);
        lines.forEach((l, list) ->
                list.forEach(t -> m.put(t, l)));
    }


    @Override
    public void visitTopLevel(JCTree.JCCompilationUnit jcCompilationUnit) {
        super.visitTopLevel(jcCompilationUnit);
        visitTree(jcCompilationUnit);
    }

    @Override
    public void visitImport(JCTree.JCImport jcImport) {
        super.visitImport(jcImport);
        visitTree(jcImport);
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        super.visitClassDef(jcClassDecl);
        visitTree(jcClassDecl);
    }

    @Override
    public void visitMethodDef(JCTree.JCMethodDecl jcMethodDecl) {
        super.visitMethodDef(jcMethodDecl);
        visitTree(jcMethodDecl);
    }

    @Override
    public void visitVarDef(JCTree.JCVariableDecl jcVariableDecl) {
        super.visitVarDef(jcVariableDecl);
        visitTree(jcVariableDecl);
    }

    @Override
    public void visitSkip(JCTree.JCSkip jcSkip) {
        super.visitSkip(jcSkip);
        visitTree(jcSkip);
    }

    @Override
    public void visitBlock(JCTree.JCBlock jcBlock) {
        super.visitBlock(jcBlock);
        visitTree(jcBlock);
    }

    @Override
    public void visitDoLoop(JCTree.JCDoWhileLoop jcDoWhileLoop) {
        super.visitDoLoop(jcDoWhileLoop);
        visitTree(jcDoWhileLoop);
    }

    @Override
    public void visitWhileLoop(JCTree.JCWhileLoop jcWhileLoop) {
        super.visitWhileLoop(jcWhileLoop);
        visitTree(jcWhileLoop);
    }

    @Override
    public void visitForLoop(JCTree.JCForLoop jcForLoop) {
        super.visitForLoop(jcForLoop);
        visitTree(jcForLoop);
    }

    @Override
    public void visitForeachLoop(JCTree.JCEnhancedForLoop jcEnhancedForLoop) {
        super.visitForeachLoop(jcEnhancedForLoop);
        visitTree(jcEnhancedForLoop);
    }

    @Override
    public void visitLabelled(JCTree.JCLabeledStatement jcLabeledStatement) {
        super.visitLabelled(jcLabeledStatement);
        visitTree(jcLabeledStatement);
    }

    @Override
    public void visitSwitch(JCTree.JCSwitch jcSwitch) {
        super.visitSwitch(jcSwitch);
        visitTree(jcSwitch);
    }

    @Override
    public void visitCase(JCTree.JCCase jcCase) {
        super.visitCase(jcCase);
        visitTree(jcCase);
    }

    @Override
    public void visitSynchronized(JCTree.JCSynchronized jcSynchronized) {
        super.visitSynchronized(jcSynchronized);
        visitTree(jcSynchronized);
    }

    @Override
    public void visitTry(JCTree.JCTry jcTry) {
        super.visitTry(jcTry);
        visitTree(jcTry);
    }

    @Override
    public void visitCatch(JCTree.JCCatch jcCatch) {
        super.visitCatch(jcCatch);
        visitTree(jcCatch);
    }

    @Override
    public void visitConditional(JCTree.JCConditional jcConditional) {
        super.visitConditional(jcConditional);
        visitTree(jcConditional);
    }

    @Override
    public void visitIf(JCTree.JCIf jcIf) {
        super.visitIf(jcIf);
        visitTree(jcIf);
    }

    @Override
    public void visitExec(JCTree.JCExpressionStatement jcExpressionStatement) {
        super.visitExec(jcExpressionStatement);
        visitTree(jcExpressionStatement);
    }

    @Override
    public void visitBreak(JCTree.JCBreak jcBreak) {
        super.visitBreak(jcBreak);
        visitTree(jcBreak);
    }

    @Override
    public void visitContinue(JCTree.JCContinue jcContinue) {
        super.visitContinue(jcContinue);
        visitTree(jcContinue);
    }

    @Override
    public void visitReturn(JCTree.JCReturn jcReturn) {
        super.visitReturn(jcReturn);
        visitTree(jcReturn);
    }

    @Override
    public void visitThrow(JCTree.JCThrow jcThrow) {
        super.visitThrow(jcThrow);
        visitTree(jcThrow);
    }

    @Override
    public void visitAssert(JCTree.JCAssert jcAssert) {
        super.visitAssert(jcAssert);
        visitTree(jcAssert);
    }

    @Override
    public void visitApply(JCTree.JCMethodInvocation jcMethodInvocation) {
        super.visitApply(jcMethodInvocation);
        visitTree(jcMethodInvocation);
    }

    @Override
    public void visitNewClass(JCTree.JCNewClass jcNewClass) {
        super.visitNewClass(jcNewClass);
        visitTree(jcNewClass);
    }

    @Override
    public void visitLambda(JCTree.JCLambda jcLambda) {
        super.visitLambda(jcLambda);
        visitTree(jcLambda);
    }

    @Override
    public void visitNewArray(JCTree.JCNewArray jcNewArray) {
        super.visitNewArray(jcNewArray);
        visitTree(jcNewArray);
    }

    @Override
    public void visitParens(JCTree.JCParens jcParens) {
        super.visitParens(jcParens);
        visitTree(jcParens);
    }

    @Override
    public void visitAssign(JCTree.JCAssign jcAssign) {
        super.visitAssign(jcAssign);
        visitTree(jcAssign);
    }

    @Override
    public void visitAssignop(JCTree.JCAssignOp jcAssignOp) {
        super.visitAssignop(jcAssignOp);
        visitTree(jcAssignOp);
    }

    @Override
    public void visitUnary(JCTree.JCUnary jcUnary) {
        super.visitUnary(jcUnary);
        visitTree(jcUnary);
    }

    @Override
    public void visitBinary(JCTree.JCBinary jcBinary) {
        super.visitBinary(jcBinary);
        visitTree(jcBinary);
    }

    @Override
    public void visitTypeCast(JCTree.JCTypeCast jcTypeCast) {
        super.visitTypeCast(jcTypeCast);
        visitTree(jcTypeCast);
    }

    @Override
    public void visitTypeTest(JCTree.JCInstanceOf jcInstanceOf) {
        super.visitTypeTest(jcInstanceOf);
        visitTree(jcInstanceOf);
    }

    @Override
    public void visitIndexed(JCTree.JCArrayAccess jcArrayAccess) {
        super.visitIndexed(jcArrayAccess);
        visitTree(jcArrayAccess);
    }

    @Override
    public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
        super.visitSelect(jcFieldAccess);
        visitTree(jcFieldAccess);
    }

    @Override
    public void visitReference(JCTree.JCMemberReference jcMemberReference) {
        super.visitReference(jcMemberReference);
        visitTree(jcMemberReference);
    }

    @Override
    public void visitIdent(JCTree.JCIdent jcIdent) {
        super.visitIdent(jcIdent);
        visitTree(jcIdent);
    }

    @Override
    public void visitLiteral(JCTree.JCLiteral jcLiteral) {
        super.visitLiteral(jcLiteral);
        visitTree(jcLiteral);
    }

    @Override
    public void visitTypeIdent(JCTree.JCPrimitiveTypeTree jcPrimitiveTypeTree) {
        super.visitTypeIdent(jcPrimitiveTypeTree);
        visitTree(jcPrimitiveTypeTree);
    }

    @Override
    public void visitTypeArray(JCTree.JCArrayTypeTree jcArrayTypeTree) {
        super.visitTypeArray(jcArrayTypeTree);
        visitTree(jcArrayTypeTree);
    }

    @Override
    public void visitTypeApply(JCTree.JCTypeApply jcTypeApply) {
        super.visitTypeApply(jcTypeApply);
        visitTree(jcTypeApply);
    }

    @Override
    public void visitTypeUnion(JCTree.JCTypeUnion jcTypeUnion) {
        super.visitTypeUnion(jcTypeUnion);
        visitTree(jcTypeUnion);
    }

    @Override
    public void visitTypeIntersection(JCTree.JCTypeIntersection jcTypeIntersection) {
        super.visitTypeIntersection(jcTypeIntersection);
        visitTree(jcTypeIntersection);
    }

    @Override
    public void visitTypeParameter(JCTree.JCTypeParameter jcTypeParameter) {
        super.visitTypeParameter(jcTypeParameter);
        visitTree(jcTypeParameter);
    }

    @Override
    public void visitWildcard(JCTree.JCWildcard jcWildcard) {
        super.visitWildcard(jcWildcard);
        visitTree(jcWildcard);
    }

    @Override
    public void visitTypeBoundKind(JCTree.TypeBoundKind typeBoundKind) {
        super.visitTypeBoundKind(typeBoundKind);
        visitTree(typeBoundKind);
    }

    @Override
    public void visitErroneous(JCTree.JCErroneous jcErroneous) {
        super.visitErroneous(jcErroneous);
        visitTree(jcErroneous);
    }

    @Override
    public void visitLetExpr(JCTree.LetExpr letExpr) {
        super.visitLetExpr(letExpr);
        visitTree(letExpr);
    }

    @Override
    public void visitModifiers(JCTree.JCModifiers jcModifiers) {
        super.visitModifiers(jcModifiers);
        visitTree(jcModifiers);
    }

    @Override
    public void visitAnnotation(JCTree.JCAnnotation jcAnnotation) {
        super.visitAnnotation(jcAnnotation);
        visitTree(jcAnnotation);
    }

    @Override
    public void visitAnnotatedType(JCTree.JCAnnotatedType jcAnnotatedType) {
        super.visitAnnotatedType(jcAnnotatedType);
        visitTree(jcAnnotatedType);
    }

    private JCTree.JCIf buildIf(JCTree.JCStatement action, List<JCTree.JCExpression> conditions) {
        TreeCopier<?> treeCopier = new TreeCopier<>(maker);
        JCTree copy = treeCopier.copy(action);
        JCTree.JCExpression condition = conditions.stream().reduce((a, b) -> maker.Binary(JCTree.Tag.OR, a, b)).orElse(conditions.get(0));
        return maker.If(condition, action, ((JCTree.JCStatement) copy));
    }

    @Override
    public void visitTree(JCTree t) {
        if (m.containsKey(t)) {
            int line = m.get(t);
            List<JCTree> jcTrees = lines.get(line);
            if (!jcTrees.get(0).equals(t)) {
                this.result = maker.at(t.pos).Skip();
            } else {
                jcTrees.forEach(System.out::println);
                com.sun.tools.javac.util.List<JCTree.JCStatement> from = com.sun.tools.javac.util.List.from(jcTrees.stream()
                        .map(t1 -> {
                            if (t1 instanceof JCTree.JCExpression) {
                                return maker.Exec(((JCTree.JCExpression) t1));
                            } else {
                                return ((JCTree.JCStatement) t1);
                            }
                        }).collect(Collectors.toList()));
                JCTree.JCStatement statement = maker.at(t.pos).Block(0, from);
                List<String> conditions = breakpoints.get(line).stream().map(c -> c.condition).collect(Collectors.toList());

                List<JCTree.JCExpression> conditionExpr = new ArrayList<>();
                for (String condition : conditions) {
                    JavacParser parser = ParserFactory.instance(task.getContext()).newParser(condition, false, false, false);
                    conditionExpr.add(parser.parseExpression());
                }

                int conditionLine = lineMap.getStartPosition(line - 1);
                int elseLine = lineMap.getStartPosition(line + 1);
                conditionExpr.forEach(expr -> expr.accept(new PositionFiller(conditionLine), null));
                conditionExpr.forEach(expr -> expr.setPos(conditionLine));

                JCTree.JCIf ifSt = buildIf(statement, conditionExpr);

                ifSt.setPos(conditionLine);
                ifSt.thenpart.setPos(t.pos);
                ifSt.thenpart.accept(new PositionFiller(t.pos), null);
                ifSt.elsepart.setPos(elseLine);
                ifSt.elsepart.accept(new PositionFiller(elseLine), null);

                this.result = ifSt;
            }
        }

    }
}
