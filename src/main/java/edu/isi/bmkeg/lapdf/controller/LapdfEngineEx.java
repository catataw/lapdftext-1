package edu.isi.bmkeg.lapdf.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.isi.bmkeg.lapdf.classification.ruleBased.RuleBasedChunkClassifier;
import edu.isi.bmkeg.lapdf.extraction.exceptions.ClassificationException;
import edu.isi.bmkeg.lapdf.model.ChunkBlock;
import edu.isi.bmkeg.lapdf.model.LapdfDocument;
import edu.isi.bmkeg.lapdf.model.PageBlock;
import edu.isi.bmkeg.lapdf.model.RTree.RTModelFactory;
import edu.isi.bmkeg.lapdf.model.ordering.SpatialOrdering;

/**
 * <p>The extension class is to set startPage and endPage to classify the pdf file
 * If startPage and endPage are not set, classifyDocument() method will run from 1 to total pages 
 * of document, the behavior will be the same to its parent - LapdfEngine
 * <p>
 * 
 * @author <a href="mailto:phat@manifera.com">Phat Nguyen</a>
 * @see LapdfEngine
 */
public class LapdfEngineEx extends LapdfEngine {

	private Logger LOG = LoggerFactory.getLogger(LapdfEngineEx.class);
	
	private int startPage = 1;
	
	private int endPage = Integer.MAX_VALUE;
	
	public LapdfEngineEx() throws Exception {
		super();
	}
	
	/**
	 * Classifies the chunks in a file based on the rule file
	 * @param document - an instantiated LapdfDocument
	 * @param ruleFile - a rule file on disk
	 * @throws IOException 
	 */
	@Override
	public void classifyDocument(LapdfDocument document,
			File ruleFile) 
					throws ClassificationException, 
					IOException {
		
		RuleBasedChunkClassifier classfier = new RuleBasedChunkClassifier(
				ruleFile.getPath(), new RTModelFactory());
		
		int max = (endPage == Integer.MAX_VALUE ? document.getTotalNumberOfPages() : endPage);
		
		for (int i = startPage; i <= max; i++) {
			
			PageBlock page = document.getPage(i);
			
			List<ChunkBlock> chunkList = page.getAllChunkBlocks(
					SpatialOrdering.MIXED_MODE);

			classfier.classify(chunkList);

		}
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		if(startPage < 1) {
			
			LOG.error("startPage must be >= 1");
			throw new IllegalArgumentException("startPage must be >= 1");
		}
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		if(endPage < 1 || endPage < startPage) {
			
			LOG.error("endPage must be >= 1 and >= {}", startPage);
			throw new IllegalArgumentException("endPage must be >= 1 and >= " + startPage);
		}
		this.endPage = endPage;
	}
	
}
