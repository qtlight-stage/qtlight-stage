package NMindMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Consumer;

class NFrame extends JFrame {
    int eventnum = 0;
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

    private NCommandSender command;
    private NFrame MF = this;

    private NData mindMapData = new NData();
    private List<Node> nodeList = new LinkedList<>();

    private JButton setIpButton = this.createMenuButton("Set IP", 0, 0);
    private JTextPane setIpText = this.createTextPaneButton("localhost", 0, menuHeight);
    private JButton createNodeButton = this.createMenuButton("Create Node", 0, 2 * menuHeight);
    private JButton removeNodeButton = this.createMenuButton("Remove Node", 0, 3 * menuHeight);
    private JButton createArrowButton = this.createMenuButton("Create Arrow", 0, 4 * menuHeight);
    private JButton removeArrowButton = this.createMenuButton("Remove Arrow", 0, 5 * menuHeight);
    private JButton editNodeButton = this.createMenuButton("Edit Node", 0, 6 * menuHeight);
    private JButton moveNodeButton = this.createMenuButton("Move Node", 0, 7 * menuHeight);
    private Consumer<String> onSetIp;

    private MindMapPanel panel = new MindMapPanel();;

    NFrame(int width, int height) {
        setTitle("NMindMap");
        Fwidth = width;
        Fheight = height;

        setContentPane(panel);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu mnMenu = new JMenu("Client");
        menuBar.add(mnMenu);
        getContentPane().setLayout(null);

        setControlButtonsEnable(false);
        panel.add(setIpButton);
        panel.add(setIpText);
        panel.add(createNodeButton);
        panel.add(removeNodeButton);
        panel.add(createArrowButton);
        panel.add(removeArrowButton);
        panel.add(editNodeButton);
        panel.add(moveNodeButton);
    }

    void listenSetIp(Consumer<String> onSetIp) {
        this.onSetIp = onSetIp;
    }

    void setData(NData newData) {
        this.mindMapData = newData;
        for (Node node : this.nodeList) {
            panel.remove(node);
        }
        this.nodeList.clear();
        this.mindMapData.vertexList.forEach(this::addNodeFromVertex);
        drawMindMap();
    }

    void setMain(NCommandSender M) {
        command = M;
    }

    private JTextPane createTextPaneButton(String content, int x, int y) {
        JTextPane pane = new JTextPane();
        pane.setText(content);
        pane.setBounds(x, y, menuWidth, menuHeight);
        return pane;
    }

    private JButton createMenuButton(String name, int x, int y) {
        JButton newButton = new JButton(name);
        newButton.setBounds(x, y, menuWidth, menuHeight);
        newButton.addActionListener(new MenuActionListener());
        return newButton;
    }

    private void setEnables(Boolean enable, Component... components) {
        for (Component component : components) {
            component.setEnabled(enable);
        }
    }

    private void setControlButtonsEnable(Boolean enable) {
        setEnables(enable, createNodeButton, removeNodeButton, createArrowButton, removeArrowButton, editNodeButton, moveNodeButton);
    }

    private Node findNode(int nodeId) {
        for (Node node : nodeList) {
            if (node.getId() == nodeId) {
                return node;
            }
        }
        return null;
    }

    void addNode(int nodeId, String content, int x, int y, int width, int height) {
        this.mindMapData.createVertex(nodeId, content, x, y, width, height);
        Node newNode = new Node(nodeId);
        newNode.setText(content);
        newNode.setBounds(x, y, width, height);
        newNode.setFocusPainted(false);
        newNode.setContentAreaFilled(false);
        newNode.addActionListener(new NodeActionListener());
        nodeList.add(newNode);
        this.getContentPane().add(newNode);
        newNode.updateUI();
    }

    private void addNodeFromVertex(NVertex vertex) {
        Node newNode = new Node(vertex.id());
        newNode.setText(vertex.content());
        newNode.setBounds(vertex.x(), vertex.y(), vertex.width(), vertex.height());
        newNode.setFocusPainted(false);
        newNode.setContentAreaFilled(false);
        newNode.addActionListener(new NodeActionListener());
        nodeList.add(newNode);
        this.getContentPane().add(newNode);
        newNode.updateUI();
    }

    void removeNode(int nodeId) {
        this.mindMapData.removeVertex(nodeId);
        int i = 0;
        Node node = null;
        while (i < nodeList.size()) {
            node = nodeList.get(i);
            if (node.getId() == nodeId) {
                getContentPane().remove(node);
                nodeList.remove(i);
                break;
            }
            i++;
        }
        drawMindMap();
        if (node != null) {
            getContentPane().remove(node);
        }
    }

