package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.WarehouseDTO;
import com.hust.qlts.project.dto.DataPage;

public interface WarehouseService {
    DataPage<WarehouseDTO> getPageWarehouseSeach(WarehouseDTO dto);
    WarehouseDTO create(WarehouseDTO dto);
    WarehouseDTO update(WarehouseDTO dto);
    Boolean delete (Long id);
    WarehouseDTO findById(Long Id);
    WarehouseDTO findByCode(String code);
}
