package miniexp.truffle.revisitors;

import miniexp.truffle.model.FunDef;
import miniexp.truffle.model.Program;
import miniexp.truffle.model.Start;
import miniexp.truffle.model.objects.SLNull;
import miniexp.truffle.model.stmt.*;
import miniexp.truffle.model.stmt.expr.*;
import miniexp.truffle.model.stmt.expr.values.BoolValue;
import miniexp.truffle.model.stmt.expr.values.IntValue;
import miniexp.truffle.model.stmt.expr.values.StringValue;
import miniexp.truffle.revisitors.exceptions.SLReturnException;

public interface ExecSLRevisitor extends miniexp.truffle.revisitor.SLRevisitor<AndT, BlockT, BoolValueT, BreakT,
        ContinueT, DivideT, EqualT, ExprT, FunDefT, GreaterT, GreaterEqualT, IfT, IntValueT, LowerT, LowerEqualT,
        MinusT, MultiplyT, NotEqualT, OrT, PlusT, ProgramT, ReturnT, StartT, StmtT, StringValueT, WhileT> {
    @Override
    default IntValueT _intValue(IntValue it) {
        return it::getValue;
    }

    @Override
    default ReturnT _return(Return it) {
        return () -> {
            final Object result;
            if (it.getExpr() != null) {
                result = $(it.getExpr()).eval();
            } else {
                result = SLNull.SINGLETON;
            }
            throw new SLReturnException(result);
        };
    }

    @Override
    default GreaterEqualT _greaterEqual(GreaterEqual it) {
        return () -> {
            //$(it.getLeft()).eval().compareTo()
            return null;
        };
    }

    @Override
    default StringValueT _stringValue(StringValue it) {
        return null;
    }

    @Override
    default MinusT _minus(Minus it) {
        return null;
    }

    @Override
    default BlockT _block(Block it) {
        return null;
    }

    @Override
    default NotEqualT _notEqual(NotEqual it) {
        return null;
    }

    @Override
    default ProgramT _program(Program it) {
        return null;
    }

    @Override
    default IfT _if(If it) {
        return null;
    }

    @Override
    default LowerT _lower(Lower it) {
        return null;
    }

    @Override
    default LowerEqualT _lowerEqual(LowerEqual it) {
        return null;
    }

    @Override
    default MultiplyT _multiply(Multiply it) {
        return null;
    }

    @Override
    default AndT _and(And it) {
        return null;
    }

    @Override
    default DivideT _divide(Divide it) {
        return null;
    }

    @Override
    default EqualT _equal(Equal it) {
        return null;
    }

    @Override
    default GreaterT _greater(Greater it) {
        return null;
    }

    @Override
    default BoolValueT _boolValue(BoolValue it) {
        return null;
    }

    @Override
    default ContinueT _continue(Continue it) {
        return null;
    }

    @Override
    default WhileT _while(While it) {
        return null;
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
        return null;
    }
}
