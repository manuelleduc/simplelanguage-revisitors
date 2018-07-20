package fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values;

import com.oracle.truffle.api.nodes.NodeInfo;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;


@NodeInfo(description = "string-value")
public class StringValue extends Expr {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
