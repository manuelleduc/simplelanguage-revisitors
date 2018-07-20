package fr.mleduc.simplelanguage.revisitor.model;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import fr.mleduc.simplelanguage.revisitor.lang.SLContext;
import fr.mleduc.simplelanguage.revisitor.lang.SLRLanguage;
import fr.mleduc.simplelanguage.revisitor.model.objects.SLNull;

import java.util.Map;

public class EvalRoot extends RootNode {

    private final Map<String, RootCallTarget> functions;
    private final RootCallTarget rootFunction;
    @CompilerDirectives.CompilationFinal
    private boolean registered;

    private final TruffleLanguage.ContextReference<SLContext> reference;

//    @Child
//    private DirectCallNode mainCallNode;

    public EvalRoot(SLRLanguage language, RootCallTarget rootFunction, Map<String, RootCallTarget> functions) {
        super(null);
        this.functions = functions;
//        this.mainCallNode = rootFunction != null ? DirectCallNode.create(rootFunction) : null;
        this.rootFunction = rootFunction;
        this.reference = language.getContextReference();
    }

    @Override
    public Object execute(VirtualFrame frame) {

        if (!registered) {
            /* Function registration is a slow-path operation that must not be compiled. */
            CompilerDirectives.transferToInterpreterAndInvalidate();
            reference.get().getFunctionRegistry().register(functions);
            registered = true;
        }

        Object[] arguments = frame.getArguments();
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = SLContext.fromForeignValue(arguments[i]);
        }
        //return mainCallNode.call(arguments);

        if (this.rootFunction != null)
            return this.rootFunction.getRootNode().execute(frame);
        else
            return SLNull.SINGLETON;
    }
}
