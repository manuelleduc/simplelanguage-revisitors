package fr.mleduc.simplelanguage.revisitor.revisitors;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.profiles.ConditionProfile;
import fr.mleduc.simplelanguage.revisitor.model.*;
import fr.mleduc.simplelanguage.revisitor.model.objects.SLNull;
import fr.mleduc.simplelanguage.revisitor.model.stmt.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.Return;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.*;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Minus;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.IntValue;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values.StringValue;
import fr.mleduc.simplelanguage.revisitor.revisitor.SLRevisitor;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLBreakException;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLContinueException;
import fr.mleduc.simplelanguage.revisitor.revisitors.exceptions.SLReturnException;
import fr.mleduc.simplelanguage.revisitor.revisitors.semantics.*;


import java.util.Arrays;
import java.util.Objects;

public interface ExecSLRevisitor extends SLRevisitor<AndT, BinaryT, BlockT, BoolValueT, BreakT, ContinueT, DivideT,
        EqualT, EvalRootT, ExprT, FunDefT, FunctionBodyT, FunctionLiteralT, GreaterT, GreaterEqualT, IfT, IntValueT, InvokeT,
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
            return new RuntimeException("GreaterEqual type error");
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
            return new RuntimeException("Minus type error");
        };
    }

    @Override
    default BlockT _block(Block it) {
        return new BlockT() {
            @Override
            @ExplodeLoop
            public void execute(VirtualFrame frame) {
                CompilerAsserts.compilationConstant(it.getStmts().length);
                for (Stmt stmt : it.getStatements())
                    $(stmt).execute(frame);
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
        return frame -> Arrays.stream(it.getFunDefs()).filter(it1 -> Objects.equals(it1.getName(), "main")).findAny()
                .map(f -> $(f).call(frame))
                .orElseThrow(() -> new RuntimeException("No main method found"));
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
            return new RuntimeException("Lower type error");
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
            return new RuntimeException("LowerEqual type error");
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
            return new RuntimeException("Multiply type error");
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
            return new RuntimeException("And type error");
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
            return new RuntimeException("Divide type error");
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
            return new RuntimeException("LowerEqual type error");
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
        return null;
    }

    @Override
    default BreakT _break(Break it) {
        return null;
    }

    @Override
    default PlusT _plus(Plus it) {
        return null;
    }

    @Override
    default StartT _start(Start it) {
        return null;
    }

    @Override
    default FunDefT _funDef(FunDef it) {
        return (frame) -> {

//            VirtualFrame nf = Truffle.getRuntime().createVirtualFrame(args, frame.getFrameDescriptor());

            try {
                $(it.getBlock()).execute(frame);
            } catch (SLReturnException e) {
                return e.getResult();
            }
            return SLNull.SINGLETON;
        };
    }

    @Override
    default RootT _root(Root it) {
        return null;
    }

    @Override
    default ReadArgumentT _readArgument(ReadArgument it) {
        return null;
    }

    @Override
    default UnboxT _unbox(Unbox it) {
        return null;
    }

    @Override
    default FunctionLiteralT _functionLiteral(FunctionLiteral it) {
        return null;
    }

    @Override
    default WriteLocalVariableT _writeLocalVariable(WriteLocalVariable it) {
        return null;
    }

    @Override
    default InvokeT _invoke(Invoke it) {
        return null;
    }

    @Override
    default ReadLocalVariableT _readLocalVariable(ReadLocalVariable it) {
        return null;
    }

    @Override
    default FunctionBodyT _functionBody(FunctionBody it) {
        return null;
    }

    @Override
    default NotT _not(Not it) {
        return null;
    }

    @Override
    default EvalRootT _evalRoot(EvalRoot it) {
        return null;
    }
}
