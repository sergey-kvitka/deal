package com.kvitka.deal.entities;

import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory implements Serializable {
    private ApplicationStatus status;
    private ZonedDateTime time;
    private ChangeType changeType;

    public StatusHistory(ApplicationStatus status, ChangeType changeType) {
        this.status = status;
        this.time = ZonedDateTime.now();
        this.changeType = changeType;
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("status", status.name())
                .put("time", time.toString())
                .put("changeType", changeType.name());
    }

    public static StatusHistory from(JSONObject jsonObject) {
        return new StatusHistory(
                ApplicationStatus.valueOf(jsonObject.getString("status")),
                ZonedDateTime.parse(jsonObject.getString("time")),
                ChangeType.valueOf(jsonObject.getString("changeType"))
        );
    }
}
