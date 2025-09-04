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

package pt.up.fe.specs.gprofer.data;

import java.util.List;
import java.util.Map;

/**
 * Represents the parsed data from a gprof profiling session, including the table of profiling lines and the list of hotspots.
 */
public class GprofData {

    private final Map<String, GprofLine> table;
    private final List<String> hotspots;

    /**
     * Constructs a GprofData object with the given table and hotspots.
     *
     * @param table a map of function names to their profiling data
     * @param hotspots a list of function names sorted by their profiling percentage
     */
    public GprofData(Map<String, GprofLine> table, List<String> hotspots) {
        this.table = table;
        this.hotspots = hotspots;
    }

    /**
     * Returns the table of profiling data.
     *
     * @return a map of function names to GprofLine objects
     */
    public Map<String, GprofLine> getTable() {
        return table;
    }

    /**
     * Returns the list of hotspots (function names sorted by percentage).
     *
     * @return a list of function names
     */
    public List<String> getHotspots() {
        return hotspots;
    }

}
