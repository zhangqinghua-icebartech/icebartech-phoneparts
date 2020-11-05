package com.icebartech.core.modules;

import com.icebartech.core.po.BasePo;
import com.icebartech.core.utils.hibernate.FieldBean;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BaseService<D extends T, T extends BasePo> {

    Boolean exists(Long id);

    Boolean exists(Object... fieldBeans);

    Boolean exists(FieldBean... fieldBeans);

    Boolean exists(List<FieldBean> fieldBeans);

    D findOne(Long id);

    D findOne(Object... fieldBeans);

    D findOne(FieldBean... fieldBeans);

    D findOne(List<FieldBean> fieldBeans);

    D findOneOrNull(Long id);

    D findOneOrNull(Object... fieldBeans);

    D findOneOrNull(FieldBean... fieldBeans);

    D findOneOrNull(List<FieldBean> fieldBeans);

    D findDetail(Long id);

    D findDetail(Object... fieldBeans);

    D findDetail(FieldBean... fieldBeans);

    D findDetail(List<FieldBean> fieldBeans);

    List<D> findList(Object param);

    List<D> findList(Object... fieldBeans);

    List<D> findList(FieldBean fieldBean);

    List<D> findList(FieldBean... fieldBeans);

    List<D> findList(List<FieldBean> fieldBeans);

    Page<D> findPage(Object param);

    Page<D> findPage(Object... fieldBeans);

    Page<D> findPage(FieldBean... fieldBeans);

    Page<D> findPage(List<FieldBean> fieldBeans);

    Long insert(Object param);

    Long insert(Object... fieldBeans);

    Long insert(FieldBean... fieldBeans);

    Long insert(List<FieldBean> fieldBeans);

    Boolean update(Object param);

    Boolean update(Object... fieldBeans);

    Boolean update(FieldBean... fieldBeans);

    Boolean update(List<FieldBean> fieldBeans);

    Boolean delete(Long id);

    Boolean delete(Object... fieldBeans);

    Boolean delete(FieldBean... fieldBeans);

    Boolean delete(List<FieldBean> fieldBeans);
}
