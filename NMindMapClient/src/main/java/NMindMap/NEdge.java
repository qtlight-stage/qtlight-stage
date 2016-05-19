package NMindMap;

class NEdge {
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

	NEdge(NVertex start, NVertex end){
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

	void modifyStart (NVertex start){
		this.NstartId = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();
	}

	void modifyEnd (NVertex end){
		this.NendId = end.id();
		this.NendX = end.x();
		this.NendY = end.y();
		this.NendWidth = end.width();
		this.NendHeight = end.height();
	}

	int getStartId(){ return this.NstartId;}
	int getEndId(){return this.NendId;}

	int getStartX(){return this.NstartX + (this.NstartWidth / 2);}
	int getStartY(){return this.NstartY + (this.NstartHeight / 2);}
	int getEndX(){return this.NendX + (this.NendWidth / 2);}
	int getEndY(){return this.NendY + (this.NendHeight / 2);}
}
