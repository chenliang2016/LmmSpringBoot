package com.lmm.zuul.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse {
    public boolean isSuccess() {
        return code == ResultCode.SUCCESS;
    }

    @Builder.Default
    public ResultCode code = ResultCode.SUCCESS;

    public String message;

    public Object data;


}
