package controller;

import view.dbView;
import view.introViewer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import model.*;

public class Controller {
	private DBManager dbmanager;
	private dbView dbview;
	private FileAnalyzer fileanalyzer;
	private FileLoader fileloader;
	private Comparer comparer;
	private Mat Img;
	private static int flag = 0;
	private Vector<fileInfo> result_list;
	
	private int total_data;
	private int current_data=0;
	
	public int getTotalData(){return total_data;}
	public int getCurrentData(){return current_data;}
	
	public void load(){
		// ��ü �Ҵ�
		Img = new Mat();
		dbmanager = new DBManager();
		dbview = new dbView();
		fileanalyzer = new FileAnalyzer();
		comparer = new Comparer();
		result_list = new Vector<fileInfo>();
	}
	
	// setImage�� 2���� �޼ҵ�� �����ε�
	// 1. �̹�����ü(Mat)�� ���� ���޹޴� ���
	// 2. �̹��� ���� ���(String)���� ���޹޴� ���
	public void setImage(Mat inputImg){
		// ���� �̹��� ����(View -> Controller)
		Img = inputImg;
		try {
			comparer.setImg(Img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// Comparer�� �̹��� ����
		flag = 1;
	}
	
	public void setImage(Image inputImg){
		BufferedImage buf = new BufferedImage(inputImg.getWidth(null), inputImg.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		Graphics bg = buf.getGraphics();
	    bg.drawImage(inputImg, 0, 0, null);
	    bg.dispose();
		byte[] data = ((DataBufferByte) buf.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(buf.getHeight(), buf.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		try {
			comparer.setImg(mat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setImage(String dir){

		Img = Imgcodecs.imread(dir);
		try {
			comparer.setImg(Img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// Comparer�� �̹��� ����
		flag=1;
	}
	
	public void setApproximity(){
		try {
			comparer.getApproximity();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	// ��Ȯ�� �񱳸޼ҵ� ȣ��
	}
	
	// ��Ʈ�ѷ��� �Ҵ�� �̹��� ��ü ����
	public void clear(){
		Img = new Mat();
		flag = 0;
	}

	// �̹����� �𵨷� �������ִ� �޼ҵ�
	// ���ø����̼� ��ɰ� ���õ� ���� Comparer
	// �� Comparer�� �������ִ� �κ�
	public Vector<fileInfo> toModel(int threshold) throws IOException{
		current_data = 0;
		total_data = 0;
		if(flag==1){
			result_list = comparer.getList(threshold);
			result_list = comparer.matcher(result_list);
			
			Collections.sort(result_list, new compare_featurescore());
			flag=0;
			comparer.clear();
			return result_list;
		}
		else{
			System.out.println("There is no input image.");
			comparer.clear();
			return null;
		}
	}
	
	public Vector<fileInfo> getColorSortedVector(int threshold){
		Vector<fileInfo> retVec = new Vector<fileInfo>();
		retVec = comparer.getList(threshold);
		comparer.clear();
		return retVec;
	}
	
	public Vector<fileInfo> getFeatureSortedVector(Vector<fileInfo> vec){
		Vector<fileInfo> retVec = new Vector<fileInfo>();
		retVec = comparer.sort_feature(vec);
		comparer.clear();
		return retVec;
	}
	
	
	public fileInfo toModelbyIndex(int threshold, fileInfo info) throws IOException{
			
		result_list = comparer.getList(threshold);
		fileInfo ret_info = info;
		ret_info = comparer.matcherbyfileInfo(info);
		comparer.clear();
		return ret_info;
		
	}
	
	public static class compare_featurescore implements Comparator<fileInfo>{

		@Override
		public int compare(fileInfo arg0, fileInfo arg1) {
			// TODO Auto-generated method stub
			return arg0.FEATUREscore > arg1.FEATUREscore ? -1 : (arg0.FEATUREscore == arg1.FEATUREscore ? 0 : 1);
		}
		
	}
}
