package com.chargepile.util;

public abstract class Message<T> {
	
	/**标识  1：成功 0：异常   -1：失败**/
	private String flag = "0";
	
	/**接口返回的描述信息**/
	private String error = "签名错误";

	/**返回实体对象**/
	protected T msg;
	
	protected abstract T getMsg();
	protected abstract void setMsg(T msg);
	

	/**
	 * @return flag 
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

}
