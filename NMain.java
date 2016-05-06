package NMindMap;

import javax.swing.JFrame;

public class NMain {
	static int frameWidth = 1000;
	static int frameHeight = 500;
	static NFrame mainFrame = new NFrame(frameWidth, frameHeight);

	public static void main(String[] args) {
		NCommend C = new NCommend();
		mainFrame.setMain(C);
		mainFrame.setSize(frameWidth, frameHeight);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);

		// 아래부분에 네트워크 통신 부분 필요
		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		int x = 100;
		while (true) {
			end = System.currentTimeMillis();
			if (end - start > 5000) {
				C.commendAddNode(mainFrame, "test", x, x, 120, 25);
				System.out.println("hello");
				start = end;
				x = x + 20;
			}

		}
	}

}