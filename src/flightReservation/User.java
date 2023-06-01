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
import java.time.LocalDate;
import java.util.ArrayList;

public class User {
	
	private String id;
	private String pw;
	private String name;
	private ArrayList<FlightTicket> flightTicketList = new ArrayList<>();

    public User(String id, String pw, String name, ArrayList<FlightTicket> flightTicketList) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.flightTicketList = flightTicketList;
    }


    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getName() {
        return name;
    }

    public ArrayList<FlightTicket> getFlightTicketList() {
        return flightTicketList;
    }

    public void setFlightTicketList(ArrayList<FlightTicket> flightTicketList) {


    }

    public int getMil() {
    	int sum = 0;
    	boolean isIn3months = false;
    	//마일리지 계산
    	try {
			BufferedReader ubr = new BufferedReader(new InputStreamReader(new FileInputStream("./src/user/" + this.id + ".txt"), "UTF-8"));
			String line = ubr.readLine();//첫줄은 필요X
			while ((line = ubr.readLine()) != null) {
				String[] date_str = line.split(" ")[1].split("/");
				if (!isIn3months) {//3개월 이내 날짜를 만나기 전까지
					int year;
					if (Integer.parseInt(date_str[0]) > LocalDate.now().getMonth().getValue()) 
						year=2022;
					else year = 2023;
					LocalDate date = LocalDate.of(year, Integer.parseInt(date_str[0]), Integer.parseInt(date_str[1]));
					if (date.isAfter(LocalDate.now().minusMonths(3))) {//3개월 이내 -> 마일리지 계산 시작
						isIn3months = true;
					}
				}
				line = ubr.readLine();//2 퍼스트 01 02 200000 0 1
				if (isIn3months) {//3개월 이내 날짜를 만나면
					String[] toks_str = line.split(" ");
					int nums = Integer.parseInt(toks_str[0]);
					int[] used = new int[nums];
					for (int i=0; i<nums; i++) {
						used[i] = Integer.parseInt(toks_str[2+nums+1+i]);
					}
					
					int notPayedByMil = 0;//0,1
					int notUsedAsMil = 0;//1
					for (int i=0; i<nums; i++) {
						if (used[i]==0) notPayedByMil++;
						if (used[i]==1) {
							notPayedByMil++;
							notUsedAsMil++;
						}
					}
					
					int total_price = Integer.parseInt(toks_str[2+nums]);
					int each_price = total_price / notPayedByMil;
					double mil = 0.05 * notUsedAsMil * each_price;
					sum += (int) mil;
				}
			}
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return sum;
    }
}
