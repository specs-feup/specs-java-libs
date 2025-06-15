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
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;
import org.specs.generators.java.utils.UniqueList;
import org.specs.generators.java.utils.Utils;

import pt.up.fe.specs.util.SpecsLogs;
import tdrc.utils.StringUtils;

/**
 * Represents a method declaration for a Java class, including return type, arguments, and modifiers.
 *
 * @author Tiago
 */
public class Method implements IGenerate {

    private Privacy privacy;
    private String name;
    private JavaDoc javaDocComment;
    private JavaType returnType;
    private List<Annotation> annotations;
    private List<Modifier> modifiers;
    private List<Argument> arguments;
    private boolean body;
    private StringBuffer methodBody;

    /**
     * Generates a public method with the specified return type and name.
     *
     * @param returnType the return type of the method
     * @param name the name of the method
     */
    public Method(JavaType returnType, String name) {
        init(returnType, name);
    }

    /**
     * Generates a method with the specified return type, name, and privacy level.
     *
     * @param returnType the return type of the method
     * @param name the name of the method
     * @param privacy the privacy level
     */
    public Method(JavaType returnType, String name, Privacy privacy) {
        init(returnType, name);
        this.privacy = privacy;
    }

    /**
     * Generates a method with the specified return type, name, and modifier.
     *
     * @param returnType the return type of the method
     * @param name the name of the method
     * @param modifier the modifier for the method
     */
    public Method(JavaType returnType, String name, Modifier modifier) {
        init(returnType, name);
        modifiers.add(modifier);
    }

    /**
     * Generates a method with the specified return type, name, privacy, and modifier.
     *
     * @param returnType the return type of the method
     * @param name the name of the method
     * @param privacy the privacy level
     * @param modifier the modifier for the method
     */
    public Method(JavaType returnType, String name, Privacy privacy, Modifier modifier) {
        init(returnType, name);
        this.privacy = privacy;
        modifiers.add(modifier);
    }

    /**
     * Initialize Method instance
     * 
     * @param returnType
     * @param name
     */
    private void init(JavaType returnType, String name) {
        this.name = name;
        this.returnType = returnType;
        privacy = Privacy.PUBLIC;
        annotations = new UniqueList<>();
        modifiers = new ArrayList<>();
        arguments = new ArrayList<>();
        javaDocComment = new JavaDoc();
        body = true;
        methodBody = new StringBuffer();
    }

    /**
     * Add a new modifier to the method
     * 
     * @param newMod
     *            the new modifier
     */
    public void add(Modifier newMod) {
        if (!modifiers.contains(newMod)) {
            modifiers.add(newMod);
        }
    }

    /**
     * Removes a annotation from the class
     * 
     * @param annotation
     *            the annotation to remove
     * @return true if the annotation was successfully removed
     */
    public boolean remove(Modifier mod) {
        return modifiers.remove(mod);
    }

    /**
     * Add a new argument to the method's arguments
     * 
     * @param the
     *            new modifier
     */
    public void addArgument(JavaType classType, String name) {
        addArgument(new Argument(classType, name));
    }

    /**
     * Add a new argument to the method's arguments
     * 
     * @param the
     *            new modifier
     */
    public void addArgument(Argument argument) {
        arguments.add(argument);
    }

    /**
     * Add a new argument to the method's arguments
     * 
     * @param the
     *            new modifier
     */
    public void addArgument(Class<?> classType, String name) {
        final Argument newArg = new Argument(new JavaType(classType), name);
        arguments.add(newArg);
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
     * Generate java source based on the method's privacy, modifiers, return type and name
     * 
     * @param indentiation
     *            the code indentation
     * @return
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder methodStr = javaDocComment.generateCode(indentation);
        methodStr.append(ln());

        for (final Annotation annot : annotations) {
            methodStr.append(Utils.indent(indentation));
            methodStr.append(annot);
            methodStr.append(ln());
        }

        methodStr.append(Utils.indent(indentation));

        methodStr.append(privacy);
        for (final Modifier mod : modifiers) {
            methodStr.append(" ");
            methodStr.append(mod);
        }
        methodStr.append(" ");
        methodStr.append(returnType.getSimpleType());
        methodStr.append(" ");
        methodStr.append(name);
        methodStr.append("(");
        final String joinedArgs = StringUtils.join(arguments, Argument::toString, ", ");
        methodStr.append(joinedArgs);

        methodStr.append(")");
        if (!body || modifiers.contains(Modifier.ABSTRACT)) {
            methodStr.append(";");
        } else {
            methodStr.append(" {" + ln());
            final StringBuilder indent = Utils.indent(indentation + 1);
            methodStr.append(indent);
            if (methodBody.length() != 0) {
                final String bodyCode = methodBody.toString().replace(ln(), ln() + indent).trim();
                methodStr.append(bodyCode);

            } else {
                methodStr.append("// TODO Auto-generated method stub" + ln());
                SpecsLogs.warn("Potential bug: check this");
                if (!returnType.equals(Primitive.VOID.getType())) {

                    final String returnValue = JavaTypeFactory.getDefaultValue(returnType);
                    methodStr.append(indent);
                    methodStr.append("return ");
                    methodStr.append(returnValue);
                    methodStr.append(";");
                }
            }
            methodStr.append(ln());
            methodStr.append(Utils.indent(indentation));
            methodStr.append("}");
        }
        return methodStr;
    }

    @Override
    public String toString() {
        return generateCode(0).toString();
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(boolean body) {
        this.body = body;
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
    public void appendCode(StringBuffer code) {
        methodBody.append(code);
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
    public void appendCodeln(String code) {
        methodBody.append(code + ln());
    }

    /**
     * @return the returnType
     */
    public JavaType getReturnType() {
        return returnType;
    }

    /**
     * @param returnType
     *            the returnType to set
     */
    public void setReturnType(JavaType returnType) {
        this.returnType = returnType;
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
     * @return the javaDocComment
     */
    public JavaDoc getJavaDocComment() {
        return javaDocComment;
    }

    /**
     * @param javaDocComment
     *            the javaDocComment to set
     */
    public void setJavaDocComment(JavaDoc javaDocComment) {
        this.javaDocComment = javaDocComment;
    }

    /**
     * @return the modifiers
     */
    public List<Modifier> getModifiers() {
        return modifiers;
    }

    /**
     * @param modifiers
     *            the modifiers to set
     */
    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @return the arguments
     */
    public List<Argument> getParams() {
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
     * @return the body
     */
    public boolean isBody() {
        return body;
    }

    public void clearCode() {
        methodBody.delete(0, methodBody.length());
    }

    @Override
    public Method clone() {
        final Method clone = new Method(returnType.clone(), name, privacy);
        annotations.forEach(an -> clone.add(an));
        modifiers.forEach(mod -> clone.add(mod));
        arguments.forEach(arg -> clone.addArgument(arg.clone()));
        clone.setJavaDocComment(getJavaDocComment().clone());
        clone.setMethodBody(new StringBuffer(methodBody.toString()));
        clone.body = body;
        return clone;
    }
}
