package com.example.delibuddy.domain.party;

public enum PartyStatus {
    OPEN("모집중"),
    DONE("모집종료");

    private String status;

    PartyStatus(String status) {
        this.status = status;
    }
}
