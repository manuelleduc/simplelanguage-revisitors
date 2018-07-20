package fr.mleduc.simplelanguage.revisitor.lang;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Scope;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.TruffleObject;

import java.util.Collections;

public class SLContext {
    private final TruffleLanguage.Env env;
    private final Iterable<Scope> topScopes;
    private final FunDefRegistry functionRegistry;
    private VirtualFrame frame;

    public SLContext(SLRLanguage language, TruffleLanguage.Env env) {
        this.env = env;
        this.functionRegistry = new FunDefRegistry(language);
        this.topScopes =  Collections.singleton(Scope.newBuilder("global", functionRegistry.getFunctionsObject()).build());
    }

    public static Object fromForeignValue(Object a) {
        if (a instanceof Integer || a instanceof String || a instanceof Boolean) {
            return a;
        } else if (a instanceof Character) {
            return String.valueOf(a);
        } else if (a instanceof Number) {
            return fromForeignNumber(a);
        } else if (a instanceof TruffleObject) {
            return a;
        } else if (a instanceof SLContext) {
            return a;
        }
        CompilerDirectives.transferToInterpreter();
        throw new IllegalStateException(a + " is not a Truffle value");
    }

    @CompilerDirectives.TruffleBoundary
    private static int fromForeignNumber(Object a) {
        return ((Number) a).intValue();
    }

    public Iterable<Scope> getTopScopes() {
        return topScopes;
    }

    public FunDefRegistry getFunctionRegistry() {
        return functionRegistry;
    }

    public void saveFrame(VirtualFrame frame) {
        this.frame = frame;
    }

    public VirtualFrame getFrame() {
        return frame;
    }
}
