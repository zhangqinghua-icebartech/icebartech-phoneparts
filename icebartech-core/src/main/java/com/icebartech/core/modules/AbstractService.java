package com.icebartech.core.modules;

import com.icebartech.core.enums.ChooseType;
import com.icebartech.core.enums.CommonResultCodeEnum;
import com.icebartech.core.exception.ServiceException;
import com.icebartech.core.local.LocalUser;
import com.icebartech.core.local.UserThreadLocal;
import com.icebartech.core.po.BasePo;
import com.icebartech.core.repository.BaseRepository;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.hibernate.FieldBean;
import com.icebartech.core.utils.hibernate.JPAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.icebartech.core.vo.QueryParam.*;


@Slf4j
@Transactional
public abstract class AbstractService<D extends P, P extends BasePo, R extends BaseRepository<P>> implements BaseService<D, P>, ReferenceObserver {

    @Autowired
    public R repository;
    @Autowired
    private ReferenceService referenceService;

    private Class<D> dclass;
    private Class<P> pclass;
    private String beanname;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void initClass() {
        dclass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        pclass = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        beanname = dclass.getSimpleName();
    }

    private String getUserInfo() {
        LocalUser localUser = UserThreadLocal.getUserInfo(true);
        return null == localUser ? "[SYS]" : localUser.getUserEnum() + ":" + localUser.getUserId();
    }

    private P findPo(Long id, Boolean nullable) {
        return findPo(new ArrayList<>(Collections.singletonList(eq(P::getId, id))), nullable);
    }

    private P findPo(Object param, Boolean nullable) {
        return findPo(object2FieldBeans(param), nullable);
    }

    private P findPo(List<FieldBean> fieldBeans, Boolean nullable) {
        Assert.notNull(fieldBeans, String.format("Failed to find %s data, fieldBeans cannot be null", beanname));
        // 查询未删除的，第一条的数据
        fieldBeans.add(pageSize(1));
        fieldBeans.add(eq(P::getIsDeleted, ChooseType.n));

        Page<P> page = this.repository.findAll(JPAUtil.getSpec(fieldBeans), JPAUtil.getPageable(fieldBeans));

        if (page.getContent().size() != 0) return page.getContent().get(0);
        if (!nullable) {
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, String.format("Failed to find %s data, %s[%s] doesn't exist", beanname, beanname, fieldBeansToString(fieldBeans)));
        }

