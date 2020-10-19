package subwayModel;

public class ResultModel {
	private StationModel start; //起点
	private StationModel end;   //途中到每个点最近的路，该点作为终点
	private int distance = 0;
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	private boolean isExchange = false; //true为换乘，false为没有换乘
	
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
