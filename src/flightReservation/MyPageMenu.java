import java.io.*;
import java.util.Scanner;

public class nnewwMyPageMenu {

    User user;
    static Scanner scan = new Scanner(System.in);

    public nnewwMyPageMenu(User user) {	//메인에서 로그인한 후에 그 회원에 해당하는 user객체를 생성해서 여러 클래스에서 공유해서 이용하도록 해야할 듯?
        //생성자에서 인자로 user를 받아서 MypageMenu 클래스 내부 user 변수에 넣어주고 이용하거나 아니면 mainmenu에서 showmypage바로 호출?
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
    }
    public void change() {
        // TODO Auto-generated method stub

        boolean s = true;
        boolean find = false;
        File oldfile = new File(user.getName()+user.getId()+".txt");
        File newfile = new File("new"+user.getName()+user.getId()+".txt");

        File oldFlightRe = new File("FlightReservation-file_data.txt");
        File newFlightRe = new File("newFlightReservation-file_data.txt");

        System.out.println("변경할 비행편의 이름을 입력하세요.");
        while (s) {
            System.out.print("FlightReservation > ");
            String fname = scan.next();  //변경할 비행편 이름
            //String filename = "홍길동1234.txt";  //사용자 비행예약파일 이름

            try(Scanner scan = new Scanner(oldfile)){  //파일 내용 읽어오기

                FileWriter fileWriter1 = new FileWriter(newfile);
                PrintWriter printWriter1 = new PrintWriter(fileWriter1);

                String str = scan.nextLine(); //이름 아이디 읽기.
                printWriter1.println(str); // 새 파일에 str 내용 붙여넣기

                while(scan.hasNextLine()) {

                    String flight_str = scan.nextLine(); //비행편 정보 line scan
                    printWriter1.println(flight_str);  //그대로 새 파일에 붙여넣기
                    String[] flight = flight_str.split(" "); //비행편 이름 골라내기 위해서

                    String flightTicket_str = scan.nextLine(); //비행기 티켓 정보 line
                    String[] flightTicket = flightTicket_str.split(" "); //인원, 클래스, 좌석번호 구분하기

                    if(flight[0].equals(fname)) { //비행편 이름이 같다면; flight[0]에 비행편 이름 저장되어있음;
                        //좌석 변경해서 저장하기;
                        s = false;
                        find = true;
                        int num = Integer.parseInt(flightTicket[0]); //티켓 인원을 int로 바꿔주기

                        //전에 예약한 좌석 정보 저장하기
                        String[] exSeat = new String[num];
                        for(int i=0;i<num;i++){
                            exSeat[i] = flightTicket[i+2];
                        }

                        //전체 비행편에서 해당 비행편 좌석 수정해주기
                        Scanner scan2 = new Scanner(oldFlightRe);
                        FileWriter fileWriter2 = new FileWriter(newFlightRe);
                        PrintWriter printWriter2 = new PrintWriter(fileWriter2);
                        while(scan2.hasNextLine()){

                            String str2 = scan2.nextLine();
                            String[] findFlight = str2.split(" ");
                            printWriter2.println(str2);
                            if(findFlight[0].equals(fname)){  //같은 비행편을 찾았으면
                                str2 = scan2.nextLine();
                                String[] seatNum = str2.split(" ");
                                for(int i=0;i<seatNum.length;i++){
                                    for(int j=0;j<exSeat.length;j++){
                                        if(seatNum[i].equals(exSeat[j]))
                                            seatNum[i] = "";  //전체 비행편에서 전에 예약한 좌석 삭제해주기
                                    }
                                }
                                //좌석 새로 수정해서 넣어주기
                                //printWriter1.print();
                                String[] newSeat = new String[num];
                                newSeat = showSeat(flightTicket[1], num, seatNum); //클래스 이름과 인원 인자로 넘겨주기
                                printWriter1.print(flightTicket[0] + " " + flightTicket[1] + " ");

                                for(String a : seatNum){  //삭제한 후 좌석 현황 넣기
                                    if(!a.equals(""))
                                        printWriter2.print(a+" ");
                                }
                                for(String a : newSeat){  //삭제한 후 좌석 현황 넣기
                                    printWriter1.print(a+" ");
                                    printWriter2.print(a+" ");
                                }
                                printWriter2.println();
                            }
                        }
                        printWriter2.close();
                        scan2.close();
                        printWriter1.println();
                    }else {  //내가 찾는 비행편이 아니라면 그냥 그대로 써주기
                        printWriter1.println(flightTicket_str);
                    }

                }
                printWriter1.close();
            }catch(FileNotFoundException e) {
                System.out.println("예약 파일 확인 불가");
            }catch (IOException e) {
                e.printStackTrace();
            }
            if(!find) { //비행편이 없다면
                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
        boolean del1 = oldfile.delete();
        boolean rename1 = newfile.renameTo(oldfile);
        boolean del2 = oldFlightRe.delete();
        boolean rename2 = newFlightRe.renameTo(oldFlightRe);
        //if(del1)
            System.out.println("예약 변경이 완료되었습니다. 감사합니다.");
    }

    private String[] showSeat(String classname, int num, String[] alreadyseat) {

        String[] result = new String[num];
        String[] canSelectSeat;

        String[] first = {"01","02","03","04"};
        String[] business = {"05","06","07","08","09","10","11","12","13","14","15","16"};
        String[] eco = {"17","18","19","20","21","22","23","24","25","26","27","28","29"
                ,"30","31","32","33","34","35","36","37","38","39"
                ,"40","41","42","43","44","45","46","47","48","49","50","51","52"};


        ///////////////퍼스트 좌석/////////////
        if(classname.equals("퍼스트")){
            canSelectSeat = new String[first.length];
            int idx = 0;
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "퍼스트 석의 잔여 좌석 번호는 ");
            boolean all = false;
            for(int i=0;i< first.length;i++){
                boolean seat1 = true; //같은게 없으면 계속 true 유지 -> 좌석 출력
                for(int j=0;j<alreadyseat.length;j++){
                    if(first[i].equals(alreadyseat[j]))
                        seat1 = false; //같은 게 있다면 출력 못하니깐 false
                }
                if(seat1) {
                    all = true; //좌석을 하나라도 출력했으니
                    System.out.print(first[i]+" ");
                    canSelectSeat[idx++] = first[i];
                }
            }
            if(!all)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSelectSeat, num); //좌석 선택하기

        }

        ///////////////비지니스 좌석/////////////
        else if(classname.equals("비지니스")){
            canSelectSeat = new String[business.length];
            int idx = 0;
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "비지니스 석의 잔여 좌석 번호는 ");
            boolean all = false;
            for(int i=0;i< business.length;i++){
                boolean seat1 = true; //같은게 없으면 계속 true 유지 -> 좌석 출력
                for(int j=0;j<alreadyseat.length;j++){
                    if(business[i].equals(alreadyseat[j]))
                        seat1 = false; //같은 게 있다면 출력 못하니깐 false
                }
                if(seat1) {
                    all = true; //좌석을 하나라도 출력했으니
                    System.out.print(business[i]+" ");
                    canSelectSeat[idx++] = business[i];
                }
            }
            if(!all)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSelectSeat, num); //좌석 선택하기
        }

        ///////////////이코노미 좌석/////////////
        else{
            canSelectSeat = new String[eco.length];
            int idx = 0;
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "이코노미 석의 잔여 좌석 번호는 ");
            boolean all = false;
            for(int i=0;i< eco.length;i++){
                boolean seat1 = true; //같은게 없으면 계속 true 유지 -> 좌석 출력
                for(int j=0;j<alreadyseat.length;j++){
                    if(eco[i].equals(alreadyseat[j]))
                        seat1 = false; //같은 게 있다면 출력 못하니깐 false
                }
                if(seat1) {
                    all = true; //좌석을 하나라도 출력했으니
                    System.out.print(eco[i]+" ");
                    canSelectSeat[idx++] = eco[i];
                }
            }
            if(!all)System.out.println("없습니다.");
            else{System.out.println("입니다.");}

            result = SelectSeat(canSelectSeat, num); //좌석 선택하기
        }

        return result;
    }

    //좌석 선택하기
    private String[] SelectSeat(String[] seat, int num) {
        //문법 규칙 조건 아직 안 넣음.
        String[] result = new String[num];
        String[] newSeat = new String[num];
        scan.nextLine();
        while(true) {
            try{
                System.out.print("FlightReservation > ");
                String inputSeat = scan.nextLine();  //선택할 자리 입력하기
                String a = inputSeat.replaceAll(" ", "");
                String[] inputSeatString = a.split("");  //한글자씩 넣어주기.

                for(String s : inputSeatString){ //숫자 아니라면 예외 발생 //문법규칙부합X
                    int err = Integer.parseInt(s);
                }

                if(inputSeatString.length == num*2){  //의미규칙부합X

                    for(int i=0;i<num;i++)
                        newSeat[i] = inputSeatString[i*2]+inputSeatString[i*2+1];

                    int ll = 0;
                    int ii = 0;
                    for(int i=0;i<num;i++){
                        for(int j=i+1;j<num;j++){  //같은 좌석을 여러 번 입력하는 경우 거르기 위함
                            if(newSeat[i].equals(newSeat[j])){
                                ll++;
                            }}
                        for(String r : seat){ //해당 class가 아닌 범위의 좌석을 입력하는 경우 거르기 위함
                            if(r==null)
                                r = " ";
                            if(r.equals(newSeat[i]))
                                ii++;
                        }
                    }
                    if(ll==0 && ii==num) return newSeat;
                    else System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 선택한 클래스의 여석 번호 중 하나여야 하며, " +
                            "각각의 좌석 번호는 모두 달라야 합니다. 좌석 번호를 다시 입력하세요.");
                }
                else{
                    System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 선택한 클래스의 여석 번호 중 하나여야 하며, " +
                            "각각의 좌석 번호는 모두 달라야 합니다. 좌석 번호를 다시 입력하세요.");
                }
            }catch (NumberFormatException e){
                System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시 입력하세요.");
            }
        }
    }
}

