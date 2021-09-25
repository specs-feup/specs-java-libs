package org.specs.JMatIOPlus;

import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.specs.MatlabIR.MatlabParsingUtils;

import com.jmatio.io.MatFileReader;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a .mat file.
 * 
 * 
 * @author Pedro Pinto
 * 
 */
public class MatFile {

    // The file handle
    private File file;

    // The list of variables
    private final List<MLArray> variables;

    // private final boolean isCoderMain;
    // private final MemoryLayout memoryLayout;

    /*
    public MatFile(File matlabInputFile) {
    
    }
    */

    public MatFile(File file, List<MLArray> variables) {
	this.file = file;
	this.variables = variables;
	// this.isCoderMain = isCoderMain;
	// this.memoryLayout = memoryLayout;
    }

    /**
     * 
     * @param matlabInputFile
     *            - the full path to the .mat file
     * @param isCoderMain
     *            - the name of the matlab source file
     * @param memoryLayout
     */
    /*
    public MatFile(File matlabInputFile, boolean isCoderMain, MemoryLayout memoryLayout) {
    this(matlabInputFile, new ArrayList<MLArray>(), isCoderMain, memoryLayout);
    // this.file = new File(matlabInputFile);
    // this.file = matlabInputFile;
    // this.variables = FactoryUtils.newArrayList();
    // this.isCoderMain = isCoderMain;
    }
    */
    /**
     * Constructor which uses Column Major as default MemoryLayout.
     * 
     * 
     * @param file
     *            - the file handler representing the .mat file
     */
    public MatFile(File file) {
	this(file, new ArrayList<MLArray>());
	// this.file = file;
	// this.variables = FactoryUtils.newArrayList();
    }

    /**
     * 
     * @param fullPath
     *            - the full path to the .mat file
     * @param fileName
     *            - the name of the matlab source file
     * @param variables
     *            - the list of variables of this .mat file
     */
    /*
    public MatFile(String fullPath, String fileName, List<MLArray> variables) {
    this(new File(fullPath, fileName), variables, false);
    //	this.file = new File(fullPath, fileName);
    //	this.variables = variables;
    }
    */
    /**
     * 
     * @param file
     *            - the file handler representing the .mat file
     * @param variables
     *            - the list of variables of this .mat file
     */
    /*
    public MatFile(File file, List<MLArray> variables) {
    this(file, variables, false);
    }
    */

    /**
     * @return the file
     */
    public File getFile() {
	return file;
    }

    public void setFile(File file) {
	this.file = file;
    }

    /**
     * @return the variables
     */
    public List<MLArray> getVariables() {
	return variables;
    }

    /**
     * @return the file's name
     */
    public String getFileName() {
	return file.getName();
    }

    /**
     * @return the file's absolute path
     */
    public String getFilePath() {
	return file.getAbsolutePath();
    }

    /**
     * Adds a variable to the list of variables.
     * 
     * @param variable
     *            - the variable to add
     */
    public void addVariable(MLArray variable) {
	this.variables.add(variable);
    }

    /**
     * Writes the file to the disk.
     * 
     * @return the status of the operation, 0 if everything went fine and 1 otherwise
     */
    public int write() {

	int status = 0;

	try {

	    // If any of the parent folders of the file doesn't exist, create
	    // them
	    if (file.getParent() != null) {
		SpecsIo.mkdir(file.getParent());
	    }

	    // Write the file
	    MatFileWriter.write(file, variables);

	} catch (IOException e) {
	    status = 1;
	    SpecsLogs.warn("Error writting to file \"" + getFileName() + "\":\n"
		    + e.getMessage());
	}

	return status;
    }

    /**
     * Reads the contents of the mat file (.mat) and saves them to variables.
     * 
     * @return true if everything went fine and false otherwise
     */
    public boolean read() {

	MatFileReader reader = null;

	try {

	    // Read the file
	    reader = new MatFileReader(file);
	} catch (IOException e) {
	    SpecsLogs.warn("Error reading file '" + getFileName() + "':\n" + e.getMessage());
	    return false;
	} catch (BufferUnderflowException e) {
	    SpecsLogs.warn("JMatIO Buffer Underflow Exception while reading file '"
		    + getFileName() + "':\n");
	    e.printStackTrace();
	    return false;
	}
	// Get the contents
	Map<String, MLArray> matFileContents = reader.getContent();

	for (MLArray variable : matFileContents.values()) {
	    variables.add(variable);
	}

	return true;
    }

    /**
     * Writes the contents of this MatFile to a matlab source file (.mat -> .m).
     * 
     * @param matlabFile
     *            - the matlab source file
     * @return true if succeeded, false otherwise
     */
    public boolean writeToMatlabFile(File matlabFile) {

	String matlabCode = contentsAsMatlabCode();
	return SpecsIo.write(matlabFile, matlabCode);
    }

