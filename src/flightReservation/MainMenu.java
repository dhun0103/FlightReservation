package flightReservation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MainMenu {
	
	private User user;
	
	MainMenu(){}
	
	MainMenu(User user) {
		this.user = user;
	}
	
	BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
	
	public void showMenu() throws IOException {
		System.out.println("[메인 메뉴] 실행할 메뉴를 선택해주세요.");
		System.out.println("1.Reservation");
		System.out.println("2.MyPage");
		System.out.println("3.Exit");
		while (true) {
			System.out.print("FlightReservation> ");
			
			String input = bf.readLine();
			input = input.replaceAll("[\\s\\t\\v]", "");
			input = input.toLowerCase();
//			System.out.println(input);
			
			if (input.equals("1") || input.equals("1reservation") || input.equals("1.") || input.equals("1.reservation") || input.equals("reservation")) {
				makeReservation();
			}
			else if (input.equals("2") || input.equals("2mypage") || input.equals("2.") || input.equals("2.mypage") || input.equals("mypage")) {
				MyPageMenu myPageMenu = new MyPageMenu(this.user);
				//마이페이지 메뉴 출력
			}
			else if (input.equals("3") || input.equals("3exit") || input.equals("3.") || input.equals("3.exit") || input.equals("exit")) {
				System.out.println("비행기 예약 프로그램을 종료합니다.");
				System.exit(0);
			}
			else {
				System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
			}
			
		}
	}
	
	public void makeReservation() throws IOException {
		System.out.println("[예약]");
		Flight flight = new Flight();
				
		//날짜
		flight.setDate(inputDate(flight));
		//출발지
		flight.setDept(inputDeptDest(true,flight));
		//도착지
		flight.setDest(inputDeptDest(false,flight));
		
		//비행편 출력
		printFlights(flight);

		
	}
	
	public void printFlights(Flight flight) {	
		ArrayList<Flight> list = new ArrayList<Flight>();
		try {
			BufferedReader ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("FlightReservation-file_data.txt"),"UTF-8"));
			String line;
			while((line=ffbr.readLine())!=null) {
				if (line.equals(flight.getDate())) {//입력한 날짜와 일치하는 줄을 만나면
					while(!((line=ffbr.readLine())==null || line.equals(""))) {
						String[] info = line.split(" ");
						if (info[1].equals(flight.getDept()) && info[2].equals(flight.getDest())) {
							Flight fl = new Flight(info[0],flight.getDate(),info[1],info[2],info[3],info[4]);
							list.add(fl);
						}
						ffbr.readLine();//좌석 줄은 뛰어넘기
					}
					break;
				}
			}
			
			String[] date = flight.getDate().split("/");
			
			if (list.isEmpty()) {//해당 비행편이 없는 경우
				System.out.printf("%s월 %s일에 %s에서 %s로 가는 비행편이 없습니다.\n",date[0],date[1],flight.getDept(),flight.getDest());
			}else {
				System.out.printf("%s에서 %s로 가는 %s월 %s일 비행편 목록입니다.", flight.getDept(), flight.getDest(), date[0], date[1]);
				System.out.println("\n");
				
				for (Flight fl : list) {
					System.out.printf("%s : %s>%s / %s월 %s일 / %s / %s\n", fl.getId(),fl.getDept(),fl.getDest(),date[0],date[1],fl.getComp(),fl.getDir());
				}
				System.out.println();
			}
			
			ffbr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public String inputDate(Flight flight) throws IOException {
		String res;
		while(true) {
			//날짜 입력
			System.out.println("비행 날짜를 입력해주세요.");
			System.out.print("FlightReservation> ");
			String dateInput = bf.readLine(); 
			//날짜 문법규칙 체크
			if (!(dateInput.matches("\\d{2}-\\d{2}-\\d{2}") || dateInput.matches("\\d{2}/\\d{2}/\\d{2}") 
					|| dateInput.matches("\\d{2}\\.\\d{2}\\.\\d{2}") || dateInput.matches("\\d{6}"))) {
				System.out.println("!오류 : 날짜 입력 형식이 맞지 않습니다. 다시 입력해주세요.");
				continue;
			} 
			//날짜 의미규칙 체크
			dateInput = dateInput.replaceAll("[-/.]", "");//구분자 제거
			try {
				LocalDate date = LocalDate.of(Integer.parseInt("20"+dateInput.substring(0,2)), Integer.parseInt(dateInput.substring(2,4)), Integer.parseInt(dateInput.substring(4,6)));
				LocalDate start = LocalDate.now().minusDays(1);//오늘날짜-1
				LocalDate last = LocalDate.now().plusMonths(2).withDayOfMonth(1);//(현재 달+2)월 1일
				if (!(date.isAfter(start) && date.isBefore(last))) {//범위 밖의 날짜
					System.out.println("!오류 : 잘못된 입력입니다. 현재 날짜 이후 다음 달의 마지막 날 이내의 날짜를 입력해주세요.");
					continue;
				}
				res = date.getMonthValue()+"/"+date.getDayOfMonth();
				return res;
			} catch(DateTimeException e) {//그레고리력에 존재하지 않는 날짜 (ex.23/13/25)
				System.out.println("!오류 : 잘못된 입력입니다. 날짜는 현행 그레고리력에 존재하는 날짜여야 합니다.");
				continue;
			}
		}
	}
	
	public String inputDeptDest(boolean isDept, Flight flight) throws IOException {
		String res;//올바른 입력을 받았을 때 리턴할 출발지 또는 목적지
		loop: while (true) {
			//출발지 및 목적지 입력
			if (isDept) System.out.println("출발지를 입력하세요.");
			else System.out.println("목적지를 입력하세요.");
			System.out.print("FlightReservation> ");
			String input = bf.readLine();
			
			//출발지 및 목적지 문법규칙 체크
			if (input.isEmpty()) {//길이가 0
				System.out.println("!오류 : 출발지와 목적지는 한글과 비개행공백열으로만 이루어진 길이가 1 이상인 문자열입니다. 다시 입력해주세요.");
				continue;
			}
			//한글,비개행공백열로만 이루어져있는지 체크
			input = input.replaceAll("[\\s\\t\\v]", "");//공백 제거
			for (int i=0; i<input.length(); i++) {
				char ch = input.charAt(i);
				if (!((ch>='가' && ch<='힣') || (ch>='ㄱ' && ch<='ㅎ'))) {//한글로만 이루어지지 않은 경우
					System.out.println("!오류 : 출발지와 목적지는 한글과 비개행공백열으로만 이루어진 길이가 1 이상인 문자열입니다. 다시 입력해주세요.");
					continue loop;
				}
			}
			
			//출발지 및 목적지 의미규칙 체크
			if (!isDept) {//목적지의 경우 출발지와 같은지 체크
				if (flight.getDept().equals(input)) {
					System.out.println("!오류 : 목적지가 출발지와 같을 수 없습니다. 다시 입력해주세요.");
					continue;
				}
			}
			if (!isDeptDestExisted(isDept, input, flight)) {//출발지나 목적지가 존재하지 않으면
				if (isDept) System.out.printf("!오류 : %s에서 출발하는 비행편이 없습니다. 다시 입력해주세요.\n",input);
				else System.out.printf("!오류 : %s에 도착하는 비행편이 없습니다. 다시 입력해주세요.\n",input);
				
				continue;
			} else {
				res = input;
			}
			
			return res;
		}
		
	}
	
	public boolean isDeptDestExisted(boolean isDept, String name, Flight flight) {
		boolean isFound = false;
		try {
			BufferedReader ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("FlightReservation-file_data.txt"),"UTF-8"));
			
			String line;
			while((line = ffbr.readLine()) != null) {
				if (line.equals(flight.getDate())) {//파일 내의 해당 날짜 부분 찾음
					while(!((line=ffbr.readLine())==null || line.equals(""))) {
						String[] words = line.split(" ");
						if (isDept) {//출발지를 입력하는 경우
							if (name.equals(words[1])) {//출발지 존재
								isFound = true;
								break;
							}
						} else {//목적지의 경우 출발지, 목적지 둘다 체크 필요
							if (flight.getDept().equals(words[1]) && name.equals(words[2])) {
								isFound = true;
								break;
							}
						}	
						ffbr.readLine();//좌석 줄은 뛰어넘기
					}
				}
			}
			
			ffbr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isFound) return true;
		else return false;
	}
}
