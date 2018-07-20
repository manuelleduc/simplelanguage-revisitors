package fr.mleduc.simplelanguage.revisitor.model.stmt.expr;

import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import fr.mleduc.simplelanguage.revisitor.lang.SLContext;
import fr.mleduc.simplelanguage.revisitor.lang.SLRLanguage;

public class FunctionLiteral extends Expr {

    private final String functionName;
    private final ContextReference<SLContext> reference;

    public FunctionLiteral(SLRLanguage language, String functionName) {
        this.functionName = functionName;
        this.reference = language.getContextReference();


    }

    public String getFunctionName() {
        return functionName;
    }

    public ContextReference<SLContext> getReference() {
        return reference;
    }
}
