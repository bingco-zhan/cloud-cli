package com.by.base.service;

import com.by.crm.core.NewPageInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 服务层公共方法抽取
 * @param <V>
 * @param <T>
 */
public interface IBaseService<V, T> {

	T insert(T record);

	String update(T record);

	String delete(Serializable primaryKey);

	String batchDelete(Serializable[] primaryKey);

	NewPageInfo<T> findPage(V record, Integer pageNum, Integer pageSize);

	List<T> findList(V record);

	String check(Serializable id, Long cfmuser);

	String unCheck(Serializable id, Long cfmuser);

	String setEnabled(Long id);

	String setNotEnabled(Long id);

    T get(Serializable id);
}
