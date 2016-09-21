package model;


import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;

public class unit_dbmanager {
	
	private File dbfile;
	private DBManager dbmanager;
	
	@Before
	public void setUp(){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		dbfile = new File("data\\list\\database_temp.txt");
		dbmanager = new DBManager();
	}
	
	@Test
	public void testMake(){
		System.out.println("Make DataBase.");
		dbmanager.make();
	}
	
}
