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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.logging;

import java.util.function.Supplier;
import java.util.logging.Logger;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.lazy.ThreadSafeLazy;

/**
 * @deprecated replaced by TagLogger/one of its implementations (e.g., EnumLogger, StringLogger)
 * @author JoaoBispo
 *
 */
@Deprecated
public abstract class SpecsLogger {

    protected static String buildLoggerName(Class<? extends SpecsLogger> aClass) {
        return SpecsIo.removeExtension(aClass.getName());
    }

    protected static <T extends SpecsLogger> Lazy<T> buildLazy(Supplier<T> provider) {
        return new ThreadSafeLazy<>(provider);
    }

    private final Logger logger;

    public SpecsLogger(String name) {
        logger = Logger.getLogger(name);
    }

    public void msgInfo(String msg) {
        SpecsLogs.msgInfo(logger, msg);
    }

    public void msgWarn(String msg) {
        SpecsLogs.msgWarn(logger, msg);
    }

    /*
    public void msgWarn(String msg) {
        LoggingUtils.msgInfo(Logger.getLogger(tag), msg);
    }
    */
}
