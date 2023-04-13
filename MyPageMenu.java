import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MyPageMenu {

	User user;    //마이페이지 내부에서 이용할 user객체

	static Scanner scan = new Scanner(System.in);

	public MyPageMenu(User user) {
		this.user = user;
		showMyPage();
	}

	void showMyPage(){

		int choice = 0;
		//while(choice != 3) {
		if (user.getFlightTicketList().size() == 0) {
			//choice = 3;
			System.out.println(user.getName() + "회원님 예약 내역이 없습니다.");
			System.out.println("메인 메뉴로 돌아가려면 아무 키나 누르세요.");
			System.out.print("FlightReservation >");
			scan.next();
		} else {
			System.out.println(user.getName() + "회원의 예약 내역입니다.");
			for (FlightTicket ft : user.getFlightTicketList())
				System.out.println(ft + "\n");
			System.out.println("원하는 메뉴를 선택하세요.\n");
			System.out.println("1.예약취소\n2.예약변경\n3.뒤로가기");

			while (choice == 0) {
				System.out.print("FlightReservation >");
				String input = scan.nextLine();

				String result = input.replaceAll(" ", "");
				find(result);
			}
			switch (choice) {
				case 1:
					delete();
					break;
				case 2:
					change();
					break;
				case 3:
					break;
				default:
					break;
			}
		}
		//}
	}

	public int find(String result){

		if(result.equals("1")||result.equals("1.")||result.equals("1.예약취소")||result.equals("1예약취소")||result.equals("예약취소"))
			return 1;
		else if(result.equals("2")||result.equals("2.")||result.equals("2.예약변경")||result.equals("2예약변경")||result.equals("예약변경"))
			return 2;
		else if(result.equals("3")||result.equals("3.")||result.equals("3.뒤로가기")||result.equals("3뒤로가기")||result.equals("뒤로가기"))
			return 3;
		else
			return 0;
	}

	public void delete() { //user의 FlightTicketList에서 해당 비행편을 remove하기만 함 파일 처리는 안함
		FlightTicket ticket = null;
		System.out.println("[예약 취소]");
		System.out.println();
		System.out.println("취소할 비행편의 이름을 입력하세요.");

		while(ticket==null) {
			System.out.print("FlightReservation >");
			String input = scan.nextLine();
			input = input.replaceAll(" ", "");
			for(FlightTicket ft : user.getFlightTicketList()){
				if(input.equals(ft.getFlight().getId()))
					ticket = ft;
			}
			if(ticket==null){
				System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
			}
		}

		System.out.println(ticket.getFlight().getId()+" 비행편 예약을 정말로 취소하시겠습니까?(예/아니오)");
		while(true){
			System.out.print("FlightReservation >");
			String input = scan.nextLine();
			input = input.trim();
			if(input.equals("예")){
				String[] snum = new String[ticket.getSeat().size()]; //삭제하고싶은 좌석의 번호를 배열에 저장
				for(String s : ticket.getSeat()){
					int i =0;
					snum[i]=s;
				}
				int j=0;
				String dummy = "";
				
				//비행편 파일 수정
				try {
					BufferedReader br = new BufferedReader(new FileReader("FlightReservation-file_data.txt"));
					String line = null;

					while((line = br.readLine()) != null) {    //파일 끝까지 한 줄씩 읽기
						if (line.equals(ticket.getFlight().getDate())) {    //취소할 비행편의 날짜인지 비교
							dummy += (line + "\r\n");    //날짜를 dummy에 추가
							while(!((line=br.readLine())==null || line.equals(""))) {    //해당 날짜의 비행편들을 읽기
								String[] info = line.split(" ");
								if(info[0].equals(ticket.getFlight().getId())){    //해당하는 비행편의 id 찾기
									String seats = null;
									seats = br.readLine();
									String[] seat = seats.split(" ");
									for(int i=0;i<seat.length;i++){
										if(j< snum.length && seat[i].equals(snum[j])){
											seat[i]=null;
											j++;
										}
									}
									seats = "";
									for(int i=0;i<seat.length;i++){
										if(seat[i]!=null)
											seats += seat[i]+" ";
									}
									dummy += (line + "\r\n");    //비행편 정보 dummy에 추가
									dummy += (seats + "\r\n");    //예약된 좌석 번호들 dummy에 추가
								}else {    //해당하는 비행편이 아닌 경우 수정하지 않고 dummy에 추가
									dummy += (line + "\r\n");
									line = br.readLine();
									dummy += (line + "\r\n");
								}
							}
							dummy += "\r\n";
						}else {    //해당 날짜가 아닌 줄들은 수정하지 않고 dummy에 추가
							dummy += (line + "\r\n");
						}
					}
					FileWriter fw = new FileWriter("FlightReservation-file_data.txt");
					fw.write(dummy);
					br.close();
					fw.close();
					//System.out.println("수정완료");

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				//사용자 파일 수정
				user.getFlightTicketList().remove(ticket);
				System.out.println(ticket.getFlight().getId()+" 비행편 예약을 취소했습니다.");
				return;
			}else if(input.equals("아니오")) {
				return;
			}else{
				System.out.println(("!오류 : 예 또는 아니오로 입력해주세요."));
			}
		}
	}

	public void change() {
		// TODO Auto-generated method stub

	}
}
