package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.Controller;
import model.fileInfo;

import java.awt.*;

class searchViewer extends JFrame implements ActionListener, Runnable{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JProgressBar pb;
	private Vector<fileInfo> vec;
	private String inputimagepath;
	private Image inputimage;
	private Controller controller;
	private JButton[] img;
	private int percent = 0;
	private int clicked = 0;
	
	private JButton research;
	private JButton back;
		
	public searchViewer(Vector<fileInfo> v, Image img, Controller cont) throws IOException{
		Container con = getContentPane();
		controller = cont;
		inputimage = img;
		vec = v;
		
		System.out.println("File List######");
		for(int i=0;i<vec.size();i++)System.out.println(vec.get(i).filename);
		con.setLayout(new BorderLayout());
		//System.out.println(inputimagepath);
		cont.setImage(img);
		
		
		createPanel1();
		createPanel2();
		createPanel3();
		createProgressBar();
		setTitle("Searching Program");
		setSize(960,780);
		setResizable(false);
		setVisible(true);
		new Thread(this).start();
	}
	
	public void createProgressBar(){
		pb = new JProgressBar();
		pb.setMinimum(0);
		pb.setMaximum(99);
		pb.setValue(0);
		pb.setStringPainted(true);
		panel3.add(pb, BorderLayout.CENTER);
	}
	

	public void createPanel1() {
		panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());
		panel1.setPreferredSize(new Dimension(200,640));
		panel1.setBackground(Color.white);

		ImageIcon icon = new ImageIcon(inputimage);
		Image im = icon.getImage();
		Image newim = im.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		icon = new ImageIcon(newim);
		JButton img = new JButton(icon);
		panel1.add(img, BorderLayout.CENTER);
		
		add(panel1, BorderLayout.WEST);
	}
	

	public void createPanel2() throws IOException {
		panel2 = new JPanel();
		panel2.setLayout(new GridLayout(4,3));
		img = new JButton[12];
		/*for(int i=0;i<vec.size();i++){
			System.out.println(vec.get(i).filename + " " + vec.get(i).FEATUREscore + " " + vec.get(i).Huescore + " " + vec.get(i).RGBscore);
		}*/
		setIcon();
		for(int i=0;i<12;i++){
			panel2.add(img[i]);
		}
		add(panel2, BorderLayout.CENTER);
		panel2.updateUI();
	}
	
	public void setIcon(){
		if(clicked > 0){
			panel2.removeAll();
			panel2 = new JPanel();
			panel2.setLayout(new GridLayout(4,3));
			img = new JButton[12];
		}
		for(int i=0;i<12;i++){
			ImageIcon icon = new ImageIcon("data\\img\\" + vec.get(i).filename);
			String[] brand_name = vec.get(i).filename.split("_");
			Image im = icon.getImage();
			Image newim = im.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
			icon = new ImageIcon(newim);
			img[i] = new JButton(icon);
			img[i].addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JOptionPane.showMessageDialog(null, brand_name[0]+" 제품입니다.");
				}
				
			});
			if(clicked>0){
				panel2.add(img[i]);
				this.add(panel2, BorderLayout.CENTER);
			}
			img[i].updateUI();
		}
		panel2.updateUI();
	}
	
	public void createPanel3(){
		panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());
		research = new JButton("재검색");
		back = new JButton("돌아가기");
		back.addActionListener(this);
		
		research.addActionListener(this);
		panel3.add(research, BorderLayout.WEST);
		panel3.add(back, BorderLayout.EAST);
		add(panel3, BorderLayout.SOUTH);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{synchronized(this){wait();}}
			catch(InterruptedException e){}
			try {
				controller.clear();
				controller.setImage(inputimagepath);
				vec = new Vector<fileInfo>();
				vec = controller.getColorSortedVector(0);
				Vector<fileInfo> filelist = new Vector<fileInfo>();
				
				int total = vec.size();
				for(int i=0;i<total;i++){
					filelist.add(controller.toModelbyIndex(0, vec.get(i)));
					percent = (int)Math.abs(((double)i/(double)total)*100);
					pb.setValue(percent);
				}
				vec.clear();
				vec = controller.getFeatureSortedVector(filelist);
				setIcon();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == research){
			System.out.println("research");
			clicked++;
			synchronized(this){notifyAll();}
		}
		if(e.getSource() == back){
			
		}
	}
}