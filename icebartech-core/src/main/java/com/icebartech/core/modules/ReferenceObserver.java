package com.icebartech.core.modules;

/**
 * 外部引用变更观察者
 */
public interface ReferenceObserver {

    void referenceInsert(Long referenceId, Object reference);

    void referenceUpdate(Long referenceId, Object reference);

    void referenceDelete(Long referenceId, Object reference);
}
