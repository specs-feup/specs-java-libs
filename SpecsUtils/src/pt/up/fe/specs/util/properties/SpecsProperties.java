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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.properties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.KeyProvider;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Helper wrapper around java.util.Properties.
 * 
 * @author JoaoBispo
 *
 */
public class SpecsProperties {

    private final Properties props;

    public SpecsProperties(Properties props) {
        this.props = props;
    }

    public static SpecsProperties newEmpty() {
        return new SpecsProperties(new Properties());
    }

    /**
     * Helper method which accepts a ResourceProvider and copies the file if it does not exist.
     * 
     * @param resource
     * @return
     */
    public static SpecsProperties newInstance(ResourceProvider resource) {

        File propsFile = resource.writeVersioned(SpecsIo.getWorkingDir(), resource.getClass()).getFile();
        /*
        File propsFile = new File(resource.getResourceName());
        
        // If file does not exist, copy resource
        if (!propsFile.isFile()) {
        propsFile = IoUtils.resourceCopy(resource.getResource(), IoUtils.getWorkingDir(), false);
        }
        */
        return newInstance(propsFile);
    }

    public static SpecsProperties newInstance(File propertiesFile) {
        Preconditions.checkNotNull(propertiesFile, "Input file must not be null");

        try (InputStream inputStream = new FileInputStream(propertiesFile)) {
            return load(inputStream);
        } catch (Exception ex) {
            throw new RuntimeException("Could not load properties file '" + propertiesFile + "'", ex);
        }
    }

    /**
     * Given a File object, loads the contents of the file into a Java Properties object.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file, could not load the Properties object)
     * returns null and logs the cause.
     * 
     * @param file
     *            a File object representing a file.
     * @return If successfull, a Properties objects with the contents of the file. Null otherwise.
     */
    private static SpecsProperties load(InputStream inputStream) {

        Properties props = new Properties();

        try {
            props.load(inputStream);
            inputStream.close();
        } catch (IOException ex) {
            SpecsLogs.warn("IOException: " + ex.getMessage());
            return null;
        }

        return new SpecsProperties(props);
    }

    public boolean hasKey(KeyProvider<String> key) {
        return props.keySet().contains(key.getKey());
    }

    /**
     * Returns the string corresponding to the given key.
     * 
     * <p>
     * Trims the string before returning.
     * 
     * @param key
     * @return
     */
    public String get(KeyProvider<String> key) {
        if (!props.keySet().contains(key.getKey())) {
            SpecsLogs.msgInfo("! Properties file is missing key '" + key.getKey() + "'");
            return "";
        }
        return props.getProperty(key.getKey()).trim();
    }

    public Object put(KeyProvider<String> key, Object value) {
        return props.put(key.getKey(), value);
    }

    public int getInt(KeyProvider<String> key) {
        String intString = get(key);

        return Integer.parseInt(intString);
    }

    public boolean getBoolean(KeyProvider<String> key) {
        String boolString = get(key);

        return Boolean.parseBoolean(boolString);
    }

    /**
     * Builds a folder from a property.
     * 
     */
    public File getFolder(KeyProvider<String> key) {
        String folderName = get(key);
        File folder = null;
        if (!folderName.equals("")) {
            folder = SpecsIo.mkdir(folderName);
        }

        return folder;
    }

    public Optional<File> getExistingFile(KeyProvider<String> key) {
        String filename = get(key);
        File file = new File(filename);

        return file.isFile() ? Optional.of(file) : Optional.empty();
    }

    public Optional<File> getExistingFolder(KeyProvider<String> key) {
        var folder = getFolder(key);

        return folder.isDirectory() ? Optional.of(folder) : Optional.empty();
    }

    public <T extends Enum<T> & StringProvider> Optional<T> getEnum(KeyProvider<String> key,
            EnumHelperWithValue<T> helper) {
        String enumName = get(key);

        if (enumName == null) {
            return Optional.empty();
        }

        return Optional.of(helper.fromValue(enumName));
    }

    /**
     * Saves a properties object to a file.
     * 
     * @param outputfile
     * @param props
     * @return true if there was no problems. False otherwise
     */
    public boolean store(File outputfile) {

        try (OutputStream outStream = new BufferedOutputStream(new FileOutputStream(outputfile))) {

            props.store(outStream, "");
            outStream.close();

        } catch (IOException ex) {
            SpecsLogs.warn("Could not save properties object to file '" + outputfile + "'", ex);
            return false;
        }

        return true;
    }

    /**
     * 
     * @return the internal Properties object
     */
    public Properties getProperties() {
        return props;
    }

    @Override
    public String toString() {
        return props.toString();
    }

    /**
     * Creates a JSON string equivalent to this properties object.
     * 
     * @return
     */
    public String toJson() {
        StringBuilder json = new StringBuilder();

        json.append("{");

        String jsonContents = props.entrySet().stream()
                .map(entry -> entry.getKey().toString() + ":" + entry.getValue().toString())
                .collect(Collectors.joining(", "));

        json.append(jsonContents);

        json.append("}");

        return json.toString();
    }

    public String getOrElse(KeyProvider<String> key, String defaultValue) {
        if (!hasKey(key)) {
            return defaultValue;
        }

        return get(key);
    }
}
