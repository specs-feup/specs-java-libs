/*
 * Copyright 2013 SPeCS.
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
package org.specs.generators.java.classtypes;

import java.util.List;

import org.specs.generators.java.members.Field;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.utils.UniqueList;
import org.specs.generators.java.utils.Utils;

import tdrc.utils.StringUtils;

/**
 * Java interface generation
 * 
 * @author Tiago @
 */
public class Interface extends ClassType {
    private List<JavaType> interfaces;

    // Fields and methods pertaining to the java interface
    private List<Field> fields;
    private List<Method> methods;

    /**
     * Create a public interface with name and package
     * 
     * @param name
     *            the name for the interface
     * @param interfacePackage
     *            the interface package
     */
    public Interface(String name, String interfacePackage) {
	super(name, interfacePackage);
	init();
    }

    /**
     * Initialize the Interface' lists
     */
    private void init() {
	interfaces = new UniqueList<>();
	fields = new UniqueList<>();
	methods = new UniqueList<>();
    }

    /**
     * Generate the corresponding java interface code, containing the package, imports, fields, methods, etc.
     * 
     * @param indentation
     *            level of indentation
     * @return the generated java interface code
     */
    @Override
    public StringBuilder generateCode(int indentation) {
	final StringBuilder interfaceGen = generateClassHeader(indentation);
	// if (!interfacePackage.isEmpty()) {
	// interfaceGen.append("package ");
	// interfaceGen.append(interfacePackage);
	// interfaceGen.append(";\n\n");
	// }
	// for (final String importPack : imports) {
	// interfaceGen.append("import ");
	// interfaceGen.append(importPack);
	// interfaceGen.append(";\n");
	// }
	// interfaceGen.append("\n");
	// interfaceGen.append(javaDocComment.generateCode(0));
	// interfaceGen.append("\n");
	//
	// for (final Annotation annot : annotations) {
	// interfaceGen.append(annot);
	// interfaceGen.append("\n");
	// }
	//
	// interfaceGen.append(privacy);
	// interfaceGen.append(" ");
	interfaceGen.append("interface ");
	interfaceGen.append(getName());
	if (!interfaces.isEmpty()) {
	    interfaceGen.append(" extends ");
	    final String joinedInterfaces = StringUtils.join(interfaces, JavaType::getSimpleType, ", ");
	    interfaceGen.append(joinedInterfaces);
	}
	interfaceGen.append(" ");
	interfaceGen.append("{\n");
	interfaceGen.append(Utils.indent(1));
	interfaceGen.append("//Fields\n");
	for (final Field field : fields) {
	    final StringBuilder fieldBuf = field.generateCode(1);
	    interfaceGen.append(fieldBuf);
	    interfaceGen.append("\n");
	}
	interfaceGen.append("\n");
	interfaceGen.append(Utils.indent(1));
	interfaceGen.append("//Methods\n");
	for (final Method method : methods) {
	    final StringBuilder methodBuf = method.generateCode(1);
	    interfaceGen.append(methodBuf);
	    interfaceGen.append("\n");
	}

	interfaceGen.append(generateClassTail(indentation));
	return interfaceGen;
    }

    /**
     * Add a new extended interface to the interface. This method automatically adds the required import for the added
     * interface
     * 
     * @param interfaceinterface
     *            the new interface
     * @return true if the interface was successfully added
     */
    public boolean addInterface(JavaType interfaceinterface) {
	final boolean isAdded = interfaces.add(interfaceinterface);
	if (isAdded) {

	    addImport(interfaceinterface);
	}
	return isAdded;
    }

    /**
     * Removes an interface from the interface. This does not remove automatically the required import related to the
     * removed interface
     * 
     * @param interfaceinterface
     *            the interface to remove
     * @return true if the interface was successfully removed
     */
    public boolean removeInterface(String interfaceinterface) {
	return interfaces.remove(interfaceinterface);
    }

    /**
     * Add a new field to the interface
     * 
     * @param field
     *            the new field
     * @return true if the field was successfully added
     */
    public boolean addField(Field field) {
	final boolean ret = fields.add(field);
	if (ret) {
	    field.setDefaultInitializer(true);
	    addImport(field.getType());

	}
	return ret;
    }

    /**
     * Removes a field from the interface
     * 
     * @param field
     *            the field to remove
     * @return true if the field was successfully removed
     */
    public boolean removeField(Field field) {
	return fields.remove(field);
    }

    /**
     * Add a new method to the interface. THis method automatically adds the imports required for the return type and
     * the arguments. Note that if the method is updated (e.g.: change return type or add arguments) the imports are not
     * updated.
     * 
     * @param method
     *            the new method
     * @return true if the method was successfully added
     */
    public boolean addMethod(Method method) {
	final boolean ret = methods.add(method);
	if (ret) {
	    method.setBody(false);

	    addImport(method.getReturnType());
	    // Add the imports for the argument of the method
	    method.getParams().stream().forEach(arg -> addImport(arg.getClassType()));
	}
	return ret;
    }

    /**
     * Removes a method from the interface
     * 
     * @param method
     *            the method to remove
     * @return true if the method was successfully removed
     */
    public boolean removeMethod(Method method) {
	return methods.remove(method);
    }

}
