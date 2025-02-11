package com.metamong.mt.domain.report.model.constant;

public enum ReportType {
    FACILITY_ISSUE("시설 상태 불량"),
    ABUSIVE_LANGUAGE("폭언 및 욕설"),
    INAPPROPRIATE_AD("부적절한 광고"),
    INCORRECT_BILLING("부정확한 요금 청구"),
    HARASSMENT("성희롱 및 성추행"),
    OTHER("기타");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }    
}
