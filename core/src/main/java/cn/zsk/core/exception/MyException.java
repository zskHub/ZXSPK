package cn.zsk.core.exception;

/**
 * @author zsk
 * @date 2017/12/15.
 *
 */
public class MyException extends RuntimeException {

  private String message;

  public MyException(String message){
    super(message);
    this.message=message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
