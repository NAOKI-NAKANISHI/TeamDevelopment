package com.internousdev.i1810a.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.i1810a.dto.CartInfoDTO;
import com.internousdev.i1810a.util.DBConnector;

public class CartInfoDAO {

	 /*ResultSetで列のデータを取得する際は列番号で指定したほうが多少効率的
	 *以下を参照
	 *https://docs.oracle.com/javase/jp/1.4/guide/jdbc/getstart/resultset.html
	 *今回は列名で書いたほうがわかりやすいので列値取得は列名で指定している
	 */

	public List<CartInfoDTO> getCartInfoDtoList(String loginId) {
		DBConnector dbConnector = new DBConnector();

		List<CartInfoDTO> cartInfoDtoList = new ArrayList<CartInfoDTO>();
		//以下のSQL文はユーザごとのカートの中身を外部結合を用いて一覧を表示している
		String sql="select"
		+ " ci.id as id,"
		+ " ci.user_id as user_id,"
		+ " ci.temp_user_id as temp_user_id,"
		+ " ci.product_id as product_id,"
		+ " sum(ci.product_count) as product_count,"
		+ " pi.price as price,"
		+ " pi.regist_date as regist_date,"
		+ " pi.update_date as update_date,"
		+ " pi.product_name as product_name,"
		+ " pi.product_name_kana as product_name_kana,"
		+ " pi.product_description as product_description,"
		+ " pi.category_id as category_id,"
		+ " pi.image_file_path as image_file_path, "
		+ " pi.image_file_name as image_file_name, "
		+ " pi.release_date as release_date,"
		+ " pi.release_company as release_company,"
		+ " pi.status as status,"
		+ " (sum(ci.product_count) * pi.price) as subtotal"
		+ " FROM cart_info as ci"				//cart_infoを別名としてciと名づけている
		+ " LEFT JOIN product_info as pi"		//product_infoを別名としてpiと名づけている
		+ " ON ci.product_id = pi.product_id"	//LEFT JOIN 左側に指定された表(cart_info)のすべての行が表示される
		+ " WHERE ci.user_id = ?"				//ONで結合する条件を指定
		+ " group by product_id";				//productIDでグループ化

		try{
			Connection con = dbConnector.getConnection();

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, loginId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				CartInfoDTO cartInfoDTO = new CartInfoDTO();
				cartInfoDTO.setId(rs.getInt("id"));
				cartInfoDTO.setUserId(rs.getString("user_id"));
				cartInfoDTO.setTempUserId(rs.getString("temp_user_id"));
				cartInfoDTO.setProductId(rs.getInt("product_id"));
				cartInfoDTO.setProductCount(rs.getInt("product_count"));
				cartInfoDTO.setPrice(rs.getInt("price"));
				cartInfoDTO.setRegistDate(rs.getDate("regist_date"));
				cartInfoDTO.setUpdateDate(rs.getDate("update_date"));
				cartInfoDTO.setProductName(rs.getString("product_name"));
				cartInfoDTO.setProductNameKana(rs.getString("product_name_kana"));
				cartInfoDTO.setProductDescription(rs.getString("product_description"));	//使用しない？
				cartInfoDTO.setCategoryId(rs.getInt("category_id"));					//使用しない？
				cartInfoDTO.setImageFilePath(rs.getString("image_file_path"));
				cartInfoDTO.setImageFileName(rs.getString("image_file_name"));
				cartInfoDTO.setReleaseDate(rs.getDate("release_date"));
				cartInfoDTO.setReleaseCompany(rs.getString("release_company"));
				cartInfoDTO.setStatus(rs.getString("status"));							//使用しない？
				cartInfoDTO.setSubtotal(rs.getInt("subtotal"));
				cartInfoDtoList.add(cartInfoDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cartInfoDtoList;
	}

	public int getTotalPrice(String userId) {
		int totalPrice = 0;
		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		//as は別名
		String sql = "select sum(product_count * price) as total_price from cart_info where user_id=? group by user_id";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet resultSet = ps.executeQuery();
			if(resultSet.next()) {
				totalPrice = resultSet.getInt("total_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return totalPrice;
	}

	public int regist(String userId, String tempUserId, int productId, String productCount, int price) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "insert into cart_info(user_id, temp_user_id, product_id, product_count, price, regist_date,update_date)"
				+ " values (?, ?, ?, ?, ?, now(), now())";
		/*
		 * NOW([fsp])
		 * 現在の日付と時間を 'YYYY-MM-DD HH:MM:SS' または YYYYMMDDHHMMSS 書式の値として返します。
		 * MySQL 5.6.4 の時点では、0 から 6 までの小数秒の精度を指定するために fsp 引数が指定されている場合は、
		 * その桁数の小数秒部分が戻り値に含まれます。5.6.4 よりも前では、すべての引数が無視される。
		 */
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);
			preparedStatement.setString(2, tempUserId);
			preparedStatement.setInt(3, productId);
			preparedStatement.setString(4, productCount);
			preparedStatement.setInt(5, price);
			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	public int delete(String productId,String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "delete from cart_info where product_id=? and user_id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, productId);
			preparedStatement.setString(2, userId);
			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteAll(String userId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		int count = 0;
		String sql = "delete from cart_info where user_id=?";

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userId);

			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public boolean isExistsCartInfo() {

		return false;
	}

	public void linkToLoginId(String tempUserId, String loginId) {
		DBConnector dbConnector = new DBConnector();
		Connection connection = dbConnector.getConnection();
		String sql = "update cart_info set user_id=?, temp_user_id = null where temp_user_id=?";
		int count =0;

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginId);
			preparedStatement.setString(2, tempUserId);
			count = preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
