package com.itwill.my_real_korea.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwill.my_real_korea.dao.user.UserImgDao;
import com.itwill.my_real_korea.dto.user.UserImg;
@Service
public class UserImgServiceImpl implements UserImgService {
	@Autowired
	private UserImgDao userImgDao;
	
	public UserImgServiceImpl() {
	}
	
	@Override
	public int createUserImg(UserImg userImg) throws Exception {
		return userImgDao.createUserImg(userImg);
	}
	
	@Override
	public int updateUserImg(UserImg userImg) throws Exception {
		return userImgDao.updateUserImg(userImg);
	}
	
	@Override
	public int removeUserImg(int userImgNo) throws Exception {
		return userImgDao.removeUserImg(userImgNo);
	}
	
	@Override
	public UserImg findUserImg(String userId) throws Exception {
		return userImgDao.findUserImg(userId);
	}
	
	
	

}
