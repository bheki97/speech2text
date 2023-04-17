package za.ac.bheki97.speech2text.model.translation;

public class TranslationDto {
    private String text;
    private String originLang;
    private String transLang;

    public TranslationDto(String text, String originLang, String transLang) {
        this.text = text;
        this.originLang = originLang;
        this.transLang = transLang;
    }

    public TranslationDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOriginLang() {
        return originLang;
    }

    public void setOriginLang(String originLang) {
        this.originLang = originLang;
    }

    public String getTransLang() {
        return transLang;
    }

    public void setTransLang(String transLang) {
        this.transLang = transLang;
    }

    @Override
    public String toString() {
        return "TranslateDto{" +
                "text='" + text + '\'' +
                ", originLang='" + originLang + '\'' +
                ", transLang='" + transLang + '\'' +
                '}';
    }

}
