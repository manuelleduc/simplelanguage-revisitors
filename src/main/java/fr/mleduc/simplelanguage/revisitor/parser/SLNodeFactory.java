/*
 * Copyright (c) 2012, 2016, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package fr.mleduc.simplelanguage.revisitor.parser;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.source.Source;

import fr.mleduc.simplelanguage.revisitor.lang.SLRLanguage;
import fr.mleduc.simplelanguage.revisitor.model.Root;
import fr.mleduc.simplelanguage.revisitor.model.stmt.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.IntValue;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.StringValue;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class used by the SL {@link Parser} to create nodes. The code is factored out of the
 * automatically generated parser to keep the attributed grammar of SL small.
 */
public class SLNodeFactory {

    /**
     * Local variable names that are visible in the current block. Variables are not visible outside
     * of their defining block, to prevent the usage of undefined variables. Because of that, we can
     * decide during parsing if a name references a local variable or is a function name.
     */
    static class LexicalScope {
        protected final LexicalScope outer;
        protected final Map<String, FrameSlot> locals;

        LexicalScope(LexicalScope outer) {
            this.outer = outer;
            this.locals = new HashMap<>();
            if (outer != null) {
                locals.putAll(outer.locals);
            }
        }
    }

    /* State while parsing a source unit. */
    private final Source source;
    private final Map<String, RootCallTarget> allFunctions;

    /* State while parsing a function. */
    private int functionStartPos;
    private String functionName;
    private int functionBodyStartPos; // includes parameter list
    private int parameterCount;
    private FrameDescriptor frameDescriptor;
    private List<Stmt> methodNodes;

    /* State while parsing a block. */
    private LexicalScope lexicalScope;
    private final SLRLanguage language;

    public SLNodeFactory(SLRLanguage language, Source source) {
        this.language = language;
        this.source = source;
        this.allFunctions = new HashMap<>();
    }

    public Map<String, RootCallTarget> getAllFunctions() {
        return allFunctions;
    }

    public void startFunction(Token nameToken, Token bodyStartToken) {
        assert functionStartPos == 0;
        assert functionName == null;
        assert functionBodyStartPos == 0;
        assert parameterCount == 0;
        assert frameDescriptor == null;
        assert lexicalScope == null;

        functionStartPos = nameToken.getStartIndex();
        functionName = nameToken.getText();
        functionBodyStartPos = bodyStartToken.getStartIndex();
        frameDescriptor = new FrameDescriptor();
        methodNodes = new ArrayList<>();
        startBlock();
    }

    public void addFormalParameter(Token nameToken) {
        /*
         * Method parameters are assigned to local variables at the beginning of the method. This
         * ensures that accesses to parameters are specialized the same way as local variables are
         * specialized.
         */
        final ReadArgument readArg = new ReadArgument(parameterCount);
        Expr assignment = createAssignment(createStringLiteral(nameToken, false), readArg);
        methodNodes.add(assignment);
        parameterCount++;
    }

    public void finishFunction(Stmt bodyNode) {
        if (bodyNode == null) {
            // a state update that would otherwise be performed by finishBlock
            lexicalScope = lexicalScope.outer;
        } else {
            methodNodes.add(bodyNode);
            //final int bodyEndPos = bodyNode.getSourceEndIndex();
            //final SourceSection functionSrc = source.createSection(functionStartPos, bodyEndPos - functionStartPos);
            final Stmt methodBlock = finishBlock(methodNodes, functionBodyStartPos);
            assert lexicalScope == null : "Wrong scoping of blocks in parser";

            final FunctionBody functionBodyNode = new FunctionBody(methodBlock);
            //functionBodyNode.setSourceSection(functionSrc.getCharIndex(), functionSrc.getCharLength());

            final Root rootNode = new Root(language, frameDescriptor, functionBodyNode, functionName);
            allFunctions.put(functionName, Truffle.getRuntime().createCallTarget(rootNode));
        }

        functionStartPos = 0;
        functionName = null;
        functionBodyStartPos = 0;
        parameterCount = 0;
        frameDescriptor = null;
        lexicalScope = null;
    }

    public void startBlock() {
        lexicalScope = new LexicalScope(lexicalScope);
    }

