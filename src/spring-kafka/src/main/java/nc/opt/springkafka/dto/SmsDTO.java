package nc.opt.springkafka.dto;

public class SmsDTO {

    private String phoneNumberEmitter;

    private String phoneNumberReceiver;

    private String message;

    public String getPhoneNumberEmitter() {
        return phoneNumberEmitter;
    }

    public void setPhoneNumberEmitter(String phoneNumberEmitter) {
        this.phoneNumberEmitter = phoneNumberEmitter;
    }

    public String getPhoneNumberReceiver() {
        return phoneNumberReceiver;
    }

    public void setPhoneNumberReceiver(String phoneNumberReceiver) {
        this.phoneNumberReceiver = phoneNumberReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SmsDTO{" +
                "phoneNumberEmitter=" + phoneNumberEmitter +
                ", phoneNumberReceiver=" + phoneNumberReceiver +
                ", message='" + message + '\'' +
                '}';
    }
}
