package com.metamong.mt.domain.member.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "fct_provider")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FctProvider {
    
    @Id
    private Long provId;
    
    @OneToOne
    @MapsId
    @JoinColumn(name="prov_id")
    private Member member;

    @Column(name="biz_name")
    private String bizName;
    
    @Column(name="biz_reg_num")
    private String bizRegNum;
    
    @OneToOne(mappedBy = "fctProvider", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Account account;
    
    public void updateInfo(FctProvider provider) {
        this.bizName = provider.getBizName();
        this.bizRegNum = provider.getBizRegNum();
    }
}