    void addArrow(int startId, int endId) {
        this.mindMapData.createEdge(startId, endId);
        drawMindMap();
    }

    void removeArrow(int startId, int endId) {
        this.mindMapData.removeEdge(startId, endId);
        drawMindMap();
    }

    void editNode(int nodeId, String contents, int width, int height) {
        Node n = this.findNode(nodeId);
        this.getContentPane().remove(n);
        NVertex v = this.mindMapData.getVertex(nodeId);
        v.modifyContent(contents);
        v.modifySize(width, height);
        List<NEdge> edgeList = this.mindMapData.edgeList;
        for (NEdge edge : edgeList) {
            if (edge.getStartId() == nodeId) {
                edge.modifyStart(v);
            } else if (edge.getEndId() == nodeId) {
                edge.modifyEnd(v);
            }
        }
        n.setText(contents);
        n.setSize(width, height);
        this.getContentPane().add(n);
        drawMindMap();
    }

    void moveNode(int nodeId, int x, int y) {
        Node n = this.findNode(nodeId);
        this.getContentPane().remove(n);
        NVertex v = this.mindMapData.getVertex(nodeId);
        v.modifyCoordinate(x, y);
        List<NEdge> edgeList = this.mindMapData.edgeList;
        for (NEdge edge : edgeList) {
            if (edge.getStartId() == nodeId) {
                edge.modifyStart(v);
            } else if (edge.getEndId() == nodeId) {
                edge.modifyEnd(v);
            }
        }
        n.setLocation(x, y);
        this.getContentPane().add(n);
        drawMindMap();
    }

    private void clearMindMap() {
        Graphics g = this.getLayeredPane().getGraphics();
        g.clearRect(menuWidth + marginWidth, marginHeight, Fwidth, Fheight);
    }

    private void drawMindMap() {
        clearMindMap();
        Graphics g = this.getLayeredPane().getGraphics();
        List<NEdge> edgeList = this.mindMapData.edgeList;
        for (NEdge edge : edgeList) {
            g.drawLine(edge.getStartX(), edge.getStartY() + edgeHeight, edge.getEndX(),
                    edge.getEndY() + edgeHeight);
        }
        nodeList.forEach(Node::updateUI);
    }

    private class MenuActionListener implements ActionListener {// butten event

        public void actionPerformed(ActionEvent e) {
            if (eventnum == EOption)
                return;
            JButton b = (JButton) e.getSource();
            if (b == setIpButton && !setIpText.getText().equals("")) {
                onSetIp.accept(setIpText.getText());
                setControlButtonsEnable(true);
            } else if (b.getText().equals("Create Node")) {
                eventnum = ECreateNode;
                selectedNode = null;
            } else if (b.getText().equals("Remove Node")) {
                eventnum = ERemoveNode;
                selectedNode = null;
            } else if (b.getText().equals("Create Arrow")) {
                eventnum = ECreateArrowFst;
                selectedNode = null;
            } else if (b.getText().equals("Remove Arrow")) {
                eventnum = ERemoveArrowFst;
                selectedNode = null;
            } else if (b.getText().equals("Edit Node")) {
                eventnum = EEditNode;
                selectedNode = null;
            } else if (b.getText().equals("Move Node")) {
                eventnum = EMoveNode;
                selectedNode = null;
            }
        }
    }

    private class NodeActionListener implements ActionListener {// Node event

        public void actionPerformed(ActionEvent e) {
            if (eventnum == ECreateArrowFst) {
                selectedNode = (Node) e.getSource();
                eventnum = ECreateArrowSnd;
            } else if (eventnum == ECreateArrowSnd) {
                Node n = (Node) e.getSource();
                if (selectedNode.getId() == n.getId())
                    return;
                command.commandAddArrow(selectedNode.getId(), n.getId());
                selectedNode = null;
                eventnum = EDefault;
            } else if (eventnum == ERemoveArrowFst) {
                selectedNode = (Node) e.getSource();
                eventnum = ERemoveArrowSnd;
            } else if (eventnum == ERemoveArrowSnd) {
                Node n = (Node) e.getSource();
                if (selectedNode.getId() == n.getId())
                    return;
                command.commandRemoveArrow(selectedNode.getId(), n.getId());
                selectedNode = null;
                eventnum = EDefault;
            } else if (eventnum == ERemoveNode) {
                Node n = (Node) e.getSource();
                //removeNode(n.getId());
                command.commandRemoveNode(n.getId());
                eventnum = EDefault;
            } else if (eventnum == EEditNode) {
                Node n = (Node) e.getSource();
                eventnum = EOption;
                new EditNodeOptionFrame(MF, n);
            } else if (eventnum == EMoveNode) {
                selectedNode = (Node) e.getSource();
            }
        }
    }

