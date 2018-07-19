package miniexp.truffle.lang;

import com.oracle.truffle.api.TruffleLanguage;
import miniexp.truffle.model.Start;

public class MiniexpLang extends TruffleLanguage<MiniexpCtx> {
    @Override
    protected MiniexpCtx createContext(Env env) {
        return new MiniexpCtx(env);
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return object instanceof Start;
    }
}
