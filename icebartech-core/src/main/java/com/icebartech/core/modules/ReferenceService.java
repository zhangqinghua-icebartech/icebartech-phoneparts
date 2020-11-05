package com.icebartech.core.modules;

public interface ReferenceService {

    void addObserver(Class referenceClass, ReferenceObserver o);

    void notifyInsert(Long referenceId, Object reference);

    void notifyUpdate(Long referenceId, Object reference);

    void notifyDelete(Long referenceId, Object reference);

}
