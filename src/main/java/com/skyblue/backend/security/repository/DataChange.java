package com.skyblue.backend.security.repository;
import java.time.LocalDateTime;


public interface DataChange {

    void setCreateBy(String createBy);

    void setCreateTime(LocalDateTime createTime);

    void setLastUpdateBy(String lastUpdateBy);

    void setLastUpdateTime(LocalDateTime lastUpdateTime);
}
