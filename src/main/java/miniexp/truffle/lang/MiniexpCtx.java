package miniexp.truffle.lang;

import com.oracle.truffle.api.TruffleLanguage;

public class MiniexpCtx {
    private final TruffleLanguage.Env env;

    public MiniexpCtx(TruffleLanguage.Env env) {
        this.env = env;
    }
}
