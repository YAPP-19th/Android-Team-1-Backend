package com.example.delibuddy.domain.party;

public enum PartyStatus {
    OPEN("모집중"),
    ORDERING("주문중"),
    DONE("주문완료");

    private String status;

    PartyStatus(String status) {
        this.status = status;
    }
}
