package com.example.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @CreatedBy
    @Column(updatable = false)
    private String updatedBy;
}
