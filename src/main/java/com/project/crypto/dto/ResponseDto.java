package com.project.crypto.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto {

    private String status;
    private String message;

    public void setSuccess(){
        this.status = "success";
        this.message = "Berhasil";
    }

    public void setFailed(String msg){
        this.status = "failed";
        this.message = msg;
    }
}
