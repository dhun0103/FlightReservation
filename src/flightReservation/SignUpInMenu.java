package flightReservation;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class SignUpInMenu {
  Scanner scan = new Scanner(System.in);

   
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
         menuinput();
      }
      
   }
   
   String fileidread(String id) {
      String newid=id;
      try {
         File folder = new File("C:\\member\\"); //c드라이브에 member 폴더 생성
         File[] fileList =folder.listFiles();
         
         for(File file :fileList) {
            int i=1;
            String line="";
            if(file.isFile()&&file.canRead()) {
               FileReader filereader = new FileReader(file);
               BufferedReader bufReader = new BufferedReader(filereader);
               while((line=bufReader.readLine())!=null) {
                  if(i%4==2) {
                     if(id.equals(line)) {
                        System.out.println("!오류: 이미 존재하는 아이디입니다. 다시 입력해주세요.");
                        fileidread(newid=scan.nextLine());
                        System.out.println(newid);
                     }
                  }
                  i++;
               }
               
            }
         }
      }catch (FileNotFoundException e) {
         
      }catch(IOException e) {
         System.out.println(e);;
      }
      return newid;
   }
   
   void filewrite(String name,String id,String pw) {
      try {
         File folder = new File("C:\\member\\");
         String title = id+".txt";
         File file=new File(folder,title);
         FileWriter fw = new FileWriter(file);
         fw.write(name+"\r\n");
         fw.write(id+"\r\n");
         fw.write(pw+"\r\n");
         fw.write("null\r\n");
         fw.flush();
         fw.close();
      }catch(Exception e) {
         e.printStackTrace();
      }
   }
   
   void signup() {

         System.out.println("이름을 입력하세요.");
         String name = scan.nextLine();
         while(name.isBlank()||name.trim()!=name||(!Pattern.matches("^[0-9a-zA-z가-힣]*$",name.replaceAll("\\s","")))) {
            System.out.println("!오류 : 이름은 길이가 1 이상인 한글, 영문 대/소문자, 비개행공백열만으로 구성된 문자열이어야 합니다. 또한 첫 문자와 끝 문자는 비개행공백열1이 아니어야 합니다. 다시 입력해주세요.");
            name = scan.nextLine();
         }
         System.out.println("아이디를 입력하세요.");
         String id = scan.nextLine();
         while(!(Pattern.matches("^[0-9a-zA-z]*$",id))||id.length()<5||id.length()>10) {
            System.out.println("!오류 : 아이디는 영문 대/소문자와 숫자로만 이루어진 길이가 5 이상 10 이하인 문자열이어야합니다. 다시 입력해주세요.");
            id = scan.nextLine();
         }
         id=fileidread(id);
         System.out.println("비밀번호를 입력하세요.");
         String pw = scan.nextLine();
         while(!(Pattern.matches("^[0-9a-zA-z]*$",pw))||pw.length()<8||id.length()>20) {
            System.out.println("!오류 : 비밀번호는 영문 대/소문자와 숫자로만 이루어진 길이가 8이상 20이하인 문자열이어야합니다. 다시 입력해주세요.");
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
         String id = scan.nextLine();
         File folder = new File("C:\\member\\");
         File[] filelist = folder.listFiles();
         boolean temp = true;
         boolean isID = true;
         String line1="";
         String line2="";
         
         while(temp) {
            for(File file:filelist) {
               if(file.isFile()&&file.canRead()) {
                  FileReader filereader = new FileReader(file);
                  BufferedReader bufReader = new BufferedReader(filereader);
                  bufReader.readLine();
                  line1=bufReader.readLine();
                  if(line1.equals(id)) {
                     isID = false;
                     System.out.println("비밀번호를 입력해주세요.");
                     String pw=scan.nextLine();
                     line2=bufReader.readLine();
                     while(!(line2.equals(pw))) {
                        System.out.println("!오류 : 틀린 비밀번호입니다. 다시 입력해주세요.");
                        pw=scan.nextLine();
                     }
                     temp=false;
                  }
               }
            }
            if(isID){
               System.out.println("!오류 : 등록되지 않은 아이디입니다. 다시 입력해주세요.");
               id = scan.nextLine();
            }
         }
         
         System.out.println("로그인 완료!\n");
         
      }catch(Exception e) {
         e.printStackTrace();
      }
      
   }
   
   void exit() {
      System.out.println("비행기 예약 프로그램을 종료합니다.");
      System.exit(0);
   }
}