    public Stmt finishBlock(List<Stmt> bodyNodes, int startPos) {
        lexicalScope = lexicalScope.outer;

        if (containsNull(bodyNodes)) {
            return null;
        }

        List<Stmt> flattenedNodes = new ArrayList<>(bodyNodes.size());
        flattenBlocks(bodyNodes, flattenedNodes);
//        for (Stmt statement : flattenedNodes) {
//            if (statement.hasSource() && !isHaltInCondition(statement)) {
//                statement.addStatementTag();
//            }
//        }
        Block blockNode = new Block(flattenedNodes.toArray(new Stmt[flattenedNodes.size()]));
        //blockNode.setSourceSection(startPos, length);
        return blockNode;
    }

    private static boolean isHaltInCondition(Stmt statement) {
        return (statement instanceof If) || (statement instanceof While);
    }

    private void flattenBlocks(Iterable<? extends Stmt> bodyNodes, List<Stmt> flattenedNodes) {
        for (Stmt n : bodyNodes) {
            if (n instanceof Block) {
                flattenBlocks(((Block) n).getStatements(), flattenedNodes);
            } else {
                flattenedNodes.add(n);
            }
        }
    }

    /**
     * Returns an {@link SLDebuggerNode} for the given token.
     *
     * @param debuggerToken The token containing the debugger node's info.
     * @return A SLDebuggerNode for the given token.
     */
    Stmt createDebugger(Token debuggerToken) {
//        final SLDebuggerNode debuggerNode = new SLDebuggerNode();
//        srcFromToken(debuggerNode, debuggerToken);
        // TODO
        return null;
    }

    /**
     * Returns an {@link SLBreakNode} for the given token.
     *
     * @param breakToken The token containing the break node's info.
     * @return A SLBreakNode for the given token.
     */
    public Stmt createBreak(Token breakToken) {
//        final SLBreakNode breakNode = new SLBreakNode();
//        srcFromToken(breakNode, breakToken);

        // TODO
        return null;
    }

    /**
     * Returns an {@link Continue} for the given token.
     *
     * @param continueToken The token containing the continue node's info.
     * @return A Continue built using the given token.
     */
    public Stmt createContinue(Token continueToken) {
        final Continue continueNode = new Continue();
        srcFromToken(continueNode, continueToken);
        return continueNode;
    }

    /**
     * Returns an {@link While} for the given parameters.
     *
     * @param whileToken    The token containing the while node's info
     * @param conditionNode The conditional node for this while loop
     * @param bodyNode      The body of the while loop
     * @return A While built using the given parameters. null if either conditionNode or
     * bodyNode is null.
     */
    public Stmt createWhile(Token whileToken, Expr conditionNode, Stmt bodyNode) {
        if (conditionNode == null || bodyNode == null) {
            return null;
        }

        //  conditionNode.addStatementTag();
        final int start = whileToken.getStartIndex();
        //final int end = bodyNode.getSourceEndIndex();
        final While whileNode = new While(conditionNode, bodyNode);
        //whileNode.setSourceSection(start, end - start);
        return whileNode;
    }

    /**
     * Returns an {@link If} for the given parameters.
     *
     * @param ifToken       The token containing the if node's info
     * @param conditionNode The condition node of this if statement
     * @param thenPartNode  The then part of the if
     * @param elsePartNode  The else part of the if (null if no else part)
     * @return An If for the given parameters. null if either conditionNode or thenPartNode is
     * null.
     */
    public Stmt createIf(Token ifToken, Expr conditionNode, Stmt thenPartNode, Stmt elsePartNode) {
        if (conditionNode == null || thenPartNode == null) {
            return null;
        }

        // conditionNode.addStatementTag();
        final int start = ifToken.getStartIndex();
        //final int end = elsePartNode == null ? thenPartNode.getSourceEndIndex() : elsePartNode.getSourceEndIndex();
        final If ifNode = new If(conditionNode, thenPartNode, elsePartNode);
        //ifNode.setSourceSection(start, end - start);
        return ifNode;
    }

    /**
     * Returns an {@link Return} for the given parameters.
     *
     * @param t         The token containing the return node's info
     * @param valueNode The value of the return (null if not returning a value)
     * @return An Return for the given parameters.
     */
    public Stmt createReturn(Token t, Expr valueNode) {
        final int start = t.getStartIndex();
        //final int length = valueNode == null ? t.getText().length() : valueNode.getSourceEndIndex() - start;
        final Return returnNode = new Return(valueNode);
        //returnNode.setSourceSection(start, length);
        return returnNode;
    }

