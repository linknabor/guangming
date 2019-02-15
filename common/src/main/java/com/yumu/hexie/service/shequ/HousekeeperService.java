package com.yumu.hexie.service.shequ;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yumu.hexie.model.community.Thread;

public interface HousekeeperService {

    //获取自己发布列表
	List<Thread> getThreadList(long userId,String threadCategory,Pageable page);

}
