package com.internousdev.i1810a.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.CartInfoDAO;
import com.internousdev.i1810a.dao.MCategoryDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.dto.MCategoryDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware{
	private String categoryId;
	private String keywords;
	private List<MCategoryDTO> mCategoryDtoList = new ArrayList<MCategoryDTO>();
	private Map<String, Object> session;
	public String execute() {
		String result = ERROR;
		String userId = null;
		CartInfoDAO cartInfoDao = new CartInfoDAO();
		List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();

		if(session.containsKey("userId")) {
			//ログイン済み
			userId = String.valueOf(session.get("userId"));
		}else if (session.containsKey("tempUserId")) {
			//一時的なID
			userId = String.valueOf(session.get("tempUserId"));
		}
		session.remove("checkListErrorMessageList");
			cartInfoDtoList = cartInfoDao.getCartInfoDtoList(userId);
			Iterator<CartInfoDTO> iterator = cartInfoDtoList.iterator();
			if(!(iterator.hasNext())) {
				cartInfoDtoList = null;
			}
			session.put("cartInfoDtoList", cartInfoDtoList);//SQLでカートの中身を取得し、セッションに格納
		

		int totalPrice = Integer.parseInt(String.valueOf(cartInfoDao.getTotalPrice(userId)));
		session.put("totalPrice", totalPrice);
		result = SUCCESS;

		if(!session.containsKey("mCategoryList")) {
			MCategoryDAO mCategoryDao = new MCategoryDAO();
			//カテゴリー情報を取得
			//IDは1から4まで
			mCategoryDtoList = mCategoryDao.getMCategoryList();

			session.put("mCategoryDtoList", mCategoryDtoList);
		}
		return result;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
