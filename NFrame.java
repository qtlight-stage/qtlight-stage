package NMindMap;
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
	private int ERemoveNode = 2;
	private int ECreateArrowFst = 3;
	private int ECreateArrowSnd = 4;
	private int ERemoveArrowFst = 5;
	private int ERemoveArrowSnd = 6;
	private int EEditNode = 7;
	private int EMoveNode = 8;
	
	private int Fwidth;
	private int Fheight;
	private int edgeWidth = 0;
	private int edgeHeight = 25;
	private int marginWidth = 8;
	private int marginHeight = 55;
	private int menuWidth = 140;
	private int menuHeight = 20;
	
	private Node selectedNode = null;
	
	private NFrame MF = this;
	
	private NData mindMapData = new NData(); 
	private List<Node> nodeList = new LinkedList<Node>();
	
	public NFrame(int width, int height) {
		setTitle("NMindMap");
		Fwidth = width;
		Fheight = height;
		
		setContentPane(new MindMapPanel());
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);
		JMenuItem mntmClose = new JMenuItem("Close");
		mnMenu.add(mntmClose);
		getContentPane().setLayout(null);
		
		this.addMenuButton("Create Node", 0, 0);
		this.addMenuButton("Remove Node", 0, menuHeight);
		this.addMenuButton("Create Arrow", 0, 2 * menuHeight);
		this.addMenuButton("Remove Arrow", 0, 3 * menuHeight);
		this.addMenuButton("Edit Node", 0, 4 * menuHeight);
		this.addMenuButton("Move Node", 0, 5 * menuHeight);
	}
	
	public void addMenuButton(String name, int x, int y){
		JButton newButton = new JButton(name);
		newButton.setBounds(x, y, menuWidth, menuHeight);
		newButton.addActionListener(new MenuActionListener());
		this.getContentPane().add(newButton);
	}
	
	public void addNode(String contents, int x, int y, int width, int height){
		int nodeId = this.mindMapData.createVertex(contents, x, y, width, height);
		Node newNode = new Node(nodeId);
		newNode.setText(contents);
		newNode.setBounds(x, y, width, height);
		newNode.setFocusPainted(false);
		newNode.setContentAreaFilled(false);
		newNode.addActionListener(new NodeActionListener());
		nodeList.add(newNode);
		this.getContentPane().add(newNode);
		newNode.updateUI();
	}
	
	public void removeNode(Node n){
		int nodeId = n.getId();
		this.mindMapData.removeVertex(nodeId);
		int i = 0;
		while (i < nodeList.size()){
			if(nodeList.get(i).getId() == nodeId)
				break;
			i++;
		}
		getContentPane().remove(nodeList.get(i));
		nodeList.remove(i);
		drawMindMap();
	}
	
	public void addArrow(Node start, Node end){
		this.mindMapData.createEdge(start.getId(), end.getId());
		drawMindMap();
	}
	
	public void removeArrow(Node start, Node end){
		this.mindMapData.removeEdge(start.getId(), end.getId());
		drawMindMap();
	}

	
	public void editNode(Node n, String contents, int width, int height){
		this.getContentPane().remove(n);
		int nodeId = n.getId();
		NVertex v = this.mindMapData.getVertex(nodeId);
		v.modifyContents(contents);
		v.modifySize(width, height);
		int i = 0;
		List<NEdge> edgeList = this.mindMapData.getEdgeList();
		NEdge e;
		while (i < edgeList.size()){
			e = edgeList.get(i);
			if (e.getStart() == nodeId){
				e.modifyStart(v);
			} else if(e.getEnd() == nodeId){
				e.modifyEnd(v);
			}
			i++;
		}
		n.setText(contents);
		n.setSize(width, height);
		this.getContentPane().add(n);
		drawMindMap();
	}
	
	public void moveNode(Node n, int x, int y){
		this.getContentPane().remove(n);
		int nodeId = n.getId();
		NVertex v = this.mindMapData.getVertex(nodeId);
		v.modifyCoordinate(x, y);
		int i = 0;
		List<NEdge> edgeList = this.mindMapData.getEdgeList();
		NEdge e;
		while (i < edgeList.size()){
			e = edgeList.get(i);
			if (e.getStart() == nodeId){
				e.modifyStart(v);
			} else if(e.getEnd() == nodeId){
				e.modifyEnd(v);
			}
			i++;
		}
		n.setLocation(x, y);
		this.getContentPane().add(n);
		drawMindMap();
	}
	
	public void clearMindMap(){
    	Graphics g = this.getLayeredPane().getGraphics();
		g.clearRect(menuWidth + marginWidth, marginHeight, Fwidth, Fheight);
	}
	
	public void drawMindMap(){
		clearMindMap();
		int i = 0;
		Graphics g = this.getLayeredPane().getGraphics();
		List<NEdge> edgeList = this.mindMapData.getEdgeList();
		while (i < edgeList.size()){
			g.drawLine(edgeList.get(i).getStartX(), edgeList.get(i).getStartY() + edgeHeight,
			           edgeList.get(i).getEndX(), edgeList.get(i).getEndY() + edgeHeight);
			i++;
		} i = 0;
		while (i < nodeList.size()){
//			nodeList.get(i).setText(String.valueOf(nodeList.get(i).getId()));
			nodeList.get(i).updateUI();
			i++;
		}
	}	
	
    private class MenuActionListener implements ActionListener {//butten event
        public void actionPerformed(ActionEvent e) {
        	if (eventnum == EOption)
        		return;
            JButton b = (JButton) e.getSource();
            if (b.getText().equals("Create Node")){
                eventnum = ECreateNode;
                selectedNode = null;
            } else if (b.getText().equals("Remove Node")){
                eventnum = ERemoveNode;
                selectedNode = null;
            } else if (b.getText().equals("Create Arrow")){
            	eventnum = ECreateArrowFst;
            	selectedNode = null;
            } else if (b.getText().equals("Remove Arrow")){
            	eventnum = ERemoveArrowFst;
            	selectedNode = null;
            } else if (b.getText().equals("Edit Node")){
            	eventnum = EEditNode;
            	selectedNode = null;
            } else if (b.getText().equals("Move Node")){
            	eventnum = EMoveNode;
            	selectedNode = null;
            }
        }
    }
    
    private class NodeActionListener implements ActionListener {//Node event
        public void actionPerformed(ActionEvent e) {
        	if(eventnum == ECreateArrowFst){
        		selectedNode = (Node) e.getSource();
        		eventnum = ECreateArrowSnd;
        	} else if(eventnum == ECreateArrowSnd){
        		Node n = (Node) e.getSource();
        		if (selectedNode.getId() == n.getId())
        			return;
        		addArrow(selectedNode, n);
        		selectedNode = null;
        		eventnum = EDefault;
        	}
        	
        	else if(eventnum == ERemoveArrowFst){
        		selectedNode = (Node) e.getSource();
        		eventnum = ERemoveArrowSnd;
        	} else if(eventnum == ERemoveArrowSnd){
        		Node n = (Node) e.getSource();
        		if (selectedNode.getId() == n.getId())
        			return;
        		removeArrow(selectedNode, n);
        		selectedNode = null;
        		eventnum = EDefault;
        	}
        	
        	else if (eventnum == ERemoveNode){
        		Node n = (Node) e.getSource();
        		removeNode(n);
        		eventnum = EDefault;
        	}
        	
        	else if(eventnum == EEditNode){
        		Node n = (Node) e.getSource();
				eventnum = EOption;
				new EditNodeOptionFrame(MF, n);
			} else if(eventnum == EMoveNode){
        		selectedNode = (Node) e.getSource();
			}
        }
    }
	
	class MindMapPanel extends JPanel{
		Point startP = null;
		Point endP = null;
		
		public MindMapPanel(){
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
				} else if(eventnum == EMoveNode){
					if (startP.x < menuWidth || selectedNode == null)
						return;
					moveNode(selectedNode, startP.x, startP.y);
					eventnum = EDefault;
				}
			}
			
			public void mouseReleased(MouseEvent e){
				endP = e.getPoint();
			}
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
    				addNode(Ocontents.getText(), P.x, P.y, Integer.valueOf(Owidth.getText()), Integer.valueOf(Oheight.getText()));
    				dispose();
    				PF.eventnum = EDefault;
    			}
    			else if (b.getText().equals("Cancel")){
    	    		dispose();
    	    		PF.eventnum = EDefault;
    			}
    		}
    	}
    }

    class EditNodeOptionFrame extends JFrame{
    	private NFrame PF;
    	private Node N;
    	private int marginWidth = 10;
    	private int marginHeight = 10;
    	private int labelWidth = 80;
    	private int labelHeight = 25;
    	private JTextPane Ocontents;
    	private JTextPane Owidth;
    	private JTextPane Oheight;
    	
    	public EditNodeOptionFrame(NFrame Parent, Node n){
    		super("Node Option");
    		PF = Parent;
    		N = n;
    		
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
    		Ocontents.setText(N.getText());
    		Ocontents.setBounds(marginWidth + labelWidth, marginHeight, labelWidth, labelHeight);
    		getContentPane().add(Ocontents);
    		
    		Owidth = new JTextPane();
    		Owidth.setText(String.valueOf(N.getWidth()));
    		Owidth.setBounds(marginWidth + labelWidth, 2 * marginHeight + labelHeight, labelWidth, labelHeight);
    		getContentPane().add(Owidth);
    		
    		Oheight = new JTextPane();
    		Oheight.setText(String.valueOf(N.getHeight()));
    		Oheight.setBounds(marginWidth + labelWidth, 3 * marginHeight + 2* labelHeight, labelWidth, labelHeight);
    		getContentPane().add(Oheight);
    		
    		JButton btnNewButton = new JButton("Confirm");
    		btnNewButton.setBounds(marginWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
    		getContentPane().add(btnNewButton);
    		btnNewButton.addActionListener(new EOPActionListener());
    		
    		JButton btnNewButton_1 = new JButton("Cancel");
    		btnNewButton_1.setBounds(2 * marginWidth + labelWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
    		getContentPane().add(btnNewButton_1);
    		btnNewButton_1.addActionListener(new EOPActionListener());
    		setVisible(true);
    	}
    	private class EOPActionListener implements ActionListener {
    		public void actionPerformed(ActionEvent e) {
    			JButton b = (JButton) e.getSource();
    			if (b.getText().equals("Confirm")){
    				editNode(N, Ocontents.getText(), Integer.valueOf(Owidth.getText()), Integer.valueOf(Oheight.getText()));
    				dispose();
    				PF.eventnum = EDefault;
    			}
    			else if (b.getText().equals("Cancel")){    				
    	    		dispose();
    	    		PF.eventnum = EDefault;
    	    	}
    		}
    	}
    }

}
