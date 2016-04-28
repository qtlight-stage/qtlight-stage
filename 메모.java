//모든 메모는 여기에 위키 불편함
//메모에 대한 구상이 구체화되면 코드로 하던가 위키로 보내던가하셈

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
//이때 예시
public class frameName2 extends JFrame {
    public void makeNewNode(int Vid, String Vcontents, int Vx, int Vy, int Vwidth, int Vheight){
      NNode newNode = new JTextPane();
      newNode.setId(Vid);
      newNode.setText(Vcontents);
      newNode.setBounds(Vx, Vy, Vwidth, Vheight);
      getContentPane().add(NewNode);
}
//NNode extends JTextPane
