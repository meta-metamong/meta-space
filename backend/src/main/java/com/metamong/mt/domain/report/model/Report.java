package com.metamong.mt.domain.report.model;

import java.time.LocalDateTime;

import com.metamong.mt.domain.report.model.constant.ReportType;
import com.metamong.mt.global.constant.BooleanAlt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="report")
@SequenceGenerator(
        name = "report_pk_generator",
        sequenceName = "report_pk_seq",
        initialValue = 1,
        allocationSize = 1
)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="report_pk_generator")
    private Long reportId;
    
    @Column(name="reporter_id")
    private Long reporterId;
    
    @Column(name="reported_id")
    private Long reportedId;
    
    @Column(name="report_msg")
    private String reportMsg;
    
    @Column(name="report_date")
    private LocalDateTime reportDate;
    
    @Column(name="report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    
    @Column(name="is_processed")
    @Enumerated(EnumType.STRING)
    private BooleanAlt isProcessed;
    
    public void process() {
        this.isProcessed = BooleanAlt.Y;
    }
}
