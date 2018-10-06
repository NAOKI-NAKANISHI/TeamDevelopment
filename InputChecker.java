package com.internousdev.sampleweb.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import java.util.List;

import org.apache.commons.lang3.StringUtils
;
import org.omg.CORBA.PRIVATE_MEMBER;

import com.ibm.icu.util.BytesTrie.Entry;
import com.sun.javafx.collections.MappingChange.Map;
public class InputChecker {
	/**
	 * このプログラムは渡された値を正規表現か検証するものです。
	 * 使い方はdoCheckに9つの引数を渡します。後半部分には当てはまる形式にtrueを入れます。
	 * emailを判別したいのであれば英字、数字、記号を含むため下記のようになります。
	 * 例(質問の内容,値,最小文字数,最大文字数,true,false,false,true,true)となります。
	 * 結果はList形式で渡されることに気を付けてください。
	 ***/
	public static final int TYPE_HIRAGANA = 1 << 0;
	public static final int TYPE_KATAKANA = 1 << 1;
	public static final int TYPE_KANJI    = 1 << 2;
	public static final int TYPE_HANKAKU_NUMBER   = 1 << 3;
	public static final int TYPE_HANKAKU_SYMBOL   = 1 << 4;
	public static final int TYPE_HANKAKU_ALPHABET = 1 << 5;
	
	//ビットフィールドを用いた場合。ビットフィールドを用いるとdoCheckメソッドのように大量にフラグを渡す必要がなくなり、or演算子を用いて引数は1つで判定可能
	//このテクニックは便利なので使えるようにする
	public List<String> checkInputeString(String fieldName, String target, int type, int min, int max){
		List<String> messageList = new ArrayList<String>();					//エラーメッセージを格納するためのリスト
		if(type == 0) {
			return messageList;
		}
        if (target.length() < min || target.length() > max) {
        	messageList.add(fieldName + "は" + min + "文字以上" + max + "文字以下で入力してください。");
        	return messageList;
		}    
		String[] patternList = {
				"ぁ-ん",
				"ァ-ヺ",
				"一-龠",
				"0-9",
				"@.,;:!#$%&'*+-/=?^_`{|}~",
				"a-zA-Z"
		};
		String[] allCharacterTypeList = {
				"ひらがな",
				"カタカナ",
				"漢字",
				"数字",
				"記号",
				"英字"
		};
		
		String patternString = "^[";
		List<String> requiredCharacterTypeList = new ArrayList<String>();
		for(int patternIndex = 0;patternIndex <= 5;patternIndex++) {
			if(((type >> patternIndex) & 1) == 0) {
				continue;
			}
			String subsetString = patternList[patternIndex];
			String description = allCharacterTypeList[patternIndex];
			patternString += subsetString;
			requiredCharacterTypeList.add(description);
		}
		patternString +="]+$";
		
		String requiredCharacterType = "";
		for(int i = 0;i < requiredCharacterTypeList.size();i++){
			if(i == 0){
				requiredCharacterType += requiredCharacterTypeList.get(i).toString();
			}else{
				requiredCharacterType += "、" + requiredCharacterTypeList.get(i).toString();
			}
		}

        Pattern patternCheck = Pattern.compile(patternString);//正規表現を用いてチェックを行う
        Matcher matcher = patternCheck.matcher(target);
        
        if(!(matcher.find()) && messageList.isEmpty()) {
        	messageList.add(fieldName + "は" + requiredCharacterType + "で入力してください。");
	    }
        
		return messageList;
	}
	
	public List<String> checkPattern(String type, String target, String triger, int min, int max) {
		List<String> messageList = new ArrayList<String>();					//エラーメッセージを格納するためのリスト
		HashMap<String, String> map = new HashMap<>();
		
		if(StringUtils.isEmpty(target)){
			messageList.add(type + "を入力してください。");
		}
		map.put("半角英字、漢字、ひらがな", "\\b([一-龠a-zA-Zぁ-ゞ])+\\b");
		map.put("ひらがな", "\\b([ぁ-ゞ])+\\b");
		map.put("半角英数字、半角記号", "\\b([0-9a-zA-Z-/:-@\\[-\\`\\{-\\~])+\\b");
		map.put("半角英数字", "\\b([0-9a-zA-Z])+\\b");
		map.put("半角英数字、漢字、ひらがな、カタカナ、半角記号", "\\b([0-9a-zA-Z一-龠ぁ-ゞァ-ヶ-/:-@\\[-\\`\\{-\\~])+\\b");
		map.put("半角数字", "\\b([0-9])+\\b");
		map.put("半角英数字、漢字、ひらがな、カタカナ", "\\b([0-9a-zA-Z一-龠ぁ-ゞァ-ヶ])+\\b");
		
		/*	三項演算子を用いて強引に１行で書いた場合はこうなる。
		 
		String pattern = "半角英字、漢字、ひらがな".equals(triger) ? "\\b([一-龠a-zA-Zぁ-ゞ])+\\b" : 
					"ひらがな".equals(triger) ? "\\b([ぁ-ゞ])+\\b" : 
				 	 "半角英数字、半角記号".equals(triger) ? "\\b([0-9a-zA-Z-/:-@\\[-\\`\\{-\\~])+\\b" : 
				 "半角英数字".equals(triger) ? "\\b([0-9a-zA-Z])+\\b" :
				 		 "半角英数字、漢字、ひらがな、カタカナ、半角記号".equals(triger) ? "\\b([0-9a-zA-Z一-龠ぁ-ゞァ-ヶ-/:-@\\[-\\`\\{-\\~])+\\b" : 
				 		 "半角数字".equals(triger) ? "\\b([0-9])+\\b" : "\\b([0-9a-zA-Z一-龠ぁ-ゞァ-ヶ])+\\b";
			
		Pattern patternCheck = Pattern.compile(pattern));
 		*/
		
        Pattern patternCheck = Pattern.compile(map.get(triger));//正規表現を用いてチェックを行う
        Matcher matcher = patternCheck.matcher(target);
        
        if(!(matcher.find()) && messageList.isEmpty()) {
        	messageList.add(type + "は" + triger + "で入力してください。");
	    }
        if (target.length() < min || target.length() > max) {
        	messageList.add(type + "は" + min + "文字以上" + max + "文字以下で入力してください。");
		}    
		return messageList;
	}

		
	
