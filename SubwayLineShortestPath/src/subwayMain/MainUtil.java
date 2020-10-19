package subwayMain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import subwayModel.LineModel;
import subwayModel.ResultModel;
import subwayModel.StationModel;
import subwayUtil.DataProcessing;

public class MainUtil {
	public static DataProcessing dataProcessing;
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			dataProcessing = new DataProcessing("./src/data.txt");
		}
		catch(IOException ex) {
			System.out.println("数据地址路径不正确");
			ex.printStackTrace();
		}
		System.out.println("选择：1.查询路线；2.查询起点终点最短路径");
		int option1 = input.nextInt();
		while(option1!=1&&option1!=2) {
			System.out.println("选择：1.查询路线；2.查询起点终点最短路径");
			option1 = input.nextInt();
		}
		if(option1==1) {
			System.out.println("请输入路线名：");
			String name = input.next();
			//查询路线
			searchLineData(name);
		}
		else if(option1==2) {
			System.out.print("请输入起点：");
			String start = input.next();
			System.out.print("请输入终点：");
			String end = input.next();
			//判断站点存在
			//查询最短路径
			dijkstraToMin(start, end);
		}
		input.close();
//		if(args.length<3) {
//			System.out.println("Usage: java subwayMain.MainUtil \"filePath\" \"a or b\" \"line name\" or \"start\" and \"end\"");
//			System.exit(1);
//		}
//		if(args[1].equals("a")||args[1].equals("b")) {
//			System.out.println("Usage: java subwayMain.MainUtil \"filePath\" \"a or b\" \"line name\" or \"start\" and \"end\"");
//			System.out.println("请输入参数a或b,a:查询线路名\tb:查询起点终点的最短路径");
//			System.exit(1);
//		}
//		if(args[1].equals("b")&&args.length!=4) {
//			System.out.println("b:查询起点终点的最短路径 ,请输入起点和终点");
//			System.exit(1);
//		}
	}
	
	public static void searchLineData(String name) {
		int flagnum=-1;
		for(int i=0;i<dataProcessing.lineSet.size();i++) {
			if(name.equals(dataProcessing.lineSet.get(i).getName())){
				flagnum = i;
				break;
			}
		}
		if(flagnum==-1) {
			System.out.print("该地铁线路不存在");
		}
		else {
			System.out.print(name+": ");
			for(StationModel stm:dataProcessing.lineSet.get(flagnum).getStations()) {
				System.out.print(stm.getStationName()+"  ");
			}
		}
	}
	
	public static void dijkstraToMin(String start,String end) {
		//作为neighbor刚被分析完的结果站点，等待进一步深度分析，分析它的neighbor，分析完了删除
		List<StationModel> resultStation = new ArrayList<StationModel>();
		//已经全部分析完的station，分析完一个就加入到这里，并将它的neighbor加入前一个List
		List<StationModel> analyzedStation = new ArrayList<StationModel>();
		
		//结果集
		List<ResultModel> result = new ArrayList<ResultModel>();
		//存放最终需要的站点的结果
		List<ResultModel> endResult = new ArrayList<ResultModel>();	
		
		//找到起点
		StationModel startStation = new StationModel();
		startStation = findStation(start);
		if(startStation==null) {
			System.out.println("起点不存在");
			System.exit(1);
		}
		StationModel endStation = new StationModel();
		endStation = findStation(end);
		if(endStation==null) {
			System.out.println("终点不存在");
			System.exit(1);
		}
		if(start.equals(end)) {
			System.out.print("已经到达终点"+start);
			System.exit(1);
		}
		//构建起点结果
		ResultModel startResult = new ResultModel();
		startResult.setDistance(0);
		startResult.setStart(startStation);
		startResult.setEnd(startStation);
		startResult.setExchange(false);
		
		ResultModel tempResult = new ResultModel();
		StationModel tempStation = new StationModel();
		result.add(startResult);
		resultStation.add(startStation);
		//通过neighbor的站点遍历，从待深度分析的station中get station
		while(!resultStation.isEmpty()) {
			tempStation = resultStation.get(0); //当前等待分析其neighbor的station
			//tempResult中为以当前station为end的result，需要寻找从当前的结果集中
			tempResult = findResult(tempStation, result);
			
			//若在已经深度分析结束的List中，已经分析过邻居了
			if(analyzedStation.contains(tempStation)) {
				continue;
			}
			
			//若当前的tempResult已为最终需要的终点，已在结果集中，可以退出查找
			if(tempStation.getStationName().equals(end)) {
				break;
			}
			//遍历neighbor
			for(int i=0;i<tempStation.getNeighborStationName().size();i++) {
				int flag = 0;
				ResultModel rm = new ResultModel(); //每一个station都只有一个result，其为result中end
				StationModel stm = new StationModel(); //该station
				stm = tempStation.getNeighborStationName().get(i);
				//判断该station是否在结果集中，比较距离，更小的话就替换，如果进行了深度分析的话，说明当前待分析的距离一定>=该距离，已经为最小距离
				for(int j=0;j<result.size();j++) {
					if(result.get(j).getEnd().equals(stm)) {
						flag = 1;
						if(tempResult.getDistance()+1<result.get(j).getDistance()) {
							//不可能发生
						}
						else {
							break;
						}
					}
				}
				//在结果集中
				if(flag==1) {
					continue;
				}
				//不在结果集中，构建结果，并传入结果集，将station加入待深度分析
				else {
					rm.setDistance(tempResult.getDistance()+1);
					rm.setStart(tempStation);
					rm.setEnd(tempStation.getNeighborStationName().get(i));
					result.add(rm);
					resultStation.add(tempStation.getNeighborStationName().get(i));
				}
			}
			analyzedStation.add(tempStation);
			resultStation.remove(0);
		}
		endResult = findEndResult(start,end,result);
		Collections.reverse(endResult);
		if(endResult==null) {
			System.out.println("终点不存在");
			System.exit(1);
		}
		int number = endResult.size()+1;
		System.out.println("共"+number+"站");
		for(int i=0;i<endResult.size();i++) {
			//换乘了，显示几号线
			if(endResult.get(i).isExchange()) {
				System.out.print(endResult.get(i).getEnd().getStationName());
				System.out.println();
				String str = new String();
				str = returnExchangeLine(endResult.get(i).getStart(), endResult.get(i).getEnd(), endResult.get(i+1).getEnd());
				System.out.println("->"+str);
			}
			if(i==0) {
				String str = new String();
				str = returnFirstLine(endResult.get(i).getEnd(), endResult.get(i).getStart());
				System.out.println(str);
				System.out.print(endResult.get(i).getStart().getStationName()+"  ");
			}
			
			System.out.print(endResult.get(i).getEnd().getStationName()+"  ");
		}
		
	}
	
	//返回第一条线
	public static String returnFirstLine(StationModel s1,StationModel s2) {
		for(int i=0;i<s1.getLineName().size();i++) {
			if(s2.getLineName().contains(s1.getLineName().get(i))) {
				return s1.getLineName().get(i);
			}
		}
		return null;
	}
	
	public static String returnExchangeLine(StationModel start,StationModel now,StationModel end) {
		List<String> isNowButStart = new ArrayList<String>();
		for(int i=0;i<now.getLineName().size();i++) {
			if(start.getLineName().contains(now.getLineName().get(i))){
				continue;
			}
			else {
				isNowButStart.add(now.getLineName().get(i));
			}
		}
		for(int i=0;i<isNowButStart.size();i++) {
			if(end.getLineName().contains(isNowButStart.get(i))) {
				return isNowButStart.get(i);
			}
		}
		return null;
	}
	
	public static List<ResultModel> findEndResult(String start,String end,List<ResultModel> data) {
		//根据终点开始遍历，直到找到起点，并且判断换乘，这站的前一站和后一站有没有同一条线
		List<ResultModel> result = new ArrayList<ResultModel>();
		String nowend = new String();
		nowend = end;
		int flag=0;
		while(!nowend.equals(start)) {
			for(int i=0;i<data.size();i++) {
				if(data.get(i).getEnd().getStationName().equals(nowend)) {
					result.add(data.get(i));
					nowend = data.get(i).getStart().getStationName();
				}
			}
			if(nowend.equals(start)) {
				flag=1;
			}
		}
		if(flag==1) {
			//返回result前对是否换乘进行判断
			for(int i=1;i<result.size();i++) {
				if(i!=result.size()-1&&isExchange(result.get(i-1).getEnd(), result.get(i+1).getEnd())) {
					result.get(i).setExchange(true);
				}
			}
			return result;
		}
		else
			return null;
	}
	
	public static ResultModel findResult(StationModel s,List<ResultModel> result) {
		for(ResultModel rm:result) {
			if(rm.getEnd().getStationName().equals(s.getStationName())) {
				return rm;
			}
		}
		return null;
	}
	
	public static boolean isExchange(StationModel s1,StationModel s2) {
		int  isExchange = 1;
		for(String ln1:s1.getLineName()) {
			for(String ln2:s2.getLineName()) {
				if(ln1.equals(ln2)) {
					isExchange = 0;
					//没有换乘
					return false;
				}
			}
		}
		//换乘了
		return true;
	}
	
	//寻找起点站
	public static StationModel findStation(String name) {
		StationModel station = new StationModel();
		for(int i=0;i<dataProcessing.lineSet.size();i++) {
			for(int j=0;j<dataProcessing.lineSet.get(i).getStations().size();j++) {
				if(name.equals(dataProcessing.lineSet.get(i).getStations().get(j).getStationName())) {
					station = dataProcessing.lineSet.get(i).getStations().get(j);
					return station;
				}
			}
		}
		return null;
	}
}
