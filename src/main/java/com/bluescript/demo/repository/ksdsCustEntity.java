package com.bluescript.demo.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Component
@Entity
@Table(name = "KSDSCUST")

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ksdsCustEntity {

    @Id
    @Column(name = "CUSTOMERNUMBER")
    private long customerNum;
    @Lob
    @Column(name = "CUSTOMERDATA")
    private String customerArea;

}
