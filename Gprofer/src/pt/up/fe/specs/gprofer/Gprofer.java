/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.gprofer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import pt.up.fe.specs.gprofer.data.GprofData;
import pt.up.fe.specs.gprofer.data.GprofLine;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.stringsplitter.StringSplitter;
import pt.up.fe.specs.util.stringsplitter.StringSplitterRules;
import pt.up.fe.specs.util.system.ProcessOutput;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Utility class for profiling binaries using gprof and parsing the results.
 */
public class Gprofer {

    /**
     * Parses a gprof text profile from the given file path.
     *
     * @param textProfilePath path to the gprof text profile
     * @return a GprofData object containing the parsed data
     */
    public static GprofData parseTextProfile(String textProfilePath) {
        File textProfile = new File(textProfilePath);
        return parseTextProfile(textProfile);
    }

    /**
     * Parses a gprof text profile from the given file.
     *
     * @param textProfile file containing the gprof text profile
     * @return a GprofData object containing the parsed data
     */
    private static GprofData parseTextProfile(File textProfile) {
        return parseGprof(SpecsIo.toInputStream(textProfile));
    }

    /**
     * Profiles the given binary using gprof in a temporary directory.
     *
     * @param binaryPath path to the binary to profile
     * @return a GprofData object containing the profiling results
     */
    public static GprofData profile(String binaryPath) {
        return profile(new File(binaryPath));
    }

    /**
     * Profiles the given binary using gprof in a temporary directory.
     *
     * @param binary the binary file to profile
     * @return a GprofData object containing the profiling results
     */
    public static GprofData profile(File binary) {
        return profile(binary, Collections.emptyList(), 1);
    }

    /**
     * Profiles the given binary using gprof in a temporary directory, with arguments and number of runs.
     *
     * @param binary the binary file to profile
     * @param args arguments to pass to the binary
     * @param numRuns number of times to run the binary
     * @return a GprofData object containing the profiling results
     */
    public static GprofData profile(File binary, List<String> args, int numRuns) {
        File workingDir = SpecsIo.mkdir(
                SpecsIo.getTempFolder(),
                "gprofer_" + UUID.randomUUID().toString());
        boolean deleteWorkingDir = true;
        boolean checkReturn = true;
        return profile(binary, args, numRuns, workingDir, deleteWorkingDir, checkReturn);
    }

    /**
     * Profiles the given binary using gprof in the provided directory.
     *
     * @param binary the binary file to profile
     * @param args arguments to pass to the binary
     * @param numRuns number of times to run the binary
     * @param workingDir the working directory to use
     * @param deleteWorkingDir whether to delete the working directory after profiling
     * @param checkReturn whether to check the return code of the process
     * @return a GprofData object containing the profiling results
     */
    public static GprofData profile(File binary, List<String> args, int numRuns, File workingDir,
            boolean deleteWorkingDir, boolean checkReturn) {
        if (!binary.exists()) {
            throw new RuntimeException("Could not locate the binary \"" + binary + "\".");
        }
        if (!workingDir.exists()) {
            throw new RuntimeException("Could not locate the working directory \"" + workingDir + "\".");
        }
        int currentRun = 0;
        List<File> gmons = new ArrayList<>();
        List<File> filesToDelete = new ArrayList<>();
        if (deleteWorkingDir) {
            filesToDelete.add(workingDir);
        }
        while (currentRun < numRuns) {
            runBinary(binary, args, workingDir, checkReturn);
            makeGmon(currentRun, workingDir, filesToDelete, gmons);
            currentRun++;
        }
        GprofData data = summarizeGmons(binary, workingDir, gmons, filesToDelete);
        deleteTempFiles(filesToDelete);
        return data;
    }

    /**
     * Summarizes the gmon files into a single GprofData object.
     *
     * @param binary the binary file
     * @param workingDir the working directory
     * @param gmons the list of gmon files
     * @param filesToDelete files to delete after processing
     * @return a GprofData object containing the summarized data
     */
    private static GprofData summarizeGmons(File binary, File workingDir,
            List<File> gmons, List<File> filesToDelete) {
        List<String> command = new ArrayList<>();
        command.add("gprof");
        command.add("-bp");
        command.add("-zc");
        command.add("-s");
        command.add(binary.getAbsolutePath());
        List<String> gmonNames = gmons.stream().map(File::getAbsolutePath).collect(Collectors.toList());
        command.addAll(gmonNames);
        ProcessOutput<GprofData, InputStream> result = SpecsSystem.runProcess(
                command,
                workingDir,
                Gprofer::parseGprof, Function.identity());
        File gmonSum = new File(workingDir, "gmon.sum");
        filesToDelete.add(gmonSum);
        return result.getStdOut();
    }

