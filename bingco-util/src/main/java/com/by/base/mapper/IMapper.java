package com.by.base.mapper;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 通用方法抽取接口<br />
 * 继承此类必须复写selectSelective方法,因为mapper.xml中去除了定义
 * @param <V> 一般是Vo类型
 * @param <T> 一般是Dto类型
 */
public interface IMapper<V, T> {

	int deleteByPrimaryKey(Serializable id);

	int insert(T record);

	int insertSelective(T record);

	T selectByPrimaryKey(Serializable id);

	int updateByPrimaryKeySelective(T record);

	List<T> selectByColumn1(@Param("cloumn") String cloumn, @Param("value") Serializable value);

	List<T> selectByColumn2(@Param("cloumn1") String cloumn1, @Param("value1") Serializable value1,
                                                          @Param("cloumn2") String cloumn2, @Param("value2") Serializable value2);

	List<T> selectByColumn3(@Param("cloumn1") String cloumn1, @Param("value1") Serializable value1,
							@Param("cloumn2") String cloumn2, @Param("value2") Serializable value2,
							@Param("cloumn3") String cloumn3, @Param("value3") Serializable value3);

	List<T> selectByColumn4(@Param("cloumn1") String cloumn1, @Param("value1") Serializable value1,
							@Param("cloumn2") String cloumn2, @Param("value2") Serializable value2,
							@Param("cloumn3") String cloumn3, @Param("value3") Serializable value3,
							@Param("cloumn4") String cloumn4, @Param("value4") Serializable value4);

	List<T> selectSelective(V record);

    Integer checkOrElse(@Param("id") Serializable id, @Param("status") Byte status, @Param("cfmuser") Long cfmuser);

	Integer enabledOrElse(@Param("id") Serializable id, @Param("enabled") String enabled);
}
