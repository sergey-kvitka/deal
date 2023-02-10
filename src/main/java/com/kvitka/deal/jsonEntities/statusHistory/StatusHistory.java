package com.kvitka.deal.jsonEntities.statusHistory;

import com.kvitka.deal.enums.ApplicationStatus;
import com.kvitka.deal.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class StatusHistory implements Serializable {
    private List<StatusHistoryUnit> statusHistoryUnits;

    public StatusHistory(List<StatusHistoryUnit> statusHistoryUnits) {
        this.statusHistoryUnits = new ArrayList<>(statusHistoryUnits);
    }

    @SuppressWarnings("all")
    public StatusHistory(StatusHistory statusHistory) {
        this(statusHistory.getAll()
                .stream()
                .map(StatusHistoryUnit::new)
                .collect(Collectors.toList()));
    }

    public int size() {
        return statusHistoryUnits.size();
    }

    public StatusHistoryUnit getLast() {
        int size = size();
        if (size == 0) return null;
        return statusHistoryUnits.get(size - 1);
    }

    public StatusHistoryUnit get(int index) {
        return statusHistoryUnits.get(index);
    }

    public void add(StatusHistoryUnit statusHistoryUnit) {
        statusHistoryUnits.add(statusHistoryUnit);
    }

    public List<StatusHistoryUnit> getAll() {
        return new ArrayList<>(statusHistoryUnits);
    }

    public void set(List<StatusHistoryUnit> statusHistoryUnits) {
        this.statusHistoryUnits = new ArrayList<>(statusHistoryUnits);
    }

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class StatusHistoryUnit implements Serializable {
        private ApplicationStatus status;
        private ZonedDateTime time;
        private ChangeType changeType;

        public StatusHistoryUnit(ApplicationStatus status, ChangeType changeType) {
            this.status = status;
            this.time = ZonedDateTime.now();
            this.changeType = changeType;
        }

        public StatusHistoryUnit(StatusHistoryUnit statusHistoryUnit) {
            this.status = statusHistoryUnit.status;
            this.time = statusHistoryUnit.getTime().minusNanos(0);
            this.changeType = statusHistoryUnit.changeType;
        }
    }
}
