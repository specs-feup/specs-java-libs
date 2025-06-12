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
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.utils.Utils;

/**
 * Represents a JavaDoc comment for code generation, including tags and descriptions.
 *
 * @author Tiago
 */
public class JavaDoc implements IGenerate {

    private StringBuilder comment;
    private final List<JavaDocTag> tags;

    /**
     * Constructs an empty JavaDoc comment.
     */
    public JavaDoc() {
        tags = new ArrayList<>();
        setComment(new StringBuilder());
    }

    /**
     * Constructs a JavaDoc comment with a predefined StringBuilder.
     *
     * @param comment the StringBuilder containing the comment
     */
    public JavaDoc(StringBuilder comment) {
        tags = new ArrayList<>();
        setComment(comment);
    }

    /**
     * Constructs a JavaDoc comment with a predefined String.
     *
     * @param comment the String containing the comment
     */
    public JavaDoc(String comment) {
        tags = new ArrayList<>();
        setComment(new StringBuilder(comment));
    }

    /**
     * Appends a string to the current comment.
     *
     * @param comment the string to append
     * @return the updated StringBuilder
     */
    public StringBuilder appendComment(String comment) {
        return this.comment.append(comment);
    }

    /**
     * Adds a tag with no description.
     *
     * @param tag the new tag for the comment
     */
    public void addTag(JDocTag tag) {
        tags.add(new JavaDocTag(tag));
    }

    /**
     * Adds a tag with a string description.
     *
     * @param tag the new tag for the comment
     * @param descriptionStr the description string
     */
    public void addTag(JDocTag tag, String descriptionStr) {
        tags.add(new JavaDocTag(tag, descriptionStr));
    }

    /**
     * Adds a tag with a StringBuilder description.
     *
     * @param tag the new tag for the comment
     * @param description the description as a StringBuilder
     */
    public void addTag(JDocTag tag, StringBuilder description) {
        tags.add(new JavaDocTag(tag, description));
    }

    /**
     * Returns the comment StringBuilder.
     *
     * @return the comment
     */
    public StringBuilder getComment() {
        return comment;
    }

    /**
     * Sets the comment StringBuilder.
     *
     * @param comment the comment to set
     */
    public void setComment(StringBuilder comment) {
        this.comment = comment;
    }

    /**
     * Removes a tag from the list of tags.
     *
     * @param index the position of the tag
     * @return the removed tag
     */
    public JavaDocTag removeTag(int index) {
        return tags.remove(index);
    }

    /**
     * Gets a tag in the position index.
     *
     * @param index the position of the tag
     * @return the tag at the specified index
     */
    public JavaDocTag getTag(int index) {
        return tags.get(index);
    }

    /**
     * Generates a javadoc comment with the specified indentation.
     *
     * @param indentation the level of indentation
     * @return the generated javadoc comment
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder commentBuilder = Utils.indent(indentation);
        commentBuilder.append("/**" + ln());
        commentBuilder.append(Utils.indent(indentation));
        commentBuilder.append(" * ");
        String commentStr = comment.toString();
        commentStr = commentStr.replace(ln(), ln() + Utils.indent(indentation) + " * ");
        commentBuilder.append(commentStr);
        commentBuilder.append(ln());
        for (final JavaDocTag tag : tags) {
            commentBuilder.append(Utils.indent(indentation));
            commentBuilder.append(" * ");

            commentStr = tag.generateCode(0).toString();
            commentStr = commentStr.replace(ln(), ln() + Utils.indent(indentation) + " * " + Utils.indent(1));
            commentBuilder.append(commentStr);
            commentBuilder.append(ln());
        }
        commentBuilder.append(Utils.indent(indentation));
        commentBuilder.append(" */");
        return commentBuilder;
    }

    /**
     * Clones the current JavaDoc instance.
     *
     * @return a new JavaDoc instance with the same content
     */
    @Override
    public JavaDoc clone() {
        final JavaDoc javaDoc = new JavaDoc(new StringBuilder(comment));
        tags.forEach(t -> javaDoc.addTag(t.getTag(), t.getDescription()));
        return javaDoc;
    }
}
