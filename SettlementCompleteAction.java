package com.internousdev.i1810a.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.CartInfoDAO;
import com.internousdev.i1810a.dao.PurchaseHistoryInfoDAO;
import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.dto.DestinationInfoDTO;
import com.internousdev.i1810a.dto.PurchaseHistoryInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class SettlementCompleteAction extends ActionSupport implements SessionAware{

	private String id;
	private String categoryId;
	private Map<String, Object> session;
	private String userId;

	public String execute() {
		String result = ERROR;
		if(!(session.get("userId") == null)){
			userId = session.get("userId").toString();

		}


		@SuppressWarnings("unchecked")
		ArrayList<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = (ArrayList<PurchaseHistoryInfoDTO>)session.get("purchaseHistoryInfoDtoList");
		System.out.println("purchaseHistoryInfoDtoListの中身を表示");
		for(PurchaseHistoryInfoDTO dao:purchaseHistoryInfoDtoList) {
			System.out.println(dao.getProductId());
			System.out.println(dao.getProductName());
			System.out.println(dao.getProductCount());
		}

		@SuppressWarnings("unchecked")
		//以下の行で登録されている住所を取得
		ArrayList<DestinationInfoDTO> destinationInfoDtoList = (ArrayList<DestinationInfoDTO>)session.get("destinationInfoDtoList");

		for(int i=0;i<purchaseHistoryInfoDtoList.size();i++) {
			purchaseHistoryInfoDtoList.get(i).setDestinationId(destinationInfoDtoList.get(0).getId());
		}

		PurchaseHistoryInfoDAO purchaseHistoryInfoDAO = new PurchaseHistoryInfoDAO();
		int count = 0;
		System.out.println("registの引数を表示");
		for(int i=0; i<purchaseHistoryInfoDtoList.size();i++) {
			count += purchaseHistoryInfoDAO.regist(userId,
					purchaseHistoryInfoDtoList.get(i).getProductId(),
					purchaseHistoryInfoDtoList.get(i).getProductCount(),
					purchaseHistoryInfoDtoList.get(i).getDestinationId(),
					purchaseHistoryInfoDtoList.get(i).getSubtotal()
					);
			System.out.println(purchaseHistoryInfoDtoList.get(i).getProductId());
			System.out.println(purchaseHistoryInfoDtoList.get(i).getProductCount());
			System.out.println(purchaseHistoryInfoDtoList.get(i).getSubtotal());
		}
		if(count > 0) {
			CartInfoDAO cartInfoDAO = new CartInfoDAO();
			if(count > 0) {
			count = cartInfoDAO.deleteAll(userId);



				List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();
				cartInfoDtoList = cartInfoDAO.getCartInfoDtoList(userId);
				Iterator<CartInfoDTO> iterator = cartInfoDtoList.iterator();
				if(!(iterator.hasNext())) {
					cartInfoDtoList = null;
				}
				session.put("cartInfoDtoList", cartInfoDtoList);

				int totalPrice = Integer.parseInt(String.valueOf(cartInfoDAO.getTotalPrice(userId)));
				session.put("totalPrice", totalPrice);
				result = SUCCESS;

		}}
		//session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDAO);
		return result;

	}

	public String getId() {
	return id;
	}

	public void setId(String id) {
	this.id = id;
	}

	public String getCategoryId() {
	return categoryId;
	}
	public void setCategoryId(String categoryId) {
	this.categoryId = categoryId;
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
