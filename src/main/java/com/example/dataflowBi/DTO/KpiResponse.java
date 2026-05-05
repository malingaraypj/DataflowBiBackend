//package com.example.dataflowBi.DTO;
//
//import java.math.BigDecimal;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//
//@Data
//@AllArgsConstructor
//public class KpiResponse {
//	private String title;
//    private BigDecimal currentValue;
//    private Double percentageChange; 
//    private String trend;  // up , down, flat
//}
//
 // hari 

package com.example.dataflowBi.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KpiResponse {
    private List<KpiResult> kpis;
}
