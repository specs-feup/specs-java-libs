/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.jadx;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import jadx.api.JadxArgs;
import jadx.api.JadxDecompiler;
import jadx.api.JadxDecompiler.ProgressListener;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

public class SpecsJadx {

    private static final Map<File, File> CACHED_DECOMPILATIONS = new HashMap<>();
    private static final String CACHE_FOLDERNAME = "specs_jadx_cache";

    private static List<String> CACHE_FILTER = new ArrayList<>();
    private Predicate<String> classFilter = cls -> (true);

    static {
        var baseCacheFolder = getCacheFolder();

        SpecsIo.deleteFolderContents(baseCacheFolder);
        baseCacheFolder.deleteOnExit();
    }

    public static File getCacheFolder() {
        return SpecsIo.getTempFolder(CACHE_FOLDERNAME);
    }

    public File decompileAPK(File apk) throws DecompilationFailedException {
        return decompileAPK(apk, null);
    }

    public File decompileAPK(File apk, List<String> packageFilter) throws DecompilationFailedException {

        // Delete cache if filter changed
        if (packageFilter != null && (!packageFilter.containsAll(CACHE_FILTER)
                || !CACHE_FILTER.containsAll(packageFilter))) {
            CACHED_DECOMPILATIONS.remove(apk);
            CACHE_FILTER = new ArrayList<String>(packageFilter);
        }

        if (CACHED_DECOMPILATIONS.containsKey(apk)) {
            var folder = CACHED_DECOMPILATIONS.get(apk);
            SpecsLogs.info(
                    String.format("JADX: CACHED | %s -> %s", apk.getAbsolutePath(), folder.getAbsolutePath()));
            return folder;
        }

        var outputFolder = SpecsIo.mkdir(getCacheFolder(), UUID.randomUUID().toString());

        SpecsLogs.info(
                String.format("Jadx: DECOMPILE START | %s -> %s", apk.getAbsolutePath(),
                        outputFolder.getAbsolutePath()));

        JadxArgs jadxArgs = new JadxArgs();
        jadxArgs.setInputFile(apk);
        jadxArgs.setSkipResources(true);
        jadxArgs.setOutDir(outputFolder);

        if (packageFilter != null && packageFilter.size() > 0) {

            classFilter = cls -> packageFilter.stream()
                    .anyMatch(prefix -> cls.startsWith(prefix));

            jadxArgs.setClassFilter(classFilter);
            SpecsLogs.info(
                    String.format("Jadx: DECOMPILE FILTER | %s", packageFilter));
        }

        try (JadxDecompiler jadx = new JadxDecompiler(jadxArgs)) {
            jadx.load();

            SpecsLogs.info(
                    String.format("Jadx: DECOMPILING | Found %d packages and %d classes",
                            jadx.getPackages().stream().filter(pac -> classFilter.test(pac.getFullName())).count(),
                            jadx.getClasses().stream().filter(cls -> classFilter.test(cls.getFullName())).count()));

            jadx.save(3000, new ProgressListener() {
                @Override
                public void progress(long done, long total) {
                    SpecsLogs.info(String.format("Jadx: DECOMPILING | %d%%", (done * 100L) / total));
                }
            });
            SpecsLogs.info("Jadx: DECOMPILE FINISHED");
            CACHED_DECOMPILATIONS.put(apk, outputFolder);

            return outputFolder;
        } catch (Exception e) {
            throw new DecompilationFailedException(e.getMessage(), e);
        }
    }
}
