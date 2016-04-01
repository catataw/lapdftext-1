package edu.isi.bmkeg.lapdf.bin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.isi.bmkeg.lapdf.controller.LapdfEngineEx;
import edu.isi.bmkeg.lapdf.model.LapdfDocument;
import edu.isi.bmkeg.lapdf.utils.Constant;
import edu.isi.bmkeg.lapdf.xml.model.LapdftextXMLDocument;
import edu.isi.bmkeg.utils.xml.XmlBindingTools;

public class BlockifyClassifyWithRulesDemo {

	// Start page of pdf need to be extracted
	public static final int START = 69;
	
	// End page of pdf need to be extracted
	public static final int END = 69;
	
	public static final String ARTICLE_FILE = "sph_article_title_rules.drl";
	
	public static final String HIGHLIGHT_FILE = "sph_article_highlight_rules.drl";
	
	public static final String BODY_FILE = "sph_article_body_rules.drl";
	
	public static final String RULES_FILE = "sph_article_rules.drl";
	
	// Pdf file input
	public static final String INPUT_FILE = "/Users/phat/Desktop/Article-Pdf/1603_AS_HK.pdf";
	
	// Directory output
	public static final String OUTPUT_DIR = "/Users/phat/Desktop/Article-Pdf/result/demo3";
	
	// Drools rules file
	public static final String RULE_FILE_PATH = "/Users/phat/Development/source-code/pdf-parser/src/main/resources/rules/";
	
	public static void main(String[] args) throws Exception {
		
		File inputFileOrDir = new File(INPUT_FILE);
		
		File outDir = new File(OUTPUT_DIR);
		
		// Init engine
		LapdfEngineEx engine = new LapdfEngineEx();
		engine.setStartPage(START);
		engine.setEndPage(END);
		
		String pdfStem = inputFileOrDir.getName();
		pdfStem = pdfStem.replaceAll("\\.pdf", "");
		
		String outPath = outDir + "/" + pdfStem + "_" + START + "-" + END + "_lapdf_" + System.currentTimeMillis() + ".xml";
		
		File outXmlFile = new File(outPath);
		
		LapdfDocument lapdf = engine.blockifyFile(inputFileOrDir);
		
		engine.classifyDocument(lapdf, initRuleFiles());
		
		LapdftextXMLDocument xmlDoc = lapdf.convertToLapdftextXmlFormat(START, END);
		
		XmlBindingTools.saveAsXml(xmlDoc, outXmlFile);
	}

	public static Map<String, File> initRuleFiles() throws IOException {
		
		Map<String, File> ruleFileMap = new HashMap<>();
		
		// Title rule file
		File ruleTitleFile = new File(RULE_FILE_PATH + ARTICLE_FILE);
		
		
		
		// Highlight rule file
		File ruleHighlightFile = new File(RULE_FILE_PATH + HIGHLIGHT_FILE);
		
		// Body rule file
		File ruleBodyFile = new File(RULE_FILE_PATH + HIGHLIGHT_FILE);
		
		ruleFileMap.put(Constant.RuleFileKey.ARTICLE_TITLE_RULE, ruleTitleFile);
		ruleFileMap.put(Constant.RuleFileKey.ARTICLE_HIGHLIGHT_RULE, ruleHighlightFile);
		ruleFileMap.put(Constant.RuleFileKey.ARTICLE_BODY_RULE, ruleBodyFile);
		
		return ruleFileMap;
	}
}
