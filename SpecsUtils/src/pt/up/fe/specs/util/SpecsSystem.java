/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import pt.up.fe.specs.util.classmap.ClassSet;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.properties.SpecsProperty;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.ProcessOutput;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.system.StreamToString;
import pt.up.fe.specs.util.utilities.JarPath;
import pt.up.fe.specs.util.utilities.ProgressCounter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility methods related to system tasks.
 *
 * @author Joao Bispo
 */
public class SpecsSystem {

    private static final ClassSet<Object> IMMUTABLE_JAVA_CLASSES = ClassSet.newInstance(Boolean.class, String.class,
            Number.class, Class.class);

    private static final Lazy<Boolean> IS_DEBUG = Lazy.newInstance(SpecsSystem::testIsDebug);

    private static final boolean IS_LINUX = System.getProperty("os.name").toLowerCase().startsWith("linux");
    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    private static final Map<String, Method> CACHED_METHODS = new HashMap<>();
    private static final Map<String, Optional<Field>> CACHED_FIELDS = new HashMap<>();

    private static final String BUILD_NUMBER_ATTR = "Build-Number";

    private static final Lazy<String> WINDOWS_POWERSHELL = Lazy.newInstance(SpecsSystem::findPwsh);

    private static boolean testIsDebug() {

        // Test if file debug exists in working directory
        if (new File("debug").isFile()) {
            return true;
        }

        // Test if file debug exists in JAR directory
        return JarPath.getJarFolder()
                .map(jarFolder -> new File(jarFolder, "debug").isFile())
                .orElse(false);
    }

