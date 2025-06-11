/**
 * Copyright 2015 SPeCS.
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

package tdrc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Utility class for serialization operations in tdrcLibrary.
 */
public class SerializeUtils {
	/**
	 * Exports a serializable object to a given output stream.
	 * 
	 * @param obj the object to be serialized
	 * @param outStream the output stream where the object will be written
	 * @throws RuntimeException if a problem occurs during serialization
	 */
	public static <T extends Serializable> void toStream(T obj, OutputStream outStream) {

		// Write object with ObjectOutputStream
		try (ObjectOutputStream obj_out = new ObjectOutputStream(outStream)) {
			// Write object out to disk
			obj_out.writeObject(obj);
		} catch (final IOException e) {
			throw new RuntimeException("Problem during serialization.", e);
		}
	}

	/**
	 * Imports a serializable object from a given input stream.
	 * 
	 * @param inputStream the input stream from which the object will be read
	 * @param targetClass the class type of the object to be deserialized
	 * @return the deserialized object
	 * @throws RuntimeException if a problem occurs during deserialization
	 */
	public static <T extends Serializable> T fromStream(InputStream inputStream, Class<T> targetClass) {

		// Read object with ObjectInputStream
		try (ObjectInputStream obj_in = new ObjectInputStream(inputStream)) {
			// Read object from stream
			return targetClass.cast(obj_in.readObject());
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Problem during deserialization.", e);
		}
	}
}
