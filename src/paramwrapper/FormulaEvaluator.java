package paramwrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormulaEvaluator {

	private static final Logger LOGGER = Logger.getLogger(FormulaEvaluator.class.getName());
	private String modelString;
	private String property;
	private ParamModel model;
	private String formula;
	private boolean usePrism;
	private String paramPath;
	private long elapsedTime;

	public FormulaEvaluator(String modelString, String property, ParamModel model, boolean usePrism, String paramPath) {
		super();
		this.paramPath = paramPath;
		this.usePrism = usePrism;
		this.modelString = modelString;
		this.property = property;
		this.model = model;
	}

	public String evaluate() {

		try {
			LOGGER.finer(this.modelString);
			File modelFile = File.createTempFile("model", "param");
			FileWriter modelWriter = new FileWriter(modelFile);
			modelWriter.write(modelString);
			modelWriter.flush();
			modelWriter.close();

			File propertyFile = File.createTempFile("property", "prop");
			FileWriter propertyWriter = new FileWriter(propertyFile);
			propertyWriter.write(property);
			propertyWriter.flush();
			propertyWriter.close();

			File resultsFile = File.createTempFile("result", null);

			long startTime = System.nanoTime();
			if (this.usePrism && !modelString.contains("const")) {
				formula = invokeModelChecker(modelFile.getAbsolutePath(), propertyFile.getAbsolutePath(),
						resultsFile.getAbsolutePath());
			} else if (this.usePrism) {
				formula = invokeParametricPRISM(model, modelFile.getAbsolutePath(), propertyFile.getAbsolutePath(),
						resultsFile.getAbsolutePath());
			} else {
				formula = invokeParametricModelChecker(modelFile.getAbsolutePath(), propertyFile.getAbsolutePath(),
						resultsFile.getAbsolutePath());
			}
			this.elapsedTime = System.nanoTime() - startTime;
			return formula.trim().replaceAll("\\s+", "");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		return "";

	}
	
	private String invokeParametricModelChecker(String modelPath, String propertyPath, String resultsPath)
			throws IOException {
		String commandLine = paramPath + " " + modelPath + " " + propertyPath + " " + "--result-file " + resultsPath;
		return invokeAndGetResult(commandLine, resultsPath + ".out");
	}

	private String invokeParametricPRISM(ParamModel model, String modelPath, String propertyPath, String resultsPath)
			throws IOException {
		String commandLine = paramPath + " " + modelPath + " " + propertyPath + " " + "-exportresults " + resultsPath
				+ " " + "-param " + String.join(",", model.getParameters());
		String rawResult = invokeAndGetResult(commandLine, resultsPath);
		int openBracket = rawResult.indexOf("{");
		int closeBracket = rawResult.indexOf("}");
		String expression = rawResult.substring(openBracket + 1, closeBracket);
		return expression.trim().replace('|', '/');
	}

	private String invokeModelChecker(String modelPath, String propertyPath, String resultsPath) throws IOException {
		String commandLine = paramPath + " " + modelPath + " " + propertyPath + " " + "-exportresults " + resultsPath;
		return invokeAndGetResult(commandLine, resultsPath);
	}

	private String invokeAndGetResult(String commandLine, String resultsPath) throws IOException {
		LOGGER.fine(commandLine);
		Process program = Runtime.getRuntime().exec(commandLine);
		int exitCode = 0;
		try {
			exitCode = program.waitFor();
		} catch (InterruptedException e) {
			LOGGER.severe("Exit code: " + exitCode);
			LOGGER.log(Level.SEVERE, e.toString(), e);
		}
		List<String> lines = Files.readAllLines(Paths.get(resultsPath), Charset.forName("UTF-8"));
		lines.removeIf(String::isEmpty);
		// Formula
		return lines.get(lines.size() - 1);
	}
	
	public long getElapsedTime(){
		return this.elapsedTime;
	}


}
