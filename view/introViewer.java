package view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;

import org.opencv.core.Mat;

import controller.Controller;
import model.fileInfo;

import java.awt.*;

public class introViewer extends JFrame implements ActionListener, Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2813900788162626126L;
	private final String version = new String("ver 0.2.1");
	
	final static int interval = 100;
	int k =0;
	private Controller controller;
	private JPanel panel;
	private JPanel panel_grid2;
	private JButton button;
	private JButton findbutton;
	private JLabel label1;
	private Label label2;
	private JFileChooser fileChooser = new JFileChooser();
	private JProgressBar pb;
	private int percent = 0;
	private MouseHandler mh;
	private ImageIcon icon;
	
	private JTextField tf;
	
	private JButton resizebut;
	private JButton cancelbut;
	
	private Mat image;
	
	
	public introViewer(){
		controller = new Controller();
		controller.load();	
		mh = new MouseHandler();
		image = new Mat();
		
		tf = new JTextField(version);
		tf.setEnabled(false);
		tf.setBounds(900, 580, 60, 20);
		add(tf);
		setLayout(null);	//layout null
		createPanel1();
		createLabel2();
		createLabel1();
		
		setTitle("Searching Program");
		setSize(960,640);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);	
		new Thread(this).start();
	}
	
	public void createLabel1() {
		label1 = new JLabel(new ImageIcon("image\\back.jpg"));
		label1.setBounds(0, 0, 960, 640);
		add(label1);
	}
	
	public void createLabel2(){
		label2 = new Label(new ImageIcon("image\\pic.jpg"));
		label2.setLayout(null);
		label2.setBounds(323,-100, 300, 700);
		
		resizebut = new JButton("resize");
		resizebut.setBounds(650, 350, 80, 40);
		resizebut.addActionListener(this);
		
		cancelbut = new JButton("cancel");
		cancelbut.setBounds(650,300, 80, 40);
		cancelbut.addActionListener(this);
		
		add(label2);
		add(resizebut);
		add(cancelbut);
	}

	public void createPanel1() {
		panel = new JPanel(new GridLayout(2, 1));
		panel.setSize(160, 100);
		panel.setBounds(310, 450, 320, 80);
		panel.setBackground(Color.white);
		
		panel_grid2 = new JPanel(new GridLayout(0, 2));
		
		button = new JButton(new ImageIcon("image\\search.jpg"));
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setSize(160, 50);
		button.addActionListener(this);
		
		
		findbutton = new JButton(new ImageIcon("image\\find.jpg"));
		findbutton.setOpaque(false);
		findbutton.setContentAreaFilled(false);
		findbutton.setBorderPainted(false);
		findbutton.setSize(160, 50);
		findbutton.addActionListener(this);
		
		
		pb = new JProgressBar();
		
		pb.setSize(160, 30);
		pb.setMaximum(99);
		pb.setMinimum(0);
		pb.setStringPainted(true);
		
		panel_grid2.add(button);
		panel_grid2.add(findbutton);
		panel.add(panel_grid2);
		panel.add(pb);
		
		this.add(panel);
	}
	
	public int getPercent(){
		return this.percent;
	}
	
	public void run(){
		while(true){
			try{synchronized(this){wait();}}
			catch(InterruptedException e){}
			controller.setApproximity();
			Vector<fileInfo> filelist = new Vector<fileInfo>();
			Vector<fileInfo> filelist2 = new Vector<fileInfo>();
			try {
				filelist2.clear();
				filelist.clear();
				filelist = controller.getColorSortedVector(70);
				
				int total = filelist.size();
				for(int i=0;i<total;i++){
					filelist2.add(controller.toModelbyIndex(70, filelist.get(i)));
					percent = (int)Math.abs(((double)i/(double)total)*100);	
					pb.setValue(percent);
				}
				filelist2 = controller.getFeatureSortedVector(filelist2);
				searchViewer sv = new searchViewer(filelist2, icon.getImage(), controller);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == findbutton){
			controller.clear();
			Image selected_img = icon.getImage();
			controller.setImage(selected_img);
			synchronized(this){notifyAll();}
		}
		else if(e.getSource() == button){
			controller.clear();
			if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				icon = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath());
				Image im = icon.getImage();
				Image newim = im.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
				icon = new ImageIcon(newim);
				label2.setIcon(icon);
				mh.setLabel(label2);
				label2.addMouseListener(mh);
				label2.addMouseMotionListener(mh);
			}
		}
		else if(e.getSource() == resizebut){
			if(label2.getFlag() == true){
				Image img = icon.getImage();
				BufferedImage original = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics bg = original.getGraphics();
			    bg.drawImage(img, 0, 0, null);
			    bg.dispose();
				int x = label2.getStartX();
				int y = label2.getStartY() - 147;
				int w = label2.getFinishX() - x;
				int h = label2.getFinishY() - 147 - y;
				BufferedImage subImage = original.getSubimage(x, y, w, h);
				
				img = (Image) subImage;
				Image newim = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
				icon = new ImageIcon(newim);
				label2.setIcon(icon);
				label2.rectangle(0,0,0,0);
			}
		}
		else if(e.getSource() == cancelbut){
			label2.rectangle(0, 0, 0, 0);
			label2.updateUI();
		}
	}
	
	
	class Label extends JLabel{
		private int x, y, nx, ny;
		private int startX, startY;
		private int finishX, finishY;
		private boolean flag=false;
		Label(ImageIcon ii){
			super(ii);
		}
		
		public int getStartX(){return startX;}
		public int getStartY(){return startY;}
		public int getFinishX(){return finishX;}
		public int getFinishY(){return finishY;}
		public boolean getFlag(){return flag;}
		
		public void rectangle(int _x, int _y, int _nx, int _ny){
			x = _x;
			y = _y;
			nx = _nx;
			ny = _ny;
			if(x==0 && y==0 && nx==0 && ny==0)flag = false;
			else flag = true;
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setColor(new Color(255,0,0));
			if(x<=nx && y<=ny){
				g.drawRect(x, y, nx-x, ny-y);
				startX = x;
				startY = y;
				finishX = nx;
				finishY = ny;
			}
			if(x<=nx && y>ny){
				g.drawRect(x, ny, nx-x, y-ny);
				startX = x;
				startY = ny;
				finishX = nx;
				finishY = y;
			}
			if(x>nx && y<=ny){
				g.drawRect(nx, y, x-nx, ny-y);
				startX = nx;
				startY = y;
				finishX = x;
				finishY = ny;
			}
			if(x>nx && y>ny){
				g.drawRect(nx, ny, nx-x, ny-y);
				startX = nx;
				startY = ny;
				finishX = x;
				finishY = y;
			}
		}
	}
	
	class MouseHandler implements MouseListener, MouseMotionListener{
		
		Label label;
		int startX, startY;
		int finishX, finishY;
		
		public void setLabel(Label l){
			label = l;
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY();
			startX = x;
			startY = y;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY();
			finishX = x;
			finishY = y;
			label2.rectangle(startX, startY, finishX, finishY);
			label2.updateUI();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
			int curX = e.getX();
			int curY = e.getY();
			label2.rectangle(startX, startY, curX, curY);
			label2.updateUI();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}	
	}
}
