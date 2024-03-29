/*
 * Copyright 2011 SPeCS Research Group.
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

package org.suikasoft.GsonPlus;

import java.util.AbstractList;

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * For compatibility with legacy Clava configuration files
 * 
 * @author Joao Bispo
 */
public class JsonStringList extends AbstractList<String> {

    @Override
    public String get(int index) {
        throw new NotImplementedException(this);
    }

    @Override
    public int size() {
        throw new NotImplementedException(this);
    }

}
