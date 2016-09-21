package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import controller.DBController;

public class dbView extends JFrame{
	
	private JButton makebut;

	private DBController dbcontroller;
	
	public dbView(){
	}
	
	public void setGUI(){
		dbcontroller = new DBController();
		this.setLayout(new GridLayout(0, 1));
		makebut = new JButton("make db");
		makebut.setSize(300, 100);
		add(makebut);
		
		makebut.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dbcontroller.toModel(DBController.MAKE_DB);
			}});
		
		this.setSize(300, 300);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	
}
