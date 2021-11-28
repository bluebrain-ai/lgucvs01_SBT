package com.bluescript.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface KsdscustRepository extends CrudRepository<ksdsCustEntity, Long> {

}