package edu.isi.bmkeg.lapdf.bin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.uima.resource.ResourceInitializationException;

import edu.isi.bmkeg.lapdf.controller.LapdfEngine;
import edu.isi.bmkeg.lapdf.controller.LapdfMode;
import edu.isi.bmkeg.lapdf.model.LapdfDocument;
import edu.isi.bmkeg.lapdf.text.SpatialLayoutFeaturesReportGenerator;
import edu.isi.bmkeg.lapdf.uima.cpe.CommandLineFitPipeline;
import edu.isi.bmkeg.lapdf.xml.model.LapdftextXMLDocument;
import edu.isi.bmkeg.utils.Converters;
import edu.isi.bmkeg.utils.xml.XmlBindingTools;

public class BlockStatistics {

	private static String USAGE = "usage: <input-dir-or-file> [<output-dir>]\n\n"
			+ "<input-dir-or-file> - the full path to the PDF file or directory to be extracted \n"
			+ "<output-dir> (optional or '-') - the full path to the output directory \n\n"
			+ "Running this command on a PDF file or directory will generate \n"
			+ "statistics about each chunk in each pdf document .\n";

	public static void main(String args[]) throws Exception {

		LapdfEngine engine = new LapdfEngine();

		if (args.length < 1) {
			System.err.println(USAGE);
			System.exit(1);
		}

		String inputFileOrDirPath = args[0];
		String outputDirPath = "";
		String ruleFilePath = "";

		File inputFileOrDir = new File(inputFileOrDirPath);
		if (!inputFileOrDir.exists()) {
			System.err.println(USAGE);
			System.err.println("Input file / dir '" + inputFileOrDirPath
					+ "' does not exist.");
			System.err.println("Please include full path");
			System.exit(1);
		}

		// output folder is set.
		if (args.length > 1) {
			outputDirPath = args[1];
		} else {
			outputDirPath = "-";
		}

		if (outputDirPath.equals("-")) {
			if (inputFileOrDir.isDirectory()) {
				outputDirPath = inputFileOrDirPath;
			} else {
				outputDirPath = inputFileOrDir.getParent();
			}
		}

		File outDir = new File(outputDirPath);
		if (!outDir.exists()) {
			outDir.mkdir();
		}

		if (inputFileOrDir.isDirectory()) {

			Pattern patt = Pattern.compile("\\.pdf$");
			Map<String, File> inputFiles = Converters.recursivelyListFiles(
					inputFileOrDir, patt);
			Iterator<String> it = inputFiles.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				File pdf = inputFiles.get(key);
				String pdfStem = pdf.getName();
				pdfStem = pdfStem.replaceAll("\\.pdf", "");

				String outPath = Converters.mimicDirectoryStructure(
						inputFileOrDir, outDir, pdf).getPath();
				outPath = outPath.replaceAll("\\.pdf", "_spatial.xml");

				File outFile = new File(outPath + "_bStats.txt");

				LapdfDocument lapdf = engine.blockifyFile(pdf);
				engine.writeSpatialFeaturesReport(lapdf, outFile);

			}

		} else {

			String pdfStem = inputFileOrDir.getName();
			pdfStem = pdfStem.replaceAll("\\.pdf", "");

			String outPath = outDir.getPath() + "/" + pdfStem + "_bStats.txt";
			File outFile = new File(outPath);

			LapdfDocument lapdf = engine.blockifyFile(inputFileOrDir);
			engine.writeSpatialFeaturesReport(lapdf, outFile);
		}

	}

}
