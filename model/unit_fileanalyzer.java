package model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Core;

public class unit_fileanalyzer {
	
	private FileAnalyzer fileanalyzer;
	
	@Test
	public void setUp(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		fileanalyzer = new FileAnalyzer();
	}
	
	@Test
	public void testgetRGBbyMat() {
		System.out.println("Test getRGB by Mat");
		
	}

}
