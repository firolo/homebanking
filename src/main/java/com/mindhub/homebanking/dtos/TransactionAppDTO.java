package com.mindhub.homebanking.dtos;

import java.time.LocalDateTime;

public class TransactionAppDTO {
    private LocalDateTime dateFrom;
    private LocalDateTime dateThru;

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public LocalDateTime getDateThru() {
        return dateThru;
    }

    public TransactionAppDTO() {}

    public TransactionAppDTO(LocalDateTime dateFrom, LocalDateTime dateThru) {
        this.dateFrom = dateFrom;
        this.dateThru = dateThru;
    }
}
