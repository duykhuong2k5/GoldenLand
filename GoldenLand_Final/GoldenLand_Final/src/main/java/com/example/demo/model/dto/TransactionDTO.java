package com.example.demo.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;
    private String note;
    private String code;
    private Long amount;
    private String status;
    private Long buildingId;
    private String buildingName;
}
