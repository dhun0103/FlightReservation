package flightReservation;

import java.util.Arrays;

public class FlightTicket {
    private Flight flight;
    private String clas;
    private int num;
    private String[] seat;

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
}
