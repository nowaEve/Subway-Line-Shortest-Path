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
			System.out.println("���ݵ�ַ·������ȷ");
			ex.printStackTrace();
		}
		System.out.println("ѡ��1.��ѯ·�ߣ�2.��ѯ����յ����·��");
		int option1 = input.nextInt();
		while(option1!=1&&option1!=2) {
			System.out.println("ѡ��1.��ѯ·�ߣ�2.��ѯ����յ����·��");
			option1 = input.nextInt();
		}
		if(option1==1) {
			System.out.println("������·������");
			String name = input.next();
			//��ѯ·��
			searchLineData(name);
		}
		else if(option1==2) {
			System.out.print("��������㣺");
			String start = input.next();
			System.out.print("�������յ㣺");
			String end = input.next();
			//�ж�վ�����
			//��ѯ���·��
			dijkstraToMin(start, end);
		}
		input.close();
//		if(args.length<3) {
//			System.out.println("Usage: java subwayMain.MainUtil \"filePath\" \"a or b\" \"line name\" or \"start\" and \"end\"");
//			System.exit(1);
//		}
//		if(args[1].equals("a")||args[1].equals("b")) {
//			System.out.println("Usage: java subwayMain.MainUtil \"filePath\" \"a or b\" \"line name\" or \"start\" and \"end\"");
//			System.out.println("���������a��b,a:��ѯ��·��\tb:��ѯ����յ�����·��");
//			System.exit(1);
//		}
//		if(args[1].equals("b")&&args.length!=4) {
//			System.out.println("b:��ѯ����յ�����·�� ,�����������յ�");
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
			System.out.print("�õ�����·������");
		}
		else {
			System.out.print(name+": ");
			for(StationModel stm:dataProcessing.lineSet.get(flagnum).getStations()) {
				System.out.print(stm.getStationName()+"  ");
			}
		}
	}
	
	public static void dijkstraToMin(String start,String end) {
		//��Ϊneighbor�ձ�������Ľ��վ�㣬�ȴ���һ����ȷ�������������neighbor����������ɾ��
		List<StationModel> resultStation = new ArrayList<StationModel>();
		//�Ѿ�ȫ���������station��������һ���ͼ��뵽�����������neighbor����ǰһ��List
		List<StationModel> analyzedStation = new ArrayList<StationModel>();
		
		//�����
		List<ResultModel> result = new ArrayList<ResultModel>();
		//���������Ҫ��վ��Ľ��
		List<ResultModel> endResult = new ArrayList<ResultModel>();	
		
		//�ҵ����
		StationModel startStation = new StationModel();
		startStation = findStation(start);
		if(startStation==null) {
			System.out.println("��㲻����");
			System.exit(1);
		}
		StationModel endStation = new StationModel();
		endStation = findStation(end);
		if(endStation==null) {
			System.out.println("�յ㲻����");
			System.exit(1);
		}
		if(start.equals(end)) {
			System.out.print("�Ѿ������յ�"+start);
			System.exit(1);
		}
		//���������
		ResultModel startResult = new ResultModel();
		startResult.setDistance(0);
		startResult.setStart(startStation);
		startResult.setEnd(startStation);
		startResult.setExchange(false);
		
		ResultModel tempResult = new ResultModel();
		StationModel tempStation = new StationModel();
		result.add(startResult);
		resultStation.add(startStation);
		//ͨ��neighbor��վ��������Ӵ���ȷ�����station��get station
		while(!resultStation.isEmpty()) {
			tempStation = resultStation.get(0); //��ǰ�ȴ�������neighbor��station
			//tempResult��Ϊ�Ե�ǰstationΪend��result����ҪѰ�Ҵӵ�ǰ�Ľ������
			tempResult = findResult(tempStation, result);
			
			//�����Ѿ���ȷ���������List�У��Ѿ��������ھ���
			if(analyzedStation.contains(tempStation)) {
				continue;
			}
			
			//����ǰ��tempResult��Ϊ������Ҫ���յ㣬���ڽ�����У������˳�����
			if(tempStation.getStationName().equals(end)) {
				break;
			}
			//����neighbor
			for(int i=0;i<tempStation.getNeighborStationName().size();i++) {
				int flag = 0;
				ResultModel rm = new ResultModel(); //ÿһ��station��ֻ��һ��result����Ϊresult��end
				StationModel stm = new StationModel(); //��station
				stm = tempStation.getNeighborStationName().get(i);
				//�жϸ�station�Ƿ��ڽ�����У��ȽϾ��룬��С�Ļ����滻�������������ȷ����Ļ���˵����ǰ�������ľ���һ��>=�þ��룬�Ѿ�Ϊ��С����
				for(int j=0;j<result.size();j++) {
					if(result.get(j).getEnd().equals(stm)) {
						flag = 1;
						if(tempResult.getDistance()+1<result.get(j).getDistance()) {
							//�����ܷ���
						}
						else {
							break;
						}
					}
				}
				//�ڽ������
				if(flag==1) {
					continue;
				}
				//���ڽ�����У������������������������station�������ȷ���
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
			System.out.println("�յ㲻����");
			System.exit(1);
		}
		int number = endResult.size()+1;
		System.out.println("��"+number+"վ");
		for(int i=0;i<endResult.size();i++) {
			//�����ˣ���ʾ������
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
	
	//���ص�һ����
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
		//�����յ㿪ʼ������ֱ���ҵ���㣬�����жϻ��ˣ���վ��ǰһվ�ͺ�һվ��û��ͬһ����
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
			//����resultǰ���Ƿ񻻳˽����ж�
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
					//û�л���
					return false;
				}
			}
		}
		//������
		return true;
	}
	
	//Ѱ�����վ
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
