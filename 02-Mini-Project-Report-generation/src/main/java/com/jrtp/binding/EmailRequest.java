package com.jrtp.binding;

import lombok.Data;

@Data
public class EmailRequest {
    private String email;
    private boolean sendPdf;
    private boolean sendExcel;
}
