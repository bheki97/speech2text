package za.ac.bheki97.speech2text.model.recycler.guest.model;

import java.util.Objects;

public class Speaker extends Guest{

    private String speech;

    public Speaker() {
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Speaker speaker = (Speaker) o;
        return speech.equals(speaker.speech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speech);
    }

    @Override
    public String toString() {
        return "Speaker{" +
                "guestId=" + getGuestId() +
                ", account=" + getAccount().toString() +
                ", joindate='" + getJoindate() +
                "speech='" + speech + '\'' +
                '}';
    }
}