    private class MindMapPanel extends JPanel {
        Point startP = null;
        Point endP = null;

        MindMapPanel() {
            this.addMouseListener(new MyMouseListener());
        }

        class MyMouseListener extends MouseAdapter {
            public void mousePressed(MouseEvent e) {
                startP = e.getPoint();
                if (eventnum == ECreateNode) {
                    if (startP.x < menuWidth)
                        return;
                    eventnum = EOption;
                    new CreateNodeOptionFrame(MF, startP);
                } else if (eventnum == EMoveNode) {
                    if (startP.x < menuWidth || selectedNode == null)
                        return;
                    command.commandMoveNode(selectedNode.getId(), startP.x, startP.y);
                    eventnum = EDefault;
                }
            }

            public void mouseReleased(MouseEvent e) {
                endP = e.getPoint();
            }
        }
    }

    private class Node extends JButton {
        private int id;

        Node(int _id) {
            id = _id;
        }

        int getId() {
            return id;
        }
    }

    private class CreateNodeOptionFrame extends JFrame {
        private Point P;
        private NFrame PF;
        private int marginWidth = 10;
        private int marginHeight = 10;
        private int labelWidth = 80;
        private int labelHeight = 25;
        private JTextPane Ocontents;
        private JTextPane Owidth;
        private JTextPane Oheight;

        CreateNodeOptionFrame(NFrame Parent, Point _P) {
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
            Oheight.setBounds(marginWidth + labelWidth, 3 * marginHeight + 2 * labelHeight, labelWidth, labelHeight);
            getContentPane().add(Oheight);

            JButton btnNewButton = new JButton("Create");
            btnNewButton.setBounds(marginWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
            getContentPane().add(btnNewButton);
            btnNewButton.addActionListener(new COPActionListener());

            JButton btnNewButton_1 = new JButton("Cancel");
            btnNewButton_1.setBounds(2 * marginWidth + labelWidth, 4 * marginHeight + 3 * labelHeight, labelWidth,
                    labelHeight);
            getContentPane().add(btnNewButton_1);
            btnNewButton_1.addActionListener(new COPActionListener());
            setVisible(true);
        }

        private class COPActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                if (b.getText().equals("Create")) {
                    //MF.addNode(1,Ocontents.getText(), P.x, P.y, Integer.valueOf(Owidth.getText()),
                    //                Integer.valueOf(Oheight.getText()));
                    command.commandAddNode(Ocontents.getText(), P.x, P.y, Integer.valueOf(Owidth.getText()),
                            Integer.valueOf(Oheight.getText()));
                    dispose();
                    PF.eventnum = EDefault;
                } else if (b.getText().equals("Cancel")) {
                    dispose();
                    PF.eventnum = EDefault;
                }
            }
        }
    }

    private class EditNodeOptionFrame extends JFrame {
        private NFrame PF;
        private Node N;
        private int marginWidth = 10;
        private int marginHeight = 10;
        private int labelWidth = 80;
        private int labelHeight = 25;
        private JTextPane Ocontents;
        private JTextPane Owidth;
        private JTextPane Oheight;

        EditNodeOptionFrame(NFrame Parent, Node n) {
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
            Oheight.setBounds(marginWidth + labelWidth, 3 * marginHeight + 2 * labelHeight, labelWidth, labelHeight);
            getContentPane().add(Oheight);

            JButton btnNewButton = new JButton("Confirm");
            btnNewButton.setBounds(marginWidth, 4 * marginHeight + 3 * labelHeight, labelWidth, labelHeight);
            getContentPane().add(btnNewButton);
            btnNewButton.addActionListener(new EOPActionListener());

            JButton btnNewButton_1 = new JButton("Cancel");
            btnNewButton_1.setBounds(2 * marginWidth + labelWidth, 4 * marginHeight + 3 * labelHeight, labelWidth,
                    labelHeight);
            getContentPane().add(btnNewButton_1);
            btnNewButton_1.addActionListener(new EOPActionListener());
            setVisible(true);
        }

        private class EOPActionListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JButton b = (JButton) e.getSource();
                if (b.getText().equals("Confirm")) {
                    command.commandEditNode(N.getId(), Ocontents.getText(), Integer.valueOf(Owidth.getText()),
                            Integer.valueOf(Oheight.getText()));
                    dispose();
                    PF.eventnum = EDefault;
                } else if (b.getText().equals("Cancel")) {
                    dispose();
                    PF.eventnum = EDefault;
                }
            }
        }
    }

}
