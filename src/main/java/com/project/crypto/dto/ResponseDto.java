package com.project.crypto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {

    private Integer code;
    private String status;
    private String message;

    public void setSuccess(){
        this.code = 200;
        this.status = "success";
        this.message = "Berhasil";
    }

    public void setFailed(String msg){
        this.code = 400;
        this.status = "failed";
        this.message = msg;
    }
}
