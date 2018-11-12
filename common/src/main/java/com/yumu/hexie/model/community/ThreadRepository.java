/**
 * 
 */
package com.yumu.hexie.model.community;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author HuYM
 *
 */
public interface ThreadRepository extends JpaRepository<Thread, Long> {

	
	public List<Thread> findByThreadStatus(String threadStatus, Pageable page);
	
	public List<Thread> findByThreadStatusAndUserSectId(String threadStatus, long userSectId, Pageable page);
	
	@Query(value="from Thread t where t.threadStatus = ?1 and t.userSectId = ?2 and t.threadCategory = ?3 ")
	public List<Thread> getThreadListByCategory(String threadStatus, long userSectId, String threadCategory, Pageable page);
	
	@Query(value="from Thread t where t.threadStatus = ?1 and t.threadCategory = ?2 ")
	public List<Thread> getThreadListByCategory(String threadStatus, String threadCategory, Pageable page);
	
	@Query(value="from Thread t where t.threadStatus = ?1 and t.userSectId = ?2 and t.threadCategory <> ?3 ")
	public List<Thread> getThreadListByNewCategory(String threadStatus, long userSectId, String threadCategory, Pageable page);
	
	@Query(value="from Thread t where t.threadStatus = ?1 and t.threadCategory <> ?2 ")
	public List<Thread> getThreadListByNewCategory(String threadStatus, String threadCategory, Pageable page);
	
	public List<Thread> findByThreadStatusAndUserId(String threadStatus, long userId, Sort sort);
	
	//根据userId和threadStatus获取发布列表
	@Query(value="from Thread t where t.threadStatus = ?1 and t.userId = ?2 and (t.threadCategory=2 or t.threadCategory=3)")
	public List<Thread> getThreadListByUserId(String threadStatus, long userId, Pageable page);
	//根据userId,threadStatus,threadCategory获取发布列表
	@Query(value="from Thread t where t.threadStatus = ?1 and t.userId = ?2 and t.threadCategory=?3")
	public List<Thread> getThreadListByUserIdAndCategory(String threadStatus, long userId, String threadCategory,Pageable page);
	
}
