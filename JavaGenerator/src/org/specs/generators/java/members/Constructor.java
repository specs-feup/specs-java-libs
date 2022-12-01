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
import java.util.Collection;
import java.util.List;
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
 * Constructor declaration for a {@link JavaClass}
 * 
 * @author Tiago
 * 
 */
public class Constructor implements IGenerate {
    private Privacy privacy;
    private JavaClass javaClass;
    private JavaEnum javaEnum;
    private JavaDoc javaDocComment;
    private List<Argument> arguments;
    private StringBuffer methodBody;

    /**
     * Generate a public, empty constructor for the {@link JavaClass}
     * 
     * @param javaClass
     *            the class pertaining the constructor
     */
    public Constructor(JavaClass javaClass) {
        this.javaClass = javaClass;
        privacy = Privacy.PUBLIC;
        init(javaClass);
    }

    /**
     * Generate a private, empty constructor for the {@link JavaEnum}
     *
     * @param javaEnum
     *            the enum pertaining the constructor
     */
    public Constructor(JavaEnum javaEnum) {
        this.javaEnum = javaEnum;
        privacy = Privacy.PRIVATE;
        init(javaEnum);
    }

    /**
     * Initialize Constructor instance
     * 
     * @param javaClass
     */
    private void init(JavaClass javaClass) {
        arguments = new ArrayList<>();
        javaDocComment = new JavaDoc();
        javaClass.add(this);
        methodBody = new StringBuffer();
    }

    /**
     * Generate a empty constructor, with required privacy, for the {@link JavaClass}
     * 
     * @param privacy
     *            the privacy level
     * @param javaClass
     *            the class pertaining the constructor
     */
    public Constructor(Privacy privacy, JavaClass javaClass) {
        this.javaClass = javaClass;
        this.privacy = privacy;
        init(javaClass);
    }

    /**
     * Add a new argument to the constructor's arguments
     * 
     * @param the
     *            new modifier
     */
    public void addArgument(JavaType classType, String name) {
        final Argument newArg = new Argument(classType, name);
        arguments.add(newArg);
    }

    /**
     * Add a new argument based on a field
     * 
     * @param the
     *            new modifier
     */
    public void addArgument(Field field) {
        final Argument newArg = new Argument(field.getType(), field.getName());
        arguments.add(newArg);
    }

    /**
     * Add a list of field as argument
     * 
     * @param the
     *            new modifier
     */
    public void addArguments(Collection<Field> field) {
        field.forEach(this::addArgument);
    }

    /**
     * Generate java source based on the privacy, arguments and name
     * 
     * @param indentiation
     *            the code indentation
     * @return the generated java constructor code
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
        } else {
            constructorStr.append(javaClass.getName());
        }
        constructorStr.append("(");

        final String joinedArguments = StringUtils.join(arguments, Argument::toString, ", ");
        constructorStr.append(joinedArguments);
        constructorStr.append(")");

        constructorStr.append("{");
        final StringBuilder indent = Utils.indent(indentation + 1);
        if (methodBody.length() != 0) {
            constructorStr.append(ln() + indent);
            final String bodyCode = methodBody.toString().replace(ln(), ln() + indent).trim();
            constructorStr.append(bodyCode);
            constructorStr.append(ln() + indent0);
        } else {
            // constructorStr.append("// TODO Auto-generated constructor
            // stub\n");
            // constructorStr.append(Utils.indent(indentation));
        }
        constructorStr.append("}");
        return constructorStr;
    }

    /**
     * Append text to the javadoc comment
     * 
     * @param comment
     *            the text to append
     * @return the {@link StringBuilder} with the new comment
     */
    public StringBuilder appendComment(String comment) {
        return javaDocComment.appendComment(comment);
    }

    /**
     * Add a new javadoc tag to the comment, with no description
     * 
     * @param tag
     *            the new tag to add
     */
    public void addJavaDocTag(JDocTag tag) {
        javaDocComment.addTag(tag);
    }

    /**
     * Add a new javadoc tag to the comment with description
     * 
     * @param tag
     *            the new tag to add
     * @param description
     *            the tag description
     */
    public void addJavaDocTag(JDocTag tag, String description) {
        javaDocComment.addTag(tag, description);
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
     * @return the javaClass
     */
    public JavaClass getJavaClass() {
        return javaClass;
    }

    /**
     * @param javaClass
     *            the javaClass to set
     */
    public void setJavaClass(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    /**
     * @return the arguments
     */
    public List<Argument> getArguments() {
        return arguments;
    }

    /**
     * @param arguments
     *            the arguments to set
     */
    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    /**
     * @return the methodBody
     */
    public StringBuffer getMethodBody() {
        return methodBody;
    }

    /**
     * @param methodBody
     *            the methodBody to set
     */
    public void setMethodBody(StringBuffer methodBody) {
        this.methodBody = methodBody;
    }

    /**
     * Append code to the method body
     * 
     * @param code
     *            the code to append
     */
    public void appendCode(String code) {
        methodBody.append(code);
    }

    /**
     * Append code to the method body
     * 
     * @param code
     *            the code to append
     */
    public void appendCode(StringBuffer code) {
        methodBody.append(code);
    }

    /**
     * Append default code based on the parameters of the constructor
     * 
     * @param useSetters
     *            use the set methods instead of assignments;
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

    public void clearCode() {
        methodBody.delete(0, methodBody.length());
    }
}
