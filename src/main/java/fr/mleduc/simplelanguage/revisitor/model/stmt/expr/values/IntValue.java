package fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values;

import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;


@NodeInfo(description = "int-value")
public class IntValue extends Expr {

    private Integer value;

    public IntValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
