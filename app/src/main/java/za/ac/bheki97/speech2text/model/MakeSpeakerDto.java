package za.ac.bheki97.speech2text.model;

public class MakeSpeakerDto {

    private int hostId;
    private int guestId;
    private String eventKey;

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public MakeSpeakerDto() {
    }

    @Override
    public String toString() {
        return "MakeSpeakerDto{" +
                "hostId=" + hostId +
                ", guestId=" + guestId +
                ", eventKey='" + eventKey + '\'' +
                '}';
    }
}
