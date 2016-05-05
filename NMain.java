package NMindMap;
import javax.swing.JFrame;

public class NMain {
	public static void main(String[] args) 
	{
		int frameWidth = 1000;
		int frameHeight = 500;
		NFrame s=new NFrame(frameWidth, frameHeight);
		s.setSize(frameWidth, frameHeight);
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.setVisible(true);
	}
}
