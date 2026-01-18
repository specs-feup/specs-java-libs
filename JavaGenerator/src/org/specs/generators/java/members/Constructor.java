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
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.classtypes.JavaClass;
import org.specs.generators.java.classtypes.JavaEnum;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.utils.Utils;

import tdrc.utils.StringUtils;

/**
 * Represents a constructor declaration for a Java class or enum.
 *
 * @author Tiago
 */
public class Constructor implements IGenerate {
    private Privacy privacy;
    private JavaClass javaClass;
    private JavaEnum javaEnum;
    private JavaDoc javaDocComment;
    private List<Argument> arguments;
    private StringBuffer methodBody;

    /**
     * Generates a public, empty constructor for the specified Java class.
     *
     * @param javaClass the class pertaining to the constructor
     */
    public Constructor(JavaClass javaClass) {
        setJavaClass(javaClass);
        privacy = Privacy.PUBLIC;
        init(javaClass);
    }

    /**
     * Generates a private, empty constructor for the specified Java enum.
     *
     * @param javaEnum the enum pertaining to the constructor
     */
    public Constructor(JavaEnum javaEnum) {
        if (javaEnum == null) {
            throw new IllegalArgumentException("Java enum cannot be null");
        }
        this.javaEnum = javaEnum;
        privacy = Privacy.PRIVATE;
        init(javaEnum);
    }

    /**
     * Initializes the Constructor instance.
     *
     * @param javaClass the class pertaining to the constructor
     */
    private void init(JavaClass javaClass) {
        arguments = new ArrayList<>();
        javaDocComment = new JavaDoc();
        javaClass.add(this);
        methodBody = new StringBuffer();
    }

    /**
     * Generates an empty constructor with the required privacy for the specified
     * Java class.
     *
     * @param privacy   the privacy level
     * @param javaClass the class pertaining to the constructor
     */
    public Constructor(Privacy privacy, JavaClass javaClass) {
        setJavaClass(javaClass);
        this.privacy = privacy;
        init(javaClass);
    }

    /**
     * Adds a new argument to the constructor's arguments.
     *
     * @param classType the type of the argument
     * @param name      the name of the argument
     */
    public void addArgument(JavaType classType, String name) {
        final Argument newArg = new Argument(classType, name);
        arguments.add(newArg);
    }

    /**
     * Adds a new argument based on a field.
     *
     * @param field the field to add as an argument
     */
    public void addArgument(Field field) {
        final Argument newArg = new Argument(field.getType(), field.getName());
        arguments.add(newArg);
    }

    /**
     * Adds a list of fields as arguments.
     *
     * @param field the collection of fields to add as arguments
     */
    public void addArguments(Collection<Field> field) {
        field.forEach(this::addArgument);
    }

    /**
     * Generates Java source code based on the privacy, arguments, and name.
     *
     * @param indentation the code indentation
     * @return the generated Java constructor code
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder indent0 = Utils.indent(indentation);
        final StringBuilder constructorStr = javaDocComment.generateCode(indentation);
        constructorStr.append(ln());
        constructorStr.append(indent0);
        constructorStr.append(privacy);
        constructorStr.append(" ");
        if (javaEnum != null) {
            constructorStr.append(javaEnum.getName());
        } else if (javaClass != null) {
            constructorStr.append(javaClass.getName());
        } else {
            throw new IllegalStateException("Constructor must be associated with a Java class or enum");
        }
        constructorStr.append("(");

        final String joinedArguments = StringUtils.join(arguments, Argument::toString, ", ");
        constructorStr.append(joinedArguments);
        constructorStr.append(")");

        constructorStr.append("{");
        final StringBuilder indent = Utils.indent(indentation + 1);
        if (!methodBody.isEmpty()) {
            constructorStr.append(ln() + indent);
            final String bodyCode = methodBody.toString().replace(ln(), ln() + indent).trim();
            constructorStr.append(bodyCode);
            constructorStr.append(ln() + indent0);
        }
        constructorStr.append("}");
        return constructorStr;
    }

    /**
     * Appends text to the Javadoc comment.
     *
     * @param comment the text to append
     * @return the {@link StringBuilder} with the new comment
     */
    public StringBuilder appendComment(String comment) {
        return javaDocComment.appendComment(comment);
    }

    /**
     * Adds a new Javadoc tag to the comment, with no description.
     *
     * @param tag the new tag to add
     */
    public void addJavaDocTag(JDocTag tag) {
        javaDocComment.addTag(tag);
    }

    /**
     * Adds a new Javadoc tag to the comment with a description.
     *
     * @param tag         the new tag to add
     * @param description the tag description
     */
    public void addJavaDocTag(JDocTag tag, String description) {
        javaDocComment.addTag(tag, description);
    }

    @Override
    public String toString() {
        return generateCode(0).toString();
    }

    /**
     * @return the privacy level
     */
    public Privacy getPrivacy() {
        return privacy;
    }

    /**
     * Sets the privacy level.
     *
     * @param privacy the privacy to set
     */
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    /**
     * @return the Java class
     */
    public JavaClass getJavaClass() {
        return javaClass;
    }

    /**
     * Sets the Java class.
     *
     * @param javaClass the Java class to set
     */
    public void setJavaClass(JavaClass javaClass) throws IllegalArgumentException {
        if (javaClass == null) {
            throw new IllegalArgumentException("Java class cannot be null");
        }
        this.javaClass = javaClass;
    }

    /**
     * @return the list of arguments
     */
    public List<Argument> getArguments() {
        return arguments;
    }

    /**
     * Sets the list of arguments.
     *
     * @param arguments the arguments to set
     */
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    /**
     * @return the method body
     */
    public StringBuffer getMethodBody() {
        return methodBody;
    }

    /**
     * Sets the method body.
     *
     * @param methodBody the method body to set
     */
    public void setMethodBody(StringBuffer methodBody) {
        this.methodBody = methodBody;
    }

    /**
     * Appends code to the method body.
     *
     * @param code the code to append
     */
    public void appendCode(String code) {
        methodBody.append(code);
    }

    /**
     * Appends code to the method body.
     *
     * @param code the code to append
     */
    public void appendCode(StringBuffer code) {
        methodBody.append(code);
    }

    /**
     * Appends default code based on the parameters of the constructor.
     *
     * @param useSetters use the set methods instead of assignments
     */
    public void appendDefaultCode(boolean useSetters) {
        Consumer<Argument> generateAssignment;
        if (useSetters) {
            generateAssignment = arg -> {
                final String name = arg.getName();
                final String firstCharToUpper = StringUtils.firstCharToUpper(name);
                appendCode("this.set" + firstCharToUpper + "(" + name + ");" + ln());
            };
        } else {
            generateAssignment = arg -> appendCode("this." + arg.getName() + " = " + arg.getName() + ";" + ln());
        }
        arguments.forEach(generateAssignment);
    }

    /**
     * Clears the method body.
     */
    public void clearCode() {
        methodBody.delete(0, methodBody.length());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Constructor that = (Constructor) obj;

        return this.hashCode() == that.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(privacy, javaClass, javaEnum, javaDocComment, arguments, methodBody);
    }
}
