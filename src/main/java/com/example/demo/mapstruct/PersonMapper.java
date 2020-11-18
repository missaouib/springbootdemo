package com.example.demo.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * 通过MapStruct自动生成的mapper是无状态的和线程安全的，可以同时被若干个线程访问。
 */
@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "firstName",target = "name")
    Person personDTOToPerson(PersonDTO personDTO);
}
