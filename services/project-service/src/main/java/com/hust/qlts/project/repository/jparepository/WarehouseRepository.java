package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository  extends JpaRepository<WarehouseEntity,Long> {
    String sql="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p ORDER BY p.CREATE_DATE DESC";
    @Query(value = sql, nativeQuery = true, countQuery = sql)
    List<WarehouseEntity> getPart();
    String sql1="SELECT p.ID as id ,p.CODE as code,p.NAME as name from PART as p  WHERE p.NAME = :toUpperCase" ;
    @Query(value = sql1,nativeQuery = true)
    List<WarehouseEntity> getName(String toUpperCase);

    @Query(value = "select * from WAREHOUSE where CODE=?1 and STATUS= 1 ", nativeQuery = true)
    WarehouseEntity findByCode(String code);

    @Query(value = "select * from WAREHOUSE where WAREHOUSE_ID=?1 and STATUS=1", nativeQuery = true)
    WarehouseEntity findByID(Long id);
}
