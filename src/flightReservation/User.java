package flightReservation;

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

}
