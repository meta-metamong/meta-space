package com.metamong.mt.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @Column(name="prov_id")
    private Long provId;
    
    @Column(name="biz_name")
    private String bizName;
    
    @Column(name="biz_reg_num")
    private String bizRegNum;
    
    public void updateInfo(FctProvider provider) {
        this.bizName = provider.getBizName();
        this.bizRegNum = provider.getBizRegNum();
    }
}
