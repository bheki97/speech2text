package za.ac.bheki97.speech2text.model.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import za.ac.bheki97.speech2text.model.user.Host;
import za.ac.bheki97.speech2text.recycler.guest.model.Guest;

public class Event implements Serializable {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private String eventKey;
    private Host host;
    private String occasion;
    private String description;
    private String date;
    private List<Guest> guests;

    public Event() {
        guests = new ArrayList<>();
    }
    public Event(Event event){
        this.eventKey = event.getEventKey();
        this.host = event.getHost();
        this.occasion = event.getOccasion();
        this.description = event.getDescription();
        this.date = event.getDate();
        guests = event.getGuests();
    }

    public Event(String eventKey, Host host, String occasion, String description, String date) {
        this.eventKey = eventKey;
        this.host = host;
        this.occasion = occasion;
        this.description = description;
        this.date = date;
        guests = new ArrayList<>();
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
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

    public void setLocalDateTime(LocalDateTime localDateTime){
        this.date = localDateTime.format(FORMATTER);
    }

    public LocalDateTime getLocalDateTime(){

        LocalDateTime localDateTime = LocalDateTime.parse(date, FORMATTER);
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventKey.equals(event.eventKey) && host.equals(event.host) && occasion.equals(event.occasion) && description.equals(event.description) && date.equals(event.date) && guests.equals(event.guests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventKey, host, occasion, description, date, guests);
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
