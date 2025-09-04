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

package pt.up.fe.specs.lang;

import org.apache.commons.lang3.SystemUtils;

/**
 * Utility class providing wrappers around Apache commons-lang methods for system platform identification.
 * <p>
 * Includes methods to check the current operating system and platform details.
 *
 * TODO: Rename to ApachePlatforms
 *
 * @author JoaoBispo
 */
public class SpecsPlatforms {

    /**
     * Returns true if the operating system is a form of Windows.
     *
     * @return true if Windows OS, false otherwise
     */
    public static boolean isWindows() {
        return SystemUtils.IS_OS_WINDOWS;
    }

    /**
     * Returns true if the operating system is a form of Linux.
     *
     * @return true if Linux OS, false otherwise
     */
    public static boolean isLinux() {
        return SystemUtils.IS_OS_LINUX;
    }

    /**
     * Returns true if the operating system is Linux running on ARM architecture.
     *
     * @return true if Linux ARM, false otherwise
     */
    public static boolean isLinuxArm() {
        return SystemUtils.IS_OS_LINUX && "arm".equals(System.getProperty("os.arch").toLowerCase());
    }

    /**
     * Returns true if the operating system version indicates CentOS 6.
     *
     * @return true if CentOS 6, false otherwise
     */
    public static boolean isCentos6() {
        return System.getProperty("os.version").contains(".el6.");
    }

    /**
     * Returns the name of the current platform/OS.
     *
     * @return the OS name
     */
    public static String getPlatformName() {
        return SystemUtils.OS_NAME;
    }

    /**
     * Returns true if the operating system is a form of Unix (Linux or Solaris).
     *
     * @return true if Unix OS, false otherwise
     */
    public static boolean isUnix() {
        return SystemUtils.IS_OS_UNIX;
    }

    /**
     * Returns true if the operating system is a form of Mac OS.
     *
     * @return true if Mac OS, false otherwise
     */
    public static boolean isMac() {
        return SystemUtils.IS_OS_MAC;
    }

    // TODO: Implement shell-related utilities if needed in the future.
}
