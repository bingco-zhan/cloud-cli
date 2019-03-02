package com.by.base.controller;

import com.by.crm.core.RestfulResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

public abstract class AbstractBaseCheckController<V, T> extends AbstractBaseController<V, T> {

    @ApiOperation("审核")
    @ApiResponses({ @ApiResponse(code = 200, message = "审核成功"), @ApiResponse(code = 500, message = "审核失败"), @ApiResponse(code = 403, message = "参数有误") })
    @GetMapping("/check")
    public RestfulResponse<Void> check(Long id, Long cfmuser) {
        RestfulResponse<Void> response = new RestfulResponse<>();
        try {
            String message = this.getService().check(id, cfmuser);
            return returnResult(response, message, "审核成功!");
        } catch (RuntimeException e) {
            response.setCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @ApiOperation("弃审")
    @ApiResponses({ @ApiResponse(code = 200, message = "弃审成功"), @ApiResponse(code = 500, message = "弃审失败"), @ApiResponse(code = 403, message = "参数有误") })
    @GetMapping("/unCheck")
    public RestfulResponse<Void> unCheck(Long id, Long cfmuser) {
        RestfulResponse<Void> response = new RestfulResponse<>();
        try {
            String message = this.getService().unCheck(id, cfmuser);
            return returnResult(response, message, "弃审成功!");
        } catch (RuntimeException e) {
            response.setCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }
}
