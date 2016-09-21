package model;

import java.io.File;
import java.util.Comparator;
import java.util.Vector;

import org.opencv.imgproc.*;
import org.opencv.imgcodecs.*;
import org.opencv.core.*;

/*
 * FileAnalyzer Class
 * DB에서 데이터를 열고, 그 데이터를 분석하여 텍스트로 저장하는 클래스
 * 데이터는 파일 이름과 RGB평균을 분석하고
 * 그 정보들을 ;로 구분하여 txt파일에 저장한다.
 */


public class FileAnalyzer {
	
	public FileAnalyzer(){
		
	}
	
	public Vector<String> process(){
		Vector<String> fileList = new Vector<String>();
		
		
		return fileList;
	}
	public RGB getRGB(Mat input)
	{
		System.out.println("RGB분석");
		//RGB 분석해주세요
		Mat img = input;
		Mat hsv = new Mat();
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		
		int pixels = 0;
		int x = img.height();
		int y = img.width();
		
		int r=0, g=0, b=0, h=0, s=0, v=0;
		for (int i=0;i<x-1;i++){
			for (int j=0;j<y-1;j++){
				double[] BGR = img.get(i, j);
				double[] HSV = hsv.get(i, j);
				if(BGR.length < 3)continue;
				//System.out.println(i + " " + j + " " + BGR[0] + " " + BGR[1] + " " + BGR[2]);
				if(BGR[0] > 250.0 && BGR[1] > 250.0 && BGR[2] > 250.0)continue;
				if(BGR[0] < 5.0 && BGR[1] < 5.0 && BGR[2] < 5.0)continue;
				r += (int)BGR[2];
				g += (int)BGR[0];
				b += (int)BGR[1];
				h += (int)HSV[0];
				s += (int)HSV[1];
				v += (int)HSV[2];
				pixels++;
			}
		}
		r /= pixels;
		g /= pixels;
		b /= pixels;
		h /= pixels;
		s /= pixels;
		v /= pixels;
		
		RGB ret = new RGB();
		ret.setR(r);
		ret.setG(g);
		ret.setB(b);
		ret.setH(h);
		ret.setS(s);
		ret.setV(v);
		return ret;
	
		
		//rgb.set_red(100);
		//rgb.set_green(100);
		
		//rgb.set_blue(100);
	}
	public RGB getRGB(File file)
	{
		//RGB 분석해주세요
		Mat img = new Mat();
		img = Imgcodecs.imread("data\\img\\" + file.getName());
		Mat hsv = new Mat();
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		System.out.println(file.getAbsolutePath());
		int pixels = 0;
		int x = img.height();
		int y = img.width();
		int r=0, g=0, b=0, h=0, s=0, v=0;
		for (int i=0;i<x;i++){
			for (int j=0;j<y;j++){
				double[] BGR = img.get(i, j);
				double[] HSV = hsv.get(i, j);
				if(BGR[0] > 250.0 && BGR[1] > 250.0 && BGR[2] > 250.0)continue;
				if(BGR[0] < 5.0 && BGR[1] < 5.0 && BGR[2] < 5.0)continue;
				r += (int)BGR[2];
				g += (int)BGR[0];
				b += (int)BGR[1];
				h += (int)HSV[0];
				s += (int)HSV[1];
				v += (int)HSV[2];
				pixels++;
			}
		}
		r /= pixels;
		g /= pixels;
		b /= pixels;
		h /= pixels;
		s /= pixels;
		v /= pixels;
		RGB ret = new RGB();
		ret.setR(r);
		ret.setG(g);
		ret.setB(b);
		ret.setH(h);
		ret.setS(s);
		ret.setV(v);
		return ret;
	
		
		//rgb.set_red(100);
		//rgb.set_green(100);
		
		//rgb.set_blue(100);
	}
	
	
	
	//아래는 database에 더 추가되야할 가능성 있는 것들
	
	//public String getType(File file) 
	public static class comparer implements Comparator<KeyPoint>{

		@Override
		public int compare(KeyPoint o1, KeyPoint o2) {
			// TODO Auto-generated method stub
			return o1.response > o2.response ? -1 : (o1.response == o2.response ? 0 : 1); 
		}
		
	}

}
