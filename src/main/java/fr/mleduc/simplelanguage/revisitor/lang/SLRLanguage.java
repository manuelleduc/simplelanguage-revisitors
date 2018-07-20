package fr.mleduc.simplelanguage.revisitor.lang;

import com.oracle.truffle.api.*;
import com.oracle.truffle.api.source.Source;
import fr.mleduc.simplelanguage.revisitor.model.EvalRoot;
import fr.mleduc.simplelanguage.revisitor.model.Start;
import fr.mleduc.simplelanguage.revisitor.parser.SimpleLanguageParser;

import java.util.Map;

@TruffleLanguage.Registration(id = SLRLanguage.ID, name = "SLR", mimeType = SLRLanguage.MIME_TYPE)
public final class SLRLanguage extends com.oracle.truffle.api.TruffleLanguage<SLContext> {

    public static final String ID = "slr";
    public static final String MIME_TYPE = "application/x-slr";

    public SLRLanguage() {

    }


    @Override
    protected SLContext createContext(Env env) {
        return new SLContext(this, env);
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return object instanceof Start;
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        final Source source = request.getSource();

        final Map<String, RootCallTarget> functions = SimpleLanguageParser.parseSL(this, source);

        final RootCallTarget main = functions.get("main");

        return Truffle.getRuntime().createCallTarget(new EvalRoot(this, main, functions));
    }

    @Override
    protected Iterable<Scope> findTopScopes(SLContext context) {
        return context.getTopScopes();
    }
}