    /**
     * Returns the corresponding subclass of {@link Expr} for binary expressions. </br>
     * These nodes are currently not instrumented.
     *
     * @param opToken   The operator of the binary expression
     * @param leftNode  The left node of the expression
     * @param rightNode The right node of the expression
     * @return A subclass of Expr using the given parameters based on the given opToken.
     * null if either leftNode or rightNode is null.
     */
    public Expr createBinary(Token opToken, Expr leftNode, Expr rightNode) {
        if (leftNode == null || rightNode == null) {
            return null;
        }
        final Expr leftUnboxed;
        if (leftNode instanceof Binary) {  // Binary never returns boxed value
            leftUnboxed = leftNode;
        } else {
            leftUnboxed = new Unbox(leftNode);
        }
        final Expr rightUnboxed;
        if (rightNode instanceof Binary) {  // Binary never returns boxed value
            rightUnboxed = rightNode;
        } else {
            rightUnboxed = new Unbox(rightNode);
        }

        final Expr result;
        switch (opToken.getText()) {
            case "+":
                result = new Plus(leftUnboxed, rightUnboxed);
                break;
            case "*":
                result = new Multiply(leftUnboxed, rightUnboxed);
                break;
            case "/":
                result = new Divide(leftUnboxed, rightUnboxed);
                break;
            case "-":
                result = new Minus(leftUnboxed, rightUnboxed);
                break;
            case "<":
                result = new Lower(leftUnboxed, rightUnboxed);
                break;
            case "<=":
                result = new LowerEqual(leftUnboxed, rightUnboxed);
                break;
            case ">":
                result = new Not(new LowerEqual(leftUnboxed, rightUnboxed));
                break;
            case ">=":
                result = new Not(new Lower(leftUnboxed, rightUnboxed));
                break;
            case "==":
                result = new Equal(leftUnboxed, rightUnboxed);
                break;
            case "!=":
                result = new Not(new Equal(leftUnboxed, rightUnboxed));
                break;
            case "&&":
                result = new And(leftUnboxed, rightUnboxed);
                break;
            case "||":
                result = new Or(leftUnboxed, rightUnboxed);
                break;
            default:
                throw new RuntimeException("unexpected operation: " + opToken.getText());
        }

//        int start = leftNode.getSourceCharIndex();
//        int length = rightNode.getSourceEndIndex() - start;
//        result.setSourceSection(start, length);

        return result;
    }

    /**
     * Returns an {@link Invoke} for the given parameters.
     *
     * @param functionNode   The function being called
     * @param parameterNodes The parameters of the function call
     * @param finalToken     A token used to determine the end of the sourceSelection for this call
     * @return An Invoke for the given parameters. null if functionNode or any of the
     * parameterNodes are null.
     */
    public Expr createCall(Expr functionNode, List<Expr> parameterNodes, Token finalToken) {
        if (functionNode == null || containsNull(parameterNodes)) {
            return null;
        }

        final Expr result = new Invoke(functionNode, parameterNodes.toArray(new Expr[parameterNodes.size()]));

//        final int startPos = functionNode.getSourceCharIndex();
        final int endPos = finalToken.getStartIndex() + finalToken.getText().length();
//        result.setSourceSection(startPos, endPos - startPos);

        return result;
    }

    /**
     * Returns an {@link SLWriteLocalVariableNode} for the given parameters.
     *
     * @param nameNode  The name of the variable being assigned
     * @param valueNode The value to be assigned
     * @return An Expr for the given parameters. null if nameNode or valueNode is null.
     */
    public Expr createAssignment(Expr nameNode, Expr valueNode) {
        if (nameNode == null || valueNode == null) {
            return null;
        }

        String name = ((StringValue) nameNode).getValue();
        FrameSlot frameSlot = frameDescriptor.findOrAddFrameSlot(name);
        lexicalScope.locals.put(name, frameSlot);
        final Expr result = new WriteLocalVariable(valueNode, frameSlot);

//        if (valueNode.hasSource()) {
//            final int start = nameNode.getSourceCharIndex();
//            final int length = valueNode.getSourceEndIndex() - start;
//            result.setSourceSection(start, length);
//        }

        return result;
    }

