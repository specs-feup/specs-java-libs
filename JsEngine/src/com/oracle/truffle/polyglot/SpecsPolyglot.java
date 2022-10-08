package com.oracle.truffle.polyglot;

import com.oracle.truffle.js.runtime.GraalJSException;
import com.oracle.truffle.js.runtime.builtins.JSErrorObject;

public class SpecsPolyglot {

    public static GraalJSException getException(Object possibleError) {
        if (!(possibleError instanceof PolyglotWrapper)) {
            return null;
        }

        var hostWrapper = (PolyglotWrapper) possibleError;

        var guestObject = hostWrapper.getGuestObject();

        if (!(guestObject instanceof JSErrorObject)) {
            return null;
        }
        // System.out.println("GUEST: " + ((JSErrorObject) guestObject).ownPropertyKeys());
        // System.out.println("STACK: " + ((JSError) ((JSErrorObject) guestObject).get("stack")).);
        return ((JSErrorObject) guestObject).getException();
    }
}
