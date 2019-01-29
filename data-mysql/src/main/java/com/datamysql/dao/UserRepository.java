package com.datamysql.dao;

import com.datamysql.entry.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/28.17:57 首次创建
 * @date 2019/1/28.17:57 最后修改
 * @copyright 中科软科技股份有限公司
 */
public interface UserRepository extends CrudRepository<User, Integer> {

}