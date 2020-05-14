package com.icebartech.core.modules;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReferenceServiceImpl implements ReferenceService {

    /**
     * 观察者列表
     * <p>
     * 商品分类 -> 商品观察者
     * <p>
     * 角色 -> 用户观察者、权限观察者
     */
    private Map<Class, List<ReferenceObserver>> observersMap = new HashMap<>();

    @Override
    public void addObserver(Class referenceClass, ReferenceObserver o) {
        List<ReferenceObserver> referenceObservers = observersMap.computeIfAbsent(referenceClass, l -> new ArrayList<>());
        if (!referenceObservers.contains(o)) {
            referenceObservers.add(o);
        }
    }

    @Override
    public void notifyInsert(Long referenceId, Object reference) {
        observersMap.getOrDefault(reference.getClass(), new ArrayList<>()).forEach(s -> s.referenceInsert(referenceId, reference));
    }

    @Override
    public void notifyUpdate(Long referenceId, Object reference) {
        observersMap.getOrDefault(reference.getClass(), new ArrayList<>()).forEach(s -> s.referenceUpdate(referenceId, reference));
    }

    @Override
    public void notifyDelete(Long referenceId, Object reference) {
        observersMap.getOrDefault(reference.getClass(), new ArrayList<>()).forEach(s -> s.referenceDelete(referenceId, reference));
    }
}
