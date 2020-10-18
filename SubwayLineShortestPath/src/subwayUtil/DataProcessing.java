package subwayUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.LogicalMessage;

import subwayModel.LineModel;
import subwayModel.StationModel;

public class DataProcessing {
	
	public static List<LineModel> lineSet = new ArrayList<LineModel>(); //������·��Ϣ
	
	public DataProcessing(String pathname) throws IOException {
		File filename = new File(pathname);
		InputStreamReader input = new InputStreamReader(new FileInputStream(filename));
		BufferedReader br = new BufferedReader(input);
		
		String readstr = "";
		readstr = br.readLine();
		int numline = Integer.parseInt(readstr);
		
		List<String> lines = new ArrayList<String>(); //���е������ļ���
		
		//��ȡ��·ͼ
		for(int i=0;i<numline;i++) {
			readstr = br.readLine();
			String[] stationsOfLine = readstr.split(" ");
			
			LineModel line = new LineModel();
			List<StationModel> stations = new ArrayList<StationModel>();
			
			
			line.setName(stationsOfLine[0]);//��·��
			//����ÿһ��վ�㣺����վ������վ.�����뵽��Ӧ������
			for(int j=0;j<stationsOfLine.length-1;j++) {
				
				StationModel station = new StationModel();
				List<String> lineName = new ArrayList<String>(); //ÿ��վ�е�����
				List<StationModel> neighborStation = new ArrayList<StationModel>();
				
				station.setStationName(stationsOfLine[j+1]);
				int isExist = 0;
				int isCircle = 0;
				//ȷ���Ƿ�Ϊ����վ(�Ѵ���)��lineSet������Ŀǰ������
				int flagnum=0,stationNum=0;
				for(int lineindex = 0;lineindex<lineSet.size();lineindex++) {
					for( int k=0;k<lineSet.get(lineindex).getStations().size();k++) {
						if(stationsOfLine[j+1].equals(lineSet.get(lineindex).getStations().get(k).getStationName())) {
							flagnum=lineindex;
							stationNum = k;
							station = lineSet.get(lineindex).getStations().get(k);
							lineName = station.getLineName();
							neighborStation = station.getNeighborStationName();
							lineName.add(line.getName());
							isExist = 1;
							break;
						}
					}
				}
				if(isExist==0) { //��վ��������
					lineName.add(line.getName());
				}
				//���neighborStation
				//��line���Ѿ����ڸ�վ��,Ϊ���ߣ���Ϊ���һվ����ôǰ��Ϊ����վ�Ѿ��жϹ���
				int p;
				for(p=1;p<j+1;p++) {
					if(stationsOfLine[p].equals(station.getStationName())) {
						station = line.getOneStation(p-1);
						lineName = station.getLineName();
						neighborStation = station.getNeighborStationName();
					}
				}
				//�����һվ����վ��neighbor�б�
				if(j!=0) {
					neighborStation.add(line.getStations().get(j-1));
				}
				station.setLineName(lineName);
				station.setNeighborStationName(neighborStation);
				//�ȴ�station���ͣ������վ����һվ��neighbor�б�
				if(j!=0) {
					List<StationModel> newNeighborStations = new ArrayList<StationModel>();
					newNeighborStations = line.getStations().get(j-1).getNeighborStationName();
					newNeighborStations.add(station);
					line.getStations().get(j-1).setNeighborStationName(newNeighborStations);
				}
				stations.add(station);
				if(isExist == 1) {
					List<StationModel> newStations = new ArrayList<StationModel>();
					newStations = lineSet.get(flagnum).getStations();
					newStations.set(stationNum, station);
					lineSet.get(flagnum).setStations(newStations);
				}
				if(isCircle == 1) {
					stations.set(p-1, station);
				}
				line.setStations(stations);
			}
			lineSet.add(line);
			
		}
//		System.out.println(lineSet.get(0).getOneStation(0).getStationName());
	}

}
