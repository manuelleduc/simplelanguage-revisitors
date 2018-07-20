package fr.mleduc.simplelanguage.revisitor.revisitors;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.BranchProfile;
import com.oracle.truffle.api.profiles.ConditionProfile;
import fr.mleduc.simplelanguage.revisitor.lang.FunDef;
import fr.mleduc.simplelanguage.revisitor.lang.SLContext;
import fr.mleduc.simplelanguage.revisitor.model.EvalRoot;
import fr.mleduc.simplelanguage.revisitor.model.Program;
import fr.mleduc.simplelanguage.revisitor.model.Root;
import fr.mleduc.simplelanguage.revisitor.model.Start;
import fr.mleduc.simplelanguage.revisitor.model.objects.SLNull;
import fr.mleduc.simplelanguage.revisitor.model.stmt.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.BoolValue;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.IntValue;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.StringValue;
import fr.mleduc.simplelanguage.revisitor.revisitor.SLRevisitor;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLBreakException;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLContinueException;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLReturnException;
import fr.mleduc.simplelanguage.revisitor.revisitors.semantics.*;

import java.util.Objects;

public interface ExecSLRevisitor extends SLRevisitor<AndT, BinaryT, BlockT, BoolValueT, BreakT, ContinueT, DivideT,
        EqualT, EvalRootT, ExprT, FunctionBodyT, FunctionLiteralT, GreaterT, GreaterEqualT, IfT, IntValueT, InvokeT,
        LowerT, LowerEqualT, MinusT, MultiplyT, NotT, NotEqualT, OrT, PlusT, ProgramT, ReadArgumentT,
        ReadLocalVariableT, ReturnT, RootT, StartT, StmtT, StringValueT, UnboxT, WhileT, WriteLocalVariableT> {
    @Override
    default IntValueT _intValue(IntValue it) {
        return frame -> it.getValue();
    }

    @Override
    default ReturnT _return(Return it) {
        return frame -> {
            final Object result;
            if (it.getExpr() != null) {
                result = $(it.getExpr()).eval(frame);
            } else {
                result = SLNull.SINGLETON;
            }
            throw new SLReturnException(result);
        };
    }

    @Override
    default GreaterEqualT _greaterEqual(GreaterEqual it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left >= (Integer) right;
            }
            throw new RuntimeException("GreaterEqual type error");
        };
    }

    @Override
    default StringValueT _stringValue(StringValue it) {
        return frame -> it.getValue();
    }

    @Override
    default MinusT _minus(Minus it) {
        return frame -> {
            // TODO: probably slow
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left - (Integer) right;
            }
            throw new RuntimeException("Minus type error");
        };
    }

    @Override
    default BlockT _block(Block it) {
        return new BlockT() {
            @Override
            @ExplodeLoop
            public void execute(VirtualFrame frame) {
                CompilerAsserts.compilationConstant(it.getStmts().length);
                for (Stmt stmt : it.getStatements()) {
                    StmtT $ = $(stmt);
                    $.execute(frame);
                }
            }
        };
    }

    @Override
    default NotEqualT _notEqual(NotEqual it) {
        return frame -> {
            // TODO: probably slow
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            return !Objects.equals(left, right);
        };
    }


    @Override
    default ProgramT _program(Program it) {
//        return frame -> Arrays.stream(it.getFunDefs()).filter(it1 -> Objects.equals(it1.getName(), "main")).findAny()
//                .map(f -> f.)
//                .orElseThrow(() -> new RuntimeException("No main method found"));

        return new ProgramT() {
            @Override
            public void run(VirtualFrame frame) {
                throw new RuntimeException("Not implemented program");
            }
        };
    }

    @Override
    default IfT _if(If it) {
        return new IfT() {
            private final ConditionProfile condition = ConditionProfile.createCountingProfile();

            @Override
            public void execute(VirtualFrame frame) {
                final boolean cond = evaluateCondition(frame);
                if (condition.profile(cond)) {
                    $(it.getThenBranch()).execute(frame);
                } else if (it.getElseBranch() != null) {
                    $(it.getElseBranch()).execute(frame);
                }

            }

            private boolean evaluateCondition(VirtualFrame frame) {
                final Object eval = $(it.getCond()).eval(frame);
                if (eval instanceof Boolean)
                    return (boolean) eval;
                else throw new RuntimeException("If type error, boolean expected");
            }
        };
    }

    @Override
    default LowerT _lower(Lower it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left < (Integer) right;
            }
            throw new RuntimeException("Lower type error");
        };
    }

    @Override
    default LowerEqualT _lowerEqual(LowerEqual it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left <= (Integer) right;
            }
            throw new RuntimeException("LowerEqual type error");
        };
    }

    @Override
    default MultiplyT _multiply(Multiply it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left * (Integer) right;
            }
            throw new RuntimeException("Multiply type error");
        };
    }

    @Override
    default AndT _and(And it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Boolean && right instanceof Boolean) {
                return (boolean) left && (boolean) right;
            }
            throw new RuntimeException("And type error");
        };
    }

    @Override
    default DivideT _divide(Divide it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (int) ((int) left / (int) right);
            }
            throw new RuntimeException("Divide type error");
        };
    }

    @Override
    default EqualT _equal(Equal it) {
        return frame -> {
            // TODO: probably slow
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            return Objects.equals(left, right);
        };
    }

    @Override
    default GreaterT _greater(Greater it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left > (Integer) right;
            }
            throw new RuntimeException("LowerEqual type error");
        };
    }

    @Override
    default BoolValueT _boolValue(BoolValue it) {
        return frame -> it.getValue();
    }

    @Override
    default ContinueT _continue(Continue it) {
        return frame -> {
            throw SLContinueException.SINGLETON;
        };
    }

    @Override
    default WhileT _while(While it) {
        return new WhileT() {
            @Override
            public void execute(VirtualFrame frame) {
                try {
                    while (!evaluateCondition(frame)) {
                        /* Normal exit of the loop when loop condition is false. */
                        try {
                            $(it.getBody()).execute(frame);
                        } catch (SLContinueException ex) {
                        }
                    }
                } catch (SLBreakException e) {
                }
            }

            private boolean evaluateCondition(VirtualFrame frame) {
                final Object eval = $(it.getCond()).eval(frame);
                if (eval instanceof Boolean)
                    return (boolean) eval;
                else throw new RuntimeException("If type error, boolean expected");
            }
        };
    }

    @Override
    default OrT _or(Or it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Boolean && right instanceof Boolean) {
                return (boolean) left || (boolean) right;
            }
            throw new RuntimeException("Or type error");
        };
    }

    @Override
    default BreakT _break(Break it) {
        return new BreakT() {
            @Override
            public void execute(VirtualFrame frame) {
                throw new RuntimeException("Not implemeneted");
            }
        };
    }

    @Override
    default PlusT _plus(Plus it) {
        return frame -> {
            final Object left = $(it.getLeft()).eval(frame);
            final Object right = $(it.getRight()).eval(frame);
            if (left instanceof Integer && right instanceof Integer) {
                return (int) left + (int) right;
            }
            throw new RuntimeException("Plus type error");
        };
    }

    @Override
    default StartT _start(Start it) {
        return new StartT() {
            @Override
            public int hashCode() {
                throw new RuntimeException("Not implemented start");
            }
        };
    }

