package cn.ck.security.event;

/**
 * @author chengkun
 * @since 2019/1/23 20:44
 */
public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
