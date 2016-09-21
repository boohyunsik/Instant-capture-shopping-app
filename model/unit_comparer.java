package model;


import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class unit_comparer {

	Comparer comparer;
	File file;
	String[] fileList;
	@Before
	public void setUp() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		comparer = new Comparer();
		
		File file = new File("C:\\Users\\bhs\\inputdata");
		String[] fileList = file.list();
		int num = fileList.length;
		
		int selected_idx = (int)((Math.random() * 10000) % num);
		Mat img = new Mat();
		img = Imgcodecs.imread("C:\\Users\\bhs\\inputdata\\" + fileList[selected_idx]);
		System.out.println("Random Selected file : " + fileList[selected_idx]);
		try {
			comparer.setImg(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Set Image");
		System.out.println();
	}
	
	@Test
	public void testapproximity(){
		System.out.println("Test Approximity");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
	}
	
	@Test
	public void testmatch(){
		System.out.println("Test feature matching(By filename) function");
		File file = new File("data\\img");
		String[] filelist = file.list();
		int num = filelist.length;
		int r = (int)(Math.random() * 10000) % num;
		comparer.matchFeature("data\\img\\" + filelist[r]);
		System.out.println();
	}
	
	@Test
	public void testgetlist(){
		System.out.println("Test get list function");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<fileInfo> vec = new Vector<fileInfo>();
		vec = comparer.getList(50);
		System.out.println("ÃÑ º¤ÅÍ ¿ø¼Ò ¼ö : " + vec.size());
		System.out.println();
	}
	
	@Test
	public void testsort(){
		System.out.println("Test sorting function");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comparer.sort();
		Vector<fileInfo> vec = comparer.getVector();
		for(int i=0;i<vec.size()*0.2;i++){
			System.out.println(vec.get(i).filename + " " + vec.get(i).Huescore);
		}
		System.out.println();
	}
	
	@Test
	public void testsort_feature(){
		System.out.println("Test sorting function - sort by features.");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<fileInfo> vec = comparer.getVector();
		comparer.sort_feature(vec);
		for(int i=0;i<vec.size()*0.2;i++){
			System.out.println(vec.get(i).filename + " " + vec.get(i).FEATUREscore);
		}
		System.out.println();
	}
	
	@Test
	public void testmatcher(){
		System.out.println("Test matcher function");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<fileInfo> vec = comparer.getVector();
		comparer.matcher(vec);
		for(int i=0;i<10;i++){
			System.out.println(vec.get(i).filename + " " + vec.get(i).FEATUREscore);
		}
		System.out.println();
	}
	
	@Test
	public void testmatcherbyInfo(){
		System.out.println("Test matcher(by fileInfo) function");
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector<fileInfo> vec = comparer.getVector();
		for(int i=0;i<vec.size()*0.2;i++){
			comparer.matcherbyfileInfo(vec.get(i));
		}
		System.out.println();
	}
	
	@Test
	public void testmatchFeature(){
		System.out.println("test matchfeature function");
		File dir = new File("data\\img");
		String[] flist = dir.list();
		int len = flist.length;
		for(int i=0;i<len;i++){
			comparer.matchFeature("data\\img\\" + flist[i]);
		}
		System.out.println();
	}
}
