package com.kvitka.deal.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentScheduleElement implements Serializable {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainDebt;

    public JSONObject toJson() {
        return new JSONObject()
                .put("number", number)
                .put("date", date.toString())
                .put("totalPayment", totalPayment.toPlainString())
                .put("interestPayment", interestPayment.toPlainString())
                .put("debtPayment", debtPayment.toPlainString())
                .put("remainDebt", remainDebt.toPlainString());
    }

    public static PaymentScheduleElement from(JSONObject jsonObject) {
        return new PaymentScheduleElement(
                jsonObject.getInt("number"),
                LocalDate.parse(jsonObject.getString("date")),
                new BigDecimal(jsonObject.getString("totalPayment")),
                new BigDecimal(jsonObject.getString("interestPayment")),
                new BigDecimal(jsonObject.getString("debtPayment")),
                new BigDecimal(jsonObject.getString("remainDebt"))
        );
    }
}
