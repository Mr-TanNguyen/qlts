package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.DataPage;
import com.hust.qlts.project.dto.WarehouseDTO;
import com.hust.qlts.project.entity.WarehouseEntity;
import com.hust.qlts.project.repository.customreporsitory.HumanResourcesCustomRepository;
import com.hust.qlts.project.repository.customreporsitory.WarehouseCustomRepository;
import com.hust.qlts.project.repository.jparepository.WarehouseRepository;
import com.hust.qlts.project.service.WarehouseService;
import common.ErrorCode;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service(value = "warehouse")

public class WarehouseServiceMpl implements WarehouseService {
    @Autowired
    EntityManager em;
    private final Logger log = LogManager.getLogger(HumanResourcesCustomRepository.class);

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    com.hust.qlts.project.service.mapper.warehouseMapper warehouseMapper;

    @Autowired private WarehouseCustomRepository warehouseCustomRepository;
    @Override
    public DataPage<WarehouseDTO> getPageWarehouseSeach(WarehouseDTO dto) {
        log.info("-----------------Danh sach nhà cung cấp--------------");

        DataPage<WarehouseDTO> data = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
        //    String s = String.valueOf(dto.getActive());
//        if (!s.equals("null")) {
//            if (dto.getActive() == 0)
//                dto.setActive(null);
//        }

        LocalDate today = LocalDate.now();
        int currentDate1 = today.getDayOfMonth();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();
        int a = 0;

        List<WarehouseDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(warehouseCustomRepository.getWarehouseSearch(dto))) {
            listProject = warehouseCustomRepository.getWarehouseSearch(dto);
            data.setData(listProject);
        }
        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getPageSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }

    @Override
    public WarehouseDTO create(WarehouseDTO dto) {
        WarehouseEntity warehouseEntity = warehouseRepository.findByCode(dto.getCode());
        if (null != warehouseEntity && dto.getIdWare() == null) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
        } else if (null != warehouseEntity) {
            //TODO: Update  nha cung cấp
           // warehouseEntity.setWarehouseID(dto.getIdWare());
            warehouseEntity.setName(dto.getFullName());
            warehouseEntity.setCode(dto.getCode());
//            warehouseEntity.setAreaid(dto.getArea_id());
            warehouseEntity.setAddress(dto.getAddress());
            warehouseEntity.setNote(dto.getNote());
            warehouseEntity.setParid(dto.getPartId());
//            warehouseEntity.setProvincecode(dto.getProvinceID());

        } else if(dto.getIdWare() == null){
            //TODO: create nha cung cap
             warehouseEntity = new WarehouseEntity();
            warehouseEntity.setName(dto.getFullName());
            warehouseEntity.setCode(dto.getCode());
//            warehouseEntity.setAreaid(dto.getArea_id());
            warehouseEntity.setAddress(dto.getAddress());
            warehouseEntity.setNote(dto.getNote());
            warehouseEntity.setParid(dto.getPartId());
            warehouseEntity.setStatus(1);
//            warehouseEntity.setProvincecode(dto.getProvinceID());

        }
        warehouseRepository.save(warehouseEntity);
        return convertEntitytoDTO(warehouseEntity);
    }

    @Override
    public WarehouseDTO update(WarehouseDTO dto) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        if (null != id){
            WarehouseEntity supplierEntity = warehouseRepository.findByID(id);
            if (supplierEntity !=null) {
                supplierEntity.setStatus(0);
                warehouseRepository.save(supplierEntity);
                log.info("<--- DELETE SUPPLIER COMPLETE");
                return true;
            } else {
                log.error("<--- CAN'T DELETE SUPPLIER RESOURCES");
                return false;
            }
        }

        return false;
    }

    @Override
    public WarehouseDTO findById(Long Id) {
         if (!warehouseRepository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        return convertEntitytoDTO(warehouseRepository.findById(Id).get());
    }


    @Override
    public WarehouseDTO findByCode(String code) {
        if (null != warehouseRepository.findByCode(code)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return warehouseMapper.toDto(warehouseRepository.findByCode(code));
    }

    public WarehouseDTO convertEntitytoDTO(WarehouseEntity warehouseEntity){
        WarehouseDTO warehouseDTO = new WarehouseDTO();
        warehouseDTO.setAddress(warehouseEntity.getAddress());
        warehouseDTO.setCode(warehouseEntity.getCode());
        warehouseDTO.setFullName(warehouseEntity.getName());
        warehouseDTO.setNote(warehouseEntity.getNote());
      // warehouseDTO.setID(warehouseEntity.getWarehouseID());
//       warehouseDTO.setProvinceID(warehouseEntity.getProvincecode());
       warehouseDTO.setPartId(warehouseEntity.getParid());
        return warehouseDTO;
    }
}
