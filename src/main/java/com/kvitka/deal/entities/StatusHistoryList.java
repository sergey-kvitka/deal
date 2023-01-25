package com.kvitka.deal.entities;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@ToString
public class StatusHistoryList implements Serializable {
    private String statusHistoryJsonArrayString = new JSONArray().toString();

    public StatusHistoryList(List<StatusHistory> statusHistoryList) {
        setList(statusHistoryList);
    }

    public List<StatusHistory> getList() {
        JSONArray statusHistoryJsonArray = new JSONArray(statusHistoryJsonArrayString);
        int length = statusHistoryJsonArray.length();
        ArrayList<StatusHistory> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(StatusHistory.from(statusHistoryJsonArray.getJSONObject(i)));
        }
        return list;
    }

    public void setList(List<StatusHistory> statusHistoryList) {
        statusHistoryJsonArrayString = new JSONArray(statusHistoryList.stream()
                .map(StatusHistory::toJson)
                .collect(Collectors.toList())).toString();
    }

    public void add(StatusHistory statusHistory) {
        List<StatusHistory> list = getList();
        list.add(statusHistory);
        setList(list);
    }
}