//    @Override
//    default FunDefT _funDef(FunDef it) {
//        return (frame) -> {
//            try {
//                // FIXME: modular ?
//                it.getBlock().execute(frame);
//            } catch (SLReturnException e) {
//                return e.getResult();
//            }
//            return SLNull.SINGLETON;
//        };
//    }

    @Override
    default RootT _root(Root it) {
        return new RootT() {
        };
    }

    @Override
    default ReadArgumentT _readArgument(ReadArgument it) {
        return new ReadArgumentT() {

            private final BranchProfile outOfBoundsTaken = BranchProfile.create();

            @Override
            public Object eval(VirtualFrame frame) {
                Object[] args = frame.getArguments();

                final int index = it.getIndex();

                if (index < args.length) {
                    return args[index];
                } else {
                    /* In the interpreter, record profiling information that the branch was used. */
                    outOfBoundsTaken.enter();
                    /* Use the default null value. */
                    return SLNull.SINGLETON;
                }
            }
        };
    }

    @Override
    default UnboxT _unbox(Unbox it) {

        return new UnboxT() {
            @Override
            public Object eval(VirtualFrame frame) {
                Object val = $(it.getLeft()).eval(frame);

                if (val instanceof Integer) {
                    return val;
                } else if (val instanceof Boolean) {
                    return val;
                } else {
                    return val;
                }
            }
        };

    }

    @Override
    default FunctionLiteralT _functionLiteral(FunctionLiteral it) {
        return new FunctionLiteralT() {

            @CompilerDirectives.CompilationFinal
            private FunDef cachedFunction;

            @Override
            public Object eval(VirtualFrame frame) {
                if (cachedFunction == null) {
                    /* We are about to change a @CompilationFinal field. */
                    CompilerDirectives.transferToInterpreterAndInvalidate();
                    /* First execution of the node: lookup the function in the function registry. */
                    cachedFunction = it.getReference().get().getFunctionRegistry().lookup(it.getFunctionName(), true);
                }
                return cachedFunction;
            }
        };
    }

    @Override
    default WriteLocalVariableT _writeLocalVariable(WriteLocalVariable it) {
        return frame -> {
            final Object value = $(it.getValue()).eval(frame);
            final FrameSlot slot = it.getSlot();
            if (value instanceof String) {
                slot.setKind(FrameSlotKind.Object);
                frame.setObject(slot, value);
            } else if (value instanceof Integer) {
                slot.setKind(FrameSlotKind.Int);
                frame.setInt(slot, (Integer) value);
            } else if (value instanceof Boolean) {
                slot.setKind(FrameSlotKind.Boolean);
                frame.setBoolean(slot, (Boolean) value);
            }
            return value;
        };
    }

    @Override
    default InvokeT _invoke(Invoke it) {
        return frame -> {
            Object function = $(it.getFunctionNode()).eval(frame);

            /*
             * The number of arguments is constant for one invoke node. During compilation, the loop is
             * unrolled and the execute methods of all arguments are inlined. This is triggered by the
             * ExplodeLoop annotation on the method. The compiler assertion below illustrates that the
             * array length is really constant.
             */
            Expr[] argumentNodes = it.getArgumentNodes();
            CompilerAsserts.compilationConstant(argumentNodes.length);

            Object[] argumentValues = new Object[argumentNodes.length];
            for (int i = 0; i < argumentNodes.length; i++) {
                argumentValues[i] = $(argumentNodes[i]).eval(frame);
            }
            return it.getDispatchNode().executeDispatch(function, argumentValues);
        };
    }

    @Override
    default ReadLocalVariableT _readLocalVariable(ReadLocalVariable it) {
        return frame -> {
            if (!frame.isObject(it.getSlot())) {
                /*
                 * The FrameSlotKind has been set to Object, so from now on all writes to the local
                 * variable will be Object writes. However, now we are in a frame that still has an old
                 * non-Object value. This is a slow-path operation: we read the non-Object value, and
                 * write it immediately as an Object value so that we do not hit this path again
                 * multiple times for the same variable of the same frame.
                 */
                CompilerDirectives.transferToInterpreter();
                Object result = frame.getValue(it.getSlot());
                frame.setObject(it.getSlot(), result);
                return result;
            }

            Object objectSafe = FrameUtil.getObjectSafe(frame, it.getSlot());
            if(objectSafe == null) throw  new RuntimeException("variable is null");
            return objectSafe;
        };
    }

    @Override
    default FunctionBodyT _functionBody(FunctionBody it) {
        return new FunctionBodyT() {

            private final BranchProfile exceptionTaken = BranchProfile.create();
            private final BranchProfile nullTaken = BranchProfile.create();

            @Override
            public Object eval(VirtualFrame frame) {
                try {
                    /* Execute the function body. */
                    $(it.getBodyNode()).execute(frame);

                } catch (SLReturnException ex) {
                    /*
                     * In the interpreter, record profiling information that the function has an explicit
                     * return.
                     */
                    exceptionTaken.enter();
                    /* The exception transports the actual return value. */
                    return ex.getResult();
                }

                /*
                 * In the interpreter, record profiling information that the function ends without an
                 * explicit return.
                 */
                nullTaken.enter();
                /* Return the default null value. */
                return SLNull.SINGLETON;
            }
        };
    }

    @Override
    default NotT _not(Not it) {
        return frame -> {
            Object val = $(it.getChild()).eval(frame);
            if (val instanceof Boolean) return !(boolean) val;
            throw new RuntimeException("Type error Not");
        };
    }

    @Override
    default EvalRootT _evalRoot(EvalRoot it) {
        return new EvalRootT() {
        };
    }
}