    /**
     * Returns a {@link SLReadLocalVariableNode} if this read is a local variable or a
     * {@link SLFunctionLiteralNode} if this read is global. In SL, the only global names are
     * functions.
     *
     * @param nameNode The name of the variable/function being read
     * @return either:
     * <ul>
     * <li>A SLReadLocalVariableNode representing the local variable being read.</li>
     * <li>A SLFunctionLiteralNode representing the function definition.</li>
     * <li>null if nameNode is null.</li>
     * </ul>
     */
    public Expr createRead(Expr nameNode) {
        if (nameNode == null) {
            return null;
        }

        String name = ((StringValue) nameNode).getValue();
        final Expr result;
        final FrameSlot frameSlot = lexicalScope.locals.get(name);
        if (frameSlot != null) {
            /* Read of a local variable. */
            result = new ReadLocalVariable(frameSlot);
        } else {
            /* Read of a global name. In our language, the only global names are functions. */
            result = new FunctionLiteral(language, name);
        }
//        result.setSourceSection(nameNode.getSourceCharIndex(), nameNode.getSourceLength());
        return result;
    }

    public Expr createStringLiteral(Token literalToken, boolean removeQuotes) {
        /* Remove the trailing and ending " */
        String literal = literalToken.getText();
        if (removeQuotes) {
            assert literal.length() >= 2 && literal.startsWith("\"") && literal.endsWith("\"");
            literal = literal.substring(1, literal.length() - 1);
        }

        final StringValue result = new StringValue(literal.intern());
        srcFromToken(result, literalToken);
        return result;
    }

    public Expr createNumericLiteral(Token literalToken) {
        Expr result;
//        try {
        /* Try if the literal is small enough to fit into a long value. */
        result = new IntValue(Integer.parseInt(literalToken.getText()));
//        } catch (NumberFormatException ex) {
//            /* Overflow of long value, so fall back to BigInteger. */
//            result = new SLBigIntegerLiteralNode(new BigInteger(literalToken.getText()));
//        }
        srcFromToken(result, literalToken);
        return result;
    }

    public Expr createParenExpression(Expr expressionNode, int start, int length) {
        if (expressionNode == null) {
            return null;
        }
//
//        final SLParenExpressionNode result = new SLParenExpressionNode(expressionNode);
//        result.setSourceSection(start, length);
        return expressionNode;
    }

    /**
     * Returns an {@link SLReadPropertyNode} for the given parameters.
     *
     * @param receiverNode The receiver of the property access
     * @param nameNode     The name of the property being accessed
     * @return An Expr for the given parameters. null if receiverNode or nameNode is
     * null.
     */
    public Expr createReadProperty(Expr receiverNode, Expr nameNode) {
//        if (receiverNode == null || nameNode == null) {
//            return null;
//        }
//
//        final Expr result = SLReadPropertyNodeGen.create(receiverNode, nameNode);
//
//        final int startPos = receiverNode.getSourceCharIndex();
//        final int endPos = nameNode.getSourceEndIndex();
//        result.setSourceSection(startPos, endPos - startPos);
//
//        return result;

        // TODO
        return null;
    }

    /**
     * Returns an {@link SLWritePropertyNode} for the given parameters.
     *
     * @param receiverNode The receiver object of the property assignment
     * @param nameNode     The name of the property being assigned
     * @param valueNode    The value to be assigned
     * @return An Expr for the given parameters. null if receiverNode, nameNode or
     * valueNode is null.
     */
    public Expr createWriteProperty(Expr receiverNode, Expr nameNode, Expr valueNode) {
//        if (receiverNode == null || nameNode == null || valueNode == null) {
//            return null;
//        }
//
//        final Expr result = SLWritePropertyNodeGen.create(receiverNode, nameNode, valueNode);
//
//        final int start = receiverNode.getSourceCharIndex();
//        final int length = valueNode.getSourceEndIndex() - start;
//        result.setSourceSection(start, length);
//
//        return result;


        // TODO
        return null;
    }

    /**
     * Creates source description of a single token.
     */
    private static void srcFromToken(Stmt node, Token token) {
//        node.setSourceSection(token.getStartIndex(), token.getText().length());
    }

    /**
     * Checks whether a list contains a null.
     */
    private static boolean containsNull(List<?> list) {
        for (Object e : list) {
            if (e == null) {
                return true;
            }
        }
        return false;
    }

}
