package miniexp.truffle.model;

import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import miniexp.truffle.model.stmt.Block;

@NodeInfo(description = "fun-def")
public class FunDef extends Node {

    private String name;
    private String[] ids;
    @Child
    private Block block;


    public FunDef(String name, String[] ids, Block block) {
        this.name = name;
        this.ids = ids;
        this.block = block;
    }

    public String getName() {
        return name;
    }

    public String[] getIds() {
        return ids;
    }

    public Block getBlock() {
        return block;
    }
}
