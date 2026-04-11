package com.example.ondas_be.infrastructure.persistence.model;

import com.example.ondas_be.domain.entity.Role;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleConverter implements AttributeConverter<Role, Short> {

    @Override
    public Short convertToDatabaseColumn(Role role) {
        return (short) role.getId();
    }

    @Override
    public Role convertToEntityAttribute(Short dbData) {
        return Role.fromId(dbData);
    }
}
