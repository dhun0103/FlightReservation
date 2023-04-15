package flightReservation;

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