package model;
import java.io.File;
import java.io.FileReader;

/*
 * FileLoader Ŭ����
 * DB ���� ������� ����ϴ� Ŭ����
 */

public class FileLoader {
	
	private String dir_name = new String();
	private File dir; //������ picture
	private String[] filenames;
	private File text;
	public FileLoader(){
		text = new File("data\\list\\database.txt");
	}
	public FileLoader(String _dir_name) {
		dir_name = _dir_name;
		dir = new File(_dir_name);
		filenames = dir.list();
		text = new File("data\\txt");
	}
	public File getText(){
		return text;
	}
	public int getFileNumber(){
		return 	filenames.length;
	}
	public String getFileName(int i){
		return filenames[i];
	}
	public void insertFile(String name)
	{
		filenames[filenames.length+1]=name;
	}
	public File getFile(int i)
	{
		String fileName=filenames[i];
		File file = new File(fileName);
		return file;
	}
}

