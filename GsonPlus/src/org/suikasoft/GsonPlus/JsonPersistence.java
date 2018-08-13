/**
 * Copyright 2012 SPeCS Research Group.
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

import com.google.gson.Gson;

import pt.up.fe.specs.util.utilities.PersistenceFormat;

/**
 * @author Joao Bispo
 *
 */
public class JsonPersistence extends PersistenceFormat {

    private final Gson gson;

    /**
     * 
     */
    public JsonPersistence() {
        gson = new Gson();
    }

    /* (non-Javadoc)
     * @see org.specs.DymaLib.Graphs.Utils.PersistenceFormat.PersistenceFormat#to(java.lang.Object, java.lang.Object[])
     */
    @Override
    public String to(Object anObject) {
        return gson.toJson(anObject);
    }

    /* (non-Javadoc)
     * @see org.specs.DymaLib.Graphs.Utils.PersistenceFormat.PersistenceFormat#from(java.lang.String, java.lang.Class, java.lang.Object[])
     */
    @Override
    public <T> T from(String contents, Class<T> classOfObject) {
        return gson.fromJson(contents, classOfObject);
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.Utilities.PersistenceFormat#getExtension()
     */
    @Override
    public String getExtension() {
        return "json";
    }

}
