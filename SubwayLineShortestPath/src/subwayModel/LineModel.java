package subwayModel;

import java.util.List;

public class LineModel {
	private String name;
	private List<StationModel> stations;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<StationModel> getStations() {
		return stations;
	}
	public void setStations(List<StationModel> stations) {
		this.stations = stations;
	}
	
	public void addStation(StationModel station) {
		stations.add(station);
	}
	
	public void setStation(int index,StationModel station) {
		stations.set(index, station);
	}
	
	public StationModel getOneStation(int index) {
		return stations.get(index);
	}
}
