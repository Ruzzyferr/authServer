package com.example.authserver.mapper;

import com.example.authserver.dto.UserDto;
import com.example.authserver.dto.UserSaveRequestDto;
import com.example.authserver.entity.User;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity (UserDto dto);

    UserDto toDto (User entity);

    User toEntityFromSaveRequestDto (UserSaveRequestDto dto);

    List<UserDto> toDtoListFromEntity(List<User> all);
}
