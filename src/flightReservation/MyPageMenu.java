package flightReservation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MyPageMenu {

    User user;

    static Scanner scan = new Scanner(System.in);
    String flightfile = "./src/FlightReservation-file_data.txt";	//비행편 파일 이름
    String userfile;	//사용자 파일 이름

    public MyPageMenu(User user) {
        this.user = user;
        userfile = "./src/user/"+user.getId()+".txt";
        showMyPage();
    }

    void showMyPage(){
        int choice = 0;
        int mileage = 0;
        //mileage = user.getMil();

        while(choice != 3) {	//사용자가 3.뒤로가기를 누르거나 예약 내역이 없는 경우가 아니면 마이페이지를 반복해서 출력

            String dummy = "";

            try(Scanner scan = new Scanner(new File(userfile))){
                scan.nextLine();	//이름 비번 읽기
                while(scan.hasNextLine()) {		//예약 정보 전부 읽어서 dummy에 저장
                    String str = scan.nextLine();
                    String[] tmp = str.split(" ");
                    str = tmp[0]+" : "+tmp[2]+">"+tmp[3]+" / ";
                    tmp[1]=tmp[1].replaceAll("/", "월 ");
                    tmp[1]+="일";
                    str +=tmp[1]+" / "+tmp[4]+" / "+tmp[5];
                    dummy += str +"\n";
                    String str2 = scan.nextLine();
                    String[] tmp2 = str2.split(" ");
                    int num = Integer.parseInt(tmp2[0]);
                    str2 = "인원 : "+tmp2[0]+"명 / 좌석 : ";
                    for(int i=2;i<num+2;i++) {
                        str2 += tmp2[i]+" ";
                    }
                    dummy += str2 +"\n";
                }
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }

            if (dummy.isBlank()) {	//dummy가 비어있는 경우(예약 정보가 없음)
                choice = 3;
                System.out.println("\n"+user.getName() + "회원님 예약 내역이 없습니다.");
                System.out.println("마일리지 : "+user.getMil());
                System.out.println("\n메인 메뉴로 돌아가려면 엔터 키를 누르세요.");
                System.out.print("FlightReservation >");
                scan.nextLine();
            } else {
                choice = 0 ;
                System.out.println("\n"+user.getName() + "회원의 예약 내역입니다.\n");
                System.out.println(dummy);
                System.out.println("마일리지 : "+user.getMil());
                System.out.println("\n원하는 메뉴를 선택하세요.\n");
                System.out.println("1.예약취소\n2.예약변경\n3.뒤로가기");

                while (choice == 0) {
                    System.out.print("FlightReservation >");
                    String input = scan.nextLine();

                    String result = input.replaceAll(" ", "");
                    choice = find(result);	//그냥 find(result)만 썼었음

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
                            System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
                            break;
                    }
                }
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

        System.out.println("[예약 취소]");
        System.out.println();
        System.out.println("취소할 비행편의 이름을 입력하세요.");

        boolean exist = false;	//사용자가 입력한 비행편 이름이 user파일에 존재하는지 여부
        while(!exist) {
            System.out.print("FlightReservation >");
            String input = scan.nextLine();
            input = input.trim();

            String dummy = "";

            //취소할 티켓의 정보를 저장할 변수들
            String fname = null;
            String date = null;
            int num = 0;
            String[] seats = null;
            int[] used = null;
            String[] deleteSeat = null;
            //

            try(Scanner s = new Scanner(new File(userfile))){
                dummy += s.nextLine()+"\r\n";	//이름 비번 읽어서 dummy에 저장

                while(s.hasNextLine()) {
                    String flight_str = s.nextLine();
                    String[] flight = flight_str.split(" ");
                    String flightTicket_str;

                    if(input.equals(flight[0])) {
                        exist = true;
                        fname = flight[0];
                        flightTicket_str = s.nextLine();
                        String[] flightTicket = flightTicket_str.split(" ");
                        num = Integer.parseInt(flightTicket[0]);
                        seats = new String[num];
                        int j=0;
                        for(int i=2;i<2+num;i++) {
                            seats[j]=flightTicket[i];
                            j++;
                        }
                        j=0;
                        used = new int[num];
                        for(int i=3+num;i<3+2*num;i++) {
                            used[j]=Integer.parseInt(flightTicket[i]);
                            j++;
                        }
                        date = flight[1];

                        deleteSeat = showDelete(fname,seats,used);

                        if(deleteSeat == null)
                            return;

                        int[] usedCount = {0,0,0};
                        for(int i=0;i<used.length;i++){
                            if(used[i] == 0)
                                usedCount[0]++;
                            else if(used[i] == 1)
                                usedCount[1]++;
                            else
                                usedCount[2]++;
                        }
                        int ChangeMoneyNum = Integer.parseInt(flightTicket[2+num]) - (Integer.parseInt(flightTicket[2+num]) / (num-usedCount[2]) * usedCount[1]);
                        String money = String.valueOf(ChangeMoneyNum);
                        System.out.println("money: "+flightTicket[2+num]);
                        if(deleteSeat.length != num) {	//부분 취소인 경우
                            flightTicket_str = num-deleteSeat.length+" ";
                            flightTicket_str += flightTicket[1]+" ";

                            List<String> list = new ArrayList<>(Arrays.asList(seats));
                            for(int i=0;i<deleteSeat.length;i++) {
                                list.remove(deleteSeat[i]);
                            }
                            seats = list.toArray(new String[list.size()]);
                            for(int i=0;i<seats.length;i++) {
                                flightTicket_str += seats[i]+" ";
                            }
                            flightTicket_str += money+" ";
                            for(int i=0;i<used.length;i++) {
                                if(used[i]==0)
                                    flightTicket_str += used[i]+" ";
                            }

                            dummy += flight_str+"\r\n";
                            dummy += flightTicket_str+"\r\n";
                        }

                    }else {
                        flightTicket_str = s.nextLine();
                        dummy += flight_str+"\r\n";
                        dummy += flightTicket_str+"\r\n";
                    }
                }
            }catch(FileNotFoundException e) {
                e.printStackTrace();
            }

            if(exist) {
                //user파일 새로쓰기
                try {
                    FileWriter fw = new FileWriter(userfile);
                    fw.write(dummy);
                    fw.close();
                    System.out.println("사용자 파일 수정완료");
                }catch (IOException e) {
                    e.printStackTrace();
                }

                //비행편 파일 수정
                try {
                    BufferedReader br = new BufferedReader(new FileReader(flightfile));
                    String line = null;
                    String dummy2 = "";

                    while((line = br.readLine()) != null) {    //파일 끝까지 한 줄씩 읽기
                        if (line.equals(date)) {    //취소할 비행편의 날짜인지 비교
                            dummy2 += (line + "\r\n");    //날짜를 dummy에 추가
                            while(!((line=br.readLine())==null || line.equals(""))) {    //해당 날짜의 비행편들을 읽기
                                String[] info = line.split(" ");
                                if(info[0].equals(fname)){    //해당하는 비행편의 id 찾기
                                    String seatstr = br.readLine();
                                    String[] seat = seatstr.split(" ");
                                    int j=0;
                                    for(int i=0;i<seat.length;i++){
                                        if(j<deleteSeat.length && seat[i].equals(deleteSeat[j])){
                                            seat[i]=null;
                                            j++;
                                        }
                                    }
                                    seatstr = "";
                                    for(int i=0;i<seat.length;i++){
                                        if(seat[i]!=null)
                                            seatstr += seat[i]+" ";
                                    }
                                    dummy2 += (line + "\r\n");    //비행편 정보 dummy에 추가
                                    dummy2 += (seatstr + "\r\n");    //예약된 좌석 번호들 dummy에 추가
                                }else {    //해당하는 비행편이 아닌 경우 수정하지 않고 dummy에 추가
                                    dummy2 += (line + "\r\n");
                                    line = br.readLine();
                                    dummy2 += (line + "\r\n");
                                }
                            }
                            dummy2 += "\r\n";
                        }else {    //해당 날짜가 아닌 줄들은 수정하지 않고 dummy에 추가
                            dummy2 += (line + "\r\n");
                        }
                    }
                    FileWriter fw = new FileWriter(flightfile);
                    fw.write(dummy2);
                    br.close();
                    fw.close();
                    System.out.println("비행편 파일 수정완료");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else {
                System.out.println("!오류 : 잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

    public String[] showDelete(String fname, String[] seat, int[] used) {
        int[] usedCount = {0,0,0};

        //0이면 마일리지 사용됨, 1이면 마일리지 사용 가능, 2면 마일리지로 구매한 티켓
        //현금 결제 : 0이나 1이었던 좌석
        //0인 좌석은 취소 불가하니깐 1이었던 취소하면 총 지불가격 수정.
        for(int i=0;i<used.length;i++){
            if(used[i] == 0)
                usedCount[0]++;
            else if(used[i] == 1)
                usedCount[1]++;
            else
                usedCount[2]++;
        }

        int deleteCount = used.length - usedCount[0];
        String[] deleteSeat = new String[deleteCount];

        String ans;
        if(used.length == usedCount[0]){ //전부 다 0인 경우(취소 불가)
            System.out.println(fname + " 비행편은 취소할 수 없습니다.\n");
            return null;
        }//else if (deleteCount>0 && usedCount[2]>0){
        else if(usedCount[0]>0) {	//0이 하나 이상 포함된 경우(부분 취소일 때)

            System.out.println(fname+" 비행편에서 "+deleteCount+" 좌석만 취소할 수 있습니다. 취소할 좌석을 입력해주세요.");

            while(true) {
                try{
                    System.out.print("FlightReservation > ");
                    String inputSeat = scan.nextLine();  //선택할 자리 입력하기
                    String a = inputSeat.replaceAll(" ", ""); //공백 제거
                    String[] inputSeatString = a.split("");  //한글자씩 넣어주기.

                    for(String s : inputSeatString){ //숫자 아니라면 예외 발생 //문법규칙부합X
                        int err = Integer.parseInt(s);
                    }

                    if(inputSeatString.length == deleteCount*2){  //의미규칙부합X
                        //좌석 수를 알맞게 입력했다면
                        for(int i=0;i<deleteCount;i++)
                            deleteSeat[i] = inputSeatString[i*2]+inputSeatString[i*2+1];
                        //좌석 번호를 String으로 newSeat에 저장하기
                        int ll = 0;
                        int ii = 0;
                        for(int i=0;i<deleteCount;i++){
                            for(int j=i+1;j<deleteCount;j++){  //같은 좌석을 여러 번 입력하는 경우 거르기 위함
                                if(deleteSeat[i].equals(deleteSeat[j])){
                                    ll++;
                                }}
                            for(String r : seat){ //기존 예약 좌석이 아닌 범위의 좌석을 입력하는 경우 거르기 위함
                                if(r==null)
                                    r = " ";
                                if(r.equals(deleteSeat[i]))
                                    ii++;
                            }
                        }
                        if(ll==0 && ii==deleteCount) break;//return deleteSeat;
                        else System.out.println("!오류 : 입력 받은 각각의 좌석 번호는 예약한 좌석의 여석 번호 중 하나여야 하며, " +
                                "각각의 좌석 번호는 모두 달라야 합니다. 좌석 번호를 다시 입력하세요.");
                    }
                    else{
                        System.out.println("!오류 : "+deleteCount+" 좌석만 취소할 수 있습니다. 좌석 번호를 다시 입력하세요.");
                    }
                }catch (NumberFormatException e){
                    System.out.println("!오류 : 좌석 번호는 0또는 자연수로 이루어진 두 개의 숫자여야합니다. 좌석 번호를 다시 입력하세요.");
                }
            }

            if(usedCount[2]>0){
                System.out.println("A20 비행편 예약시 사용된 마일리지는 환급되지 않습니다.");
            }
            while(true){
                //System.out.println("A20 비행편 예약시 사용된 마일리지는 환급되지 않습니다.");
                System.out.println("정말로 취소하시겠습니까?(예/아니오)");
                System.out.print("FlightReservation >");
                ans = scan.nextLine();
                ans = ans.trim();
                if(ans.equals("예")){
                    break;
                }else if(ans.equals("아니오")) {
                    return null;
                }else {
                    System.out.println(("!오류 : 예 또는 아니오로 입력해주세요."));
                }
            }
            return deleteSeat;

        }else{	//0이 포함되지 않은 경우(전체 취소 가능할 때)
            if(usedCount[2]>0){
                System.out.println("A20 비행편 예약시 사용된 마일리지는 환급되지 않습니다.");
            }
            while(true){
                System.out.println("정말로 취소하시겠습니까?(예/아니오)");
                System.out.print("FlightReservation >");
                ans = scan.nextLine();
                ans = ans.trim();
                if(ans.equals("예")){
                    break;
                }else if(ans.equals("아니오")) {
                    return null;
                }else {
                    System.out.println(("!오류 : 예 또는 아니오로 입력해주세요."));
                }
            }
            return seat;
        }

    }

    public void change() {
        // TODO Auto-generated method stub

        boolean s = true;
        boolean find = false;
        File oldfile = new File(userfile);
        File newfile = new File( "./src/user/new"+user.getId()+".txt");

        File oldFlightRe = new File("./src/FlightReservation-file_data.txt");
        File newFlightRe = new File("./src/newFlightReservation-file_data.txt");

        System.out.println("변경할 비행편의 이름을 입력하세요.");
        while (s) {
            System.out.print("FlightReservation > ");
            String fname = scan.nextLine();  //변경할 비행편 이름
            fname = fname.trim();

            try(Scanner scan1 = new Scanner(oldfile)){  //파일 내용 읽어오기

                FileWriter fileWriter1 = new FileWriter(newfile);
                PrintWriter printWriter1 = new PrintWriter(fileWriter1);

                String str = scan1.nextLine(); //이름 비번 읽기.
                printWriter1.println(str); // 새 파일에 str 내용 붙여넣기

                while(scan1.hasNextLine()) {

                    String flight_str = scan1.nextLine(); //비행편 정보 line scan
                    printWriter1.println(flight_str);  //그대로 새 파일에 붙여넣기
                    String[] flight = flight_str.split(" "); //비행편 이름 골라내기 위해서

                    String flightTicket_str = scan1.nextLine(); //비행기 티켓 정보 line
                    String[] flightTicket = flightTicket_str.split(" "); //인원, 클래스, 좌석번호, 가격, used 구분하기

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

                            if(findFlight[0].equals(fname) ){  //같은 비행편을 찾았으면
                                str2 = scan2.nextLine();
                                String[] seatNum = str2.split(" ");
                                for(int i=0;i<seatNum.length;i++){
                                    for(int j=0;j<exSeat.length;j++){
                                        if(seatNum[i].equals(exSeat[j]))
                                            seatNum[i] = "";  //전체 비행편에서 전에 예약한 좌석 삭제해주기
                                    }
                                }
                                //좌석 새로 수정해서 넣어주기
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
                                printWriter1.print(flightTicket[2+num]+ " ");
                                for(int i=1;i<=num;i++){
                                    printWriter1.print(flightTicket[2+num+i]+ " ");
                                }
                            }
                        }
                        fileWriter2.close();
                        printWriter2.close();
                        scan2.close();
                        printWriter1.println();
                    }else {  //내가 찾는 비행편이 아니라면 그냥 그대로 써주기
                        printWriter1.println(flightTicket_str);
                    }

                }
                fileWriter1.close();
                printWriter1.close();
                scan1.close();
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


        ///////좌석 출력하기
        boolean h = true;
        int hnum = 0;
        for(int i=0;i< first.length;i++){
            for(int j=0; j< alreadyseat.length; j++){
                if(first[i].equals(alreadyseat[j]))
                    h = false;
            }
            if(h==true){
                System.out.print(first[i]+"            ");
            }
            else
                System.out.print("00            ");
            hnum++;
            h = true;
            if(hnum%2==0)
                System.out.println();
        }
        System.out.println();

        hnum = 0;
        for(int i=0;i< business.length;i++){
            for(int j=0; j< alreadyseat.length; j++){
                if(business[i].equals(alreadyseat[j]))
                    h = false;
            }
            if(h==true){
                System.out.print(business[i]+"     ");
            }
            else
                System.out.print("00     ");
            hnum++;
            h = true;
            if(hnum%3==0)
                System.out.println();
        }
        System.out.println();

        hnum = 0;
        int hSunNum = 0;
        for(int i=0;i< eco.length;i++){
            for(int j=0; j< alreadyseat.length; j++){
                if(eco[i].equals(alreadyseat[j]))
                    h = false;
            }
            if(h==true){
                System.out.print(eco[i]+"");
            }
            else
                System.out.print("00");
            hnum++;
            h = true;
            if(hnum%2==0)
                System.out.print(" ");
            else
                System.out.print("|");
            if(hnum%6==0)
                System.out.println();
        }
        System.out.println();

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

        ///////////////비즈니스 좌석/////////////
        else if(classname.equals("비즈니스")){
            canSelectSeat = new String[business.length];
            int idx = 0;
            System.out.print("다음 중 선택할 좌석을 입력해주세요. 00으로 표시된 좌석은 이미 예약된 좌석입니다.\n" +
                    "비즈니스 석의 잔여 좌석 번호는 ");
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
        //scan.nextLine();
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
