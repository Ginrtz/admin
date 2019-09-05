package com.gin.admin.dao.exception;

/**
 * dao异常
 * 
 * @author o1760
 *
 */
public class DaoException extends RuntimeException {
	private static final long serialVersionUID = 6070525308026164497L;

	public DaoException() {
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
