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
package org.specs.generators.java.members;

import java.util.ArrayList;
import java.util.List;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.classtypes.JavaClass;
import org.specs.generators.java.enums.Annotation;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.exprs.IExpression;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.utils.Utils;

/**
 * Field declaration for a {@link JavaClass} field
 * 
 * @author Tiago
 * 
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
     * Generate a private field of type classType
     * 
     * @param classType
     *            the class of the field
     * @param name
     *            the name for the field
     */
    public Field(JavaType classType, String name) {
        init(classType, name, Privacy.PRIVATE);
    }

    /**
     * Generate a field of type classType with the chosen privacy
     * 
     * @param classType
     *            the class of the field
     * @param name
     *            the name for the field
     * @param privacy
     *            the privacy level
     */
    public Field(JavaType classType, String name, Privacy privacy) {
        init(classType, name, privacy);
    }

    private void init(JavaType classType, String name, Privacy privacy) {
        this.privacy = privacy;
        this.name = name;
        this.classType = classType;
        annotations = new ArrayList<>();
        modifiers = new ArrayList<>();
        initializer = null;
        setDefaultInitializer(false);
    }

    /**
     * Add a new modifier to the field
     * 
     * @param newMod
     *            the new modifier
     */
    public void addModifier(Modifier newMod) {
        if (!modifiers.contains(newMod)) {
            modifiers.add(newMod);
        }
    }

    /**
     * Add a new annotation to the class
     * 
     * @param annotation
     *            the new annotation
     * @return true if the annotation was successfully added
     */
    public boolean add(Annotation annotation) {
        return annotations.add(annotation);
    }

    /**
     * Removes a annotation from the class
     * 
     * @param annotation
     *            the annotation to remove
     * @return true if the annotation was successfully removed
     */
    public boolean remove(Annotation annotation) {
        return annotations.remove(annotation);
    }

    /**
     * Generate java source based on the field's privacy, modifiers, class and name
     * 
     * @param indentation
     *            the code indentation
     * @return
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder fieldStr = Utils.indent(indentation);

        for (final Annotation annot : annotations) {
            fieldStr.append(Utils.indent(indentation));
            fieldStr.append(annot);
            fieldStr.append("\n");
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
     * @return the privacy
     */
    public Privacy getPrivacy() {
        return privacy;
    }

    /**
     * @param privacy
     *            the privacy to set
     */
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the classType
     */
    public JavaType getType() {
        return classType;
    }

    /**
     * @param classType
     *            the classType to set
     */
    public void setType(JavaType classType) {
        this.classType = classType;
    }

    /**
     * @return the modifiers
     */
    public List<Modifier> getModifiers() {
        return modifiers;
    }

    /**
     * @return the defaultInitializer
     */
    public boolean isDefaultInitializer() {
        return defaultInitializer;
    }

    /**
     * @param defaultInitializer
     *            the defaultInitializer to set
     */
    public void setDefaultInitializer(boolean defaultInitializer) {
        this.defaultInitializer = defaultInitializer;
    }

    public IExpression getInitializer() {
        return initializer;
    }

    public void setInitializer(IExpression initializer) {
        this.initializer = initializer;
    }
}