    /**
     * Writes the contents of this MatFile to a matlab source file (.mat -> .m).
     * 
     * @param fullPath
     *            - the full path to the matlab source file
     * @param fileName
     *            - the name of the matlab source file
     * @return true if succeeded, false otherwise
     */
    public boolean writeToMatlabFile(String fullPath, String fileName) {

	return writeToMatlabFile(new File(fullPath, fileName));
    }

    /**
     * Writes the contents of this MatFile to a matlab source file using a function (.mat -> .m). The name of the
     * function will be <b>the name of the matlab file</b>.
     * 
     * @param matlabFile
     *            - the matlab source file
     * @return true if succeeded, false otherwise
     */
    public boolean writeToMatlabFileAsFunction(File matlabFile) {

	String matlabCode = contentsAsMatlabFunctionCode(SpecsIo.removeExtension(matlabFile
		.getName()));
	return SpecsIo.write(matlabFile, matlabCode);
    }

    /**
     * Writes the contents of this MatFile to a matlab source file using a function (.mat -> .m). The name of the
     * function will be <b>the name of the matlab file (fileName)</b>.
     * 
     * @param fullPath
     *            - the full path to the matlab source file
     * @param fileName
     *            - the name of the matlab source file
     * @return true if succeeded, false otherwise
     */
    public boolean writeToMatlabFileAsFunction(String fullPath, String fileName) {

	return writeToMatlabFileAsFunction(new File(fullPath, fileName));
    }

    /**
     * Returns a string with matlab code with the declaration and initialization of the variables of this mat file.
     * 
     * @return a string with the matlab source code
     */
    public String contentsAsMatlabCode() {

	StringBuilder builder = new StringBuilder();

	for (MLArray variable : variables) {
	    builder.append(variableInitializationCode(variable));
	    builder.append("\n\n");
	}

	return builder.toString();
    }

    /**
     * Returns a string with matlab code of a function that declares, initializes and returns the variables of this mat
     * file.
     * 
     * @param functionName
     *            - the name of the function
     * 
     * @return a string with the matlab function source code
     */
    public String contentsAsMatlabFunctionCode(String functionName) {

	StringBuilder builder = new StringBuilder();

	List<String> doubleVariables = SpecsFactory.newArrayList();

	// Get the names of the variables that are of type Double
	for (MLArray variable : variables) {
	    if (MLDouble.class.isInstance(variable)) {
		doubleVariables.add(variable.getName());
	    }
	}

	builder.append("function ");
	if (doubleVariables.size() > 0) {
	    builder.append("[");

	    builder.append(doubleVariables.get(0));
	    for (int i = 1; i < doubleVariables.size(); i++) {

		builder.append(", ");
		builder.append(doubleVariables.get(i));
	    }

	    builder.append("] ");
	}

	builder.append(" = ");
	builder.append(functionName);
	builder.append("()");

	builder.append("\n\n");

	builder.append(contentsAsMatlabCode());

	builder.append("\n\nend\n\n");

	return builder.toString();
    }

    /**
     * Returns a string with matlab code of a function that declares, initializes and returns the variables of this mat
     * file. The name of the function will be <b>the name of the mat file</b>.
     * 
     * @return a string with the matlab function source code
     */
    public String contentsAsMatlabFunctionCode() {

	String functionName = SpecsIo.removeExtension(getFileName());

	return contentsAsMatlabFunctionCode(functionName);
    }

    /**
     * Returns the matlab source code for the initialization of the variable received as parameter.
     * </p>
     * <b>Assumes the variable is of type Double.</b>
     * </p>
     * 
     * @param aVariable
     *            - the variable whose code we want
     * @return a string with the matlab source code
     */
    private static String variableInitializationCode(MLArray aVariable) {

	if (!MLDouble.class.isInstance(aVariable)) {
	    SpecsLogs.warn("Variable \"" + aVariable.name + "\" is not of type Double.\n");
	    return "";
	}

	StringBuilder builder = new StringBuilder();

	MLDouble variable = (MLDouble) aVariable;
	int[] dimsArray = variable.getDimensions();

	List<Integer> dims = SpecsFactory.newArrayList();
	for (int i = 0; i < dimsArray.length; i++) {
	    dims.add(dimsArray[i]);
	}

	builder.append(variable.name);
	builder.append(" = ");

	builder.append("zeros(");
	builder.append(dims.get(0));
	for (int i = 1; i < dims.size(); i++) {
	    builder.append(", ");
	    builder.append(dims.get(i));
	}
	builder.append(");");
	builder.append("\n\n");

	for (int i = 0; i < variable.getSize(); i++) {

	    double value = variable.get(i);
	    // Call with i+1 because we're using zero indexed Java indexes
	    List<Integer> subscripts = MatlabParsingUtils.indexToSubscriptMatlab(dims, i + 1);

	    builder.append(variable.name);
	    builder.append("(");

	    builder.append(subscripts.get(0));
	    for (int j = 1; j < subscripts.size(); j++) {

		builder.append(", ");
		builder.append(subscripts.get(j));
	    }

	    builder.append(") = ");
	    builder.append(value);
	    builder.append(";\n");
	}

	return builder.toString();
    }

}
