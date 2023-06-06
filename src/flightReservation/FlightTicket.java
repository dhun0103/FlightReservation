package flightReservation;

import java.util.Arrays;

public class FlightTicket {
    private Flight flight;
    private String clas;
    private int num;
    private String[] seat;//비행편의 좌석의 예약 여부(0,1)
    private String[] rseat;//실제 좌석 번호
    public int price;
    public int[] used;

    FlightTicket() {
        this.seat = new String[53];
        Arrays.fill(seat, "1");
    }

    FlightTicket(Flight flight, String clas, int num) {
        this.flight = flight;
        this.clas = clas;
        this.num = num;
        this.seat = new String[53];
        Arrays.fill(seat, "1");
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Flight getFlight() {
        return this.flight;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getClas() {
        return this.clas;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public void setSeat(String[] seat) {
        this.seat = seat;
    }

    public String[] getSeat() {
        return this.seat;
    }
    
    public int getPrice() {
    	return this.price;
    }
    
    public void setPrice(int price) {
    	this.price = price;
    }
    
    public void setRseat(String[] rseat) {
    	this.rseat = rseat;
    }
    
    public String[] getRseat() {
    	return this.rseat;
    }
    
}