    /**
     * Helper method which receives the command and the working directory instead of
     * the builder.
     *
     */
    public static ProcessOutputAsString runProcess(List<String> command, File workingDir,
            boolean storeOutput, boolean printOutput) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        return runProcess(builder, storeOutput, printOutput);
    }

    public static ProcessOutputAsString runProcess(List<String> command, File workingDir,
            boolean storeOutput, boolean printOutput, Long timeoutNanos) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);

        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);

        ProcessOutput<String, String> output = runProcess(builder, stdout, stderr, timeoutNanos);
        return new ProcessOutputAsString(output.getReturnValue(), output.getStdOut(), output.getStdErr());
    }

    /**
     * Helper method which receives the command instead of the builder, and launches
     * the process in the current
     * directory.
     *
     */
    public static ProcessOutputAsString runProcess(List<String> command,
            boolean storeOutput, boolean printOutput) {

        return runProcess(command, SpecsIo.getWorkingDir(), storeOutput, printOutput);
    }

    /**
     * Launches the process characterized by 'builder'.
     *
     * <p>
     * If there is any problem with the process, throws an exception.
     *
     */
    public static ProcessOutputAsString runProcess(ProcessBuilder builder, boolean storeOutput, boolean printOutput) {

        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);

        ProcessOutput<String, String> output = runProcess(builder, stdout, stderr);
        return new ProcessOutputAsString(output.getReturnValue(), output.getStdOut(), output.getStdErr());
    }

    /**
     * Helper method which receives the command instead of the builder, and launches
     * the process in the current
     * directory.
     *
     */
    public static <O, E> ProcessOutput<O, E> runProcess(List<String> command,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {

        return runProcess(command, SpecsIo.getWorkingDir(), outputProcessor, errorProcessor);
    }

    /**
     * Helper method which receives the command and the working directory instead of
     * the builder.
     *
     */
    public static <O, E> ProcessOutput<O, E> runProcess(List<String> command, File workingDir,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        return runProcess(builder, outputProcessor, errorProcessor);
    }

    /**
     * Launches the process characterized by 'builder'.
     *
     * <p>
     * If there is any problem with the process, throws an exception.
     *
     */
    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {
        return runProcess(builder, outputProcessor, errorProcessor, null);
    }

    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor, Long timeoutNanos) {

        return runProcess(builder, outputProcessor, errorProcessor, null, timeoutNanos);
    }

    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor,
            Consumer<OutputStream> input, Long timeoutNanos) {

        // The command in the builder might need processing (e.g., Windows system
        // commands)
        processCommand(builder);
        SpecsLogs.debug(() -> "Launching Process: " + String.join(" ", builder.command()));

        Process process;
        try {
            // Experiment: Calling Garbage Collector before starting process in order to
            // reduce memory required to fork VM
            // http://www.bryanmarty.com/2012/01/14/forking-jvm/
            long totalMemBefore = Runtime.getRuntime().totalMemory();
            System.gc();
            long totalMemAfter = Runtime.getRuntime().totalMemory();
            SpecsLogs.msgLib("Preparing to run process, memory before -> after GC: "
                    + SpecsStrings.parseSize(totalMemBefore) + " -> " + SpecsStrings.parseSize(totalMemAfter));
            process = builder.start();

        } catch (IOException e) {
            throw new RuntimeException("Could not start process", e);
        }

        ExecutorService stdoutThread = Executors.newSingleThreadExecutor();
        InputStream inputStream = process.getInputStream();
        Future<O> outputFuture = stdoutThread.submit(() -> outputProcessor.apply(inputStream));

        ExecutorService stderrThread = Executors.newSingleThreadExecutor();
        InputStream errorStream = process.getErrorStream();
        Future<E> errorFuture = stderrThread.submit(() -> errorProcessor.apply(errorStream));

        if (input != null) {
            ExecutorService stdinThread = Executors.newSingleThreadExecutor();
            OutputStream inStream = process.getOutputStream();
            stdinThread.submit(() -> input.accept(inStream));
            stdinThread.shutdown();
        }

        // The ExecutorService objects are shutdown, as they will not
        // receive more tasks.
        stdoutThread.shutdown();
        stderrThread.shutdown();

        return executeProcess(process, timeoutNanos, outputFuture, errorFuture);
    }

    /**
     * Performs several fixes on the builder command (e.g., adapts command for
     * Windows platforms)
     *
     */
    private static void processCommand(ProcessBuilder builder) {
        // Do nothing if no command
        if (builder.command().isEmpty()) {
            return;
        }

        if (isWindows()) {
            // Check if command is a file that exists in the working folder
            File workingDir = builder.directory();
            File command = new File(workingDir, builder.command().get(0));

            // If command is a file that exists, do nothing
            if (command.isFile()) {
                return;
            }

            // Update command
            List<String> newCommand = new ArrayList<>(builder.command().size() + 2);
            newCommand.add("cmd");
            newCommand.add("/c");
            newCommand.addAll(builder.command());

            builder.command(newCommand);
        } else if (isLinux()) {
            // Update command
            List<String> newCommand = new ArrayList<>(4);
            newCommand.add("bash");
            // Same user
            newCommand.add("-l");
            // Command
            newCommand.add("-c");
            newCommand.add(builder.command().stream()
                    .map(arg -> arg.replace(" ", "\\ "))
                    .collect(Collectors.joining(" ")));

            builder.command(newCommand);
        }

    }

    private static <O, E> ProcessOutput<O, E> executeProcess(Process process,
            Long timeoutNanos, Future<O> outputFuture, Future<E> errorFuture) {

        boolean timedOut = false;

        // Read streams before the process ends
        O output = null;
        E error = null;

        // wait forever, or just for a while
        try {
            if (timeoutNanos == null) {
                process.waitFor();
                SpecsLogs.debug(() -> "Process ended on its own");

            } else {
                SpecsLogs.debug(() -> "Launched process with a timeout of " + timeoutNanos + "ns");
                timedOut = !process.waitFor(timeoutNanos, TimeUnit.NANOSECONDS);
                boolean timedOutFinal = timedOut;
                SpecsLogs.debug(() -> "Process timed out? " + timedOutFinal);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            destroyProcess(process);
            throw new RuntimeException("Could not finish process with command '"
                    + process.info().commandLine().orElse("<NOT AVAILABLE>") + "'");
        }

        Exception outputException = null;
        try {
            output = outputFuture.get(1, TimeUnit.MINUTES);
            error = errorFuture.get(1, TimeUnit.MINUTES);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while waiting for output/error streams");

        } catch (Exception e) {
            SpecsLogs.info("Exception while waiting for output/error streams: " + e.getMessage());
            outputException = e;
        }

        int returnValue = timedOut ? -1 : process.exitValue();
        if (timedOut) {
            SpecsLogs.info("Process timed out after " + SpecsStrings.parseTime(timeoutNanos));
        }

        destroyProcess(process);
        return new ProcessOutput<>(returnValue, output, error, outputException);
    }

    private static void destroyProcess(Process process) {

        // TODO: a breakpoint is necessary before process destruction, or else the
        // "insts"
        // linestream is closed

        // Get descendants of the process
        List<ProcessHandle> processDescendants = process.descendants().collect(Collectors.toList());
        SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Killing process...");
        process.destroyForcibly();
        destroyDescendants(processDescendants);
    }

    private static void destroyDescendants(List<ProcessHandle> processDescendants) {

        // Destroy descendants
        ProgressCounter counter = new ProgressCounter(processDescendants.size());
        SpecsLogs.debug("Found " + processDescendants.size() + " descendants processes");
        for (ProcessHandle handle : processDescendants) {
            SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Killing descendant process... " + counter.next());

            handle.destroyForcibly();
            SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Waiting killing...");
            try {
                handle.onExit().get(1, TimeUnit.SECONDS);
                SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Destroyed");

            } catch (TimeoutException t) {
                SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Timeout while destroying descendant process!");

            } catch (Exception e) {
                SpecsLogs.debug(() -> "SpecsSystem.executeProcess: Could not destroy descendant process!");
            }
        }
    }

    public static ThreadFactory getDaemonThreadFactory() {
        return r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        };
    }

    /**
     * @return the StackTraceElement of the previous method of the method calling
     *         this method
     */
    public static StackTraceElement getCallerMethod() {
        return getCallerMethod(3);
    }

    public static StackTraceElement getCallerMethod(int callerMethodIndex) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length <= callerMethodIndex) {
            Logger.getLogger(SpecsSystem.class.getName()).warning(
                    "StackTrace size (" + stackTraceElements.length + ") is to "
                            + "small. Should have at least " + (callerMethodIndex + 1)
                            + " elements. Returning null.");
            return null;
        }

        return stackTraceElements[callerMethodIndex];
    }

    /**
     * @return true if the given class implements the given interface. False
     *         otherwise.
     */
    public static boolean implementsInterface(Class<?> aClass, Class<?> anInterface) {
        // Build set with interfaces of the given class
        Class<?>[] array = aClass.getInterfaces();
        Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(array));
        return interfaces.contains(anInterface);
    }

    /**
     * Method with standard initialization procedures for a Java SE program.
     *
     * <p>
     * Setups the logger and the Look&Feel for Swing. Additionally, looks for the
     * file 'suika.properties' on the working
     * folder and applies its options.
     */
    public static void programStandardInit() {
        // Disable security manager for Web Start
        // System.setSecurityManager(null);
        // Redirect output to the logger
        SpecsLogs.setupConsoleOnly();
        // Read the general bootstrap configuration for the application
        SpecsProperty.applyProperties();
        // Set the look and feel according to the system
        SpecsSwing.setSystemLookAndFeel();
        // Platform specific initialization
        // PlatformUtils.programPlatformInit();

        // Debug information.
        SpecsLogs.debug("Current platform name: " + System.getProperty("os.name"));
        SpecsLogs.debug("Current platform version: " + System.getProperty("os.version"));

    }

    /**
     * @return the name of the class of the main thread, or null if could not find
     *         the main thread.
     */
    public static StackTraceElement[] getMainStackTrace() {
        String mainThread = "main";
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        for (Thread thread : map.keySet()) {
            if (!thread.getName().equals(mainThread)) {
                continue;
            }

            return map.get(thread);
        }

        SpecsLogs.warn("Could not find thread '" + mainThread + "'.");
        return null;
    }

    public static String getProgramName() {
        StackTraceElement[] stack = getMainStackTrace();
        if (stack == null) {
            SpecsLogs.warn(
                    "Could not get stack of main thread. Returning empty string.");
            return "";
        }

        StackTraceElement main = stack[stack.length - 1];

        String programName = main.getClassName();
        int dotIndex = programName.lastIndexOf(".");
        if (dotIndex != -1) {
            programName = programName.substring(dotIndex + 1);
        }

        return programName;
    }

    /**
     * Returns true if the class with the given name is available.
     *
     * <p>
     * Code taken from: <br>
     * <a href="http://www.rgagnon.com/javadetails/java-0422.html">...</a>
     *
     */
    public static boolean isAvailable(String className) {
        try {
            Class.forName(className, false, null);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Similar to 'runProcess', but returns an int with the exit code, instead of
     * the ProcessOutput.
     *
     * <p>
     * Prints the output, but does not store it to a String.
     *
     */
    public static int run(List<String> command, File workingDir) {
        ProcessOutputAsString output = runProcess(command, workingDir, false, true);
        return output.getReturnValue();
    }

    public static void getNanoTime(Runnable runnable) {
        long time = System.nanoTime();
        runnable.run();
        time = System.nanoTime() - time;
        SpecsLogs.msgInfo("time elapsed: " + SpecsStrings.parseTime(time));

    }

    public static <T> T getNanoTime(Returnable<T> runnable) {
        long time = System.nanoTime();
        T t = runnable.run();
        time = System.nanoTime() - time;
        SpecsLogs.msgInfo("time elapsed: " + SpecsStrings.parseTime(time));

        return t;
    }

    interface Returnable<T> {
        T run();
    }

    /**
     * @return the current amount of memory, in bytes
     */
    public static long getUsedMemory(boolean callGc) {
        if (callGc) {
            System.gc();
        }

        System.gc();

        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static long getUsedMemoryMb(boolean callGc) {
        var usedMemory = getUsedMemory(callGc);

        long mbFactor = (long) Math.pow(1024, 2);

        return usedMemory / mbFactor;
    }

    /**
     * Taken from here:
     * <a href="http://www.inoneo.com/en/blog/9/java/get-the-jvm-peak-memory-usage">...</a>
     */
    public static void printPeakMemoryUsage() {
        // Place this code just before the end of the program
        try {
            StringBuilder memoryUsage = new StringBuilder();
            List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean pool : pools) {
                MemoryUsage peak = pool.getPeakUsage();

                memoryUsage.append(String.format("Peak %s memory used: %s\n", pool.getName(),
                        SpecsStrings.parseSize(peak.getUsed())));
            }

            // we print the result in the console
            SpecsLogs.msgInfo(memoryUsage.toString());

        } catch (Throwable t) {
            SpecsLogs.warn("Exception in agent", t);
        }
    }

    /**
     * Do-nothing function, for cases that accept Runnable and we do not want to do
     * anything.
     */
    public static void emptyRunnable() {

    }

    /**
     * @return true if the program worked, false if it could not be started
     */
    public static boolean isCommandAvailable(List<String> command, File workingdir) {
        try {
            // Try to launch program
            runProcess(command, workingdir, false, false);
            return true;
        } catch (Exception e) {
            // Check if cause was CreateProcess error=2
            Throwable currentThrowable = e;
            while (currentThrowable.getCause() != null) {
                currentThrowable = currentThrowable.getCause();
            }

            System.out.println("MESSAGE:" + currentThrowable.getMessage());

            return false;
        }

    }

    /**
     * Adds a path to the java.library.path property, and flushes the path cache so
     * that subsequent System.load calls
     * can find it.
     *
     * @param path The path to add
     * @deprecated this will not work in OpenJDK
     */
    @Deprecated
    public static void addJavaLibraryPath(String path) {

        System.setProperty("java.library.path",
                System.getProperty("java.library.path") + File.pathSeparatorChar + path);

        try {
            Lookup cl = MethodHandles.privateLookupIn(ClassLoader.class, MethodHandles.lookup());
            VarHandle sys_paths = cl.findStaticVarHandle(ClassLoader.class, "sys_paths", String[].class);

            var value = (String[]) sys_paths.get();

            // If not set, create new array with path
            if (value == null) {
                var paths = new String[1];
                paths[0] = path;
                sys_paths.set(paths);
                return;
            }

            var newValue = Arrays.copyOf(value, value.length + 1);
            newValue[value.length] = path;
            sys_paths.set(newValue);
        } catch (Exception e) {
            // Not supposed to happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Launches the callable in another thread and waits termination.
     *
     */
    public static <T> T executeOnThreadAndWait(Callable<T> callable) {
        // Launch weaver in another thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callable);
        executor.shutdown();

        return get(future);
    }

    public static <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SpecsLogs.info("Failed to complete execution on thread, returning null");
            return null;
        } catch (ExecutionException e) {
            // Rethrow cause
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw new RuntimeException(e.getCause());
            // throw new RuntimeException("Error while executing thread", e);
        } finally {
            future.cancel(true);
        }
    }

    /**
     * The contents of the Future, or null if there was a timeout.
     *
     */
    public static <T> T get(Future<T> future, long timeout, TimeUnit unit) {
        try {
            return future.get(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SpecsLogs.debug("get(): Failed to complete execution on thread, returning null");
            return null;
        } catch (ExecutionException e) {
            // Rethrow cause
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw new RuntimeException(e.getCause());
            // throw new RuntimeException("Error while executing thread", e);
        } catch (TimeoutException e) {
            SpecsLogs.debug("get(): Timeout while retriving Future");
            return null;
        } finally {
            future.cancel(true);
        }
    }

    /**
     * Returns a double based on the major (feature) and minor (interim) segments of
     * the runtime version.
     * <p>
     * Example: if the version string is "16.3.2-internal+11-specsbuild-20220403",
     * the return will be `16.3`.
     */
    public static double getJavaVersionNumber() {
        var version = Runtime.version();

        var major = version.feature();
        var minor = version.interim();

        String versionNumber = major + "." + minor;

        return Double.parseDouble(versionNumber);
    }

    /**
     * Returns the components of the version number of the running Java VM as an
     * immutable list.
     * <p>
     * Example: if the version string is "16.3.2-internal+11-specsbuild-20220403",
     * the return will be `[16, 3, 2]`.
     */
    public static List<Integer> getJavaVersion() {
        // Get property
        var version = Runtime.version();

        return version.version();
    }

    public static boolean hasMinimumJavaVersion(int major) {
        var version = Runtime.version();
        return major >= version.feature();
    }

    public static boolean hasMinimumJavaVersion(int major, int minor) {
        var version = Runtime.version();
        return major > version.feature() || (major == version.feature() && minor >= version.interim());
    }

    public static boolean isDebug() {
        return IS_DEBUG.get();
    }

    /**
     * Uses the copy constructor to create a copy of the given object. Throws
     * exception if the class does not have a
     * copy constructor.
     *
     */
    public static <T> T copy(T object) {

        if (object == null) {
            return null;
        }

        // Check if part of Immutable objects of java library
        if (IMMUTABLE_JAVA_CLASSES.contains(object)) {
            return object;
        }

        // Get class
        @SuppressWarnings("unchecked")
        Class<T> aClass = (Class<T>) object.getClass();

        Constructor<T> constructorMethod = null;
        try {
            // Create copy constructor: new T(T data)
            constructorMethod = aClass.getConstructor(aClass);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create call to copy constructor for '" + aClass.getSimpleName()
                            + "'. Check if class contains a constructor of the form 'new T(T object)'.",
                    e);
        }

        // Invoke constructor
        try {
            return constructorMethod.newInstance(object);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not call constructor for object '" + object.getClass().getSimpleName() + "'", e);
        }

    }

    public static <T> T newInstance(String classname, Class<T> expectedClass, Object... arguments) {
        Object object = newInstance(classname, arguments);

        return expectedClass.cast(object);
    }

    public static Object newInstance(String classname, Object... arguments) {
        Class<?> aClass;
        try {
            aClass = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class '" + classname + "'", e);
        }

        return newInstance(aClass, arguments);
    }

    public static <T> T newInstance(Class<T> aClass, Object... arguments) {

        Constructor<T> constructorMethod = null;
        try {
            // Create copy constructor: new T(T data)
            // constructorMethod = aClass.getConstructor(argClasses);
            constructorMethod = getConstructor(aClass, arguments);
        } catch (Exception e) {
            Class<?>[] argClasses = new Class<?>[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                argClasses[i] = arguments[i].getClass();
            }

            throw new RuntimeException(
                    "Could not create call to constructor for '" + aClass.getSimpleName()
                            + "' with arguments '" + Arrays.toString(argClasses) + "'",
                    e);
        }

        try {
            return constructorMethod.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException("Could not create new instance for '" + aClass.getSimpleName()
                    + "' with arguments " + Arrays.toString(arguments), e);
        }

    }

    /**
     * @return the first constructor that is compatible with the given arguments, or
     *         null if none is found
     */
    public static <T> Constructor<T> getConstructor(Class<T> aClass, Object... arguments) {
        constructorTest: for (var constructor : aClass.getConstructors()) {
            // Verify if arguments are compatible
            var paramTypes = constructor.getParameterTypes();

            // Check number of parameters
            if (paramTypes.length != arguments.length) {
                continue;
            }

            for (int i = 0; i < paramTypes.length; i++) {
                if (!paramTypes[i].isAssignableFrom(arguments[i].getClass())) {
                    break constructorTest;
                }
            }

            @SuppressWarnings("unchecked")
            var validConstructor = (Constructor<T>) constructor;
            return validConstructor;
        }

        return null;
    }

    /**
     * @return true if the JVM is currently executing in a Linux system, false
     *         otherwise
     */
    public static boolean isLinux() {
        return IS_LINUX;
    }

    /**
     * @return true if the JVM is currently executing in a Windows system, false
     *         otherwise
     */
    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    /**
     * Equivalent to class.isInstance.
     *
     * <p>
     * Used when direct access to .class is not allowed.
     *
     * @return true, if the value is an instance of the given classpath
     */
    public static boolean isInstance(String className, Object value) {
        try {
            Class<?> aClass = Class.forName(className);
            return aClass.isInstance(value);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class '" + className + "'", e);
        }
    }

    public static Object invoke(Object object, String method, Object... args) {

        Class<?>[] types = new Class[args.length];
        int index = 0;
        for (var arg : args) {
            types[index] = arg.getClass();
            index++;
        }

        Class<?> invokingClass = object instanceof Class ? (Class<?>) object : object.getClass();

        // If method is static, object will be ignored
        Object invokingObject = object;

        // Choose best method
        Method invokingMethod = getMethod(invokingClass, method, types);

        if (invokingMethod == null) {
            throw new RuntimeException("Could not find method '" + method + "' for object " + object + " and arguments "
                    + Arrays.asList(types));
        }

        try {
            return invokingMethod.invoke(invokingObject, args);
        } catch (Exception e) {
            throw new RuntimeException("Error while invoking method '" + method + "'", e);
        }
    }

    /**
     * Similar to findMethod(), but caches results. Be careful, can lead to
     * unintended errors.
     *
     */
    public static Method getMethod(Class<?> invokingClass, String methodName, Class<?>... types) {
        // Use methodId to cache results
        String methodId = getMethodId(invokingClass, methodName, types);
        return CACHED_METHODS.computeIfAbsent(methodId, key -> findMethod(invokingClass, methodName, types));
    }

    private static String getMethodId(Class<?> invokingClass, String methodName, Class<?>... types) {
        StringBuilder methodId = new StringBuilder();

        methodId.append(invokingClass.getName());
        methodId.append("::").append(methodName);
        for (var type : types) {
            methodId.append(",");
            methodId.append(type.getName());
        }

        return methodId.toString();
    }

    public static Method findMethod(Class<?> invokingClass, String methodName, Class<?>... types) {
        Method invokingMethod = null;
        top: for (var classMethod : invokingClass.getMethods()) {
            // Check name
            if (!classMethod.getName().equals(methodName)) {
                continue;
            }

            // Check if types are compatible
            var paramTypes = classMethod.getParameterTypes();
            if (paramTypes.length != types.length) {
                continue;
            }

            for (int i = 0; i < paramTypes.length; i++) {
                if (!paramTypes[i].isAssignableFrom(types[i])) {
                    continue top;
                }
            }

            invokingMethod = classMethod;
        }
        return invokingMethod;
    }

    private static String getFieldId(Class<?> invokingClass, String fieldName) {
        return invokingClass.getName() + "::" + fieldName;
    }

    public static Optional<Field> getField(Class<?> invokingClass, String fieldName) {
        String fieldId = getFieldId(invokingClass, fieldName);
        return CACHED_FIELDS.computeIfAbsent(fieldId, key -> findField(invokingClass, fieldName));
    }

    private static Optional<Field> findField(Class<?> invokingClass, String fieldName) {

        try {
            return Optional.of(invokingClass.getField(fieldName));
        } catch (NoSuchFieldException e1) {
            // No field, return empty
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Could not get field '" + fieldName + "' from object of class" + invokingClass,
                    e);
        }
    }

    /**
     * Invokes the given method as a property. If the method with name 'foo()' could
     * not be found, looks for a .getFoo()
     * method.
     *
     */
    public static Object invokeAsGetter(Object object, String methodName) {
        Class<?> invokingClass = object instanceof Class ? (Class<?>) object : object.getClass();

        SpecsLogs.debug(() -> "invokeAsGetter: processing '" + methodName + "' for class '" + invokingClass + "'");

        // Check if getter is a field
        var field = getField(invokingClass, methodName);

        if (field.isPresent()) {
            return field.map(f -> {
                try {
                    SpecsLogs.debug(() -> "invokeAsGetter: found field");
                    return f.get(object);
                } catch (Exception e) {
                    throw new RuntimeException(
                            "Could not get value from field '" + methodName + "' in class " + invokingClass, e);
                }
            }).get();
        }

        // Check if it as a method with the same name and arity 0
        Method invokingMethod = getMethod(invokingClass, methodName);

        if (invokingMethod != null) {
            try {
                SpecsLogs.debug(() -> "invokeAsGetter: found method with arity 0");
                return invokingMethod.invoke(object);
            } catch (Exception e) {
                throw new RuntimeException("Error while invoking method '" + methodName + "' in class " + invokingClass,
                        e);
            }
        }

        // Try camelCase getter
        String getterName = "get" + methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1);

        invokingMethod = getMethod(invokingClass, getterName);
        if (invokingMethod != null) {
            try {
                SpecsLogs.debug(() -> "invokeAsGetter: found camelCase getter ('" + getterName + "')");
                return invokingMethod.invoke(object);
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error while invoking camelCase getter '" + getterName + "' in class " + invokingClass, e);
            }
        }

        throw new RuntimeException(
                "Could not resolve property '" + methodName + "' for instance of class '" + invokingClass + "'");
    }

    public static <K, T> List<T> getStaticFields(Class<? extends K> aClass, Class<? extends T> type) {

        List<T> fields = new ArrayList<>();

        for (Field field : aClass.getFields()) {
            if (!type.isAssignableFrom(field.getType())) {
                continue;
            }

            if (!Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            try {
                fields.add(type.cast(field.get(null)));
            } catch (Exception e) {
                throw new RuntimeException("Could not retrive value of field: " + field);
            }
        }

        return fields;
    }

    /**
     * Stops the program with an exception.
     */
    public static void stop() {
        throw new RuntimeException("STOP");
    }

    /**
     * Reads the implementation version that is in the manifest file. Reads property
     * Implementation-Version.
     *
     */
    public static String getBuildNumber() {
        // Check if manifest file exists
        if (!SpecsIo.hasResource("META-INF/MANIFEST.MF")) {
            return null;
        }
        try {
            var manifest = new Manifest(SpecsIo.resourceToStream("META-INF/MANIFEST.MF"));
            var attr = manifest.getMainAttributes();
            return attr.getValue(getBuildNumberAttr());
        } catch (IOException e) {
            SpecsLogs.info("Could not read manifest file: " + e.getMessage());
            return null;
        }

    }

    /**
     * @return the name of the attribute for SPeCS build number
     */
    public static String getBuildNumberAttr() {
        return BUILD_NUMBER_ATTR;
    }

    public static String createBuildNumber() {
        var dtf = DateTimeFormatter.ofPattern("uuuuMMdd-HHmm");
        var now = LocalDateTime.now();
        return dtf.format(now); // 20210322-16:37
    }

    /**
     * @return the fundamental cause of the exception
     */
    public static Throwable getLastCause(Throwable e) {
        var cause = e.getCause();
        while (cause != null) {
            e = cause;
            cause = e.getCause();
        }

        return e;
    }

    /**
     * Attempts to locate a PowerShell executable available in the system's PATH.
     * <p>
     * Tries to find "pwsh" (PowerShell Core) first, and if not found, falls back to
     * "powershell" (Windows PowerShell).
     * It does so by attempting to execute each candidate with a command that
     * queries the PowerShell version.
     * If a suitable executable is found, its name is returned.
     * </p>
     *
     * @return the name of the PowerShell executable found ("pwsh" or "powershell")
     * @throws IllegalStateException if no PowerShell executable is available on the
     *                               system PATH
     */
    private static String findPwsh() {
        // GitHub Windows runners have pwsh in PATH; if not, fall back to powershell.exe
        for (String exe : List.of("pwsh", "powershell")) {
            try {
                Process p = new ProcessBuilder(exe, "-NoLogo", "-NoProfile", "-Command", "$PSVersionTable.PSVersion")
                        .redirectErrorStream(true).start();
                if (p.waitFor() == 0)
                    return exe;
            } catch (Exception ignored) {
            }
        }
        throw new IllegalStateException("No PowerShell available on PATH");
    }

    /**
     * Returns the path to the Windows PowerShell executable.
     *
     * @return the path to the Windows PowerShell executable as a String
     * @throws IllegalStateException if the PowerShell executable cannot be found
     */
    public static String getWindowsPowershell() {
        return WINDOWS_POWERSHELL.get();
    }
}
