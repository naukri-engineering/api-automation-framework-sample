package utils;

import java.io.*;
import java.util.Properties;

public class ReadWritePropertyFile {
	
	public static String getProperty(String Property, String filePath) {
		try {
			Properties prop = loadProperties(filePath);
			return prop.getProperty(Property);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void setProperty(String key, String value, String filePath) {
		OutputStream out = null;
		try {
			Properties props = loadProperties(filePath);
			props.setProperty(key, value);
			out = new FileOutputStream(filePath);
			props.store(out, "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (out != null) {

				try {

					out.close();
				} catch (IOException ex) {

					System.out.println("IOException: Could not close properties output stream; " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}

	}

	public static Properties loadProperties(String resourceName) throws IOException {
		Properties properties = null;
		InputStream inputStream = null;
		try {
			inputStream = loadResource(resourceName);
			;
			if (inputStream != null) {
				properties = new Properties();
				properties.load(inputStream);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return properties;
	}

	public static InputStream loadResource(String resourceName) throws IOException {
		ClassLoader classLoader = ReadWritePropertyFile.class.getClassLoader();

		InputStream inputStream = null;

		if (classLoader != null) {
			inputStream = classLoader.getResourceAsStream(resourceName);
		}

		if (inputStream == null) {
			classLoader = ClassLoader.getSystemClassLoader();
			if (classLoader != null) {
				inputStream = classLoader.getResourceAsStream(resourceName);
			}
		}

		if (inputStream == null) {
			File file = new File(resourceName);
			if (file.exists()) {
				inputStream = new FileInputStream(file);
			}
		}

		return inputStream;
	}// end loadResource

}
