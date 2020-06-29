package nc.opt.springkafka.dto;

public class MessageDTO {

    private Long id;

    private String subject;

    private String author;

    private String body;

    private String recipient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}
