/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.gearman.utils;

import java.security.Permission;

/**
 * Security manager for Gearman workers that blocks System.exit() calls and allows all other permissions.
 * <p>
 * This class is intended to prevent accidental JVM termination by capturing calls to System.exit().
 * All other permission checks are allowed.
 */
public class GearmanSecurityManager extends SecurityManager {

    /**
     * Allows all permissions (no restrictions).
     */
    @Override
    public void checkPermission(Permission perm) {
        // allow anything.
    }

    /**
     * Allows all permissions (no restrictions).
     */
    @Override
    public void checkPermission(Permission perm, Object context) {
        // allow anything.
    }

    /**
     * Throws a SecurityException when System.exit() is called, preventing JVM termination.
     *
     * @param status the exit status
     * @throws SecurityException always
     */
    @Override
    public void checkExit(int status) {
        super.checkExit(status);
        throw new SecurityException("Captured call to System.exit(" + status + ")");
    }

}
