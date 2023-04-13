import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MyPageMenu {

    User user;
    ArrayList<Flight> list = new ArrayList<>();
    ArrayList<FlightTicket> list2 = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);

    public nnewwMyPageMenu(User user) {	
        super();
        this.user = user;
        showMyPage();
        // TODO Auto-generated constructor stub
    }

    void showMyPage(){

        int choice = 0;
        if(user.getFlightTicketList().size()!=0) {
            System.out.println(user.getName()+"회원님 예약 내역이 없습니다.");
            System.out.println("메인 메뉴로 돌아가려면 아무 키나 누르세요.");
            System.out.println("FlightReservation >");
            scan.next();

        }else {
            System.out.println(user.getName()+"회원의 예약 내역입니다.");
            for(FlightTicket ft : user.getFlightTicketList())
                System.out.println(ft+"\n");
            System.out.println("원하는 메뉴를 선택하세요.\n");
            System.out.println("1.예약취소\n2.예약변경\n3.뒤로가기");

            while(choice == 0){
                System.out.print("FlightReservation > ");
                String input = scan.nextLine();

                String result = input.replaceAll(" ", "");
                choice = find(result);
            }
            //입력을 받아서 어떤 메뉴인지 판단하는 코드..(choice를 int 값으로 설정)

            switch(choice) {
                case 1:delete();break;
                case 2:change();break;
                case 3:break;
                default :break;
            }
        }

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
    public void delete() {
        // TODO Auto-generated method stub

        boolean s = false;

        while (!s) {
            System.out.println("취소할 비행편의 이름을 입력하세요.");
            String name = scan.next();
            String filename = user.getName() + ".txt";

            //홍길동 gild1234
            //A20 4/10 인천 로스앤젤레스 A항공 직항
            //2 이코노미 21 22

            Iterator<FlightTicket> it = user.getFlightTicketList().iterator();
            while (it.hasNext()) {
                FlightTicket f = it.next();
                if (f.equals("name")) {  //FlightTicket class에서 equals 정의 필요
                    it.remove();
                    s = true;
                }
            }

        }
    }
    public void change() {
        // TODO Auto-generated method stub


        boolean s = true;
        boolean find = false;
        File newfile = new File("newfile.txt");

        System.out.println("변경할 비행편의 이름을 입력하세요.");
        while (s) {
            System.out.print("FlightReservation > ");
            String fname = scan.next();  //변경할 비행편 이름
            String filename = "홍길동1234.txt";  //사용자 비행예약파일 이름

            try(Scanner scan = new Scanner(new File(filename))){  //파일 내용 읽어오기

                FileWriter fileWriter = new FileWriter(newfile);
                PrintWriter printWriter = new PrintWriter(fileWriter);

                String str = scan.nextLine(); //이름 아이디 읽기.
                printWriter.println(str); // 새 파일에 str 내용 붙여넣기

                while(scan.hasNextLine()) {

                    String flight_str = scan.nextLine(); //비행편 정보 line scan
                    printWriter.println(flight_str);  //그대로 새 파일에 붙여넣기
                    String[] flight = flight_str.split(" "); //비행편 이름 골라내기 위해서

                    String flightTicket_str = scan.nextLine(); //비행기 티켓 정보 line
                    String[] flightTicket = flightTicket_str.split(" "); //인원, 클래스, 좌석번호 구분하기

                    if(flight[0].equals(fname)) { //비행편 이름이 같다면; flight[0]에 비행편 이름 저장되어있음;
                        //좌석 변경해서 저장하기;
                        s = false;
                        find = true;
                        int num = Integer.parseInt(flightTicket[0]); //티켓 인원을 int로 바꿔주기
                        Iterator<FlightTicket> it = user.getFlightTicketList().iterator();
                        while(it.hasNext()){ //다음 line이 있을 때까지
                            FlightTicket f = it.next();
                            if(f.equals("name")){ //수정할 비행편과 같은 전체 비행편 찾기
                                String classname = f.getClas(); // 클래스 얻어오기
                                String newSeat = showSeat(classname, num, f.getSeat()); //좌석현황출력 > 새로운 좌석 입력
                                printWriter.print(num +" "+ classname+" "+newSeat); //수정된 비행기티켓정보 넣어주기
                                System.out.println("예약 변경이 완료되었습니다. 감사합니다.");
                                s = true;
                                break;
                            }
                        }
                    }else {  //내가 찾는 비행편이 아니라면 그냥 그대로 써주기
                        printWriter.println(flightTicket_str);
                    }

                }
                printWriter.close();
            }catch(FileNotFoundException e) {
                System.out.println("예약 파일 확인 불가");
            }catch (IOException e) {
                e.printStackTrace();
            }
            if(!find) { //비행편이 없다면
                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
            }

            //홍길동 gild1234
            //A20 4/10 인천 로스앤젤레스 A항공 직항
            //2 이코노미 21 22
        }
    }

    private String showSeat(String classname, int num, ArrayList<String> seat) {

        String result = "";
        String[] first = {"01","02","03","04"};
        String[] business = {"05","06","07","08","09","10","11","12","13","14","15","16"};
        String[] eco = {"17","18","19","20","21","22","23","24","25","26","27","28","29"
                ,"30","31","32","33","34","35","36","37","38","39"
                ,"40","41","42","43","44","45","46","47","48","49","50","51","52"};

        ArrayList<String> canSeat = new ArrayList<>();
        int seatNum;
        int idx = 0;
        boolean ex = false;

        ///////////////퍼스트 좌석/////////////
        if(classname.equals("퍼스트")){
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "퍼스트 석의 잔여 좌석 번호는 ");
            for(int i=0;i< first.length;i++){
                boolean a = true;
                for(String s : seat){
                    if(s.equals(first[i])){
                        a = false;
                    }
                }
                if(a){
                    System.out.print(first[i]+" ");
                    canSeat.add(first[i]);
                    ex= true;
                }
            }
            if(ex)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSeat, num); //좌석 선택하기

        }

        ///////////////비지니스 좌석/////////////
        else if(classname.equals("비지니스")){
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "비지니스 석의 잔여 좌석 번호는 ");
            for(int i=0;i< business.length;i++){
                boolean a = true;
                for(String s : seat){
                    if(s.equals(business[i])){
                        a = false;
                    }
                }
                if(a){
                    System.out.print(business[i]+" ");
                    canSeat.add(business[i]);
                    ex= true;
                }
            }
            if(ex)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSeat, num); //좌석 선택하기
        }

        ///////////////이코노미 좌석/////////////
        else{
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "이코노미 석의 잔여 좌석 번호는 ");
            for(int i=0;i< eco.length;i++){
                boolean a = true;
                for(String s : seat){
                    if(s.equals(eco[i])){
                        a = false;
                    }
                }
                if(a){
                    System.out.print(eco[i]+" ");
                    canSeat.add(eco[i]);
                    ex= true;
                }
            }
            if(ex)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSeat, num); //좌석 선택하기
        }

        return result;
    }

    //좌석 선택하기
    private String SelectSeat(ArrayList<String> seat, int num) {
        //문법 규칙 조건 아직 안 넣음.
        String[] inputSeatString;

        System.out.print("FlightReservation > ");
        try{
            String inputSeat = scan.nextLine();  //선택할 자리 입력하기
            inputSeat.replaceAll(" ", "");
            String[] inputSeatString = new String[num*2];
            inputSeatString = inputSeat.split("");  //한글자씩 넣어주기.
            int[] newSeat = new int[num*2];
            for(int i=0;i<newSeat.length;i++)
                newSeat[i] = Integer.parseInt(inputSeatString[i]);
        }catch (NumberFormatException e){
            System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시 입력하세요.");
        }


        String result = "";

        int count = 0;
        for(String a : inputSeatString){
            count++;
            result += a;
            if(count%2==0)
                result += " ";
        }
        return result;
    }
}



