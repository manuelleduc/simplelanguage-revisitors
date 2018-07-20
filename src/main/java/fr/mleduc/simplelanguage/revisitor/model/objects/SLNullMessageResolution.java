package fr.mleduc.simplelanguage.revisitor.model.objects;

import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = SLNull.class)
public class SLNullMessageResolution {
    /*
     * An SL function resolves the IS_NULL message.
     */
    @Resolve(message = "IS_NULL")
    public abstract static class SLForeignIsNullNode extends Node {

        public Object access(Object receiver) {
            return SLNull.SINGLETON == receiver;
        }
    }

    @CanResolve
    public abstract static class CheckNull extends Node {

        protected static boolean test(TruffleObject receiver) {
            return receiver instanceof SLNull;
        }
    }
}
