package com.macode101.exam.mapper;

import com.macode101.exam.model.User;
import com.macode101.exam.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDto, User> {}
