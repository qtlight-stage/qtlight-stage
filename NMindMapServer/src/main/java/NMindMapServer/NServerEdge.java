package NMindMapServer;

import net.maritimecloud.internal.core.javax.json.Json;
import net.maritimecloud.internal.core.javax.json.JsonObject;

public class NServerEdge {
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

	public NServerEdge(NServerVertex start, NServerVertex end){
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

	public void modifyStart (NServerVertex start){
		this.NstartId = start.id();
		this.NstartX = start.x();
		this.NstartY = start.y();
		this.NstartWidth = start.width();
		this.NstartHeight = start.height();
	}

	public void modifyEnd (NServerVertex end){
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

	public JsonObject toJson() {
		return Json.createObjectBuilder()
				.add("start_vertex", this.NstartId)
				.add("end_vertex", this.NendId)
				.build();
	}
}
