package fr.mleduc.simplelanguage.revisitor.model;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import fr.mleduc.simplelanguage.revisitor.lang.SLRLanguage;
import fr.mleduc.simplelanguage.revisitor.model.stmt.expr.Expr;
import fr.mleduc.simplelanguage.revisitor.revisitors.ExecSLRevisitor;
import fr.mleduc.simplelanguage.revisitor.revisitors.semantics.ExprT;

import static com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

@NodeInfo(language = SLRLanguage.ID, description = "root")
public class Root extends RootNode {

    @Child
    private Expr bodyNode;

    /**
     * The name of the function, for printing purposes only.
     */
    private final String name;

    @CompilationFinal
    private boolean isCloningAllowed;


    public Root(SLRLanguage language, FrameDescriptor frameDescriptor, Expr bodyNode, String name) {
        super(language, frameDescriptor);
        this.bodyNode = bodyNode;
        this.name = name;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        final ExecSLRevisitor execSLRevisitor = new ExecSLRevisitor() {
        };
        ExprT $ = execSLRevisitor.$(bodyNode);
        return $.eval(frame);

    }

    public Expr getBodyNode() {
        return bodyNode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCloningAllowed() {
        return isCloningAllowed;
    }
}
