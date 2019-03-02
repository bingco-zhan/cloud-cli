package com.by.base.controller;

import com.by.crm.core.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

public abstract class AbstractBaseEnabledController<V, T> extends AbstractBaseCheckController<V, T> {

    @ApiOperation("设置失效")
    @ApiResponses({ @ApiResponse(code = 200, message = "设置成功"), @ApiResponse(code = 500, message = "设置失败"), @ApiResponse(code = 403, message = "参数有误") })
    @GetMapping("/notEnabled")
    public RestfulResponse<Void> setNotEnabled(Long id) {
        RestfulResponse<Void> response = new RestfulResponse<>();
        try {
            String message = this.getService().setNotEnabled(id);
            return returnResult(response, message, "设置成功!");
        } catch (RuntimeException e) {
            response.setCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @ApiOperation("设置有效")
    @ApiResponses({ @ApiResponse(code = 200, message = "设置成功"), @ApiResponse(code = 500, message = "设置失败"), @ApiResponse(code = 403, message = "参数有误") })
    @GetMapping("/isEnabled")
    public RestfulResponse<Void> setEnabled(Long id) {
        RestfulResponse<Void> response = new RestfulResponse<>();
        try {
            String message = this.getService().setEnabled(id);
            return returnResult(response, message, "设置成功!");
        } catch (RuntimeException e) {
            response.setCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
