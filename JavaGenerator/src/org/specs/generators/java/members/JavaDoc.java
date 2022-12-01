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
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.utils.Utils;

/**
 * Generate a comment used for a java document
 * 
 * @author Tiago
 * 
 */
public class JavaDoc implements IGenerate {

    private StringBuilder comment;
    private final List<JavaDocTag> tags;

    /**
     * Empty constructor
     */
    public JavaDoc() {
        tags = new ArrayList<>();
        setComment(new StringBuilder());
    }

    /**
     * Create a javadoc comment with a predefined {@link StringBuilder}
     * 
     * @param comment
     *            the {@link StringBuilder} containing the comment
     */
    public JavaDoc(StringBuilder comment) {
        tags = new ArrayList<>();
        setComment(comment);
    }

    /**
     * Create a javadoc comment with a predefined {@link String}
     * 
     * @param comment
     *            the {@link String} containing the comment
     */
    public JavaDoc(String comment) {
        tags = new ArrayList<>();
        setComment(new StringBuilder(comment));
    }

    public StringBuilder appendComment(String comment) {
        return this.comment.append(comment);
    }

    /**
     * Add a tag with no description
     * 
     * @param tag
     *            the new tag for the comment
     */
    public void addTag(JDocTag tag) {
        tags.add(new JavaDocTag(tag));
    }

    /**
     * Add a tag with description
     * 
     * @param tag
     *            the new tag for the comment
     */
    public void addTag(JDocTag tag, String descriptionStr) {
        tags.add(new JavaDocTag(tag, descriptionStr));
    }

    /**
     * Add a tag with description
     * 
     * @param tag
     *            the new tag for the comment
     */
    public void addTag(JDocTag tag, StringBuilder description) {
        tags.add(new JavaDocTag(tag, description));
    }

    /**
     * @return the comment
     */
    public StringBuilder getComment() {
        return comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(StringBuilder comment) {
        this.comment = comment;
    }

    /**
     * Remove a tag from the list of tags
     * 
     * @param index
     *            the position of the tag
     * @return the removed tag
     */
    public JavaDocTag removeTag(int index) {
        return tags.remove(index);
    }

    /**
     * Get a tag in the position index
     * 
     * @param index
     *            the position of the tag
     * @return
     */
    public JavaDocTag getTag(int index) {
        return tags.get(index);
    }

    /**
     * Generate a javadoc comment with the specified indentation
     * 
     * @param indentation
     * @return
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

    @Override
    public JavaDoc clone() {

        final JavaDoc javaDoc = new JavaDoc(new StringBuilder(comment));
        tags.forEach(t -> javaDoc.addTag(t.getTag(), t.getDescription()));
        return javaDoc;
    }
}
