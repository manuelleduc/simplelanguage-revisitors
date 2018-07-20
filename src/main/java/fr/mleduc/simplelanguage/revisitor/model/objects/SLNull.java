package fr.mleduc.simplelanguage.revisitor.model.objects;

import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;

public final class SLNull implements TruffleObject {

    public static final SLNull SINGLETON = new SLNull();

    private SLNull() {}

    @Override
    public ForeignAccess getForeignAccess() {
        return SLNullMessageResolutionForeign.ACCESS;
    }
}
