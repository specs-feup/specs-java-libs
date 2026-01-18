package com.oracle.truffle.polyglot;

import com.oracle.truffle.js.runtime.GraalJSException;
import com.oracle.truffle.js.runtime.builtins.JSErrorObject;

/**
 * Utility class for working with the Truffle Polyglot API in GraalVM.
 * Provides methods for polyglot context management and language interoperability.
 */
public class SpecsPolyglot {

    /**
     * Retrieves the GraalJSException from a possible error object.
     *
     * @param possibleError The object that might represent an error.
     * @return The GraalJSException if the object is a valid error, otherwise null.
     */
    public static GraalJSException getException(Object possibleError) {
        if (!(possibleError instanceof PolyglotWrapper)) {
            return null;
        }

        var hostWrapper = (PolyglotWrapper) possibleError;

        var guestObject = hostWrapper.getGuestObject();

        if (!(guestObject instanceof JSErrorObject)) {
            return null;
        }

        return ((JSErrorObject) guestObject).getException();
    }
}
