package za.ac.bheki97.speech2text.model;

import java.util.Objects;

public class JoinEventDto {

    private String eventKey;
    private String accId;

    public JoinEventDto() {
    }

    public JoinEventDto(String eventKey, String accId) {
        this.eventKey = eventKey;
        this.accId = accId;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinEventDto that = (JoinEventDto) o;
        return eventKey.equals(that.eventKey) && accId.equals(that.accId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventKey, accId);
    }

    @Override
    public String toString() {
        return "JoinEventDto{" +
                "eventKey='" + eventKey + '\'' +
                ", accId='" + accId + '\'' +
                '}';
    }
}
