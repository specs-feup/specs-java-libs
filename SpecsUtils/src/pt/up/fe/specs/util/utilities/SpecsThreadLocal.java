/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import java.util.Objects;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsLogs;

public class SpecsThreadLocal<T> {

    private final ThreadLocal<T> threadLocal;
    private final Class<T> aClass;

    public SpecsThreadLocal(Class<T> aClass) {
        this.aClass = aClass;
        this.threadLocal = new ThreadLocal<>();
    }

    public void set(T value) {
        Preconditions.checkArgument(threadLocal.get() == null,
                "Tried to set " + aClass.getName() + " but there is already a value present in this thread");
        threadLocal.set(value);
    }

    public void setWithWarning(T value) {
        if (isSet()) {
            SpecsLogs.msgInfo(
                    "Setting value of ThreadLocal of " + aClass.getName() + " that overrides a previous value");
            remove();
        }

        set(value);
    }

    public void remove() {
        Objects.requireNonNull(threadLocal.get(),
                () -> "Tried to remove " + aClass.getName() + ", but there is no value set");
        threadLocal.remove();
    }

    public void removeWithWarning() {
        if (!isSet()) {
            SpecsLogs.msgInfo(
                    "Removing value of ThreadLocal of " + aClass.getName() + " that has no value set");
            return;
        }

        remove();
    }

    public T get() {
        T value = threadLocal.get();
        Objects.requireNonNull(value, () -> "Tried to get " + aClass.getName() + ", but there is no value set");
        return value;
    }

    public boolean isSet() {
        return threadLocal.get() != null;
    }

}
