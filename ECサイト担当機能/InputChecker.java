package com.internousdev.i1810a.util;
import java.util.ArrayList;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/*
 * 	使用する際は以下のような記述でお願いします。
 *
 * 	このメソッドは入力値に対して、正規表現で判定する文字種が含まれていれば、からのリストを返し、
 * 	正規表現で判定する文字種が含まれていなければ、エラーメッセージをリストに格納して返す。
 * 	判定する文字種は可変長引数で受け取るので、何種類でも指定することが可能
 *
 *	 姓のチェックをする場合
 *	private List<String> familyNameErrorMessageList = new ArrayList<String>(); //エラーメッセージを格納するリスト
 *
 *  familyNameErrorMessageList = InputChecker.checkPattern("姓", familyname, 1, 16, "半角英字, "漢字", "ひらがな");
 *
 *  引数には、チェック対象名、入力された値、最小文字数、最大文字数、正規表現で判定する文字種を指定
 *
 *  ページの遷移先の決定は、リストがからであればSUCCESSを返し、リストの中身にエラーメッセージが格納されていれば、
 *  ERRORを返す条件判定を作成する。
 *
 *  正規表現で判定する文字種は以下のとおり 以下のパターンをそのままcheckPattern()メソッドの第5引数に指定
 *
 *	半角英語
 *	漢字
 *	ひらがな
 *	カタカナ
 *	半角英数字
 *	半角記号
 *	半角数字
 *
 */

public class InputChecker {
	//checkPatternメソッドで指定された文字種で入力値をチェック
	public List<String> checkPattern(String type, String target, int min, int max, String... triger){
		List<String> messageList = new ArrayList<String>();		//エラーメッセージを格納するためのリスト
		String pattern ="[";
		
		if(StringUtils.isEmpty(target)){						//StringUtils.isEmptyは文字列がnullでもヌルポがでない
			messageList.add(type + "を入力してください。");
		}
		for(String str:triger) {
			if (str.equals("半角英語"))	pattern += "a-zA-Z";
			if (str.equals("半角英数字"))	pattern += "0-9a-zA-Z";
			if (str.equals("漢字"))		pattern += "一-龠";
			if (str.equals("ひらがな"))	pattern += "ぁ-ゞー";
			if (str.equals("カタカナ"))	pattern += "ァ-ヶ";
			if (str.equals("半角記号"))	pattern += "@ー.,;:!#$%&'*+-/=?^_`{|}~";
			if (str.equals("半角数字"))	pattern += "0-9";
		}
		pattern += "&&[^ 　]]+$";								//否定は減算を使用し、記述は&&[^hoge]とする。
        Pattern patternCheck = Pattern.compile(pattern);		//正規表現を用いてチェックを行う
        Matcher matcher = patternCheck.matcher(target);
        
        if(!(matcher.matches()) && messageList.isEmpty()) {		//matches()は全件一致 find()は部分一致
        	messageList.add(type + "は" + String.join("、", triger) + "で入力してください。");
	    }
		if (target.length() < min || target.length() > max) {
        	messageList.add(type + "は" + min + "文字以上" + max + "文字以下で入力してください。");
		}
        return messageList;
	}

	public List<String> doPasswordCheck(String password,String reConfirmationPassword){
		List<String> stringList = new ArrayList<String>();
		if(!(password.equals(reConfirmationPassword))){
			stringList.add("入力されたパスワードが異なります。");
		}
		return stringList;
	}
}
