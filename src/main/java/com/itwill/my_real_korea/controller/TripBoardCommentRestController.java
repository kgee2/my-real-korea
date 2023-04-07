package com.itwill.my_real_korea.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itwill.my_real_korea.dto.tripboard.TripBoardComment;
import com.itwill.my_real_korea.service.tripboard.TripBoardCommentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
public class TripBoardCommentRestController {
	
	@Autowired
	private TripBoardCommentService tripBoardCommentService;
	
	//댓글 추가
	@LoginCheck
	@ApiOperation(value = "댓글 추가")
	@PostMapping(value = "/tripboard_comment", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoard_write_action(@RequestBody TripBoardComment tripBoardComment) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoardComment> data = new ArrayList<>();
		
		try {
			//댓글 추가 성공 시 code1
			tripBoardCommentService.insertTripBoardComment(tripBoardComment);
			code = 1;
			msg = "성공";
			
			tripBoardComment = tripBoardCommentService.selectByNo(tripBoardComment.getTCoNo());
			data.add(tripBoardComment);
		} catch (Exception e) {
			//댓글 추가 실패 시 code2
			e.printStackTrace();
			code = 2;
			msg = "댓글 추가 실패";
		}
		resultMap.put("code", code);		
		resultMap.put("msg", msg);		
		resultMap.put("data", data);		

		return resultMap;
		
	}
	
	//댓글 수정
	@LoginCheck
	@ApiOperation(value = "댓글 수정")
	@ApiImplicitParam(name = "tCoNo", value = "댓글 번호")
	@PutMapping(value = "/tripboard_comment/{tCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoardComment_update_form(@PathVariable(value="tCoNo") int tCoNo,
													@RequestBody TripBoardComment tripBoardComment) {
	
	Map<String, Object> resultMap = new HashMap<>();
	int code = 1;
	String msg = "성공";
	List<TripBoardComment> data = new ArrayList<TripBoardComment>();
	try {
		TripBoardComment findTripBoardComment = tripBoardCommentService.selectByNo(tCoNo);
		if(findTripBoardComment != null) {
			//댓글 수정 성공 시 code1
			tripBoardComment.setTCoNo(tCoNo);
			tripBoardCommentService.updateTripBoardComment(findTripBoardComment);
			code = 1;
			msg = "성공";
			data.add(findTripBoardComment);
		}else {
			//댓글 수정 실패 시 code2
			code = 2;
			msg = "댓글 수정 실패";
		}
	} catch (Exception e) {
		//에러 발생 시 code3
		e.printStackTrace();
		code = 3;
		msg = "관리자에게 문의하세요.";
	}
	resultMap.put("code", code);
	resultMap.put("msg", msg);
	resultMap.put("data", data);
	
	return resultMap;
	}
	
	
	
	//댓글 삭제
	@LoginCheck
	@ApiOperation(value = "댓글 삭제")
	@ApiImplicitParam(name = "tCoNo", value = "댓글 번호")
	@DeleteMapping(value = "/tripboard_comment/{tCoNo}", produces = "application/json;charset=UTF-8")
	public Map<String, Object> tripBoardComment_delete_action(@PathVariable(value = "tCoNo") int tCoNo, 
																HttpServletRequest request) {
		
		Map<String, Object> resultMap = new HashMap<>();
		int code = 1;
		String msg = "성공";
		List<TripBoardComment> data = new ArrayList<TripBoardComment>();
		try {
			//댓글 삭제 성공 시 code1
			int rowCount = tripBoardCommentService.deleteTripBoardComment(tCoNo);
			if(rowCount != 0) {
				code = 1;
				msg = "성공";
			}else {
				//댓글 삭제 실패 시 code2
				code = 2;
				msg = "댓글 삭제 실패";
				TripBoardComment failTripBoardComment = tripBoardCommentService.selectByNo(tCoNo);
				data.add(failTripBoardComment);
			}
		} catch (Exception e) {
			//에러 발생 시 code3
			e.printStackTrace();
			code = 3;
			msg = "관리자에게 문의하세요.";
		}
		return resultMap;
	}
	
	
	
	
}
