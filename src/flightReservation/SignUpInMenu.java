package flightReservation;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class SignUpInMenu {

    Scanner scan = new Scanner(System.in);

    public void start() {
        SignUpInMenu menu1 = new SignUpInMenu();

        System.out.println("비행기 예약 프로그램에 오신 것을 환영합니다!");
        menu1.screen();
        menu1.menuinput();
    }

    void screen() {
        System.out.println("[회원가입 및 로그인]");
        System.out.println("1.SignUp");
        System.out.println("2.SignIn");
        System.out.println("3.Exit");
    }

    void menuinput() {

        String input = scan.nextLine();
        String input1 = input.replaceAll("\\s","");
        String input2 = input1.toLowerCase();

        if(input2.equals("1")||input2.equals("1signup")||input2.equals("1.")||input2.equals("1.signup")||input2.equals("signup")) {
            signup();
        }
        else if(input2.equals("2")||input2.equals("2signin")||input2.equals("2.")||input2.equals("2.signin")||input2.equals("signin")) {
            signin();
        }
        else if(input2.equals("3")||input2.equals("3exit")||input2.equals("3.")||input2.equals("3.exit")||input2.equals("exit")) {
            exit();
        }
        else {
            System.out.println("!오류: 잘못된 입력입니다. 다시 입력해주세요.");
            System.out.print("FlightReservation> ");
            menuinput();
        }

    }

    String fileidread(String id) {
        String newid=id;
        File folder = new File("./src/user");
        File[] fileList =folder.listFiles();

        for(File file :fileList) {
            int i=1;
            String line="";
            if(file.isFile()&&file.canRead()) {
                if(file.getName().equals(id+".txt")) {
                    System.out.println("!오류: 이미 존재하는 아이디입니다. 다시 입력해주세요.");
                    System.out.print("FlightReservation> ");
                    newid=fileidread(newid=scan.nextLine());
                }
            }
        }
        return newid;
    }

    void filewrite(String name,String id,String pw) {
        try {
            File folder = new File("./src/user");
            String title = id+".txt";
            File file=new File(folder,title);
            FileWriter fw = new FileWriter(file);
            fw.write(name+" ");
            fw.write(pw+"\n");
            fw.flush();
            fw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    void signup() {

        System.out.println("이름을 입력하세요.");
        System.out.print("FlightReservation> ");
        String name = scan.nextLine();
        while(name.isBlank()||name.trim()!=name||(!Pattern.matches("^[0-9a-zA-z가-힣]*$",name.replaceAll("\\s","")))) {
            System.out.println("!오류 : 이름은 길이가 1 이상인 한글, 영문 대/소문자, 비개행공백열만으로 구성된 문자열이어야 합니다. 또한 첫 문자와 끝 문자는 비개행공백열1이 아니어야 합니다. 다시 입력해주세요.");
            System.out.print("FlightReservation> ");
            name = scan.nextLine();
        }

        System.out.println("아이디를 입력하세요.");
        System.out.print("FlightReservation> ");
        String id = scan.nextLine();
        boolean idcorrect=true;
        while(idcorrect) {
            id=fileidread(id);
            if(!(Pattern.matches("^[0-9a-zA-z]*$",id))||id.length()<5||id.length()>10) {
                System.out.println("!오류 : 아이디는 영문 대/소문자와 숫자로만 이루어진 길이가 5 이상 10 이하인 문자열이어야합니다. 다시 입력해주세요.");
                System.out.print("FlightReservation> ");
                id = scan.nextLine();
            }else
                idcorrect=false;
        }

        System.out.println("비밀번호를 입력하세요.");
        System.out.print("FlightReservation> ");
        String pw = scan.nextLine();
        while(!(Pattern.matches("^[0-9a-zA-z]*$",pw))||pw.length()<8||id.length()>20) {
            System.out.println("!오류 : 비밀번호는 영문 대/소문자와 숫자로만 이루어진 길이가 8이상 20이하인 문자열이어야합니다. 다시 입력해주세요.");
            System.out.print("FlightReservation> ");
            pw = scan.nextLine();
        }

        System.out.println("회원가입이 완료되었습니다.");
        filewrite(name,id,pw);

        screen();
        menuinput();

    }


    void signin() {
        try {
            System.out.println("[로그인]");
            System.out.println("아이디를 입력하세요.");
            System.out.print("FlightReservation> ");
            String id = scan.nextLine();
            File folder = new File("./src/user");
            File[] filelist = folder.listFiles();

            boolean temp=true;
            boolean isID=true;
            String name="";
            String pw="";
            String line="";
            ArrayList<FlightTicket> flightTicketList = new ArrayList<>();

            while(temp) {
                for(File file:filelist) {
                    if(file.isFile()&&file.canRead()) {
                        if(file.getName().equals(id+".txt")) {
                            isID=false;
                            FileReader filereader = new FileReader(file);
                            BufferedReader bufReader = new BufferedReader(filereader);
                            line=bufReader.readLine();
                            String arr[]=line.split("\\s");
                            System.out.println("비밀번호를 입력해주세요.");
                            System.out.print("FlightReservation> ");
                            pw=scan.nextLine();
                            while(!(arr[arr.length-1].equals(pw))) {
                                System.out.println("!오류 : 틀린 비밀번호입니다. 다시 입력해주세요.");
                                System.out.print("FlightReservation> ");
                                pw=scan.nextLine();
                            }
                            temp=false;
                            name=line.substring(0,line.length()-pw.length()-1);
                            Scanner s = new Scanner(file);
                            s.nextLine();
                            while(s.hasNextLine()) {
                                String res1[]=s.nextLine().split("\\s");
                                Flight flight= new Flight(res1[0],res1[1],res1[2],res1[3],res1[4],res1[5]);
                                String res2[]=s.nextLine().split("\\s");
                                FlightTicket flightticket = new FlightTicket(flight,res2[1],Integer.parseInt(res2[0]));
                                flightTicketList.add(flightticket);
                            }
                            s.close();
                            filereader.close();
                            bufReader.close();
                        }
                    }
                }
                if(isID) {
                    System.out.println("!오류 : 등록되지 않은 아이디입니다. 다시 입력해주세요.");
                    System.out.print("FlightReservation> ");
                    id = scan.nextLine();
                }
            }
            System.out.println("로그인 완료!\n");

            User user = new User(id,pw,name,flightTicketList);
            MainMenu mainmenu = new MainMenu(user);
            mainmenu.showMenu();

        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    void exit() {
        System.out.println("비행기 예약 프로그램을 종료합니다.");
        System.exit(0);
    }

}
