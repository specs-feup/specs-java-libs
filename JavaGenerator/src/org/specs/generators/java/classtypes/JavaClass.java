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

import java.util.Collection;
import java.util.List;

import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.members.Constructor;
import org.specs.generators.java.members.Field;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.utils.UniqueList;
import org.specs.generators.java.utils.Utils;

import tdrc.utils.StringUtils;

/**
 * Java class generation
 * 
 * @author Tiago
 * 
 */
public class JavaClass extends ClassType {

    private JavaType superClass;
    private List<JavaType> interfaces;

    // Fields and methods pertaining to the java class
    private List<Field> fields;
    private List<Method> methods;
    private List<Constructor> constructors;

    /**
     * Create a public class based on a {@link JavaType}
     *
     * @param name
     *            the name for the class
     * @param classPackage
     *            the class package
     */
    public JavaClass(JavaType javaType) {
        this(javaType.getName(), javaType.getPackage());
    }

    /**
     * Create a public class with name and package
     *
     * @param name
     *            the name for the class
     * @param classPackage
     *            the class package
     */
    public JavaClass(String name, String classPackage) {
        this(name, classPackage, null);
    }

    /**
     * Create a public class with name and package
     *
     * @param name
     *            the name for the class
     * @param classPackage
     *            the class package
     */
    public JavaClass(String name, String classPackage, Modifier modifier) {
        super(name, classPackage);
        init(modifier);
    }

    /**
     * Initialize the JavaClass' lists
     * 
     * @param modifier
     */
    private void init(Modifier modifier) {

        if (modifier != null) {
            add(modifier);
        }
        superClass = JavaTypeFactory.getObjectType();
        interfaces = new UniqueList<>();
        fields = new UniqueList<>();
        methods = new UniqueList<>();
        constructors = new UniqueList<>();
    }

