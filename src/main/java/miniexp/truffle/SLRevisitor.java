package miniexp.truffle;

import fr.mleduc.revisitor.annotation.processor.Revisitor;

@Revisitor(packages = {
        "miniexp.truffle.model", "miniexp.truffle.model.stmt", "miniexp.truffle.model.stmt.expr", "miniexp.truffle.model.stmt.expr.values"
}, name = "SimpleLanguage")
public class SLRevisitor {

}
