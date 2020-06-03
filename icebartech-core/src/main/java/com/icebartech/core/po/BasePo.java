package com.icebartech.core.po;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.*;
import java.time.LocalDateTime;

/**
 * @author Anler
 */
@Data
@Slf4j
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
public class BasePo implements Serializable {

    private static final long serialVersionUID = 9841816204289916L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键'")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @Column(updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private LocalDateTime gmtCreated;

    @ApiModelProperty(value = "修改时间")
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'")
    private LocalDateTime gmtModified;

    @ApiModelProperty(value = "创建者", hidden = true)
    @Column(columnDefinition = "VARCHAR(64) NOT NULL DEFAULT '[SYS]' COMMENT '创建者id'")
    private String creator;

    @ApiModelProperty(value = "修改者", hidden = true)
    @Column(insertable = false, columnDefinition = "VARCHAR(64) NOT NULL DEFAULT '[SYS]' COMMENT '修改者id'")
    private String modifier;

    @ApiModelProperty(value = "是否已删除", hidden = true)
    @Column(columnDefinition = "CHAR(1) NOT NULL DEFAULT 'n' COMMENT '是否已删除 y:已删除 n:未删除'")
    @Enumerated(EnumType.STRING)
    private ChooseType isDeleted;

    public <T extends BasePo> T deepCopy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            //将当前这个对象写到一个输出流当中，，因为这个对象的类实现了Serializable这个接口，所以在这个类中
            //有一个引用，这个引用如果实现了序列化，那么这个也会写到这个输出流当中

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            //这个就是将流中的东西读出类，读到一个对象流当中，这样就可以返回这两个对象的东西，实现深克
            //noinspection unchecked
            return (T) ois.readObject();
        } catch (Exception e) {
            log.error("deepCopy bean failed.", e);
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_AVAILABLE, "深度克隆对象失败," + e.getMessage());
        }
    }
}
