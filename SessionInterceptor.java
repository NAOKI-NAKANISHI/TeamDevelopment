package com.internousdev.i1810a.util;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor {
	  @Override
	  public String intercept(ActionInvocation invocation) throws Exception {
	      Map<String,Object> session = invocation.getInvocationContext().getSession();
	      if(session.isEmpty())
	          return "session"; // session is empty/expired
	      return invocation.invoke();
	  }

}