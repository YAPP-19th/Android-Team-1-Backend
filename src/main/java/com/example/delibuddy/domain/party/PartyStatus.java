package com.example.delibuddy.domain.party;

import lombok.Getter;

@Getter
public enum PartyStatus {
    OPEN("모집중"),
    ORDERING("주문중"),
    DONE("파티완료");

    private String status;

    PartyStatus(String status) {
        this.status = status;
    }

    public static PartyStatus statusBy(String status) {
        switch (status) {
            case "모집중": return PartyStatus.OPEN;
            case "주문중": return PartyStatus.ORDERING;
            case "파티완료": return PartyStatus.DONE;
            default: throw new IllegalArgumentException(status + "는 존재하지 않는 PartyStatus 입니다!");
        }
    }
}
