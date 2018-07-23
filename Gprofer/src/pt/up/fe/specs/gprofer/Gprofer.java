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
 * specific language governing permissions and limitations under the License. under the License.
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

public class Gprofer {

    public static GprofData parseTextProfile(String textProfilePath) {

        File textProfile = new File(textProfilePath);
        return parseTextProfile(textProfile);
    }

    private static GprofData parseTextProfile(File textProfile) {

        return parseGprof(SpecsIo.toInputStream(textProfile));
    }

    /**
     * Runs on a temporary directory.
     * 
     * @param binaryPath
     * @return
     */
    public static GprofData profile(String binaryPath) {

        return profile(new File(binaryPath));
    }

    /**
     * Runs on a temporary directory.
     * 
     * @param binary
     * @return
     */
    public static GprofData profile(File binary) {

        return profile(binary, Collections.emptyList(), 1);
    }

    /**
     * Runs on a temporary directory.
     * 
     * @param binary
     * @param args
     * @param numRuns
     * @return
     */
    public static GprofData profile(File binary, List<String> args, int numRuns) {

        File workingDir = SpecsIo.mkdir(
                SpecsIo.getTempFolder(),
                "gprofer_" + UUID.randomUUID().toString());

        boolean deleteWorkingDir = true;

        return profile(binary, args, numRuns, workingDir, deleteWorkingDir);
    }

    /**
     * Runs on then provided directory.
     * 
     * @param binary
     * @param args
     * @param numRuns
     * @param workingDir
     * @param deleteWorkingDir
     * @return
     */
    public static GprofData profile(File binary, List<String> args, int numRuns, File workingDir,
            boolean deleteWorkingDir) {

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

            Boolean result = runBinary(binary, args, workingDir);
            if (!result) {
                throw new RuntimeException();
            }

            makeGmon(currentRun, workingDir, filesToDelete, gmons);

            currentRun++;
        }

        GprofData data = summarizeGmons(binary, workingDir, gmons, filesToDelete);

        deleteTempFiles(filesToDelete);

        return data;
    }

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

    private static Boolean runBinary(File binary, List<String> args, File workingDir) {

        List<String> binaryCommand = new ArrayList<>();
        binaryCommand.add(binary.getAbsolutePath());
        binaryCommand.addAll(args);

        ProcessOutputAsString result = SpecsSystem.runProcess(binaryCommand, workingDir, true, false);

        if (result.isError()) {

            SpecsLogs.setPrintStackTrace(false);
            SpecsLogs.msgWarn("Could not profile the binary \"" + binary + "\". Execution terminated with error.");
            SpecsLogs.msgWarn("stdout: " + result.getStdOut());
            SpecsLogs.msgWarn("stderr: " + result.getStdErr());
            SpecsLogs.setPrintStackTrace(true);

            return false;
        }

        return true;
    }

    private static void deleteTempFiles(List<File> filesToDelete) {

        for (File file : filesToDelete) {

            if (file.isDirectory()) {
                SpecsIo.deleteFolder(file);
            } else {
                file.delete();
            }
        }
    }

    public static String getJsonData(GprofData data) {

        return new Gson().toJson(data);
    }

    private static GprofData parseGprof(String gprofOutput) {

        InputStream gprofStream = new ByteArrayInputStream(gprofOutput.getBytes(Charset.defaultCharset()));

        return parseGprof(gprofStream);
    }

    private static GprofData parseGprof(InputStream gprofStream) {

        Map<String, GprofLine> table = parseTable(gprofStream);
        List<String> hotspots = makeHotspots(table);

        return new GprofData(table, hotspots);
    }

    private static List<String> makeHotspots(Map<String, GprofLine> table) {

        List<String> hotspots = table.values().stream()
                .sorted(Comparator.comparing(GprofLine::getPercentage).reversed())
                .map(row -> row.getName())
                .collect(Collectors.toList());

        return hotspots;
    }

    private static Map<String, GprofLine> parseTable(InputStream gprofOutput) {

        LineStream lines = LineStream.newInstance(gprofOutput, "gprof output");

        return lines.stream()
                .map(Gprofer::parseLine)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(GprofLine::getName, Function.identity()));
    }

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

            String name = splitter.parse(StringSplitterRules::string);

            GprofLine gproferRow = new GprofLine(percentage, cumulativeSeconds, selfSeconds, calls, selfMsCall,
                    totalMsCall, name);

            return Optional.ofNullable(gproferRow);
        }
        return Optional.empty();
    }
}
