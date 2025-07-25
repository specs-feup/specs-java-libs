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

package pt.up.fe.specs.compress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Enum representing supported compression formats for file output.
 * <p>
 * Provides methods to create compressors for ZIP and GZ formats and to retrieve formats by extension.
 */
public enum ZipFormat implements StringProvider {

    /** ZIP file format. */
    ZIP("zip"),
    /** GZ (GZip) file format. */
    GZ("gz");

    private static final Lazy<EnumHelperWithValue<ZipFormat>> ENUM_HELPER = EnumHelperWithValue.newLazyHelperWithValue(ZipFormat.class);

    private final String extension;

    /**
     * Creates a new ZipFormat with the given file extension.
     *
     * @param extension the file extension for the format
     */
    private ZipFormat(String extension) {
        this.extension = extension;
    }

    /**
     * Returns an Optional containing the ZipFormat corresponding to the given extension, if available.
     *
     * @param extension the file extension
     * @return an Optional with the matching ZipFormat, or empty if not found
     */
    public static Optional<ZipFormat> fromExtension(String extension) {
        return ENUM_HELPER.get().fromValueTry(extension);
    }

    /**
     * Returns the string representation (file extension) of this format.
     *
     * @return the file extension
     */
    @Override
    public String getString() {
        return extension;
    }

    /**
     * Creates a new file compressor OutputStream for the given filename and output stream, according to this format.
     *
     * @param filename the name of the file to compress (used for ZIP entries)
     * @param outputStream the output stream to wrap
     * @return a new OutputStream for the compressed file
     * @throws RuntimeException if the compressor cannot be created
     */
    public OutputStream newFileCompressor(String filename, OutputStream outputStream) {
        switch (this) {
        case ZIP:
            // Create zip stream
            ZipOutputStream zipStream = new ZipOutputStream(outputStream);
            // Create zip entry
            try {
                zipStream.putNextEntry(new ZipEntry(filename));
                return zipStream;
            } catch (IOException e) {
                SpecsIo.closeStreamAfterError(zipStream);
                throw new RuntimeException("Could not add entry '" + filename + "' to zip file", e);
            }
        case GZ:
            try {
                return new GzipCompressorOutputStream(outputStream);
            } catch (IOException e) {
                throw new RuntimeException("Could not create GZip compressor", e);
            }
        default:
            throw new RuntimeException("Format not supported yet: " + this);
        }
    }

}
