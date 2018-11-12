package com.yumu.hexie.service.shequ.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.community.Thread;
import com.yumu.hexie.model.community.ThreadCommentRepository;
import com.yumu.hexie.model.community.ThreadRepository;
import com.yumu.hexie.service.shequ.HousekeeperService;
@Service
public class HousekeeperServiceImpl implements HousekeeperService {
	@Inject
	private ThreadRepository threadRepository;
	
	@Override
	public List<Thread> getThreadList(long userId,String threadCategory,Pageable page) {
		if(StringUtil.isEmpty(threadCategory)){
			return threadRepository.getThreadListByUserId(ModelConstant.THREAD_STATUS_NORMAL, userId, page);
		}
		return threadRepository.getThreadListByUserIdAndCategory(ModelConstant.THREAD_STATUS_NORMAL, userId, threadCategory, page);
	}


}
