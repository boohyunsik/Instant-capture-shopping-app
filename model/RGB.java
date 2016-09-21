package model;


public class RGB {
	private int R;
	private int G;
	private int B;
	
	private int H, S, V;
	
	public RGB()
	{
		R=0;
		G=0;
		B=0;
	}

	public int getR() {
		return R;
	}

	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public int getS() {
		return S;
	}

	public void setS(int s) {
		S = s;
	}

	public int getV() {
		return V;
	}

	public void setV(int v) {
		V = v;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}
	
}
