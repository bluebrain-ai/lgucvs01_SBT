package com.bluescript.demo.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Component
@Entity
@Table(name = "KSDSCUST")

@Data
@RequiredArgsConstructor
public class ksdsCustEntity {

    @Id
    @Column(name = "CUSTOMERNUM")
    private long customerNum;
    @Column(name = "CUSTOMERAREA")
    private String customerArea;

}
