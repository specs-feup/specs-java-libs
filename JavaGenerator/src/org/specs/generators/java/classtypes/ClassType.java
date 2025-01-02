/**
 * Copyright 2016 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.specs.generators.java.classtypes;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.enums.Annotation;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.members.JavaDoc;
import org.specs.generators.java.types.JavaGenericType;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.utils.UniqueList;
import org.specs.generators.java.utils.Utils;
import tdrc.utils.StringUtils;

import java.util.List;
import java.util.Optional;

public abstract class ClassType implements IGenerate {

    // Class package and imports
    private String classPackage;
    private List<String> imports;

    // JavaDoc comment
    private JavaDoc javaDocComment;

    // class's name, privacy, modifiers, interfaces and super-classes
    private String name;
    private Privacy privacy;
    private List<Modifier> modifiers;
    private List<Annotation> annotations;

    // Classes, interfaces and enums declarated inside this class
    private List<ClassType> innerTypes;
    private Optional<ClassType> parent;

    /**
     * Create a public class type with name and package
     *
     * @param name         the name for the class
     * @param classPackage the class package
     */
    public ClassType(String name, String classPackage) {
        init(name, classPackage);
    }

    private void init(String name, String classPackage) {
        setName(name);
        setClassPackage(classPackage);
        setPrivacy(Privacy.PUBLIC);
        parent = Optional.empty();
        setImports(new UniqueList<>());
        setJavaDocComment(new JavaDoc());
        modifiers = new UniqueList<>();
        setAnnotations(new UniqueList<>());
        setInnerTypes(new UniqueList<>());
    }

    public String getQualifiedName() {
        String thePackage = classPackage != null && !classPackage.isEmpty() ? classPackage + "." : "";

        return thePackage + getName();
    }

    /**
     * Return a list of all imports, including sub imports
     *
     * @return
     */
    // List<String> getAllImports();
    //
    // Optional<ClassType> getParent();
    //
    // void setParent(ClassType type);
    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public JavaDoc getJavaDocComment() {
        return javaDocComment;
    }

    public void setJavaDocComment(JavaDoc javaDocComment) {
        this.javaDocComment = javaDocComment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public List<ClassType> getInnerTypes() {
        return innerTypes;
    }

    public void setInnerTypes(List<ClassType> innerTypes) {
        this.innerTypes = innerTypes;
    }

    public Optional<ClassType> getParent() {
        return parent;
    }

    public void setParent(ClassType classType) {
        parent = Optional.of(classType);
    }

    /**
     * Adds a class type as an inner type of the class. This is just a temporary work around
     * <p>
     * <b>Note:</b> this method sets the parent of the type given as the invoked one
     *
     * @param type
     * @return
     */
    public boolean add(ClassType type) {

        boolean added = getInnerTypes().add(type);
        if (added) {
            type.setParent(this);
        }
        return added;

    }

    public List<String> getAllImports() {

        List<String> imports = new UniqueList<>();
        imports.addAll(getImports());
        for (ClassType type : getInnerTypes()) {
            imports.addAll(type.getAllImports());
        }
        return imports;
    }

    /**
     * Add an import to the class
     *
     * @param imports the new import
     * @return true if the import can be added, false if not
     */
    public void addImport(String... imports) {
        for (final String newImport : imports) {
            addImport(newImport);
        }
    }

    public boolean addImport(String newImport) {
        if (getClassPackage().equals(StringUtils.getPackage(newImport))) {

            return false;
        }

        return getImports().add(newImport);
    }

    /**
     * Add an import to the class
     *
     * @param imports the new import
     * @return true if the import can be added, false if not
     */
    public void addImport(Class<?>... imports) {
        for (final Class<?> newImport : imports) {
            addImport(newImport.getCanonicalName());
        }
    }

    /**
     * Add an array of imports to the class
     *
     * @param imports the new import
     * @return true if the import can be added, false if not
     */
    public void addImport(JavaType... imports) {
        for (final JavaType newImport : imports) {
            addImport(newImport);
        }
    }

    /**
     * Add an import to the interface
     *
     * @param newImport the new import
     * @return true if the import can be added, false if not
     */
    public boolean addImport(JavaType newImport) {
        boolean isAdded;
        if (!newImport.requiresImport()) {
            isAdded = false;
        } else {
            isAdded = addImport(newImport.getCanonicalName());
        }
        newImport.getGenerics().forEach(gen -> addGenericImports(gen));
        return isAdded;
    }

    /**
     * Add the imports required for each generic type used in a JavaGenericType
     *
     * @param genType
     */
    private void addGenericImports(JavaGenericType genType) {
        addImport(genType.getTheType());
        genType.getExtendingTypes().forEach(gen -> addImport(gen));
    }

    /**
     * Remove an import from the class
     *
     * @param importRem the import to be removed
     * @return true if the import was successfully removed
     */
    public boolean removeImport(String importRem) {
        return getImports().remove(importRem);
    }

    /**
     * Append text to the javadoc comment
     *
     * @param comment the text to append
     * @return the {@link StringBuilder} with the new comment
     */
    public StringBuilder appendComment(String comment) {
        return getJavaDocComment().appendComment(comment);
    }

    /**
     * Add a new javadoc tag to the comment, with no description
     *
     * @param tag the new tag to add
     */
    public void add(JDocTag tag) {
        getJavaDocComment().addTag(tag);
    }

    /**
     * Add a new javadoc tag to the comment with description
     *
     * @param tag         the new tag to add
     * @param description the tag description
     */
    public void add(JDocTag tag, String description) {
        getJavaDocComment().addTag(tag, description);
    }

    /**
     * Add a new modifier to the class
     *
     * @param modifier the new modifier
     * @return true if the modifier was successfully added
     */
    public boolean add(Modifier modifier) {
        return modifiers.add(modifier);
    }

    /**
     * Removes a modifier from the class
     *
     * @param modifier the modifier to remove
     * @return true if the modifier was successfully removed
     */
    public boolean remove(Modifier modifier) {
        return modifiers.remove(modifier);
    }

    /**
     * Add a new annotation to the class
     *
     * @param annotation the new annotation
     * @return true if the annotation was successfully added
     */
    public boolean add(Annotation annotation) {
        return getAnnotations().add(annotation);
    }

    /**
     * Removes a annotation from the class
     *
     * @param annotation the annotation to remove
     * @return true if the annotation was successfully removed
     */
    public boolean remove(Annotation annotation) {
        return getAnnotations().remove(annotation);
    }

    public StringBuilder generateClassHeader(int indentation) {
        StringBuilder classGen = new StringBuilder();
        if (!getParent().isPresent()) { // I'm the main class
            if (!getClassPackage().isEmpty()) {
                classGen.append("package ");
                classGen.append(getClassPackage());
                classGen.append(";" + ln() + ln());
            }

            getAllImports().forEach(i -> classGen.append("import " + i + ";" + ln()));

            classGen.append(ln());
        } // else my parent deals with the imports

        classGen.append(getJavaDocComment().generateCode(indentation));
        classGen.append(ln());
        final StringBuilder indentStr = Utils.indent(indentation);
        for (final Annotation annot : getAnnotations()) {
            classGen.append(indentStr);
            classGen.append(annot);
            classGen.append(ln());
        }

        if (!getPrivacy().equals(Privacy.PACKAGE_PROTECTED)) {
            classGen.append(indentStr);
            classGen.append(getPrivacy());
            classGen.append(" ");
        }

        for (final Modifier mod : modifiers) {

            classGen.append(mod);
            classGen.append(" ");
        }

        return classGen;
    }

    public StringBuilder generateClassTail(int indentation) {
        StringBuilder classGen = new StringBuilder();
        if (!getInnerTypes().isEmpty()) {
            final String joinMethods = StringUtils.join(getInnerTypes(),
                    i -> i.generateCode(indentation + 1).toString(), ln() + ln());
            classGen.append(joinMethods);
        }
        classGen.append(Utils.indent(indentation) + "}" + ln());
        return classGen;
    }
}