    /**
     * Moves the gmon.out file to a new file for the current run.
     *
     * @param currentRun the current run index
     * @param workingDir the working directory
     * @param filesToDelete files to delete after processing
     * @param gmons list to add the new gmon file to
     */
    private static void makeGmon(int currentRun, File workingDir, List<File> filesToDelete, List<File> gmons) {
        File gmon = new File(workingDir, "gmon.out");
        File newGmon = new File(gmon.getAbsolutePath() + "." + currentRun);
        gmons.add(newGmon);
        try {
            Files.move(gmon.toPath(), newGmon.toPath(), StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            throw new RuntimeException("Could not move file '" + gmon + "'", e);
        }
        filesToDelete.add(newGmon);
    }

    /**
     * Runs the binary with the given arguments in the specified working directory.
     *
     * @param binary the binary file
     * @param args arguments to pass to the binary
     * @param workingDir the working directory
     * @param checkReturn whether to check the return code of the process
     */
    private static void runBinary(File binary, List<String> args, File workingDir, boolean checkReturn) {
        List<String> binaryCommand = new ArrayList<>();
        binaryCommand.add(binary.getAbsolutePath());
        binaryCommand.addAll(args);
        ProcessOutputAsString result = SpecsSystem.runProcess(binaryCommand, workingDir, true, false);
        if (checkReturn && result.isError()) {
            SpecsLogs.setPrintStackTrace(false);
            SpecsLogs.warn("Could not profile the binary \"" + binary + "\". Execution terminated with error.");
            SpecsLogs.warn("stdout: " + result.getStdOut());
            SpecsLogs.warn("stderr: " + result.getStdErr());
            SpecsLogs.setPrintStackTrace(true);
            throw new RuntimeException();
        }
    }

    /**
     * Deletes the temporary files and folders used during profiling.
     *
     * @param filesToDelete list of files and folders to delete
     */
    private static void deleteTempFiles(List<File> filesToDelete) {
        for (File file : filesToDelete) {
            if (file.isDirectory()) {
                SpecsIo.deleteFolder(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * Converts the given GprofData object to its JSON representation.
     *
     * @param data the GprofData object
     * @return a JSON string representing the data
     */
    public static String getJsonData(GprofData data) {
        return new Gson().toJson(data);
    }

    /**
     * Parses gprof output from a string.
     *
     * @param gprofOutput the gprof output as a string
     * @return a GprofData object containing the parsed data
     */
    private static GprofData parseGprof(String gprofOutput) {
        InputStream gprofStream = new ByteArrayInputStream(gprofOutput.getBytes(Charset.defaultCharset()));
        return parseGprof(gprofStream);
    }

    /**
     * Parses gprof output from an InputStream.
     *
     * @param gprofStream the gprof output as an InputStream
     * @return a GprofData object containing the parsed data
     */
    private static GprofData parseGprof(InputStream gprofStream) {
        Map<String, GprofLine> table = parseTable(gprofStream);
        List<String> hotspots = makeHotspots(table);
        return new GprofData(table, hotspots);
    }

    /**
     * Creates a list of hotspots sorted by percentage from the profiling table.
     *
     * @param table the profiling table
     * @return a list of function names sorted by percentage
     */
    private static List<String> makeHotspots(Map<String, GprofLine> table) {
        List<String> hotspots = table.values().stream()
                .sorted(Comparator.comparing(GprofLine::getPercentage).reversed())
                .map(row -> row.getName())
                .collect(Collectors.toList());
        return hotspots;
    }

    /**
     * Parses the profiling table from the gprof output InputStream.
     *
     * @param gprofOutput the gprof output InputStream
     * @return a map of function names to GprofLine objects
     */
    private static Map<String, GprofLine> parseTable(InputStream gprofOutput) {
        LineStream lines = LineStream.newInstance(gprofOutput, "gprof output");
        return lines.stream()
                .map(Gprofer::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(GprofLine::getName, Function.identity()));
    }

    /**
     * Parses a single line of gprof output into a GprofLine object.
     *
     * @param line the line of gprof output
     * @return an Optional containing the GprofLine if parsing was successful, or empty otherwise
     */
    private static Optional<GprofLine> parseLine(String line) {
        StringSplitter splitter = new StringSplitter(line.trim());
        Optional<Double> percentageTry = splitter.parseTry(StringSplitterRules::doubleNumber);
        if (percentageTry.isPresent()) {
            Double percentage = percentageTry.get();
            Double cumulativeSeconds = splitter.parse(StringSplitterRules::doubleNumber);
            Double selfSeconds = splitter.parse(StringSplitterRules::doubleNumber);
            Integer calls = null;
            Double selfMsCall = null;
            Double totalMsCall = null;
            Optional<Integer> callsTry = splitter.parseTry(StringSplitterRules::integer);
            if (callsTry.isPresent()) {
                calls = callsTry.get();
                selfMsCall = splitter.parse(StringSplitterRules::doubleNumber);
                totalMsCall = splitter.parse(StringSplitterRules::doubleNumber);
            }
            String name = splitter.toString();
            name = name.replaceAll(", ", ",");
            GprofLine gproferRow = new GprofLine(percentage, cumulativeSeconds, selfSeconds, calls, selfMsCall,
                    totalMsCall, name);
            return Optional.ofNullable(gproferRow);
        }
        return Optional.empty();
    }
}
