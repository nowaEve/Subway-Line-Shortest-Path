package subwayModel;

import java.util.List;

public class StationModel {
	private String StationName;
	private List<String> LineName;
	private List<StationModel> NeighborStation;
	
	public String getStationName() {
		return StationName;
	}
	public void setStationName(String stationName) {
		StationName = stationName;
	}
	public List<String> getLineName() {
		return LineName;
	}
	public void setLineName(List<String> lineName) {
		LineName = lineName;
	}
	public List<StationModel> getNeighborStationName() {
		return NeighborStation;
	}
	public void setNeighborStationName(List<StationModel> neighborStationName) {
		NeighborStation = neighborStationName;
	}
}
