package testframe;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;


public class frameTest extends JFrame{
	int eventnum = 0;

	public frameTest() {
		setTitle("NMindMap");
		
		setContentPane(new MyPanel());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnMenu.add(mntmClose);
		getContentPane().setLayout(null);
		
		JButton btnCN = new JButton("Create Node");
		btnCN.setBounds(0, 0, 140, 20);
		getContentPane().add(btnCN);
		btnCN.addActionListener(new MyActionListener());
		
		JButton btnCA = new JButton("Create Arrow");
		btnCA.setBounds(0, 20, 140, 20);
		getContentPane().add(btnCA);
		btnCA.addActionListener(new MyActionListener());
	}
	
    private class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("Create Node"))
                eventnum=1;
            else if (b.getText().equals("Create Arrow"))
                eventnum=2;
        }
    }
	
	class MyPanel extends JPanel{
		int command = 0;
		Point startP = null;
		Point endP = null;
		
		public MyPanel(){
			this.addMouseListener(new MyMouseListener());
		}
		
		class MyMouseListener extends MouseAdapter{
			public void mousePressed(MouseEvent e){
				startP = e.getPoint();
				
				if(eventnum==1){
					JTextPane txtpnAaa = new JTextPane();
					txtpnAaa.setText("aaa");
					txtpnAaa.setEditable(false);
					txtpnAaa.setBounds(startP.x, startP.y, 40, 30);
					getContentPane().add(txtpnAaa);	
					eventnum=0;
				}
				else if(eventnum==2){
				Graphics g = getGraphics();
				g.drawLine(startP.x, startP.y, 140, 0);
				eventnum=0;
				}
			}
			public void mouseReleased(MouseEvent e){
				endP = e.getPoint();
			}
		}
	}
	
	public static void main(String[] args) 
	{
	frameTest s=new frameTest();
	s.setSize(1000,500);
	s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	s.setVisible(true);
	}	
}
