<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" href="./css/i1810a.css">
<title>カート画面</title>

</head>
<body>
	<jsp:include page="header.jsp" />
	<div class="main-contents">
		<div id="page-name">
			<h1>カート画面</h1>
		</div>


		<s:if test="#session.checkListErrorMessageList!=null">
			<div class="error">
				<div class="error-message">
					<div class="red">
						<s:iterator value="#session.checkListErrorMessageList">
							<s:property />
						</s:iterator>
					</div>
				</div>
			</div>
			<br>
		</s:if>


		<s:if test="#session.cartInfoDtoList.size()>0">
			<s:form id="form" action="SettlementConfirmAction">
			<s:token/>
				<s:iterator value="#session.cartInfoDtoList">
					<div class="cart-box">
						<div class=border></div>
						<div class="cart-box-in">
							<div class="cart-layaut-left">
								<div class="checkbox">
									<s:checkbox name="checkList" value="checked" fieldValue="%{productId}" />
								</div>
							</div>

							<div class="cart-layaut-center">
								<img
									src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>'
									class="cart-img" />
							</div>

							<div class="cart-layaut-right">

								<div class="cart-text">

									<table class="vertical-list-table-mini2">

										<tr>
											<th scope="row"><s:label value="商品名" /></th>
											<td><s:property value="productName" /></td>
										</tr>

										<tr>
											<th scope="row"><s:label value="ふりがな" /></th>
											<td><s:property value="productNameKana" /></td>
										</tr>

										<tr>
											<th scope="row"><s:label value="値段" /></th>
											<td><s:property value="price" />円</td>
										</tr>

										<tr>
											<th scope="row"><s:label value="発売会社名" /></th>
											<td><s:property value="releaseCompany" /></td>
										</tr>

										<tr>
											<th scope="row"><s:label value="発売年月日" /></th>
											<td><s:property value="releaseDate" /></td>
										</tr>

										<tr>
											<th scope="row"><s:label value="購入個数" /></th>
											<td><s:property value="productCount" />個</td>
										</tr>

										<tr>
											<th scope="row"><s:label value="合計金額" /></th>
											<td><s:property value="subtotal" />円</td>
										</tr>

									</table>
								</div>
							</div>

						</div>
					</div>
				</s:iterator>
				<div class=border></div>

				<div class="center">
					<div class="red">
						<div class="cart-total-price">

							<s:label value="カート合計金額 :" />
							<s:property value="#session.totalPrice" />
							円 <br>
						</div>
						<br>
						<div class="submit-btn-box-all">
							<div id="contents-btn-set">
								<s:submit value="決済" class="submit-btn-login" />
							</div>
						</div>
						<br>

						<div class="submit-btn-box-all">
							<div id="contents-btn-set">
								<s:submit value="削除" class="submit-btn-login"
									onclick="this.form.action='DeleteCartAction';" />
							</div>
						</div>
					</div>

				</div>

			</s:form>
		</s:if>
		<s:else>
			<div class="center">
				<div class="info">
					<h3>カート情報はありません。</h3>
				</div>
			</div>
		</s:else>
	</div>
	<br>
	<s:include value="footer.jsp" />
</body>
</html>