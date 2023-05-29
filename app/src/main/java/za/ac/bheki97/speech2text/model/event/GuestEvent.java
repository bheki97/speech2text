package za.ac.bheki97.speech2text.model.event;

import java.util.List;

import za.ac.bheki97.speech2text.model.user.Host;
import za.ac.bheki97.speech2text.recycler.guest.model.Speaker;

public class GuestEvent {

    private Host host;
    private List<Speaker> speakers;
    private String occasion;
    private String description;
    private String date;
    private String eventKey;

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
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
}
