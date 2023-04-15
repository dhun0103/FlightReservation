package flightReservation;

import java.util.ArrayList;
import java.util.Arrays;

public class FlightTicket {
    private Flight flight;
    private String clas;
    private int num;
    private String[] seat = new String[53];

    FlightTicket() {
    }

    FlightTicket(Flight flight, String clas, int num, String[] seat) {
        this.flight = flight;
        this.clas = clas;
        this.num = num;
        this.seat = seat;
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
