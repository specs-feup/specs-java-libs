package com.oracle.truffle.polyglot;

import com.oracle.truffle.js.runtime.GraalJSException;
import com.oracle.truffle.js.runtime.builtins.JSErrorObject;

public class SpecsPolyglot {

	public static GraalJSException getException(Object possibleError) {
		if(!(possibleError instanceof HostWrapper)) {
			return null;
		}
		
		var hostWrapper = (HostWrapper) possibleError;
		
		var guestObject = hostWrapper.getGuestObject();
		
		if(!(guestObject instanceof JSErrorObject)) {
			return null;
		}
	
		return ((JSErrorObject) guestObject).getException();
	}
}
