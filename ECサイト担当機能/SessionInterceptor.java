package com.internousdev.i1810a.util;

import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception{
		//インターセプターからアクションの処理クラスのインスタンスは ActionaInvocation#getAction で取得
		//Struts2のInterceptor内でセッション情報を取得する時は、
		//Map<String, Object> session = invocation.getInvocationContext().getSession();
		//Map<String, Object> session = ActionContext.getContext().getSession();のどちらかで取得

		Map<String, Object> session = invocation.getInvocationContext().getSession();
		if (session.isEmpty()) {
			return "session";
		}
		//次のインターセプター(orアクション)を呼び出す
		return invocation.invoke();
	}
}
/*ActionProxy、ActionInvocation、Resultは、Struts 2の核となるオブジェクト。
 * Servletコンテナからのリクエストがあると、設定に従ってActionProxyがインターセプターを呼び出します。
 * インターセプターは、ActionInvocationオブジェクトがインスタンスを保持している
 */