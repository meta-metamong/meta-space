package com.metamong.mt.domain.admin.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySalesGrowthDTO {
	String esalesMonth;
	int totalSales;
	int prevTotalSales;
	int growthRate;
	
}
