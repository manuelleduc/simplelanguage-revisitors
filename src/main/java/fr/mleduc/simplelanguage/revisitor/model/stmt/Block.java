package fr.mleduc.simplelanguage.revisitor.model.stmt;

import com.oracle.truffle.api.nodes.NodeInfo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@NodeInfo(description = "block")
public class Block extends Stmt {
    @Children
    private Stmt[] stmts;

    public Block(Stmt[] stmts) {
        this.stmts = stmts;
    }

    public Stmt[] getStmts() {
        return stmts;
    }

    public Collection<Stmt> getStatements() {
        return Collections.unmodifiableCollection(Arrays.asList(stmts));
    }
}
