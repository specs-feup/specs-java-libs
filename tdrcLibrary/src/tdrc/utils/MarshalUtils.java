/**
 * Copyright 2013 SPeCS Research Group.
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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class MarshalUtils {

	public static <T> T unmarshal(File fileSource, String sourceName, InputStream schemaFile, Class<T> rootType,
			String packageName, boolean validate) throws JAXBException, SAXException {
		return unmarshal(new StreamSource(fileSource), sourceName, schemaFile, rootType, packageName, validate);
	}

	public static <T> T unmarshal(Source source, String sourceName, InputStream schemaFile, Class<T> rootType,
			String packageName, boolean validate) throws JAXBException, SAXException {

		final SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);// W3C_XML_SCHEMA_NS_URI);

		final JAXBContext jc = JAXBContext.newInstance(packageName);
		final Unmarshaller u = jc.createUnmarshaller();
		if (validate && schemaFile != null) {
			final Source schemaSource = new StreamSource(schemaFile);
			final Schema schema = sf.newSchema(schemaSource);
			u.setSchema(schema);
		}
		final ValidationEventCollector vec = new ValidationEventCollector();
		u.setEventHandler(vec);

		JAXBElement<T> jaxbEl = null;

		jaxbEl = u.unmarshal(source, rootType);

		return jaxbEl.getValue();
	}

	public static <T> void marshal(T value, Class<T> elementClass, String packageName, QName q_name,
			OutputStream oStream) throws JAXBException {

		final JAXBElement<T> jaxbEl = createRootElement(value, q_name, elementClass);
		final JAXBContext jc = JAXBContext.newInstance(packageName);
		final Marshaller m = jc.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

		m.marshal(jaxbEl, oStream);

	}

	private static <T> JAXBElement<T> createRootElement(T value, QName q_name, Class<T> elementClass) {
		return new JAXBElement<>(q_name, elementClass, null, value);
	}
}