	public List<String> doCheck(String propertyName,String value,int minLength,int maxLength,boolean availableAlphabeticCharacters,boolean availableKanji,boolean availableHiragana,boolean availableHalfWidthDigit,boolean availableHalfWidthSymbols,boolean availableKatakana,boolean availableFullWidthSymbols){

		//検証した結果を入れるList
				List<String> stringList = new ArrayList<String>();
				List<String> characterTypeList = new ArrayList<String>();

				//入力欄が空かどうかを検証します
				if(StringUtils.isEmpty(value)){
					stringList.add(propertyName + "を入力してください。");
				}

				//入力欄が最小文字数以上、最大文字数以下かどうかを検証します
				if(value.length() < minLength || value.length() > maxLength){
					stringList.add(propertyName + "は" + minLength + "文字以上" + maxLength + "文字以下で入力してください。");
				}


				///////////入力された文字のタイプから何を判別するか決めます//////////
				String regularExpression = "";
				String errorExpression = "";


				if(availableAlphabeticCharacters || availableKanji || availableHiragana || availableHalfWidthDigit || availableHalfWidthSymbols||availableKatakana||availableFullWidthSymbols){
					regularExpression = "[^";
				}
				if(!(availableAlphabeticCharacters) || !(availableKanji) || !(availableHiragana) || !(availableHalfWidthDigit) || !(availableHalfWidthSymbols)|| !(availableKatakana)|| !(availableFullWidthSymbols)){
					errorExpression = "[^";
				}

				if(availableAlphabeticCharacters){
					regularExpression +="a-zA-Z";
						characterTypeList.add("半角英字");
				}else{
					errorExpression += "a-zA-Z";
				}

				if(availableKanji){
					regularExpression +="一-龯";
					characterTypeList.add("漢字");
				}else{
					errorExpression +="一-龯";
				}

				if(availableHiragana){
					regularExpression +="ぁ-ん";
					characterTypeList.add("ひらがな");
				}else{
					errorExpression +="ぁ-ん";
				}

				if(availableHalfWidthDigit){
					regularExpression +="0-9";
					characterTypeList.add("半角数字");
				}else{
					errorExpression+="0-9";
				}

				if(availableHalfWidthSymbols){
					regularExpression +="@.,;:!#$%&'*+-/=?^_`{|}~";
					characterTypeList.add("半角記号");
				}else{
					errorExpression +="@.,;:!#$%&'*+-/=?^_`{|}~";
				}

				if(availableKatakana){
					regularExpression +="ァ-ヺ";
					characterTypeList.add("カタカナ");
				}else{
					errorExpression +="ァ-ヺ";
				}

				if(availableFullWidthSymbols){
					regularExpression +="＠．，；：！＃＄％＆’＊＋―／＝？＾＿｀｛｜｝～";
					characterTypeList.add("全角記号");
				}else{
					errorExpression +="＠．，；：！＃＄％＆’＊＋―／＝？＾＿｀｛｜｝～";
				}

				if(!StringUtils.isEmpty(regularExpression)){
					regularExpression +="]+";
				}
				if(!StringUtils.isEmpty(errorExpression)){
					errorExpression +="]+";
				}
				////////////////////////////ここまで///////////////////////////


				//判別した項目に応じてエラーメッセージを返します
				String characterType = "";
				for(int i = 0;i < characterTypeList.size();i++){
					if(i == 0){
						characterType += characterTypeList.get(i).toString();
					}else{
						characterType += "、" + characterTypeList.get(i).toString();
					}
				}
				if(errorExpression.equals("")){
					if(value.matches(regularExpression)){
						stringList.add(propertyName + "は" + characterType + "で入力してください。");
					}
				}else{
					if(value.matches(regularExpression)||(!value.matches(errorExpression)&&!value.equals(""))){
						stringList.add(propertyName + "は" + characterType + "で入力してください。");
					}
				}

				return stringList;
			}


	//一度目のパスワードと二度目のパスワードが同じかを検証します。
	public List<String> doPasswordCheck(String password,String reConfirmationPassword){
		List<String> stringList = new ArrayList<String>();
		if(!(password.equals(reConfirmationPassword))){
			stringList.add("入力されたパスワードが異なります。");
		}
		return stringList;
	}
}
