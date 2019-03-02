package com.by.base.controller;

import com.by.crm.core.NewPageInfo;
import com.by.crm.core.RestfulResponse;
import com.by.base.exce.BaseMessageException;
import com.by.base.service.IBaseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通用API抽象
 * @author zhan_bingcong
 *
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBaseController<V, T> {
	
	/**
	 * 获取Service实例
	 * @return
	 */
	public abstract IBaseService<V, T> getService();

	@ApiOperation("通用新增")
	@ApiResponses({ @ApiResponse(code = 200, message = "新增成功"), @ApiResponse(code = 500, message = "新增失败"), @ApiResponse(code = 403, message = "参数有误") })
	@PostMapping("/insert")
	public RestfulResponse<T> insert(@RequestBody T record) {
		RestfulResponse<T> response = new RestfulResponse<>();
		try {
			record = this.getService().insert(record);

			if (record == null) {
				response.setCode(500);
				response.setMessage("新增失败!");
				return response;
			}else {
				response.setCode(200);
				response.setData(record);
				response.setMessage("新增成功!");
				return response;
			}
		} catch (BaseMessageException e) {
			response.setCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@ApiOperation("通用修改")
	@ApiResponses({ @ApiResponse(code = 200, message = "修改成功"), @ApiResponse(code = 500, message = "修改失败"), @ApiResponse(code = 403, message = "参数有误") })
	@PutMapping("/update")
	public RestfulResponse<Void> update(@RequestBody T record) {
		RestfulResponse<Void> response = new RestfulResponse<>();
		try {
			String message = getService().update(record);
			return returnResult(response, message, "修改成功!");
		} catch (BaseMessageException e) {
			response.setCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@ApiOperation("通用删除")
	@ApiResponses({ @ApiResponse(code = 200, message = "删除成功"), @ApiResponse(code = 500, message = "删除失败"), @ApiResponse(code = 403, message = "参数有误") })
	@DeleteMapping("/delete")
	public RestfulResponse<Void> delete(@RequestParam(name = "id")Long primaryKey) {
		RestfulResponse<Void> response = new RestfulResponse<>();
		try {
			String message = getService().delete(primaryKey);
			return returnResult(response, message, "删除成功!");
		} catch (BaseMessageException e) {
			e.printStackTrace();
			response.setCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@ApiOperation("通用批量删除")
	@ApiResponses({ @ApiResponse(code = 200, message = "删除成功"), @ApiResponse(code = 500, message = "删除失败"), @ApiResponse(code = 403, message = "参数有误") })
	@DeleteMapping("/batchDelete")
	public RestfulResponse<Void> batchDelete(@RequestParam(name = "ids")Long[] primaryKey) {
		RestfulResponse<Void> response = new RestfulResponse<>();
		try {
			String message = getService().batchDelete(primaryKey);
			return returnResult(response, message, "删除成功!");
		} catch (BaseMessageException e) {
			response.setCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@ApiOperation("通用查询(分页)")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询成功"), @ApiResponse(code = 500, message = "查询失败"), @ApiResponse(code = 403, message = "参数有误") })
	@GetMapping("/findPage")
	public RestfulResponse<NewPageInfo<T>> findPage(V record, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
		
		RestfulResponse<NewPageInfo<T>> response = new RestfulResponse<>();
		NewPageInfo<T> pageInfo = getService().findPage(record, pageNum, pageSize);
		
		if (pageInfo.getList() == null || pageInfo.getList().size() <= 0) {
			response.setCode(500);
			response.setMessage("未查询到分页信息!");
			return response;
		}else {
			response.setCode(200);
			response.setMessage("查询成功!");
			response.setData(pageInfo);
			return response;
		}
	}

	@ApiOperation("通用根据ID获取数据")
	@ApiResponses({ @ApiResponse(code = 200, message = "获取成功"), @ApiResponse(code = 500, message = "获取失败"), @ApiResponse(code = 403, message = "参数有误") })
	@GetMapping("/get")
	public RestfulResponse<T> get(Long id) {

		RestfulResponse<T> response = new RestfulResponse<>();
		T result = getService().get(id);

		if (result == null) {
			response.setCode(500);
			response.setMessage("未获取到数据!");
			return response;
		}else {
			response.setCode(200);
			response.setMessage("获取成功!");
			response.setData(result);
			return response;
		}
	}
	
	@ApiOperation("通用查询列表")
	@ApiResponses({ @ApiResponse(code = 200, message = "查询成功"), @ApiResponse(code = 500, message = "查询失败"), @ApiResponse(code = 403, message = "参数有误") })
	@GetMapping("/findList")
	public RestfulResponse<List<T>> findList(V record) {
		
		RestfulResponse<List<T>> response = new RestfulResponse<>();
		List<T> result = getService().findList(record);
		
		if (result == null || result.size() <= 0) {
			response.setCode(500);
			response.setMessage("未查询到分页信息!");
			return response;
		}else {
			response.setCode(200);
			response.setMessage("查询成功!");
			response.setData(result);
			return response;
		}
	}

	protected RestfulResponse returnResult(RestfulResponse response, String failMessage, String oKeyMessage) {
		if (StringUtils.isNotBlank(failMessage)) {
			response.setCode(500);
			response.setMessage(failMessage);
		} else {
			response.setCode(200);
			response.setMessage(oKeyMessage);
		}
		return response;
	}
}
