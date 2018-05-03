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

public enum ZipFormat implements StringProvider {

    ZIP("zip"),
    GZ("gz");

    private static final Lazy<EnumHelperWithValue<ZipFormat>> ENUM_HELPER = EnumHelperWithValue.newLazyHelperWithValue(ZipFormat.class);

    private final String extension;

    private ZipFormat(String extension) {
        this.extension = extension;
    }

    public static Optional<ZipFormat> fromExtension(String extension) {
        return ENUM_HELPER.get().fromValueTry(extension);
    }

    @Override
    public String getString() {
        return extension;
    }

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
