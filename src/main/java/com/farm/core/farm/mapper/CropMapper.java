package com.farm.core.farm.mapper;



import com.farm.base.farm.Crop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CropMapper {


    int delete(Long id);

    int insert(Crop record);


    Crop findById(Long id);

    List<Crop> listAll();


    int update(Crop record);
}