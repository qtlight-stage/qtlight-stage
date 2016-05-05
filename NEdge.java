package NMindMap;
public class NEdge {
	private int Nstart;
	private int Nend;
	private int NstartX;
	private int NstartY;
	private int NstartWidth;
	private int NstartHeight;
	private int NendX;
	private int NendY;
	private int NendWidth;
	private int NendHeight;
	
	public void init (NVertex start, NVertex end){
		this.Nstart = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();
		
		this.Nend = end.id();
		this.NendX = end.x();
		this.NendY = end.y();
		this.NendWidth = end.width();
		this.NendHeight = end.height();
	}
	
	public void modifyStart (NVertex start){
		this.Nstart = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();
	}
	
	public void modifyEnd (NVertex end){
		this.Nend = end.id();
		this.NendX = end.x();
		this.NendY = end.y();
		this.NendWidth = end.width();
		this.NendHeight = end.height();
	}
	
	public int getStart(){return this.Nstart;}
	public int getEnd(){return this.Nend;}
	
	public int getStartX(){return this.NstartX + (this.NstartWidth / 2);}
	public int getStartY(){return this.NstartY + (this.NstartHeight / 2);}	
	public int getEndX(){return this.NendX + (this.NendWidth / 2);}
	public int getEndY(){return this.NendY + (this.NendHeight / 2);}
}
