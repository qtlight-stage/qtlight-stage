package testframe;
import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;

public class NFrame extends JFrame{
	public int eventnum = 0;
	private int EDefault = 0;
	private int EOption = -1;
	private int ECreateNode = 1;
	private int ECreateArrow = 2;
	private int ERemoveNode = 3;
	
	private int Fwidth;
	private int Fheight;
	private int marginWidth = 8;
	private int marginHeight = 55;
	private int menuWidth = 140;
	private int menuHeight = 20;
	
	private NFrame MF = this;

	private int nodeId = 0;
	
	private List<Node> nodeList = new LinkedList<Node>();
	private List<Line> lineList = new LinkedList<Line>();
	
	public NFrame(int width, int height) {
		setTitle("NMindMap");
		
		Fwidth = width;
		Fheight = height;
		
		setContentPane(new MyPanel());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnMenu.add(mntmClose);
		getContentPane().setLayout(null);
		
		JButton bCreateNode = new JButton("Create Node");
		bCreateNode.setBounds(0, 0, menuWidth, menuHeight);
		getContentPane().add(bCreateNode);
		bCreateNode.addActionListener(new MyActionListener());
		
		JButton bCreateArrow = new JButton("Create Arrow");
		bCreateArrow.setBounds(0, menuHeight, menuWidth, menuHeight);
		getContentPane().add(bCreateArrow);
		bCreateArrow.addActionListener(new MyActionListener());
		
		JButton bRemoveNode = new JButton("Remove Node");
		bRemoveNode.setBounds(0, 2 * menuHeight, menuWidth, menuHeight);
		getContentPane().add(bRemoveNode);
		bRemoveNode.addActionListener(new MyActionListener());
		
		JButton btnC = new JButton("Clear");
		btnC.setBounds(0, 3 * menuHeight, menuWidth, menuHeight);
		btnC.setFocusPainted(false);
		btnC.setContentAreaFilled(false);
		getContentPane().add(btnC);
		btnC.addActionListener(new MyActionListener());
	}
	
