package com.internousdev.i1810a.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.i1810a.dao.DestinationInfoDAO;
import com.internousdev.i1810a.dto.DestinationInfoDTO;
import com.internousdev.i1810a.dto.PurchaseHistoryInfoDTO;
import com.internousdev.i1810a.util.CommonUtility;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

public class SettlementConfirmAction extends ActionSupport implements SessionAware{


	private String categoryId;
	private Collection<String> checkList;
	private String productId;
	private String productName;
	private String productNameKana;
	private String imageFilePath;
	private String imageFileName;
	private String price;
	private String releaseCompany;
	private String releaseDate;
	private String productCount;
	private String subtotal;
	private Map<String, Object> session;
	private boolean loginFlg;
	private int logined ;
	private String userId;
	
	public String execute() {

		String result = ERROR;

		loginFlg = false;
		
		//Cart.jspですでにエラー表示がされた後にチェックを入れて遷移してきた場合にセッションをクリアする処理を以下に実装するが、
		//ブラウザの「戻る」ボタンは特別なことをしない限り、通常はサーバとのやりとりをせずにクライアントが持つキャッシュをそのまま表示するので
		//セッションをクリアしても、ブラウザの戻るボタンを押されるとセッションは空なのに値が表示されてしまう。
		//解析ツールでHTTP通信みても通信されないことがわかる
		//この問題はi1810aサイトのすべてで問題になっている。ログアウトした後でもブラウザ戻るボタンで中身を表示できてしまうなど
//		こういった問題への対策は、いくつか考えられる
//
//		(1)ブラウザにBackボタンを表示しないようにする
//		(2)ブラウザのキャッシュが無効になるように制御する
//		(3)Backボタンで画面が戻されても、不整合が起こらないようにアプリケーションを設計する
		//ブラウザのキャッシュ機能を無効にするのが良い、開発中はキャッシュ機能無効化を推奨
		//以下で実装可能
		System.out.println(session.get("checkListErrorMessageList"));
		if(session.get("checkListErrorMessageList") != null && checkList != null) {
			session.remove("checkListErrorMessageList");
		}
		if(!(session.get("userId") == null)){
		userId = session.get("userId").toString();
		}
		System.out.println("1"+userId);

		if(!(session.get("logined") == null)){
		String loginedStr = session.get("logined").toString();
		logined = Integer.valueOf(loginedStr);
		}
		System.out.println("2"+logined);

		if(logined == 1){
//		if(session.containsKey("userId")) {
			DestinationInfoDAO destinationInfoDAO = new DestinationInfoDAO();
			List<DestinationInfoDTO> destinationInfoDtoList = new ArrayList<>();
			try {
				destinationInfoDtoList = destinationInfoDAO.getDestinationInfo(userId);
				System.out.println("aaaaaaaaaaaaaaa");
				System.out.println(session.get("userId").toString());
				System.out.println(destinationInfoDtoList);
				Iterator<DestinationInfoDTO> iterator = destinationInfoDtoList.iterator();
				if(!(iterator.hasNext())) {
					destinationInfoDtoList = null;
				}
				session.put("destinationInfoDtoList", destinationInfoDtoList);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		//Listを毎回初期化しないと商品履歴を表示した際に過去に購入した商品が何度も表示される
		List<PurchaseHistoryInfoDTO> purchaseHistoryInfoDtoList = new ArrayList<PurchaseHistoryInfoDTO>();

		CommonUtility commonUtility = new CommonUtility();
		String[] productIdList = commonUtility.parseArrayList(productId);
		String[] productNameList = commonUtility.parseArrayList(productName);
		String[] productNameKanaList = commonUtility.parseArrayList(productNameKana);
		String[] imageFilePathList = commonUtility.parseArrayList(imageFilePath);
		String[] imageFileNameList = commonUtility.parseArrayList(imageFileName);
		String[] priceList = commonUtility.parseArrayList(price);
		String[] releaseCompanyList = commonUtility.parseArrayList(releaseCompany);
		String[] releaseDateList = commonUtility.parseArrayList(releaseDate);
		String[] productCountList = commonUtility.parseArrayList(productCount);
		String[] subtotalList = commonUtility.parseArrayList(subtotal);
		for(int i=0;i<productIdList.length;i++) {
			PurchaseHistoryInfoDTO purchaseHistoryInfoDTO = new PurchaseHistoryInfoDTO();
			purchaseHistoryInfoDTO.setUserId(userId);
			purchaseHistoryInfoDTO.setProductId(Integer.parseInt(String.valueOf(productIdList[i])));
			purchaseHistoryInfoDTO.setProductName(String.valueOf(productNameList[i]));
			purchaseHistoryInfoDTO.setProductNameKana(String.valueOf(productNameKanaList[i]));
			purchaseHistoryInfoDTO.setImageFilePath(String.valueOf(imageFilePathList[i]));
			purchaseHistoryInfoDTO.setImageFileName(String.valueOf(imageFileNameList[i]));
			purchaseHistoryInfoDTO.setPrice(Integer.parseInt(String.valueOf(priceList[i])));
			purchaseHistoryInfoDTO.setReleaseCompany(String.valueOf(releaseCompanyList[i]));
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
				purchaseHistoryInfoDTO.setReleaseDate(simpleDateFormat.parse(String.valueOf(releaseDateList[i])));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			purchaseHistoryInfoDTO.setProductCount(Integer.parseInt(String.valueOf(productCountList[i])));
			purchaseHistoryInfoDTO.setSubtotal(Integer.parseInt(String.valueOf(subtotalList[i])));
			purchaseHistoryInfoDtoList.add(purchaseHistoryInfoDTO);
		}
		session.put("purchaseHistoryInfoDtoList", purchaseHistoryInfoDtoList);

		if(logined == 0){
//		if(!session.containsKey("userId")) {

			loginFlg = true;
			session.put("loginFlg", loginFlg);

			result = ERROR;
		}else {
			session.put("loginFlg", loginFlg);
			result = SUCCESS;
		}
		return result;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductNameKana() {
		return productNameKana;
	}
	public void setProductNameKana(String productNameKana) {
		this.productNameKana = productNameKana;
	}
	public String getImageFilePath() {
		return imageFilePath;
	}
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getReleaseCompany() {
		return releaseCompany;
	}
	public void setReleaseCompany(String releaseCompany) {
		this.releaseCompany = releaseCompany;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}
	public String getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(String subtotal) {
		this.subtotal = subtotal;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public boolean isLoginFlg() {
		return loginFlg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setLoginFlg(boolean loginFlg) {
		this.loginFlg = loginFlg;
	}
	public int getLogined() {
		return logined;
	}
	public void setLogined(int logined) {
		this.logined = logined;
	}


}
