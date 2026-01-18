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
package org.specs.generators.java.classtypes;

import java.util.List;

import org.specs.generators.java.members.EnumItem;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.utils.UniqueList;

import tdrc.utils.StringUtils;

/**
 * Represents a Java enum for code generation. Provides methods to manage enum
 * items and generate enum code.
 *
 * @author Tiago
 */
public class JavaEnum extends JavaClass {

    private List<EnumItem> items;

    /**
     * Create a public enum with name and package.
     *
     * @param name         the name for the enum
     * @param classPackage the class package
     */
    public JavaEnum(String name, String classPackage) {
        super(name, classPackage);
        init();
    }

    /**
     * Initialize the JavaEnum' lists.
     */
    private void init() {
        items = new UniqueList<>();
    }

    @Override
    public void setSuperClass(JavaType superClass) {
        throw new RuntimeException("An enum cannot have a super class.");
    }

    @Override
    public JavaType getSuperClass() {
        throw new RuntimeException("An enum does not have a super class.");
    }

    /**
     * Generate the corresponding java enum code, containing the package, imports,
     * items, fields, methods, etc.
     *
     * @param indentation level of indentation
     * @return the generated java enum code
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder classGen = generateClassHeader(indentation);

        classGen.append("enum ");
        classGen.append(getName());
        classGen.append(" ");
        addImplements(classGen);
        classGen.append("{" + ln());

        addItems(indentation, classGen);
        addFields(indentation, classGen);

        addConstructors(indentation, classGen);

        addMethods(indentation, classGen);

        classGen.append(generateClassTail(indentation));
        return classGen;
    }

    private void addItems(int indentation, final StringBuilder classGen) {
        final String joinedItems = StringUtils.join(items, i -> i.generateCode(indentation + 1).toString(), "," + ln());
        if (!joinedItems.isEmpty()) {
            classGen.append(joinedItems);
            classGen.append(";" + ln());
        }
    }

    /**
     * Add an enum item to the enum.
     *
     * @param item the item to append
     */
    public void add(EnumItem item) {
        items.add(item);
    }

    /**
     * Add an enum item by the item name.
     *
     * @param name the name for the new Item
     */
    public void addItem(String name) {
        items.add(new EnumItem(name));
    }
}
