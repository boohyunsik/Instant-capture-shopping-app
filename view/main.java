package view;


import java.util.Scanner;

import org.opencv.core.Core;

import controller.Controller;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//System.loadLibrary("opencv_java310");
		System.loadLibrary("opencv_java310");
		introViewer view;
		dbView dbview;
		view = new introViewer();
		dbview = new dbView();
		//dbview.setGUI();
		/*
		System.out.println("flag�Է� (0:���α׷� ����, 1:db�Ŵ���)");
		Scanner scan = new Scanner(System.in);
		flag = scan.nextInt();
		if(flag==0)view = new introViewer();
		dbview = new dbView();
		*/
	}

}
