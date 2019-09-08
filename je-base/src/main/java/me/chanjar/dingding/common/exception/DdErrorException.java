package me.chanjar.dingding.common.exception;

import me.chanjar.dingding.common.bean.result.DdError;

public class DdErrorException extends Exception {

  private static final long serialVersionUID = -6357149550353160810L;

  private DdError error;

  public DdErrorException(DdError error) {
    super(error.toString());
    this.error = error;
  }

  public DdErrorException(DdError error, Throwable cause) {
    super(error.toString(), cause);
    this.error = error;
  }

  public DdError getError() {
    return this.error;
  }


}
