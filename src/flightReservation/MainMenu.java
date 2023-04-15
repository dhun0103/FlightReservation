package flightReservation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainMenu {

	private User user;

	MainMenu(){}

	MainMenu(User user) {
		this.user = user;
	}

    Scanner sc = new Scanner(System.in);
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

    public void enterFlight() {

        FlightTicket flightTicket = new FlightTicket();

        // 비행편
        flightTicket.setFlight(inputFlight(flightTicket));
        // 클래스
        flightTicket.setClas(inputClas(flightTicket));
        // 인원
        flightTicket.setNum(inputNum(flightTicket));
        // 좌석
        flightTicket.setSeat(inputSeat(flightTicket));

    }

    public Flight inputFlight(FlightTicket flightTicket) {
        String res;
        while (true) {
            // 비행편 입력
            System.out.println("비행편을 입력해주세요.");
            System.out.print("FlightReservation> ");
            String input = sc.next();

            // 비행편 동치 확인

            return null;
        }
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
                return res;
            } else if (input.equals("비즈니스") || input.equals("비즈니스클래스") || input.equals("비즈니스 클래스")) {
                res = "비즈니스";
                return res;
            } else if (input.equals("이코노미") || input.equals("이코노미클래스") || input.equals("이코노미 클래스")) {
                res = "이코노미";
                return res;
            } else {
                System.out.println("!오류 : 잘못된입력입니다. 퍼스트 비즈니스 이코노미 중 하나로 다시 입력해주세요.");
            }
        }
    }

    public int inputNum(FlightTicket flightTicket) {
        int res;
        int left_seat = 0;
        for (int i = 1; i <= 52; i++) {
            if (flightTicket.getSeat()[i].equals(1)) left_seat++;
        }
        int filled_seat = 52 - left_seat;

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

    public String[] inputSeat(FlightTicket flightTicket) {
        while (true) {
            // 좌석 입력
            for (int i = 1; i <= 52; i++) {
                String res = Integer.toString(i);
                if (i < 10) res = "0" + res;

                if (i < 5) {
                    if (i % 2 == 1) System.out.print(res + "      "); // 띄어쓰기 6번
                    else System.out.println(res);
                } else if (i < 17) {
                    if (i % 3 == 2) System.out.println(res);
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
            System.out.println("다음 중 선택할 좌석을 입력해주세요. 99로 표시된 좌석은 이미 예약된 좌석입니다.");
            System.out.print(flightTicket.getClas() + " 석의  잔여 좌석 번호는 ");
            if (flightTicket.getClas().equals("퍼스트")) {
                for (int i = 1; i < 5; i++) {
                    if (!flightTicket.getSeat()[i].equals("99")) System.out.print("0" + Integer.toString(i) + ", ");
                }
            } else if (flightTicket.getClas().equals("비즈니스")) {
                for (int i = 5; i < 17; i++) {
                    if (!flightTicket.getSeat()[i].equals("99")) {
                        if (i < 10) System.out.print("0" + Integer.toString(i) + ", ");
                        else System.out.print(i + ", ");
                    }
                }
            } else {
                for (int i = 17; i <= 52; i++) {
                    if (!flightTicket.getSeat()[i].equals("99")) System.out.print(i + ", ");
                }
            }
            System.out.println("입니다.");
            System.out.print("FlightReservation> ");
            String input = sc.next();

            // 문법 규칙이 틀린 경우
            if (!input.matches("[0-9]+")) {
                System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시");
                System.out.println("입력하세요.");
                continue;
            }
            // 문법 규칙은 올바르지만 의미 규칙이 틀린 경우
            int[] arr = new int[53];
            Arrays.fill(arr, 0);
            input = input.replaceAll(" ", "");

            for (int i = 0; i <= flightTicket.getNum() * 2 - 1; i++) {
                String key;

                if (input.substring(i, i + 1).equals('0')) key = input.substring(i + 1, i + 2);
                else key = input.substring(i, i + 2);

                arr[Integer.parseInt(key)]++;
                i += 2;
            }
            int cnt = 0;
            for (int i = 1; i < 53; i++) {
                if (arr[i] >= 2) {
                    cnt++;
                    break;
                }
            }
            if (cnt == 1) {
                System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 선택한 클래스의 여석 번호 중 하나여야 하며, 각각의");
                System.out.println("좌석 번호는 모두 달라야 합니다. 좌석 번호를 다시 입력하세요.");

                continue;
            }

            for (int i = 0; i <= flightTicket.getNum() * 2 - 1; i++) {
                String key;

                if (input.substring(i, i + 1).equals('0')) key = input.substring(i + 1, i + 2);
                else key = input.substring(i, i + 2);

                flightTicket.getSeat()[Integer.parseInt(key)] = "99";
                i += 2;
            }
            System.out.println("해당 비행편 예약이 완료되었습니다. 감사합니다.");
        }
    }
}