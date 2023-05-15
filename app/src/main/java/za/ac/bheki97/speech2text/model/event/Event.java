package za.ac.bheki97.speech2text.model.event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import za.ac.bheki97.speech2text.model.user.Host;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;

public class Event {

    private String eventKey;

    private Host host;
    private String occasion;
    private String description;
    private String date;
    private List<Guest> guests;

    public Event() {
        guests = new ArrayList<>();
    }

    public Event(String eventKey, Host host, String occasion, String description, String date) {
        this.eventKey = eventKey;
        this.host = host;
        this.occasion = occasion;
        this.description = description;
        this.date = date;
        guests = new ArrayList<>();
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDateTime getLocalDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        return localDateTime;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventKey='" + eventKey + '\'' +
                ", host=" + host.toString() +
                ", occasion='" + occasion + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date.toString() +
                '}';
    }
}