    /**
     * Generate the corresponding java class code, containing the package, imports, fields methods, etc.
     * 
     * @param indentaton
     *            level of indentation
     * @return the generated java class code
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder classGen = generateClassHeader(indentation);

        classGen.append("class ");
        classGen.append(getName());

        if (superClass != null && !superClass.equals(JavaTypeFactory.getObjectType())) {
            classGen.append(" extends ");
            classGen.append(superClass.getSimpleType());
        }

        addImplements(classGen);
        classGen.append(" {" + ln() + ln());

        addFields(indentation, classGen);
        addConstructors(indentation, classGen);

        addMethods(indentation, classGen);

        classGen.append(generateClassTail(indentation));
        return classGen;
    }

    protected void addMethods(int indentation, final StringBuilder classGen) {

        if (!methods.isEmpty()) {
            final StringBuilder indent1 = Utils.indent(indentation + 1);

            classGen.append(indent1);
            final String joinMethods = StringUtils.join(methods,
                    method -> method.generateCode(indentation + 1).toString(), ln() + ln());
            classGen.append(joinMethods.trim());

            classGen.append(ln());

        }
    }

    protected void addConstructors(int indentation, final StringBuilder classGen) {
        if (!constructors.isEmpty()) {
            final StringBuilder indent1 = Utils.indent(indentation + 1);

            classGen.append(indent1);
            final String constStr = StringUtils.join(constructors,
                    constr -> constr.generateCode(indentation + 1).toString(), ln());
            classGen.append(constStr.trim());

            classGen.append(ln());
            // classGen.append(Utils.indent(1));
        }
    }

    protected void addFields(int indentation, final StringBuilder classGen) {

        if (!fields.isEmpty()) {
            final StringBuilder indent1 = Utils.indent(indentation + 1);

            classGen.append(indent1);
            final String fieldsStr = StringUtils.join(fields, field -> field.generateCode(indentation + 1).toString(),
                    ln());
            classGen.append(fieldsStr.trim());
            /*
             * for (Field field : fields) { StringBuilder fieldBuf =
             * field.generateCode(1); classGen.append(fieldBuf);
             * classGen.append("\n"); }
             */
            classGen.append(ln() + ln());

        }
    }

    protected void addImplements(final StringBuilder classGen) {
        if (!interfaces.isEmpty()) {
            classGen.append(" implements ");
            final String joinedInterfaces = StringUtils.join(interfaces, JavaType::getSimpleType, ", ");
            classGen.append(joinedInterfaces);

        }
    }

    /**
     * Add a new interface to the class
     * 
     * @param interfaceClass
     *            the new interface
     * @return true if the interface was successfully added
     */
    public boolean addInterface(JavaType interfaceClass) {
        final boolean isAdded = interfaces.add(interfaceClass);
        if (isAdded) {

            addImport(interfaceClass);
        }
        return isAdded;
    }

    /**
     * Removes a interface from the class
     * 
     * @param interfaceClass
     *            the interface to remove
     * @return true if the interface was successfully removed
     */
    public boolean removeInterface(JavaType interfaceClass) {
        for (int i = 0; i < interfaces.size(); i++) {
            JavaType interf = interfaces.get(i);
            if (interf.getCanonicalName().equals(interfaceClass.getCanonicalName())) {
                return interfaces.remove(interf);
            }
        }
        return false;
    }

    /**
     * Add a new constructor to the class
     * 
     * @param constructor
     *            the new contructor
     * @return true if the constructor was successfully added
     */
    public boolean add(Constructor constructor) {

        final boolean isAdded = constructors.add(constructor);

        if (isAdded) {
            constructor.getArguments().forEach(arg -> addImport(arg.getClassType()));
        }
        return isAdded;
    }

    /**
     * Removes a constructor from the class
     * 
     * @param interfaceClass
     *            the constructor to remove
     * @return true if the constructor was successfully removed
     */
    public boolean remove(Constructor constructor) {
        return constructors.remove(constructor);
    }

    /**
     * Add a new field to the class
     * 
     * @param field
     *            the new field
     * @return true if the field was successfully added
     */
    public boolean add(Field field) {
        final boolean isAdded = fields.add(field);
        if (isAdded) {
            addImport(field.getType());
        }
        return isAdded;
    }

    /**
     * Removes a field from the class
     * 
     * @param field
     *            the field to remove
     * @return true if the field was successfully removed
     */
    public boolean remove(Field field) {
        return fields.remove(field);
    }

    /**
     * Add a new method to the class
     * 
     * @param method
     *            the new method
     * @return true if the method was successfully added
     */
    public boolean add(Method method) {
        final boolean isAdded = methods.add(method);
        if (isAdded) {

            addImport(method.getReturnType());
            // Add the imports for the argument of the method
            method.getParams().stream().forEach(arg -> addImport(arg.getClassType()));
        }
        return isAdded;
    }

    /**
     * Removes a method from the class
     * 
     * @param method
     *            the method to remove
     * @return true if the method was successfully removed
     */
    public boolean remove(Method method) {
        return methods.remove(method);
    }

    /**
     * @return the superClass
     */
    public JavaType getSuperClass() {
        return superClass;
    }

    /**
     * @param superClass
     *            the superClass to set
     */
    public void setSuperClass(JavaType superClass) {
        this.superClass = superClass;
        addImport(superClass);
    }

    /**
     * Generate the interface code
     * 
     * @return
     */
    public StringBuilder generate() {
        return generateCode(0);
    }

    /**
     * @return the fields
     */
    public List<Field> getFields() {
        return fields;
    }

    /**
     * @return the methods
     */
    public List<Method> getMethods() {
        return methods;
    }

    public void addAll(Collection<Method> methods) {
        methods.addAll(methods);
    }

    public Constructor createOrGetEmptyConstructor() {
        for (final Constructor ctor : constructors) {
            if (ctor.getArguments().isEmpty()) {
                return ctor;
            }
        }
        return new Constructor(this); // already adding the constructor to
        // this JavaClass
    }

    /**
     * Create a constructor containing all the fields and generates the associated assignment code;
     * 
     * @return
     */
    public Constructor createFullConstructor() {
        final Constructor newCtor = new Constructor(this);
        newCtor.addArguments(fields);
        newCtor.appendDefaultCode(true);
        return newCtor;
    }

    public List<JavaType> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<JavaType> interfaces) {
        this.interfaces = interfaces;
    }

    public List<Constructor> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<Constructor> constructors) {
        this.constructors = constructors;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }
}
