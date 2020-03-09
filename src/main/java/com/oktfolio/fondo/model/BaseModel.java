package com.oktfolio.fondo.model;

import java.time.LocalDateTime;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/15
 */
public class BaseModel {

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Integer createBy;

    private Integer updateBy;

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }
}
