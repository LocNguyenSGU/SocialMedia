package com.example.social.media.mapper;

import com.example.social.media.entity.Role;
import com.example.social.media.payload.response.AdminDTO.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
}
