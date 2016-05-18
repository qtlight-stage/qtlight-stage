package NMindMap;

public class NEdge {
	private int NstartId;
	private int NendId;
	private int NstartX;
	private int NstartY;
	private int NstartWidth;
	private int NstartHeight;
	private int NendX;
	private int NendY;
	private int NendWidth;
	private int NendHeight;

	public NEdge(NVertex start, NVertex end){
		this.NstartId = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();

		this.NendId = end.id();
		this.NendX = end.x();
		this.NendY = end.y();
		this.NendWidth = end.width();
		this.NendHeight = end.height();
	}

	public void modifyStart (NVertex start){
		this.NstartId = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();
	}

	public void modifyEnd (NVertex end){
		this.NendId = end.id();
		this.NendX = end.x();
		this.NendY = end.y();
		this.NendWidth = end.width();
		this.NendHeight = end.height();
	}

	public int getStartId(){ return this.NstartId;}
	public int getEndId(){return this.NendId;}

	public int getStartX(){return this.NstartX + (this.NstartWidth / 2);}
	public int getStartY(){return this.NstartY + (this.NstartHeight / 2);}
	public int getEndX(){return this.NendX + (this.NendWidth / 2);}
	public int getEndY(){return this.NendY + (this.NendHeight / 2);}
}
