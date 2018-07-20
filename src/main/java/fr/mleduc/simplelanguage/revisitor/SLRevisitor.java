package fr.mleduc.simplelanguage.revisitor;

import fr.mleduc.revisitor.annotation.processor.Revisitor;

@Revisitor(packages = {
        "fr.mleduc.simplelanguage.revisitor.model",
        "fr.mleduc.simplelanguage.revisitor.model.stmt",
        "fr.mleduc.simplelanguage.revisitor.model.stmt.expr",
        "fr.mleduc.simplelanguage.revisitor.model.stmt.expr.values"
}, name = "SimpleLanguage")
public class SLRevisitor {

}
