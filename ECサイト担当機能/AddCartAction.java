package com.internousdev.i1810a.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.CartInfoDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;

public class AddCartAction extends ActionSupport implements SessionAware{

	private int productCount;

	private Map<String, Object> session;
	public String execute(){

		String result=ERROR;
		if(!(session.containsKey("price"))){
			return "injustice";
		}
		String userId = null;
		String tempUserId = null;
		int productId=(Integer)session.get("productId");//Objectが明らかにInt値の場合は単純なキャスト式の記述でOK

		int price = (Integer)session.get("price");

		//未ログインの場合
		if (!(session.containsKey("userId")) && !(session.containsKey("tempUserId"))) {
			CommonUtility commonUtility = new CommonUtility();
			session.put("tempUserId", commonUtility.getRamdomValue());
		}
		//ログイン済みの場合
		if(session.containsKey("userId")) {
			userId = String.valueOf(session.get("userId"));
		}
		//未ログインの一時ユーザーはtempUserIdをuserIdにセット
		if (!(session.containsKey("userId")) && session.containsKey("tempUserId")) {
			userId = String.valueOf(session.get("tempUserId"));
			tempUserId = String.valueOf(session.get("tempUserId"));
		}
		session.remove("checkListErrorMessageList");
		CartInfoDAO cartInfoDao = new CartInfoDAO();
		if (productCount > 5) {
			productCount = 5;
		}
		int count = cartInfoDao.regist(userId,tempUserId,productId,String.valueOf(productCount),price);
		if(count > 0) {
			result=SUCCESS;
		}
		List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();
		cartInfoDtoList = cartInfoDao.getCartInfoDtoList(userId);
		Iterator<CartInfoDTO> iterator = cartInfoDtoList.iterator();
		if(!(iterator.hasNext())) {
			cartInfoDtoList = null;
		}
		for(CartInfoDTO dto:cartInfoDtoList) {
			System.out.println("CartInfoList: : "+dto.getProductName());
		}
		session.put("cartInfoDtoList", cartInfoDtoList);
		int totalPrice = Integer.parseInt(String.valueOf(cartInfoDao.getTotalPrice(userId)));
		session.put("totalPrice", totalPrice);
		return result;
	}

	public int getProductCount() {
		return productCount;
	}
	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
