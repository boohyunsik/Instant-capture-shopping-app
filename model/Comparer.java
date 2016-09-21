package model;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import org.opencv.core.CvException;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/*
 * Comparer 클래스
 * 어플리케이션 동작에 직접적으로 연관되어있는 클래스
 * 데이터 비교는 1. RGB 비교
 * 2. Feature 비교
 * 두가지로 이루어져있다.
 */

public class Comparer {
	
	private Mat inputImg;
	private FileLoader loader;
	private FileAnalyzer analyzer;
	private int flag;

	private Vector<fileInfo> info;
	
	public Comparer(){
		flag = 0;
		analyzer = new FileAnalyzer();
		loader = new FileLoader();
		info = new Vector<fileInfo>();
	}
	
	public void clear(){
		flag = 0;
		info.clear();
	}
	
	public void setImg(Mat img) throws IOException{
		inputImg = img;
	}
	
	// DB에 저장된 텍스트파일을 열고, 그 안에 저장된 RGB평균을 비교하여
	// 상위 x%를 추려내, 총 비교해야하는 파일 수를 줄인다.
	public void getApproximity() throws IOException{
		RGB rgb = new RGB();
		rgb = analyzer.getRGB(inputImg);
		File text = loader.getText();
		try {
			FileReader reader = new FileReader(text);
			@SuppressWarnings("resource")
			BufferedReader bufreader = new BufferedReader(reader);
			String line;
			while((line = bufreader.readLine()) != null){
				//System.out.println(line);
				String[] token = line.split(";");
				//이름;R;G;B;nFeature
				//메모장 -> 프로그램
				fileInfo newfile = new fileInfo();
				newfile.filename= token[0];
				newfile.r = Integer.parseInt(token[1]);
				newfile.g = Integer.parseInt(token[2]);
				newfile.b = Integer.parseInt(token[3]);
				newfile.h = Integer.parseInt(token[4]);
				newfile.s = Integer.parseInt(token[5]);
				newfile.v = Integer.parseInt(token[6]);
				//newfile.nfeature = Integer.parseInt(token[7]);
				info.add(newfile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 벡터 점수(scoring)
		int inputR, inputG, inputB;
		int inputH;
		inputR = rgb.getR();
		inputG = rgb.getG();
		inputB = rgb.getB();
		inputH = rgb.getH();
		
		int len = (int) (info.size());

		for(int i=0;i<len;i++){
			double Rratio, Gratio, Bratio;
			double larger = (double)(inputR > info.get(i).r ? inputR : info.get(i).r);
			double smaller = (double)(inputR <= info.get(i).r ? inputR : info.get(i).r);
			Rratio = (smaller / larger) * 100;
			
			larger = (double)(inputG > info.get(i).g ? inputG : info.get(i).g);
			smaller = (double)(inputG <= info.get(i).g ? inputG : info.get(i).g);
			Gratio = (smaller / larger) * 100;
			
			larger = (double)(inputB > info.get(i).b ? inputB : info.get(i).b);
			smaller = (double)(inputB <= info.get(i).b ? inputB : info.get(i).b);
			Bratio = (smaller / larger) * 100;
			
			double Hratio;
			larger = (double)(inputH > info.get(i).h ? inputH : info.get(i).h);
			smaller = (double)(inputH <= info.get(i).h ? inputH : info.get(i).h);
			Hratio = (smaller / larger) * 100;
			info.get(i).Huescore = Hratio;
			info.get(i).RGBscore = (Rratio + Gratio + Bratio) / 3;
		}
		flag=1;
		sort();
	}
	
	public void sort(){
		if(flag==1)Collections.sort(info, new compare_Hue());
	}
	
	public Vector<fileInfo> sort_feature(Vector<fileInfo> vec){
		Collections.sort(vec, new compare_featurescore());
		return vec;
	}
	
	public Vector<fileInfo> getList(int threshold){
		if(flag==1){
			Vector<fileInfo> retVec = new Vector<fileInfo>();
			int len = (int) (info.size());
			for(int i=0;i<len;i++){
				if(info.get(i).Huescore >= (double)threshold || info.get(i).RGBscore >= (double)threshold){
					retVec.add(info.get(i));
				}
			}
			return retVec;
			
			
		}
		return null;
	}
	
	public Vector<fileInfo> matcher(Vector<fileInfo> vec){
		
		Vector<fileInfo> retVec = new Vector<fileInfo>();
		for(int i=0;i<vec.size();i++){
			int featurescore = matchFeature("data\\img\\" + vec.get(i).filename);
			if(featurescore > 0){
				vec.get(i).FEATUREscore = featurescore;
				retVec.add(vec.get(i));
			}	
		}
		Collections.sort(retVec, new compare_featurescore());
		return retVec;
	}
	
	public fileInfo matcherbyfileInfo(fileInfo info){
		
		//Vector<fileInfo> retVec = new Vector<fileInfo>();
		int featurescore = matchFeature("data\\img\\" + info.filename);
		if(featurescore > 0){
			info.FEATUREscore = featurescore;
		}
		return info;
	}
	
	public Vector<fileInfo> getVector(){
		return this.info;
	}
	
	
	// OpenCV에서 제공하는 featuredetector 클래스를 이용하여 옷과 옷의 특징을 비교한다.
	public int matchFeature(String fname){
		Mat original_gray = new Mat();
		Imgproc.cvtColor(inputImg, original_gray, Imgproc.COLOR_BGR2GRAY);
		Mat compareImg = Imgcodecs.imread(fname);
		Mat compare_gray = new Mat();
		Imgproc.cvtColor(compareImg, compare_gray, Imgproc.COLOR_BGR2GRAY);
		
		MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
		MatOfKeyPoint keypoints2 = new MatOfKeyPoint();
		Mat descriptors1 = new Mat();
		Mat descriptors2 = new Mat();
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.PYRAMID_ORB);
		
		DescriptorExtractor extractor =  DescriptorExtractor.create(DescriptorExtractor.ORB);
		detector.detect(original_gray, keypoints1);
		detector.detect(compare_gray, keypoints2);
		
		extractor.compute(original_gray, keypoints1, descriptors1);
		extractor.compute(compare_gray, keypoints2, descriptors2);
		
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMINGLUT);
		MatOfDMatch matches;
		try{
			matches = new MatOfDMatch();
			matcher.match(descriptors1, descriptors2, matches);
		}catch(CvException e){
			return 0;
		}
		
		List<DMatch> good = new ArrayList<DMatch>();
			
		if(descriptors2.cols() == descriptors1.cols()){
			matcher.match(descriptors1, descriptors2, matches);
			
			List<DMatch> match = matches.toList();
			Collections.sort(match, new comparer());

			for(int i=0;i<descriptors1.rows();i++){
				if(match.get(i).distance<=40)good.add(match.get(i));
			}
		}
		MatOfDMatch good_matches = new MatOfDMatch();
		good_matches.fromList(good);

		return good.size();

	}
	
	public static class comparer implements Comparator<DMatch>{

		@Override
		public int compare(DMatch o1, DMatch o2) {
			// TODO Auto-generated method stub
			return o1.distance < o2.distance ? -1 : (o1.distance == o2.distance ? 0 : 1); 
		}
		
	}
	
	public static class comparer2 implements Comparator<KeyPoint>{

		@Override
		public int compare(KeyPoint o1, KeyPoint o2) {
			// TODO Auto-generated method stub
			return o1.response > o2.response ? -1 : (o1.response == o2.response ? 0 : 1); 
		}
		
	}

	
	public static class compare_rgbscore implements Comparator<fileInfo>{

		@Override
		public int compare(fileInfo arg0, fileInfo arg1) {
			// TODO Auto-generated method stub
			return arg0.RGBscore < arg1.RGBscore ? -1 : (arg0.RGBscore == arg1.RGBscore ? 0 : 1);
		}
		
	}
	
	public static class compare_Hue implements Comparator<fileInfo>{

		@Override
		public int compare(fileInfo arg0, fileInfo arg1) {
			// TODO Auto-generated method stub
			return arg0.Huescore < arg1.Huescore ? -1 : (arg0.Huescore == arg1.Huescore ? 0 : 1);
		}
		
	}
	
	public static class compare_featurescore implements Comparator<fileInfo>{

		@Override
		public int compare(fileInfo arg0, fileInfo arg1) {
			// TODO Auto-generated method stub
			return arg0.FEATUREscore > arg1.FEATUREscore ? -1 : (arg0.FEATUREscore == arg1.FEATUREscore ? 0 : 1);
		}
		
	}
}
