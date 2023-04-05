package com.itwill.my_real_korea.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.itwill.my_real_korea.dto.tour.Tour;

import com.itwill.my_real_korea.service.tour.TourService;
import com.itwill.my_real_korea.util.PageMaker;
import com.itwill.my_real_korea.util.PageMakerDto;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TourControllerTest {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	TourService tourService;
	
	@Test
	void testTour_list() throws Exception{
		PageMakerDto<Tour> tours=new PageMakerDto<>();
		List<Tour> tourList=new ArrayList<>();
		
		tourList.add(new Tour(1, null, 0, 0, 0, null, 0, null, null, 0, null));
		tourList.add(new Tour(2, null, 0, 0, 0, null, 0, null, null, 0, null));

		tours.setItemList(tourList);
		tours.setPageMaker(new PageMaker(1, 10));
		tours.setTotRecordCount(2);
		
		given(tourService.findAll(1, null, 0, 0, null)).willReturn(tours);
		
		mockMvc.perform(get("/tour-list"))
			   .andExpect(status().isOk())
			   .andExpect(view().name("tour-list"))
			   .andDo(print());
		
		verify(tourService).findAll(1, null, 0, 0, null);
	}

}