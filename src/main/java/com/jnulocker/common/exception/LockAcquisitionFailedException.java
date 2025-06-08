package com.jnulocker.common.exception;

public class LockAcquisitionFailedException extends BusinessException {

    public static final BusinessException EXCEPTION = new LockAcquisitionFailedException();

    private LockAcquisitionFailedException() {
        super(CommonErrorCode.LOCK_ACQUISITION_FAILED);
    }
}
