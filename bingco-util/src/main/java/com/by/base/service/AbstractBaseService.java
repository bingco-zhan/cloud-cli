package com.by.base.service;

import com.by.base.constants.StatusEnum;
import com.by.crm.core.NewPageInfo;
import com.by.base.exce.BaseMessageException;
import com.by.base.mapper.IMapper;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 抽象化通用服务类
 * 
 * @author zhan_bingcong
 *
 * @param <T>
 */
public abstract class AbstractBaseService<V, T> implements IBaseService<V, T> {
	
	/**
	 * 返回信息并回滚(假使有错误信息)
	 * @param message
	 * @return
	 */
	protected String returnAndCallback(String message) {
		// 如果返回错误信息回滚操作
		if (StringUtils.isNotBlank(message)) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return message;
	}

	/**
	 * 获取抽象Mapper
	 *
	 * @return
	 */
	protected abstract IMapper<V, T> getMapper();

	/**
	 * 通用更新的前置方法
	 * @return
	 */
	protected String beforeUpdate(T record) {
		return null;
	}

	/**
	 * 通用新增前置方法
	 * @param record
	 * @return
	 */
	protected String beforeInsert(T record) {
		return null;
	}

	@Override
	@Transactional
	public T insert(T record) {
		String message = this.beforeInsert(record);
		if (StringUtils.isNotBlank(message)) {
			throw new BaseMessageException(message);
		}
		int count = this.getMapper().insertSelective(record);
		if (count <= 0) {
			return null;
		}
		return record;
	}

	@Override
	@Transactional
	public String update(T record) {
		String message = this.beforeUpdate(record);
		if (StringUtils.isNotBlank(message)) {
			return message;
		}
		int count = this.getMapper().updateByPrimaryKeySelective(record);
		if (count <= 0) {
			return "更新失败!";
		}

		return null;
	}

	/**
	 * 删除前置方法
	 * @param primaryKey
	 * @return
	 */
	protected String beforeDelete(Serializable primaryKey) {
		return null;
	}

	@Override
	@Transactional
	public String delete(Serializable primaryKey) {
		String message = this.beforeDelete(primaryKey);
		if (StringUtils.isNotBlank(message)) {
			return message;
		}
		int count = this.getMapper().deleteByPrimaryKey(primaryKey);
		if (count <= 0) {
			return "删除失败!";
		}
		return null;
	}

	@Override
	@Transactional
	public String batchDelete(Serializable[] primaryKey) {
		for (Serializable key : primaryKey) {
            String message = this.delete(key);
            if (StringUtils.isNotBlank(message)) {
                return message;
            }
        }
		return null;
	}

	@Override
	public String check(Serializable id, Long cfmuser) {
		getMapper().checkOrElse(id, StatusEnum.CHECKED.getStatus(), cfmuser);
		return null;
	}

	@Override
	public String unCheck(Serializable id, Long cfmuser) {
		getMapper().checkOrElse(id, StatusEnum.UNCHECK.getStatus(), cfmuser);
		return null;
	}

	@Override
	public String setNotEnabled(Long id) {
		getMapper().enabledOrElse(id, "N");
		return null;
	}

	@Override
	public String setEnabled(Long id) {
		getMapper().enabledOrElse(id, "Y");
		return null;
	}

	@Override
	public T get(Serializable id) {
        V newInstance = newInstance(id);
        List<T> list = this.findList(newInstance);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
	}

    /**
     * 实例化一个泛型V
     * @param id 填充字段值
     * @return V
     */
	private V newInstance(Serializable id) {
        try {
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Type[] arguments = pt.getActualTypeArguments();
            Class<V> clazz = (Class<V>) arguments[0];
            V v = clazz.newInstance();
            Field field = clazz.getDeclaredField("id");
            field.setAccessible(true);
            field.set(v, id);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public NewPageInfo<T> findPage(V record, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize, true, false, true);
		return new NewPageInfo<>(afterFindList(this.getMapper().selectSelective(record)));
	}

	@Override
	public List<T> findList(V record) {
		return afterFindList(this.getMapper().selectSelective(record));
	}

	/**
	 * 通用列表和分页数据查询后置处理
	 * @param list
	 * @return
	 */
	protected List<T> afterFindList(List<T> list) {
		return list;
	}
}
