package com.internousdev.i1810a.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.CartInfoDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.dto.MCategoryDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware{
	private Collection<String> checkList; //Cart.jspでチェックを入れた場合は値としてIDが入る
	private String productId;
	private String userId;
	private List<MCategoryDTO> mCategoryDtoList = new ArrayList<MCategoryDTO>();

	private Map<String, Object> session;
	public String execute() {
		String result = ERROR;

		session.remove("checkListErrorMessageList");
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		int count = 0;
		List<String> checkListErrorMessageList = new ArrayList<String>();
		//Cart.jspでチェックを入れなかった場合
		if(checkList==null) {
			checkListErrorMessageList.add("チェックされていません。");
			//Sessionに入れているのにボタン遷移した際に
			session.put("checkListErrorMessageList", checkListErrorMessageList);
			return result;
		}
//		ユーザーIDを取得
		if(session.containsKey("userId")) {
			userId = String.valueOf(session.get("userId"));
		}else if (session.containsKey("tempUserId")) {
			userId = String.valueOf(session.get("tempUserId"));
		}

		//複数のチェックを入れた場合
		for(String productId:checkList) {
			count += cartInfoDAO.delete(productId,userId);//チェックを入れた数だけ削除
		}
		if(count <= 0) {
			checkListErrorMessageList.add("チェックされていません。");
			session.put("checkListErrorMessageList", checkListErrorMessageList);
			result = ERROR;
		}else {
			List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();

			//削除した結果を反映し、カートの中身を再更新
			cartInfoDtoList = cartInfoDAO.getCartInfoDtoList(userId);
			for(CartInfoDTO dto:cartInfoDtoList) {
				System.out.println(dto.getProductName());
			}
			Iterator<CartInfoDTO> iterator = cartInfoDtoList.iterator();
			if(!(iterator.hasNext())) {
				cartInfoDtoList = null;
			}
			session.put("cartInfoDtoList", cartInfoDtoList);
			int totalPrice = Integer.parseInt(String.valueOf(cartInfoDAO.getTotalPrice(userId)));
			session.put("totalPrice", totalPrice);

			sexList.add("男性");
			sexList.add("女性");
			result=SUCCESS;
		}
		return result;
	}
	public Collection<String> getCheckList() {
		return checkList;
	}
	public void setCheckList(Collection<String> checkList) {
		this.checkList = checkList;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
