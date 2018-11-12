package com.yumu.hexie.web.shequ;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.common.util.StringUtil;
import com.yumu.hexie.model.community.Thread;
import com.yumu.hexie.model.community.ThreadComment;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.shequ.CommunityService;
import com.yumu.hexie.service.shequ.HousekeeperService;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;

@Controller(value = "housekeeperController")
public class HousekeeperController extends BaseController {
//	private static final Logger log = LoggerFactory.getLogger(HousekeeperController.class);

	private static final int PAGE_SIZE = 10;
	@Inject
	private HousekeeperService housekeeperService;

	@Inject
	private CommunityService communityService;

	// 发布 CommunityController /thread/addThread

	// 删除发布 CommunityController /thread/deleteThread

	// 添加评论 CommunityController /thread/addComment
	
	//删除评论   CommunityController /thread/deleteComment

	// 首页发布列表 默认threadCategory为空是报修列表 0是服务需求 1是意见建议
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/housekeeper/getThreadList", method = RequestMethod.GET,produces="application/json")
	@ResponseBody
	public BaseResult<List<Thread>> getThreadList(@RequestParam(defaultValue = "0", required = false) Integer pageNum,
			@ModelAttribute(Constants.USER) User user, String threadCategory) {
		Sort sort = new Sort(Direction.DESC, "createDate", "createTime");
		Pageable page = new PageRequest(pageNum, PAGE_SIZE, sort);
		List<Thread> list = housekeeperService.getThreadList(user.getId(), threadCategory, page);
		return BaseResult.successResult(list);
	}
    
	// 根据threadId获取发布详情
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/housekeeper/getThreadDetail", method = RequestMethod.GET,produces="application/json")
	@ResponseBody
	public BaseResult<Thread> getThreadDetail(@ModelAttribute(Constants.USER) User user,Long threadId) {
		if (StringUtil.isEmpty(threadId)) {
			return BaseResult.fail("未选中帖子");
		}
		Thread ret = communityService.getThreadByTreadId(threadId);
		List<ThreadComment> list = communityService.getCommentListByThreadId(threadId);
		for (int i = 0; i < list.size(); i++) {
			ThreadComment tc = list.get(i);
			if (tc.getCommentUserId() == user.getId()) {
				tc.setIsCommentOwner("true");
			} else {
				tc.setIsCommentOwner("false");
			}
		}
		ret.setComments(list);

		if (ret.getUserId() == user.getId()) {
			ret.setIsThreadOwner("true");
		} else {
			ret.setIsThreadOwner("false");
		}

		ret.setHasUnreadComment("false");
		communityService.updateThread(ret);
		return BaseResult.successResult(ret);
	}
}
