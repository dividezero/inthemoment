package com.monyets.inthemoment.service.dto;

import com.monyets.inthemoment.domain.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by hazlan on 9/3/2017.
 */
//@Data
@EqualsAndHashCode
@ToString
public class RequestResult {
    private Boolean ok;
    private List<Message> messages;
    private Boolean has_more;
    private Boolean is_limited;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Boolean getHas_more() {
        return has_more;
    }

    public void setHas_more(Boolean has_more) {
        this.has_more = has_more;
    }

    public Boolean getIs_limited() {
        return is_limited;
    }

    public void setIs_limited(Boolean is_limited) {
        this.is_limited = is_limited;
    }


}
