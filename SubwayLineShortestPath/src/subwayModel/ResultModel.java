package subwayModel;

public class ResultModel {
	private StationModel start; //���
	private StationModel end;   //;�е�ÿ���������·���õ���Ϊ�յ�
	private int distance = 0;
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	private boolean isExchange = false; //trueΪ���ˣ�falseΪû�л���
	
	public StationModel getStart() {
		return start;
	}
	public void setStart(StationModel start) {
		this.start = start;
	}
	public StationModel getEnd() {
		return end;
	}
	public void setEnd(StationModel end) {
		this.end = end;
	}
	public boolean isExchange() {
		return isExchange;
	}
	public void setExchange(boolean isExchange) {
		this.isExchange = isExchange;
	}
	
}
