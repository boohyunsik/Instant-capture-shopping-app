package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DBManager {
	
	FileLoader loader;
	FileAnalyzer analyzer;
	
	
	public void make()
	{
		loader=new FileLoader("data\\img");
		analyzer=new FileAnalyzer();
		 for(int i=0;i<loader.getFileNumber();i++){
			 
			System.out.println(loader.getFile(i));
			RGB rgb=analyzer.getRGB(loader.getFile(i));
			 try {
				 BufferedWriter out= new BufferedWriter(new FileWriter("data\\list\\database.txt",true));
				 out.write(loader.getFileName(i)+";"+String.valueOf(rgb.getR())+";"
						 +String.valueOf(rgb.getG())+";"+String.valueOf(rgb.getB())+";"
						 +String.valueOf(rgb.getH())+";"+String.valueOf(rgb.getS())+";"
						 +String.valueOf(rgb.getV()));
				 out.newLine();
				 out.flush();
				 out.close();
			 	 } catch (IOException e) {
			 		 System.err.println(e); // 에러가 있다면 메시지 출력
			 		 System.exit(1);
			 	   }
		 }	
	}
}
