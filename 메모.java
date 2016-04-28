//모든 메모는 여기에 위키 불편함

//JFrame에서
//Node 그릴때 예시

public class frameName extends JFrame {
    public void makeNewNode(String Vcontents, int Vx, int Vy, int Vwidth, int Vheight){
      JTextPane newNode = new JTextPane();
      newNode.setText(Vcontents);
      newNode.setBounds(Vx, Vy, Vwidth, Vheight);
      getContentPane().add(NewNode);
}
//JTextPane이 id도 저장할수있게 child class 정의 해야함
