package miniexp.truffle.model.stmt.expr.values;

import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.expr.Expr;

@NodeInfo(description = "bool-value")
public class BoolValue extends Expr {

    private Boolean value;

    public BoolValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}
