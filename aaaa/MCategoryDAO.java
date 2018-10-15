package com.internousdev.i1810a.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.internousdev.i1810a.dto.MCategoryDTO;
import com.internousdev.i1810a.util.DBConnector;

public class MCategoryDAO {
	public List<MCategoryDTO> getMCategoryList(){
		DBConnector dbConnector = new DBConnector();//DBと接続準備
		Connection connection = dbConnector.getConnection();//DBと接続準備
		List<MCategoryDTO> mCategoryDtoList = new ArrayList<MCategoryDTO>();//MCategoryをインスタンス化して、ArryListに配列として設置
		String sql = "select * from m_category";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();//executeQuery()でsql文を送信して、resultSetで受け取る
			while(resultSet.next()) {//next()でデータをひとつずつ調べていく
				MCategoryDTO mCategoryDTO = new MCategoryDTO();//カテゴリー情報が格納されたMCategoryDTOをインスタンス化
				mCategoryDTO.setId(resultSet.getInt("id"));//getInt()で"id"をresultSetから取得し、MCategoryDTOのsettterのsetId()で"id"をmCategoryに格納
				mCategoryDTO.setCategoryId(resultSet.getInt("category_id"));//ほぼ同上
				mCategoryDTO.setCategoryName(resultSet.getString("category_name"));//ほぼ同上
				mCategoryDTO.setCategoryDescription(resultSet.getString("category_description"));//ほぼ同上
				mCategoryDTO.setInsertDate(resultSet.getDate("insert_date"));//ほぼ同上
				mCategoryDTO.setUpdateDate(resultSet.getDate("update_date"));//ほぼ同上
				mCategoryDtoList.add(mCategoryDTO);
			}
			Iterator<MCategoryDTO> iterator = mCategoryDtoList.iterator();
			if(!(iterator.hasNext())) {
				mCategoryDtoList = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mCategoryDtoList;
	}
}
