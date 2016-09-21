package controller;

import model.DBManager;

public class DBController {
	
	public static final int MAKE_DB = 1;
	public static final int INSERT_DB = 2;
	public static final int DELETE_DB = 3;
	
	private DBManager dbmanager;
	
	public DBController(){
		dbmanager = new DBManager();
	}
	
	public boolean toModel(int flag){
		if(flag == MAKE_DB){
			System.out.println("Make DB");
			dbmanager.make();
			
			return true;
		}
		if(flag == INSERT_DB){
			
			
			return true;
		}
		if(flag == DELETE_DB){
			
			
			return true;
		}
		return false;
	}
	
}
