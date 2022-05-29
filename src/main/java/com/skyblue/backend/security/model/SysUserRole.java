package com.skyblue.backend.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.skyblue.backend.security.repository.DataChange;

import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

@Table("sys_user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements DataChange {
    @Id
    private long id;
    private long userId;
    private long roleId;
    @CreatedBy
    private String createBy;
    @CreatedDate
    private java.time.LocalDateTime createTime;
    @LastModifiedBy
    private String lastUpdateBy;
    @LastModifiedDate
    private java.time.LocalDateTime lastUpdateTime;

    public SysUserRole(long userId, long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
