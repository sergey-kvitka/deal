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
public class PaymentSchedule implements Serializable {
    private String paymentScheduleJsonArrayString = new JSONArray().toString();

    public PaymentSchedule(List<PaymentScheduleElement> paymentScheduleElements) {
        setList(paymentScheduleElements);
    }

    public List<PaymentScheduleElement> getList() {
        JSONArray paymentScheduleJsonArray = new JSONArray(paymentScheduleJsonArrayString);
        int length = paymentScheduleJsonArray.length();
        ArrayList<PaymentScheduleElement> list = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            list.add(PaymentScheduleElement.from(paymentScheduleJsonArray.getJSONObject(i)));
        }
        return list;
    }

    public void setList(List<PaymentScheduleElement> paymentScheduleElements) {
        paymentScheduleJsonArrayString = new JSONArray(paymentScheduleElements
                .stream()
                .map(PaymentScheduleElement::toJson)
                .collect(Collectors.toList()))
                .toString();
    }

    public void add(PaymentScheduleElement paymentScheduleElement) {
        List<PaymentScheduleElement> list = getList();
        list.add(paymentScheduleElement);
        setList(list);
    }
}
