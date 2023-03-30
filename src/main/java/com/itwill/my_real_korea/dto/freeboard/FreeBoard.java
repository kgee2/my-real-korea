package com.itwill.my_real_korea.dto.freeboard;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/*
 이름             널?       유형             
-------------- -------- -------------- 
F_BO_NO        NOT NULL NUMBER         
F_BO_TITLE     NOT NULL VARCHAR2(500)  
F_BO_CONTENT   NOT NULL VARCHAR2(1000) 
F_BO_DATE               DATE           
F_BO_READCOUNT          NUMBER         
CITY_NO                 NUMBER         
USER_ID                 VARCHAR2(50)   
 */
@Data
@AllArgsConstructor
@ToString

public class FreeBoard {
	private int fBoNo;
	private String fBoTitle;
	private String fBoContent;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fBoDate;
	private int fBoCount;
		/* FK */
	private int cityNo;
	private String userId;
}
