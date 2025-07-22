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
 * specific language governing permissions and limitations under the License.
 */
package org.specs.generators.java.members;

import java.util.ArrayList;
import java.util.List;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.enums.Annotation;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.exprs.IExpression;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.utils.Utils;

/**
 * Represents a field declaration for a Java class.
 *
 * @author Tiago
 */
public class Field implements IGenerate {
    private Privacy privacy;
    private String name;
    private JavaType classType;
    private List<Annotation> annotations;
    private List<Modifier> modifiers;
    private boolean defaultInitializer;
    private IExpression initializer;

    /**
     * Generates a private field of the specified type and name.
     *
     * @param classType the type of the field
     * @param name the name of the field
     */
    public Field(JavaType classType, String name) {
        init(classType, name, Privacy.PRIVATE);
    }

    /**
     * Generates a field of the specified type, name, and privacy level.
     *
     * @param classType the type of the field
     * @param name the name of the field
     * @param privacy the privacy level
     */
    public Field(JavaType classType, String name, Privacy privacy) {
        init(classType, name, privacy);
    }

    private void init(JavaType classType, String name, Privacy privacy) {
        this.privacy = privacy;
        this.name = name;
        setType(classType);
        annotations = new ArrayList<>();
        modifiers = new ArrayList<>();
        initializer = null;
        setDefaultInitializer(false);
    }

    /**
     * Adds a new modifier to the field.
     *
     * @param newMod the new modifier
     */
    public void addModifier(Modifier newMod) {
        if (!modifiers.contains(newMod)) {
            modifiers.add(newMod);
        }
    }

    /**
     * Adds a new annotation to the field.
     *
     * @param annotation the new annotation
     * @return true if the annotation was successfully added
     */
    public boolean add(Annotation annotation) {
        return annotations.add(annotation);
    }

    /**
     * Removes an annotation from the field.
     *
     * @param annotation the annotation to remove
     * @return true if the annotation was successfully removed
     */
    public boolean remove(Annotation annotation) {
        return annotations.remove(annotation);
    }

    /**
     * Generates Java source code based on the field's privacy, modifiers, type, and name.
     *
     * @param indentation the code indentation
     * @return the generated code as a StringBuilder
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder fieldStr = Utils.indent(indentation);

        for (final Annotation annot : annotations) {
            fieldStr.append(Utils.indent(indentation));
            fieldStr.append(annot);
            fieldStr.append(ln());
        }

        fieldStr.append(privacy);
        for (final Modifier mod : modifiers) {
            fieldStr.append(" ");
            fieldStr.append(mod);
        }
        fieldStr.append(" ");
        fieldStr.append(classType.getSimpleType());
        fieldStr.append(" ");
        fieldStr.append(name);

        if (initializer != null) {

            fieldStr.append(" = ");
            StringBuilder generateCode = initializer.generateCode(0);
            fieldStr.append(generateCode);
        } else if (defaultInitializer) {

            fieldStr.append(" = ");
            fieldStr.append(JavaTypeFactory.getDefaultValue(classType));
        }
        fieldStr.append(";");
        return fieldStr;
    }

    @Override
    public String toString() {
        return generateCode(0).toString();
    }

    /**
     * @return the privacy level of the field
     */
    public Privacy getPrivacy() {
        return privacy;
    }

    /**
     * Sets the privacy level of the field.
     *
     * @param privacy the privacy level to set
     */
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    /**
     * @return the name of the field
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the field.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type of the field
     */
    public JavaType getType() {
        return classType;
    }

    /**
     * Sets the type of the field.
     *
     * @param classType the type to set
     */
    public void setType(JavaType classType) throws IllegalArgumentException {
        if (classType == null) {
            throw new IllegalArgumentException("Field type cannot be null");
        }
        this.classType = classType;
    }

    /**
     * @return the list of modifiers applied to the field
     */
    public List<Modifier> getModifiers() {
        return modifiers;
    }

    /**
     * @return true if the field has a default initializer, false otherwise
     */
    public boolean isDefaultInitializer() {
        return defaultInitializer;
    }

    /**
     * Sets whether the field has a default initializer.
     *
     * @param defaultInitializer the default initializer flag to set
     */
    public void setDefaultInitializer(boolean defaultInitializer) {
        this.defaultInitializer = defaultInitializer;
    }

    /**
     * @return the initializer expression of the field
     */
    public IExpression getInitializer() {
        return initializer;
    }

    /**
     * Sets the initializer expression of the field.
     *
     * @param initializer the initializer expression to set
     */
    public void setInitializer(IExpression initializer) {
        this.initializer = initializer;
    }
}
