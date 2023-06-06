package flightReservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainMenu {

    private User user;

    MainMenu() {
    }

    MainMenu(User user) {
        this.user = user;
    }

    Scanner sc = new Scanner(System.in);
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));

    public void showMenu() throws IOException {

        while (true) {
            System.out.println("[메인 메뉴] 실행할 메뉴를 선택해주세요.");
            System.out.println("1.Reservation");
            System.out.println("2.MyPage");
            System.out.println("3.Exit");
            System.out.print("FlightReservation> ");

            String input = bf.readLine();
            input = input.replaceAll("[\\s\\t\\v]", "");
            input = input.toLowerCase();
//			System.out.println(input);

            if (input.equals("1") || input.equals("1reservation") || input.equals("1.") || input.equals("1.reservation") || input.equals("reservation")) {
                makeReservation();
            } else if (input.equals("2") || input.equals("2mypage") || input.equals("2.") || input.equals("2.mypage") || input.equals("mypage")) {
                MyPageMenu myPageMenu = new MyPageMenu(this.user);
                //마이페이지 메뉴 출력
            } else if (input.equals("3") || input.equals("3exit") || input.equals("3.") || input.equals("3.exit") || input.equals("exit")) {
                System.out.println("비행기 예약 프로그램을 종료합니다.");
                System.exit(0);
            } else {
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
        flight.setDept(inputDeptDest(true, flight));
        //도착지
        flight.setDest(inputDeptDest(false, flight));

        //비행편 출력
        ArrayList<Flight> flightList = printFlights(flight);
        //비행편 선택
        FlightTicket flightTicket = enterFlight(flightList);//*
        //결제
        pay(flightTicket);//*
    }

    public ArrayList<Flight> printFlights(Flight flight) {
        ArrayList<Flight> list = new ArrayList<Flight>();
        try {
            BufferedReader ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/FlightReservation-file_data.txt"), "UTF-8"));
            String line;
            while ((line = ffbr.readLine()) != null) {
                if (line.equals(flight.getDate())) {//입력한 날짜와 일치하는 줄을 만나면
                    while (!((line = ffbr.readLine()) == null || line.equals(""))) {
                        String[] info = line.split(" ");
                        int[] prices = {Integer.parseInt(info[5]), Integer.parseInt(info[6]), Integer.parseInt(info[7])};
                        if (info[1].equals(flight.getDept()) && info[2].equals(flight.getDest())) {
                            Flight fl = new Flight(info[0], flight.getDate(), info[1], info[2], info[3], info[4], prices);
                            list.add(fl);
                        }
                        ffbr.readLine();//좌석 줄은 뛰어넘기
                    }
                    break;
                }
            }

            String[] date = flight.getDate().split("/");

            if (list.isEmpty()) {//해당 비행편이 없는 경우
                System.out.printf("%s월 %s일에 %s에서 %s로 가는 비행편이 없습니다.\n", date[0], date[1], flight.getDept(), flight.getDest());
            } else {
                System.out.printf("%s에서 %s로 가는 %s월 %s일 비행편 목록입니다.", flight.getDept(), flight.getDest(), date[0], date[1]);
                System.out.println("\n");

                for (Flight fl : list) {
                    System.out.printf("%s : %s>%s / %s월 %s일 / %s / %s\n", fl.getId(), fl.getDept(), fl.getDest(), date[0], date[1], fl.getComp(), fl.getDir());
                }
                System.out.println();
            }

            ffbr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    public String inputDate(Flight flight) throws IOException {
        String res;
        while (true) {
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
                LocalDate date = LocalDate.of(Integer.parseInt("20" + dateInput.substring(0, 2)), Integer.parseInt(dateInput.substring(2, 4)), Integer.parseInt(dateInput.substring(4, 6)));
                LocalDate start = LocalDate.now().minusDays(1);//오늘날짜-1
                LocalDate last = LocalDate.now().plusMonths(2).withDayOfMonth(1);//(현재 달+2)월 1일
                if (!(date.isAfter(start) && date.isBefore(last))) {//범위 밖의 날짜
                    System.out.println("!오류 : 잘못된 입력입니다. 현재 날짜 이후 다음 달의 마지막 날 이내의 날짜를 입력해주세요.");
                    continue;
                }
                res = date.getMonthValue() + "/" + date.getDayOfMonth();
                return res;
            } catch (DateTimeException e) {//그레고리력에 존재하지 않는 날짜 (ex.23/13/25)
                System.out.println("!오류 : 잘못된 입력입니다. 날짜는 현행 그레고리력에 존재하는 날짜여야 합니다.");
                continue;
            }
        }
    }

    public String inputDeptDest(boolean isDept, Flight flight) throws IOException {
        String res;//올바른 입력을 받았을 때 리턴할 출발지 또는 목적지
        loop:
        while (true) {
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
            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);
                if (!((ch >= '가' && ch <= '힣') || (ch >= 'ㄱ' && ch <= 'ㅎ'))) {//한글로만 이루어지지 않은 경우
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
                if (isDept) System.out.printf("!오류 : %s에서 출발하는 비행편이 없습니다. 다시 입력해주세요.\n", input);
                else System.out.printf("!오류 : %s에 도착하는 비행편이 없습니다. 다시 입력해주세요.\n", input);

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
            BufferedReader ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/FlightReservation-file_data.txt"), "UTF-8"));

            String line;
            while ((line = ffbr.readLine()) != null) {
                if (line.equals(flight.getDate())) {//파일 내의 해당 날짜 부분 찾음
                    while (!((line = ffbr.readLine()) == null || line.equals(""))) {
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

    public FlightTicket enterFlight(ArrayList<Flight> flightList) throws IOException {

        FlightTicket flightTicket = new FlightTicket();

        // 비행편
        flightTicket.setFlight(inputFlight(flightTicket, flightList));
        // 클래스
        flightTicket.setClas(inputClas(flightTicket));
        // 인원
        flightTicket.setNum(inputNum(flightTicket));
        // 좌석
        flightTicket.setRseat(inputSeat(flightTicket));
        
        return flightTicket;
    }
    
    public void pay(FlightTicket flightTicket) throws IOException {
    	int[]  used = new int[flightTicket.getNum()];//*
    	Arrays.fill(used,-1);//*
    	flightTicket.used = used;//*
    	StringBuilder sb;//*
    	int mil = this.user.getMil();
    	Flight fl = flightTicket.getFlight();
    	//마일리지와 티켓 한장 값을 비교
    	if (mil >= fl.getPriceByClass(flightTicket.getClas())) {
    		while(true) {
    			System.out.println("마일리지 사용이 가능합니다. 결제 방식을 선택해주세요.");
    			System.out.print("FlightReservation> ");
    			String input = bf.readLine();
    			if (input.equals("카드")) {
    				//카드 결제
    				payWithCard();
    			}else if (input.equals("마일리지")) {
    				sb = payWithMileage(flightTicket, mil);//*
    			}else {
    				System.out.println("!오류 : “카드” 또는 “마일리지”로 입력해주세요.");
    				continue;
    			}
    			break;//카드나 마일리지를 입력한 경우에만
    		}
    	} else {//마일리지 사용 불가
    		//카드 결제
    		payWithCard();
    	}
    	//마일리지 결제 과정 없이 바로 카드 결제한 경우, used 배열 -1 -> 1로 수정 필요 //*
    	for (int i=0; i<flightTicket.used.length; i++) {
    		if (flightTicket.used[i]==-1) 
    			flightTicket.used[i] = 1;
    	}
    	
    	//지불 금액 계산 **
    	int notPayedByMil = 0;
    	for (int i=0; i<flightTicket.used.length; i++) {
    		if (flightTicket.used[i] == 1) notPayedByMil++;
    	}
    	flightTicket.setPrice(flightTicket.getFlight().getPriceByClass(flightTicket.getClas()) * notPayedByMil);
    	
    	//결제 완료 후 파일에 작성 **
        //FlightReservation-file_data 파일에 작성
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/FlightReservation-file_data.txt"), "UTF-8"));
            StringBuilder fr = new StringBuilder();

            String line;
            while ((line = bfr.readLine()) != null) {
                fr.append(line + "\n");//날짜가 쓰여있는 줄
                if (line.equals(flightTicket.getFlight().getDate())) {//일치하는 날짜 발견
                    while (true) {
                        line = bfr.readLine();//비행편
                        if (line == null) break;
                        if (line.equals("")) {
                            fr.append(line + "\n");
                            break;
                        }
                        fr.append(line + "\n");
                        if (line.substring(0, 3).equals(flightTicket.getFlight().getId())) {//해당 비행편 발견
                            line = bfr.readLine();//예약된 좌석이 쓰여있는 줄
                            for (String sn : flightTicket.getRseat()) {
                                line = line + sn + " ";
                            }
                            fr.append(line + "\n");
                        } else {//해당날짜 다른 비행편
                            fr.append(bfr.readLine() + "\n");//좌석이 쓰여있는 줄 그냥 추가
                        }
                    }
                }
            }
            bfr.close();
            //진짜 작성
            BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./src/FlightReservation-file_data.txt"), "UTF-8"));
            bfw.write(fr.toString());
            bfw.flush();
            bfw.close();

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //유저파일에 작성
        StringBuilder uf = new StringBuilder();
        fl = flightTicket.getFlight();
        uf.append(fl.getId() + " " + fl.getDate() + " " + fl.getDept() + " " + fl.getDest() + " " + fl.getComp() + " " + fl.getDir() + "\n");
        uf.append(flightTicket.getNum() + " " + flightTicket.getClas() + " ");
        String[] inputSeats = flightTicket.getRseat();//*
        for (int i = 0; i < inputSeats.length; i++) {
            uf.append(inputSeats[i] + " ");
        }
        uf.append(flightTicket.getPrice() + " ");//가격**
    	for (int i=0; i<flightTicket.used.length; i++) { //마일리지 사용 여부**
    		uf.append(flightTicket.used[i] + " ");
    	}
        uf.append("\n");
        //진짜 작성
        BufferedWriter ubf;
        try {
            ubf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./src/user/" + this.user.getId() + ".txt", true), "UTF-8"));
            ubf.append(uf.toString());
            ubf.flush();
            ubf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        System.out.println("해당 비행편 예약이 완료되었습니다. 감사합니다.");
    }
    
    public StringBuilder payWithMileage(FlightTicket flightTicket, int mileage) {
    	int one_ticket_price = flightTicket.getFlight().getPriceByClass(flightTicket.getClas());
    	int num_available = mileage / one_ticket_price;
    	boolean need_extra_pay = false;
    	int limit;//마일리지로 계산 가능한 금액(티켓가격*n배)
    	if (num_available < flightTicket.getNum()) {//티켓 전부 마일리지로 구매 못하는 경우
    		limit = one_ticket_price * num_available;
    		need_extra_pay = true;
    	} else {//티켓 전부 마일리지로 구매 가능한 경우
    		limit = one_ticket_price * flightTicket.getNum();
    	}
    	
    	int sum = 0;
    	StringBuilder sb = new StringBuilder();
    	try {
			BufferedReader ubr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/user/" + this.user.getId() + ".txt"), "UTF-8"));
			String line = ubr.readLine();//첫줄 스킵
			sb.append(line + "\n");
			while ((line = ubr.readLine()) != null) {
				sb.append(line+"\n");
				String[] date_str = line.split(" ")[1].split("/");
				int year;
				if (Integer.parseInt(date_str[0]) > LocalDate.now().getMonth().getValue()) 
					year=2022;
				else year = 2023;
				LocalDate date = LocalDate.of(year, Integer.parseInt(date_str[0]), Integer.parseInt(date_str[1]));
				line = ubr.readLine();//2 퍼스트 01 02 200000 0 1
				if (date.isAfter(LocalDate.now().minusMonths(3))) {//3개월 이내 -> 마일리지 계산
					String[] toks_str = line.split(" ");
					int nums = Integer.parseInt(toks_str[0]);
					
					int[] used = new int[nums];
					for (int i=0; i<nums; i++) {
						used[i] = Integer.parseInt(toks_str[2+nums+1+i]);
					}
					int notPayedByMil = 0;//0 개수 + 1 개수 (즉 마일리지로 구매X)
					for (int i=0; i<nums; i++) {
						if (used[i]==0 || used[i]==1)
							notPayedByMil++;
					}
					int total_price = Integer.parseInt(toks_str[2+nums]);
					int each_price = total_price / notPayedByMil;
					
					for (int i=0; i<nums; i++) {
						if (used[i]==1) { //마일리지로 전환되지 않은 티켓 -> 마일리지로 전환
							double mil = 0.05 * each_price;
							sum += (int) mil;
							used[i] = 0;//마일리지로 전환됨을 의미
							if (sum >= limit) { //계산 종료
								break;
							}
						}
					}
					
					line = line.substring(0, line.length()-(2*nums));
					for (int i=0; i<nums; i++) {
						line.concat(" "+used[i]);
					}
				}
				sb.append(line+"\n");
			}
			ubr.close();
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (need_extra_pay) {//카드 추가 결제 필요
    		payWithCard();
    	}
    	for (int i=0; i<flightTicket.used.length; i++) { //*
    		if (i<num_available) flightTicket.used[i] = 2;//마일리지로 구매됨
    		else flightTicket.used[i] = 1;
    	}
    	return sb;
    }
    
    public void payWithCard() {
    	
    }

    public Flight inputFlight(FlightTicket flightTicket, ArrayList<Flight> list) {
        String res;
        lp:
        while (true) {
            // 비행편 입력
            System.out.println("비행편을 입력해주세요.");
            System.out.print("FlightReservation> ");
            String input = sc.next();
            input = input.trim();

            //기존에 예약한 비행편인지 확인
            try {
                BufferedReader ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/user/" + this.user.getId() + ".txt"), "UTF-8"));
                String line = ffbr.readLine();//이름 비밀번호 있는 줄
                while ((line = ffbr.readLine()) != null) {
                    if (line.substring(0, 3).equals(input)) {//입력한 비행편이 이미 예약한 비행편
                        System.out.println("사용자가 이미 예약한 비행편입니다.");
                        ffbr.close();
                        continue lp;
                    } else {
                        ffbr.readLine();//좌석 써있는 줄
                    }
                }
                ffbr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // 비행편 동치 확인
            for (Flight fl : list) {
                if (input.equals(fl.getId())) {
                    return fl;
                }
            }

            System.out.println("잘못 입력되었습니다.");
        }
    }

    public void setFlightSeatState(FlightTicket flightTicket) {
        String[] seats = new String[53];
        Arrays.fill(seats, "1");

        BufferedReader ffbr;
        try {
            ffbr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/FlightReservation-file_data.txt"), "UTF-8"));
            String line;
            while ((line = ffbr.readLine()) != null) {
                if (line.equals("") || line.length() < 3) continue;
                if (line.substring(0, 3).equals(flightTicket.getFlight().getId())) {
                    line = ffbr.readLine();
                    if (line == null || line.equals("")) break;//예약좌석이 없으면 -> 아무도 예약 안함, 다 공석
                    String[] reservedSeat = line.split(" ");
                    for (String sn : reservedSeat) {
                        seats[Integer.parseInt(sn)] = "0";
                    }
                    break;
                }
            }
            ffbr.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //계산완료
        flightTicket.setSeat(seats);
    }

    public String inputClas(FlightTicket flightTicket) {
        String res;
        while (true) {
            // 클래스 입력
            System.out.println("예약할 좌석 클래스를 입력해주세요.");
            System.out.print("FlightReservation> ");
            String input = sc.next();

            if (input.equals("퍼스트") || input.equals("퍼스트클래스") || input.equals("퍼스트 클래스")) {
                res = "퍼스트";
            } else if (input.equals("비즈니스") || input.equals("비즈니스클래스") || input.equals("비즈니스 클래스")) {
                res = "비즈니스";
            } else if (input.equals("이코노미") || input.equals("이코노미클래스") || input.equals("이코노미 클래스")) {
                res = "이코노미";
            } else {
                System.out.println("!오류 : 잘못된입력입니다. 퍼스트 비즈니스 이코노미 중 하나로 다시 입력해주세요.");
                continue;
            }

            setFlightSeatState(flightTicket);

            // 클래스 남은 좌석수가 0이면
            int left_seat = 0;
            if (res.equals("퍼스트")) {
                for (int i = 1; i <= 4; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) left_seat++;
                }
            } else if (res.equals("비즈니스")) {
                for (int i = 5; i <= 16; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) left_seat++;
                }
            } else {
                for (int i = 17; i <= 52; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) left_seat++;
                }
            }
            if (left_seat == 0) {
                System.out.println("해당 클래스에 여석이 없습니다.");
                continue;
            }

            return res;
        }
    }

    public int inputNum(FlightTicket flightTicket) {
        int res;
        int left_seat = 0;

        if (flightTicket.getClas().equals("퍼스트")) {
            for (int i = 1; i <= 4; i++) {
                if (flightTicket.getSeat()[i].equals("1")) left_seat++;
            }
        } else if (flightTicket.getClas().equals("비즈니스")) {
            for (int i = 5; i <= 16; i++) {
                if (flightTicket.getSeat()[i].equals("1")) left_seat++;
            }
        } else {//이코노미
            for (int i = 17; i <= 52; i++) {
                if (flightTicket.getSeat()[i].equals("1")) left_seat++;
            }
        }


        while (true) {
            // 인원 입력
            System.out.println(flightTicket.getClas() + " 클래스의 남은 좌석 수는" + left_seat + "석입니다. 예약할 인원을 입력해주세요.");
            System.out.print("FlightReservation> ");
            String input = sc.next();

            // 문법 규칙이 틀린 경우
            if (input.substring(0, 1).equals('0') || !input.matches("[0-9]+|\\d+명")) {
                System.out.println("!오류 : 인원은 숫자로만 입력하거나 숫자”명”으로만 입력해야합니다. 또한,");
                System.out.println("비개행공백열0이 들어갈 수 없습니다. 다시 입력해주세요.");

                continue;
            }

            if (!input.matches("[0-9]+")) input = input.substring(0, input.length() - 1);
            int real_input = Integer.parseInt(input);
            // 문법 규칙은 올바르지만 의미 규칙이 틀린 경우
            if ((real_input >= 1 && real_input <= 5) && real_input <= left_seat) {
                res = real_input;
                return res;
            } else {
                System.out.println("!오류 : 입력받은 수는 잔여 좌석 수 보다 많을 수 없고 1 이상 5 이하의 정수여야합니다.");
                System.out.println("다시 입력해주세요.");

                continue;
            }
        }
    }

    public String[] inputSeat(FlightTicket flightTicket) throws IOException {
        lp: while (true) {
            // 좌석 입력
            for (int i = 1; i <= 52; i++) {
                String res = flightTicket.getSeat()[i].equals("1") ? Integer.toString(i) : "00";
                if (flightTicket.getSeat()[i].equals("1") && i < 10) res = "0" + res;

                if (i < 5) {
                    if (i % 2 == 1) System.out.print(res + "      "); // 띄어쓰기 6번
                    else System.out.println(res);
                } else if (i < 17) {
                    if (i % 3 == 1) System.out.println(res);
                    else System.out.print(res + "   "); // 띄어쓰기 3번
                } else {
                    if (i % 2 == 0) {
                        if (i % 6 == 4) System.out.println(res);
                        else System.out.print(res + " "); // 띄어쓰기 1번
                    } else {
                        System.out.print(res + "|");
                    }
                }
            }
            int[] brr = new int[53];
            Arrays.fill(brr, 0);

            System.out.println("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.");
            System.out.print(flightTicket.getClas() + " 석의  잔여 좌석 번호는 ");
            if (flightTicket.getClas().equals("퍼스트")) {
                for (int i = 1; i < 5; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) {
                        brr[i]++;
                        System.out.print("0" + Integer.toString(i) + ", ");
                    }
                }
            } else if (flightTicket.getClas().equals("비즈니스")) {
                for (int i = 5; i < 17; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) {
                        brr[i]++;
                        if (i < 10) System.out.print("0" + Integer.toString(i) + ", ");
                        else System.out.print(i + ", ");
                    }
                }
            } else {
                for (int i = 17; i <= 52; i++) {
                    if (flightTicket.getSeat()[i].equals("1")) {
                        brr[i]++;
                        System.out.print(i + ", ");
                    }
                }
            }
            System.out.println(" 입니다.");
            System.out.print("FlightReservation> ");
            String input = bf.readLine();
            
            //공백 제거 전 01 0 103 같은 문법규칙 미준수 확인
            String[] splited_input = input.split("[\\s\\t\\v]+");
            for (String el : splited_input) {
            	if (el.length() != 2) {
            		System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시 입력하세요.");
                    continue lp;
            	}
            }
            
            input = input.replaceAll("[\\s\\t\\v]", "");
            
            //입력한 좌석이 입력한 인원이 일치하지 않을때
            if (input.length() != flightTicket.getNum()*2) {
            	System.out.println("!오류 : 좌석 번호는 입력한 인원만큼 입력해야 합니다. 좌석 번호를 다시 입력하세요.");
            	continue;
            }
            
            String[] inputSeats = new String[flightTicket.getNum()];
            int idx=0;
            for (int i=0; i<flightTicket.getNum(); i++) {
            	inputSeats[i] = input.substring(idx,idx+2);
            	idx+=2;
            }
            
            for (String seat : inputSeats) {//입력한 좌석이 문법형식을 준수하지 않을떼
            	if (!seat.matches("[0-9]{2}")) {
            		System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시");
                    System.out.println("입력하세요.");
                    continue lp;
            	}
            }
            
            //입력한 좌석이 여석인지 확인
            String[] flightSeats = flightTicket.getSeat();
            for (String seat : inputSeats) {
            	if (flightSeats[Integer.parseInt(seat)] == "0") {
            		System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 선택한 클래스의 여석 번호 중 하나여야 합니다. 좌석 번호를 다시 입력하세요.");
                    continue lp;
            	}
            }
            //입력한 좌석 중 서로 같은 것이 있는 경우
            if (inputSeats.length > 1) {
            	for (int i=0; i<inputSeats.length-1; i++) {
            		for (int j=i+1; j<inputSeats.length; j++) {
            			if (inputSeats[i].equals(inputSeats[j])) {
            				System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 모두 달라야 합니다. 좌석 번호를 다시 입력하세요.");
            				continue lp;
            			}
            		}
            	}
            }

            
            return inputSeats;
        }
    }
}