    private class MyActionListener implements ActionListener {//butten event
        public void actionPerformed(ActionEvent e) {
        	if (eventnum == EOption)
        		return;
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("Create Node"))
                eventnum = ECreateNode;
            else if (b.getText().equals("Create Arrow"))
                eventnum = ECreateArrow;
            else if (b.getText().equals("Remove Node"))
                eventnum = ERemoveNode;
            else if (b.getText().equals("Clear")){
            	clearMindMap();
            }
        }
    }
    
    private class NodeActionListener implements ActionListener {//Node event
        public void actionPerformed(ActionEvent e) {
        	if (eventnum == ERemoveNode){
        		Node n = (Node) e.getSource();
        		int nodeId = n.getId();
        		int i = 0;
        		while (i < nodeList.size()){
        			if(nodeList.get(i).getId() == nodeId)
        				break;
        			i++;
        		}
        		getContentPane().remove(nodeList.get(i));
        		nodeList.remove(i);
        		clearMindMap();
        		drawMindMap();
        	}
        }
    }
	
	class MyPanel extends JPanel{
		Point startP = null;
		Point endP = null;
		
		public MyPanel(){
			this.addMouseListener(new MyMouseListener());
		}
		
		class MyMouseListener extends MouseAdapter{
			public void mousePressed(MouseEvent e){
				startP = e.getPoint();
				
				if(eventnum == ECreateNode){
					if (startP.x < menuWidth)
						return;
					eventnum = EOption;
					new CreateNodeOptionFrame(MF, startP);
				}
			}
			
			public void mouseReleased(MouseEvent e){
				endP = e.getPoint();
				if(eventnum == ECreateArrow){
					if ((startP.x != endP.x) && (startP.y != endP.y)){
						if (startP.x < menuWidth || endP.x < menuWidth)
							return;
						clearMindMap();
						lineList.add(new Line(startP.x + marginWidth, startP.y + marginHeight, endP.x + marginWidth, endP.y + marginHeight));
						drawMindMap();				
					}
					eventnum = EDefault;
				}
			}
		}
	}
	
	public void clearMindMap(){
    	Graphics g = getLayeredPane().getGraphics();
		g.clearRect(menuWidth + marginWidth, marginHeight, Fwidth, Fheight);
		int i = 0;
	}
	
	public void drawMindMap(){
		int i = 0;
		Graphics g = getLayeredPane().getGraphics();
		while (i < lineList.size()){
			g.drawLine(lineList.get(i).x1, lineList.get(i).y1, lineList.get(i).x2, lineList.get(i).y2);
			i++;
		}
		i = 0;
		while (i < nodeList.size()){
//			nodeList.get(i).setText(String.valueOf(nodeList.get(i).getId()));
			nodeList.get(i).updateUI();
			i++;
		}
	}
	
	class Node extends JButton{
		private int id;
		Node(int _id){
			id = _id;
		}
		int getId(){
			return id;
		}
	}
	
	class Line{
		int startid;
		int endid;
		int x1;
		int y1;
		int x2;
		int y2;
		
		Line(int _x1, int _y1, int _x2, int _y2){
			this.x1 = _x1;
			this.y1 = _y1;
			this.x2 = _x2;
			this.y2 = _y2;
		}
	}
	

    class CreateNodeOptionFrame extends JFrame{
    	private Point P;
    	private NFrame PF;
    	private int marginWidth = 10;
    	private int marginHeight = 10;
    	private int labelWidth = 80;
    	private int labelHeight = 25;
    	private JTextPane Ocontents;
    	private JTextPane Owidth;
    	private JTextPane Oheight;
    	
    	public CreateNodeOptionFrame(NFrame Parent,Point _P){
    		super("Node Option");
    		P = _P;
    		PF = Parent;
    		
    		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    		setSize(4 * marginWidth + 2 * labelWidth, 6 * marginHeight + 5 * labelHeight);
    		getContentPane().setLayout(null);
    		
    		JPanel panel = new JPanel();
    		panel.setBounds(marginWidth, marginHeight, labelWidth, labelHeight);
    		getContentPane().add(panel);
    		
    		JLabel lblNewLabel = new JLabel("Contents");
    		panel.add(lblNewLabel);
    		
    		JPanel panel_1 = new JPanel();
    		panel_1.setBounds(marginWidth, 2 * marginHeight + labelHeight, labelWidth, labelHeight);
    		getContentPane().add(panel_1);
    		
    		JLabel label = new JLabel("Width");
    		panel_1.add(label);
    		
    		JPanel panel_2 = new JPanel();
    		panel_2.setBounds(marginWidth, 3 * marginHeight + 2 * labelHeight, labelWidth, labelHeight);
    		getContentPane().add(panel_2);
    		
    		JLabel label_1 = new JLabel("Height");
    		panel_2.add(label_1);
    		
    		Ocontents = new JTextPane();
    		Ocontents.setText("New Node");
    		Ocontents.setBounds(marginWidth + labelWidth, marginHeight, labelWidth, labelHeight);
    		getContentPane().add(Ocontents);
    		
    		Owidth = new JTextPane();
    		Owidth.setText("120");
    		Owidth.setBounds(marginWidth + labelWidth, 2 * marginHeight + labelHeight, labelWidth, labelHeight);
    		getContentPane().add(Owidth);
    		
    		Oheight = new JTextPane();
    		Oheight.setText("25");
    		Oheight.setBounds(marginWidth + labelWidth, 3 * marginHeight + 2* labelHeight, labelWidth, labelHeight);
    		getContentPane().add(Oheight);
    		
    		JButton btnNewButton = new JButton("Create");
    		btnNewButton.setBounds(marginWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
    		getContentPane().add(btnNewButton);
    		btnNewButton.addActionListener(new COPActionListener());
    		
    		JButton btnNewButton_1 = new JButton("Cancel");
    		btnNewButton_1.setBounds(2 * marginWidth + labelWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
    		getContentPane().add(btnNewButton_1);
    		btnNewButton_1.addActionListener(new COPActionListener());
    		setVisible(true);
    	}
    	private class COPActionListener implements ActionListener {
    		public void actionPerformed(ActionEvent e) {
    			JButton b = (JButton) e.getSource();
    			if (b.getText().equals("Create")){
    				Node newNode = new Node(PF.nodeId);
    				newNode.setText(Ocontents.getText());
    				newNode.setBounds(P.x, P.y, Integer.valueOf(Owidth.getText()), Integer.valueOf(Oheight.getText()));
    				newNode.setFocusPainted(false);
    				newNode.setContentAreaFilled(false);
    				newNode.addActionListener(new NodeActionListener());
    				PF.nodeList.add(newNode);
    				PF.getContentPane().add(newNode);
    				newNode.updateUI();
    				PF.nodeId++;
    				PF.eventnum = EDefault;
    				dispose();
    			}
    			else if (b.getText().equals("Cancel")){
    				PF.eventnum = EDefault;
    	    		dispose();		
    			}
    		}
    	}
    }
}
