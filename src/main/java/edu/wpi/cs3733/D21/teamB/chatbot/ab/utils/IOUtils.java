package edu.wpi.cs3733.D21.teamB.chatbot.ab.utils;

import sun.misc.Launcher;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;


public class IOUtils {


	BufferedReader reader;
	BufferedWriter writer;

	public IOUtils(String filePath, String mode) {
		try {
			if (mode.equals("read")) {
				reader = new BufferedReader(new FileReader(filePath));
			} else if (mode.equals("write")) {
				(new File(filePath)).delete();
				writer = new BufferedWriter(new FileWriter(filePath, true));
			}
		} catch (IOException e) {
			System.err.println("error: " + e);
		}
	}

	
	public String readLine() {
		String result = null;
		try {
			result = reader.readLine();
		} catch (IOException e) {
			System.err.println("error: " + e);
		}
		return result;
	}


	public void writeLine(String line) {
		try {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			System.err.println("error: " + e);
		}
	}


	public void close() {
		try {
			if (reader != null) reader.close();
			if (writer != null) writer.close();
		} catch (IOException e) {
			System.err.println("error: " + e);
		}

	}


	public static void writeOutputTextLine(String prompt, String text) {
		System.out.println(prompt + ": " + text);
	}


	public static String readInputTextLine() {
		return readInputTextLine(null);
	}


	public static String readInputTextLine(String prompt) {
		if (prompt != null) {
			System.out.print(prompt + ": ");
		}
        BufferedReader lineOfText = new BufferedReader(new InputStreamReader(System.in));
		String textLine = null;
		try {
			textLine = lineOfText.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textLine;
	}


	public static File[] listFiles(String path, File jarFile) {
		List<File> fileList = new ArrayList<>();

		if(jarFile.isFile()) {  // Run with JAR file
			JarFile jar = null;
			try {
				jar = new JarFile(jarFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
			while(entries.hasMoreElements()) {
				final String name = entries.nextElement().getName();
				if (name.startsWith(path + "/")) { //filter according to the path
					InputStream inputStream = Launcher.class.getResourceAsStream(name);
					File f = new File(name);
					try {
						copyInputStreamToFile(inputStream, f);
					} catch (IOException e) {
						e.printStackTrace();
					}
					fileList.add(f);
					System.out.println(name);
				}
			}
			try {
				jar.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // Run with IDE
			final URL url = Launcher.class.getResource("/" + path);
			if (url != null) {
				try {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
						System.out.println(app);
						fileList.add(app);
					}
				} catch (URISyntaxException ex) {
					// never happens
				}
			}
		}

		File[] ret = new File[fileList.size()];
		fileList.toArray(ret);
		return ret;
	}

	private static void copyInputStreamToFile(InputStream inputStream, File file)
			throws IOException {

		// append = false
		try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
			int read;
			byte[] bytes = new byte[10000];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		}

	}


	public static String system(String evaluatedContents, String failedString) {
		Runtime rt = Runtime.getRuntime();
        //System.out.println("System "+evaluatedContents);
        try {
            Process p = rt.exec(evaluatedContents);
            InputStream istrm = p.getInputStream();
            InputStreamReader istrmrdr = new InputStreamReader(istrm);
            BufferedReader buffrdr = new BufferedReader(istrmrdr);
            String result = "";
            String data = "";
            while ((data = buffrdr.readLine()) != null) {
                result += data+"\n";
            }
            //System.out.println("Result = "+result);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return failedString;

        }
	}

	
	public static String evalScript(String engineName, String script) throws Exception {
        //System.out.println("evaluating "+script);
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String result = ""+engine.eval(script);
		return result;
	}

}