        return null;
    }

    @Override
    public Boolean exists(Long id) {
        return this.exists(new ArrayList<>(Collections.singletonList(eq(P::getId, id))));
    }

    @Override
    public Boolean exists(Object... fieldBeans) {
        return this.exists(arrays2FieldBeans(fieldBeans));
    }

    @Override
    public Boolean exists(FieldBean... fieldBeans) {
        return this.exists(new ArrayList<>(Arrays.asList(fieldBeans)));
    }

    @Override
    public Boolean exists(List<FieldBean> fieldBeans) {
        Assert.notNull(fieldBeans, String.format("Failed to check %s data exists, fieldBeans cannot be null", beanname));
        // 只查询未删除的数据
        fieldBeans.add(eq(P::getIsDeleted, ChooseType.n));

        Long count = this.repository.count(JPAUtil.getSpec(fieldBeans));

        return count > 0;
    }

    @Override
    public D findOne(Long id) {
        return mapDTO(findPo(id, false));
    }

    @Override
    public D findOne(Object... fieldBeans) {
        return mapDTO(findPo(arrays2FieldBeans(fieldBeans), false));
    }

    @Override
    public D findOne(FieldBean... fieldBeans) {
        return mapDTO(findPo(new ArrayList<>(Arrays.asList(fieldBeans)), false));
    }

    @Override
    public D findOne(List<FieldBean> fieldBeans) {
        return mapDTO(findPo(fieldBeans, false));
    }

    @Override
    public D findOneOrNull(Long id) {
        return mapDTO(findPo(id, true));
    }

    @Override
    public D findOneOrNull(Object... fieldBeans) {
        return mapDTO(findPo(arrays2FieldBeans(fieldBeans), true));
    }

    @Override
    public D findOneOrNull(FieldBean... fieldBeans) {
        return mapDTO(findPo(new ArrayList<>(Arrays.asList(fieldBeans)), true));
    }

    @Override
    public D findOneOrNull(List<FieldBean> fieldBeans) {
        return mapDTO(findPo(fieldBeans, true));
    }

    @Override
    public D findDetail(Long id) {
        D d = this.findOne(id);
        this.mapDetail(d);
        return d;
    }

    @Override
    public D findDetail(Object... fieldBeans) {
        D d = this.findOne(fieldBeans);
        this.mapDetail(d);
        return d;
    }

    @Override
    public D findDetail(FieldBean... fieldBeans) {
        D d = this.findOne(fieldBeans);
        this.mapDetail(d);
        return d;
    }

    @Override
    public D findDetail(List<FieldBean> fieldBeans) {
        D d = this.findOne(fieldBeans);
        this.mapDetail(d);
        return d;
    }

    @Override
    public List<D> findList(Object param) {
        return this.findList(object2FieldBeans(param));
    }

    @Override
    public List<D> findList(Object... fieldBeans) {
        return this.findList(arrays2FieldBeans(fieldBeans));
    }

    @Override
    public List<D> findList(FieldBean fieldBean) {
        return this.findList(new ArrayList<>(Collections.singleton(fieldBean)));
    }

    @Override
    public List<D> findList(FieldBean... fieldBeans) {
        return this.findList(new ArrayList<>(Arrays.asList(fieldBeans)));
    }

    @Override
    public List<D> findList(List<FieldBean> fieldBeans) {
        if (fieldBeans.stream().noneMatch(f -> f.getName().equals("pageIndex"))) {
            fieldBeans.add(pageIndex(1));
        }
        if (fieldBeans.stream().noneMatch(f -> f.getName().equals("pageSize"))) {
            fieldBeans.add(pageSize(5000));
        }

        // 弄成可以扩容的集合，防止报错
        return new ArrayList<>(this.findPage(fieldBeans).getContent());
    }

    @Override
    public Page<D> findPage(Object param) {
        return this.findPage(object2FieldBeans(param));
    }

    @Override
    public Page<D> findPage(Object... fieldBeans) {
        return this.findPage(arrays2FieldBeans(fieldBeans));
    }

    @Override
    public Page<D> findPage(FieldBean... fieldBeans) {
        return this.findPage(new ArrayList<>(Arrays.asList(fieldBeans)));
    }

    @Override
    public Page<D> findPage(List<FieldBean> fieldBeans) {
        fieldBeans.add(eq(P::getIsDeleted, ChooseType.n));

        Pageable pageable = JPAUtil.getPageable(fieldBeans);
        Page<P> page = this.repository.findAll(JPAUtil.getSpec(fieldBeans), pageable);
        return new PageImpl<>(this.mapDTO(page.getContent()), pageable, page.getTotalElements());
    }

    @Override
    public Long insert(Object param) {
        Assert.notNull(param, String.format("Failed to save %s, data cannot be null", beanname));

        D d = BeanMapper.map(param, dclass);

        // 新增前操作
        preInsert(d);

        P p = BeanMapper.map(d, pclass);
        p.setId(null);
        p.setIsDeleted(ChooseType.n);

        p.setGmtCreated(LocalDateTime.now());
        p.setGmtModified(LocalDateTime.now());
        p.setCreator(this.getUserInfo());
        p.setModifier(this.getUserInfo());


        try {
            this.repository.save(p);

            d.setId(p.getId());
            // 新增后操作
            postInsert(d.getId());
            postInsert(d);
            postInsert(d.getId(), d);

            // 通知观察者新增完毕
            referenceService.notifyInsert(d.getId(), d);
        } catch (ServiceException e) {
            throw e;
        } catch (JpaSystemException e) {
            throw new ServiceException(CommonResultCodeEnum.DATA_SAVE_FAILED, e.getCause().getCause().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p.getId();
    }

    @Override
    public Long insert(Object... fieldBeans) {
        return insert(BeanMapper.map(fieldBeans, dclass));
    }

    @Override
    public Long insert(FieldBean... fieldBeans) {
        Assert.notNull(fieldBeans, String.format("Failed to save %s, data cannot be null", beanname));
        Map<String, Object> params = Stream.of(fieldBeans).collect(Collectors.toMap(FieldBean::getName, FieldBean::getValue));
        return insert(BeanMapper.map(params, dclass));
    }

    @Override
    public Long insert(List<FieldBean> fieldBeans) {
        Assert.notNull(fieldBeans, String.format("Failed to save %s, data cannot be null", beanname));
        Map<String, Object> params = fieldBeans.stream().collect(Collectors.toMap(FieldBean::getName, FieldBean::getValue));
        return insert(BeanMapper.map(params, dclass));
    }

    @Override
    public Boolean update(Object param) {
        Assert.notNull(param, String.format("Failed to update %s, data cannot be null", beanname));
        D d = BeanMapper.map(param, dclass);
        Assert.notNull(d.getId(), String.format("Failed to update %s, %s id(%s) cannot be null", beanname, beanname, param));

        // 修改前操作
        preUpdate(d.getId());
        preUpdate(d);
        preUpdate(d.getId(), d);

        P update = findPo(d.getId(), false);

        // 没有这种情况，纯粹为了避开IDE提示
        if (update == null) return false;

        // 将要修改的参数赋值给update，这样JPA就可以知道哪些参数是需要修改的
        BeanMapper.copyProperties(d, update);
        update.setGmtModified(LocalDateTime.now());

        update.setModifier(this.getUserInfo());

        this.repository.save(update);

        // 修改后操作
        postUpdate(d.getId());
        postUpdate(d);
        postUpdate(d.getId(), d);

        // 通知观察者新增完毕
        referenceService.notifyUpdate(d.getId(), d);

        return true;
    }

    @Override
    public Boolean update(Object... fieldBeans) {
        return update(BeanMapper.map(fieldBeans, dclass));
    }

    @Override
    public Boolean update(FieldBean... fieldBeans) {
        Assert.notNull(fieldBeans, String.format("Failed to save %s, data cannot be null", beanname));
        Map<String, Object> params = Stream.of(fieldBeans).collect(Collectors.toMap(FieldBean::getName, FieldBean::getValue));
        return update(BeanMapper.map(params, dclass));
    }

    @Override
    public Boolean update(List<FieldBean> fieldBeans) {
        Assert.notNull(fieldBeans, String.format("Failed to save %s, data cannot be null", beanname));
        Map<String, Object> params = fieldBeans.stream().collect(Collectors.toMap(FieldBean::getName, FieldBean::getValue));
        return update(BeanMapper.map(params, dclass));
    }

    @Override
    public Boolean delete(Long id) {
        // 判断此数据是否存在
        if (!this.exists(id)) {
            throw new ServiceException(CommonResultCodeEnum.DATA_NOT_FOUND, String.format("Failed to delete %s data, %s(id=%s) doesn't exist", beanname, beanname, id));
        }


        P update = findPo(id, false);
        D d = BeanMapper.map(update, dclass);

        update.setIsDeleted(ChooseType.y);
        update.setModifier(this.getUserInfo());
        update.setGmtModified(LocalDateTime.now());

        // 删除前操作
        preDelete(id);

        this.repository.save(update);

        // 删除后操作
        postDelete(id);

        // 通知观察者删除完毕
        referenceService.notifyDelete(id, d);
        return true;
    }

    @Override
    public Boolean delete(Object... fieldBeans) {
        findList(fieldBeans).forEach(obj -> this.delete(obj.getId()));
        return true;
    }

    @Override
    public Boolean delete(FieldBean... fieldBeans) {
        findList(fieldBeans).forEach(obj -> this.delete(obj.getId()));
        return true;
    }

    @Override
    public Boolean delete(List<FieldBean> fieldBeans) {
        findList(fieldBeans).forEach(obj -> this.delete(obj.getId()));
        return true;
    }

    private D mapDTO(P p) {
        if (p == null) return null;
        D d = BeanMapper.map(p, dclass);
        warpDTO(d);
        warpDTO(d.getId(), d);
        Assert.notNull(d, String.format("%s warp to dto error, %s is null", beanname, beanname));
        return d;
    }

    private List<D> mapDTO(List<P> list) {
        return list.stream().map(this::mapDTO).collect(Collectors.toList());
    }

    private void mapDetail(D d) {
        if (d == null) return;

        this.warpDetail(d);
        this.warpDetail(d.getId(), d);
    }

    protected void warpDTO(D d) {
    }

    protected void warpDTO(Long id, D d) {
    }

    protected void warpPage(D d) {
    }

    protected void warpPage(Long id,D d) {
    }

    protected void warpDetail(D d) {
    }

    protected void warpDetail(Long id, D d) {
    }

    protected void preInsert(D d) {

    }

    protected void postInsert(Long id) {

    }

    protected void postInsert(D d) {

    }

    protected void postInsert(Long id, D d) {

    }

    protected void preUpdate(Long id) {

    }

    protected void preUpdate(D d) {

    }

    protected void preUpdate(Long id, D d) {

    }


    protected void postUpdate(Long id) {

    }

    protected void postUpdate(D d) {

    }

    protected void postUpdate(Long id, D d) {

    }

    protected void preDelete(Long id) {

    }

    protected void postDelete(Long id) {

    }

    // 注册外部引用
    @PostConstruct
    private void registerReferences() {
        // 使用反射机制注册观察者
        Class cls = this.getClass();
        Method[] methods = cls.getDeclaredMethods();

        List<String> referenceMethods = Arrays.asList(
                "referenceInsert",
                "referenceUpdate",
                "referenceDelete");
        for (Method method : methods) {
            if (referenceMethods.contains(method.getName())) {
                Type[] parameterTypes = method.getGenericParameterTypes();
                // 非法写法，不作处理
                if (parameterTypes == null || parameterTypes.length < 2) {
                    continue;
                }
                try {
                    referenceService.addObserver(Class.forName(parameterTypes[1].getTypeName()), this);
                } catch (Exception e) {
                    log.error("注册外部引用异常：" + e.toString(), e);
                }
            }
        }
    }

    // 引用对象新增通知（不写成final形式的，以便子类快速重写）
    @Override
    public void referenceInsert(Long referenceId, Object reference) {
        executeReferenceMethod("referenceInsert", referenceId, reference);
    }

    // 引用对象修改通知（不写成final形式的，以便子类快速重写）
    @Override
    public void referenceUpdate(Long referenceId, Object reference) {
        executeReferenceMethod("referenceUpdate", referenceId, reference);
    }

    // 引用对象删除通知（不写成final形式的，以便子类快速重写）
    @Override
    public void referenceDelete(Long referenceId, Object reference) {
        executeReferenceMethod("referenceDelete", referenceId, reference);
    }

    private void executeReferenceMethod(String methodName, Long referenceId, Object reference) {
        try {
            Class implClass = this.getClass();
            @SuppressWarnings("unchecked")
            Method targetMethod = implClass.getDeclaredMethod(methodName, Long.class, reference.getClass());
            // 使用this（当前对象）去执行方法
            targetMethod.invoke(this, referenceId, reference);
        } catch (NoSuchMethodException e) {
            // 方法不存在，不处理
        } catch (IllegalAccessException e) {
            // 没有访问权限，不处理
            log.info("没有访问权限");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ServiceException) {
                throw (ServiceException) e.getTargetException();
            }
            if (e.getTargetException() instanceof Exception) {
                log.error(String.format("%s %s failed, %s", this.getClass().getSimpleName(), methodName, e.getTargetException().toString()), e.getTargetException());
                throw new ServiceException(CommonResultCodeEnum.INTERFACE_INNER_INVOKE_ERROR, String.format("%s %s failed, %s", this.getClass().getSimpleName(), methodName, e.getTargetException().toString()));
            }
        }
    }
}
