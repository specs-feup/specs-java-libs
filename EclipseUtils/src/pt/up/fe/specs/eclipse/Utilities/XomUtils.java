/**
 * Copyright 2013 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class XomUtils {

    public static Document getDocument(File xmlFile) {
	return getDocument(xmlFile, false);
    }
    
    public static Document getDocument(File xmlFile, boolean testValidity) {
	String xmlContents = SpecsIo.read(xmlFile);
	
	if(xmlContents == null) {
	    return null;
	}
	
	return getDocument(xmlContents, testValidity);
	/*
	Document doc = null;
	try {
	    Builder parser = new Builder(testValidity);
	    // doc = parser.build(xmlContents);
	    doc = parser.build(xmlFile);
	} catch (ValidityException ex) {
	    LoggingUtils.msgInfo("Validity test not passed when parsing XML file '"+xmlFile.getName()+"'.");
	    doc = ex.getDocument();
	} catch (ParsingException ex) {
	    LoggingUtils.msgWarn("Parsing Exception:" + ex, ex);
	} catch (IOException ex) {
	    LoggingUtils.msgWarn("IOException:" + ex, ex);
	}

	return doc;
	*/
    }

    public static Document getDocument(String xmlContents, boolean testValidity) {
	Document doc = null;
	try {
	    Builder parser = new Builder(testValidity);
	    
	    // Convert String to StringReader
	    StringReader reader = new StringReader(xmlContents);
	    doc = parser.build(reader);
	} catch (ValidityException ex) {
	    SpecsLogs.msgInfo("Validity test not passed when parsing XML string.");
	    doc = ex.getDocument();
	} catch (ParsingException ex) {
	    SpecsLogs.msgWarn("Parsing Exception:" + ex, ex);
	} catch (IOException ex) {
	    SpecsLogs.msgWarn("IOException:" + ex, ex);
	}

	return doc;
    }

    
}
