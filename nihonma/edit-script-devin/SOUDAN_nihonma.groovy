//env.debug = true;
//@import justNameCard

/*
 * ジャストシステム様名刺用 EditScript
 * 値の管理は、injectionData で行います。
 * 　基本となるデータは、injectionData のルートのラベルで、これは、可変データ資材のデータと対応します。
 * 　ウイザード編集中は、「オモテ」「ウラ」カセットにステップインします。
 * 　ステップインした状態では、injectionData が、ステップインしたラベルに絞られますので、ルートが、"オモテ/xxxx" もしくは "ウラ/yyyy" に切り替わります。
 * 　よって、ステップインする前に、ルートのラベルを、「オモテ/」「ウラ/」の下にコピーします。
 * 　ウイザード編集での入力はすべて、「オモテ/xxxx」「ウラ/xxxx」に対する、仮想ラベルをターゲットとした入力で、パーツとは直接関係しません。（写真パーツのみ例外です）
 * 　ステップインした状態でのウイザード編集は、「オモテ/」「ウラ/」の値を更新する事を意味します。 編集時に入力された項目だけ、そのラベルが編集された事を示すフラグを立てます。
 * 　「オモテ/」「ウラ/」は、表面・裏面で編集するかしないかに関係なく、全ルートラベルをコピーします。 email / URLなど、表でも裏でも編集されるものがある為
 * 　ステップアウト後のステップで、ステップイン側のラベルのうち、編集されたものの値を、ルートラベルにコピーします。
 * 　結果的に、ルートラベルにすべての入力が反映されます。
 * 　ルートラベルの値は、表面・裏面に同名ラベルの文字パーツがある場合、それに流し込みます。（パーツは表と裏で別に分けて存在し、非表示です）
 * 　　これは、可変データ資材のデータ選択画面での編集時の、編集データ戻しの為です。
 * 　表示上のパーツは、Pで始まるラベルを付与しています。
 * 　仮想ラベルが更新されたら、表示のアップデート処理で、データラベルの値を整形して、パーツに流し込む値を、Pラベルのデータとしてセットします。（パーツに直接セットしません）
 * 　Pラベルにデータをセットし終えたら、全パーツの値更新を行います。※この時、全パーツはページ直下（実際にはステップインしているのでカセット直下）に存在して、グループパーツはありません。
 * 　Pラベルの値をパーツに反映し終えたら、必要に応じてレイアウト調整を行います。
 * 　画像はアップロードがあるので、ウイザード編集とP顔写真が、直結しています。画像をアップロード後、パーツから trackingId を取得して、ルートラベルに "cms:" + trackingId を設定します。（可変データ資材の画像列での trackingId指定方法に合わせてあります）
 * 　アップロードされた画像は、特別なパスに保存され、永遠に削除できません。（ドキュメントで使われていない事が明確な場合は、Edition CMSで直接削除しても構いません）
 */

//可変データ資材のラベルとイコールになっていないとNG。
//チェックを考えると、可変マスタとラベル名称は同じ並びにする。
//"表面パターン" / "裏面パターン" / "社員番号" は、差し込みラベルではないので除く
def getLabels() {
	return ["社員番号","役職名1","役職名2","役職名3","部署名1","部署名2","資格名1","資格名2","資格名3","氏名","氏名補足","ふりがな","email","url","携帯電話番号","携帯電話補足","住所名称","郵便番号","住所1","住所2","代表電話番号","電話番号","FAX番号","住所2住所名称","住所2郵便番号","住所2住所1","住所2住所2","住所2代表電話番号","住所2電話番号","住所2FAX番号","メッセージ1","メッセージ2","メッセージ（小）1","メッセージ（小）2","役職名（英語）1","役職名（英語）2","部署名（英語）1","資格名（英語）1","資格名（英語）2","資格名（英語）3","NAME（英語）","mobile（英語）","ADDRESS1（英語）","ADDRESS2（英語）","ADDRESS3（英語）","REPRESENTATIVE（英語）","TELEPHONE（英語）","FAX（英語）","email（英語）","url（英語）","顔写真","スポーツロゴ","選択画像1","選択画像2","選択画像3","選択画像4","選択画像5","選択画像6","選択画像7","キャッチ1","キャッチ2","選択画像1（英語）","選択画像2（英語）","選択画像3（英語）","キャッチ1（英語）","キャッチ2（英語）"];
//ギネスとの差分は"窓口画像追加"のみ・・・"顔写真"のあとに追加済み

}

//カセット一覧（表面・裏面とも）
//カセットが増えた場合は、ここに追加してください。
// code: 管理番号 , name: カセットの名前（処理では使っていません）, trackingId:カセットの識別コード
def getCassetteMap() {
	return [
		['code':'F0101','name':'日本投資ファンド','trackingId':'fd82e715ac1f0f8546d708543c477b26'],
		['code':'F0201','name':'日本PMIコンサルティング_表1','trackingId':'58f3051bac1f13b86ca7857ea32a28e7'],
	//	['code':'F0301','name':'経営プランニング研究所_表1','trackingId':'34b52688ac1f13b84a442d8b700bbcf3'],
		['code':'F0302','name':'経営プランニング研究所','trackingId':'09e617ddac1f0f85023d53ff7f87b05a'],
	//	['code':'F0401','name':'日本M＆AセンターHD_横表1','trackingId':'2fa997f2ac1f13b8587ea974351e0e6c'],
		['code':'F0402','name':'日本M＆AセンターHD','trackingId':'586f02fcac1f13b83c852856884f2066'],
	//	['code':'F0501','name':'日本M＆A_表1(ｷﾞﾈｽﾛｺﾞ入)','trackingId':'54349f5bac1f13b87d72b63479c4ed9a'],
		['code':'F0502','name':'(本社)日本M＆Aセンター','trackingId':'1ba8002cac1f07837962d562c53c67ee'],
	//	['code':'F0601','name':'日本M＆A_表2(ｷﾞﾈｽﾛｺﾞ入)','trackingId':'34ede374ac1f13b81832d25ea9c78adf'],
		['code':'F0602','name':'(他拠点)日本M＆Aセンター','trackingId':'1b547cf5ac1f078378272b36838a544a'],
		['code':'F0701','name':'日本投資ファンド_表2','trackingId':'540ee24cac1f13b8023a64272e99b07d'],
		['code':'F0801','name':'MAable','trackingId':'f233e5fcac1f13b876b0f31fc1c2e6a0'],
		['code':'F0901','name':'ネクストナビ','trackingId':'c5163de7ac1f13b879b154a072d9cc80'],
		['code':'F1001','name':'AtoG','trackingId':'f16974adac1f0f857fce7a051c58cc69'],
	//	['code':'F1101','name':'日本PMIコンサルティング_表2_HDﾛｺﾞ無','trackingId':'a4543f96ac1f0f851675a65ef0026068'],
	//	['code':'F1201','name':'みやぎ経営相談窓口','trackingId':'2ee44f86ac1f0f856047508d14b921fa'],
	//	['code':'F1301','name':'しずおか経営相談窓口','trackingId':'02293f8eac1f07832a8bf9365d13642e'],
		['code':'F1501','name':'日本DX人材センター','trackingId':'4ccb8689ac1f0f855027a35d8b7cd898'],
		['code':'F1601','name':'健康保険組合','trackingId':'0789bfaeac1f0f85789be627e20d4c80'],
	//	['code':'F1701','name':'いばらき経営相談窓口','trackingId':'9d15ad77ac1f07830120ac84b0c5d5f9'],
	//	['code':'F1801','name':'にいがた経営相談窓口','trackingId':'aa10862fac1f078308fa611d9a9aa931'],
		['code':'F1901','name':'日本サーチファンド','trackingId':'d79e03f3ac1f07832919e88a588b0f1a'],
		['code':'F2001','name':'相談窓口1拠点用','trackingId':'64f002b0ac1f078353f440adb755f015'],
		['code':'F2002','name':'相談窓口2拠点用','trackingId':'650e9840ac1f078351f641da2a69c52b'],
		['code':'B0101','name':'パーパス','trackingId':'1bae89a5ac1f078338a0cd6430e27ca9'],
		['code':'B0201','name':'THE OWNER','trackingId':'1bae3c11ac1f078303f60505614e3f20'],
		['code':'B0301','name':'日本投資ファンド_英語','trackingId':'3f186de6ac1f13b86ed95e04b6aacf7c'],
	//	['code':'B0401','name':'日本M＆A_業界再編部裏','trackingId':'ab8a0625ac1f13b87df61c61f54f5778'],
		['code':'B0501','name':'英語','trackingId':'5911338dac1f13b806a1071788f53127'],
		['code':'B0502','name':'英語2','trackingId':'8d3df069ac1f07831ea83d064402cb6c'],
		['code':'B0601','name':'HD_英語','trackingId':'39c528a8ac1f13b82c145a04d3f11b74'],
		['code':'B0701','name':'THE OWNER(プロフ)','trackingId':'1baeae82ac1f0783068db448cc885791'],
		['code':'B0801','name':'TPM','trackingId':'1baee36dac1f07832574132d65dbfebc'],
		['code':'B0901','name':'ネクストナビ','trackingId':'f1ebcf82ac1f13b816a3a29b00984090'],
	//	['code':'B1001','name':'日本M＆A_TPM事業部裏','trackingId':'2f82fd98ac1f13b819b1477b27dda939'],
	//	['code':'B1101','name':'日本M＆A_拠点一覧裏','trackingId':'3f0624c7ac1f13b8075fb85836705307'],
		['code':'B1201','name':'白紙','trackingId':'550f9aa0ac1f13b80a200397cb5b0fb6'],
		['code':'B1301','name':'AtoG_英語','trackingId':'00b3c8f4ac1f0f852984266c5426aaa1'],
		['code':'B1401','name':'白紙（お問合せ窓口）','trackingId':'1badd24bac1f07836b65b65932067615'],
		['code':'B1501','name':'みやぎ経営相談窓口','trackingId':'1baf16a5ac1f078302d00388b656b08c'],
		['code':'B1701','name':'日本サーチファンド_英語','trackingId':'d75d3ba2ac1f078364a9b23b5e0cdb8b'],
	]
}

//カセット全体の共通流し込み値設定
// カセットごとにこれと異なるものは、各カセット専用関数で上書き設定する
// 　専用関数の関数名は、"pageUpdate" + カセットのコード
/*
def injectionCommon(prefix) {
    setPartsValue(prefix, "P役職名1", getValue("役職名1"));
    setPartsValue(prefix, "P部署名1", getValue("部署名1"));
    setPartsValue(prefix, "P部署名2", getValue("部署名2"));
    setPartsValue(prefix, "P資格名1", getValue("資格名1"));
    setPartsValue(prefix, "P資格名2", getValue("資格名2"));
    setPartsValue(prefix, "P資格名3", getValue("資格名3"));
    setPartsValue(prefix, "P氏名", getValue("氏名"));
    setPartsValue(prefix, "P氏名補足", getValue("氏名補足"));
    setPartsValue(prefix, "Pふりがな", getValue("ふりがな"));
    setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
    setPartsValue(prefix, "Purl", getValue("url"));
    setPartsValue(prefix, "P携帯電話番号", addPrePostText("携帯 ", getValue("携帯電話番号"), ""));
    setPartsValue(prefix, "P携帯電話補足", getValue("携帯電話補足"));
    setPartsValue(prefix, "P住所名称", getValue("住所名称"));
    setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
    setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], ""));
    setPartsValue(prefix, "P住所1", getValue("住所1"));
    setPartsValue(prefix, "P住所2", getValue("住所2"));
    setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel ", getValue("代表電話番号"), "（代表）"));
    setPartsValue(prefix, "P電話番号", addPrePostText("/ ", getValue("電話番号"), ""));
    setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));

    setPartsValue(prefix, "P住所2住所名称", getValue("住所2住所名称"));
    setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
    setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], ""));
    setPartsValue(prefix, "P住所2住所1", getValue("住所2住所1"));
    setPartsValue(prefix, "P住所2住所2", getValue("住所2住所2"));
    setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel ", getValue("住所2代表電話番号"), "（代表）"));
    setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax ", getValue("住所2FAX番号"), ""));

    setPartsValue(prefix, "Pメッセージ1", getValue("メッセージ1"));
    setPartsValue(prefix, "Pメッセージ2", getValue("メッセージ2"));
    setPartsValue(prefix, "Pメッセージ（小）1", getValue("メッセージ（小）1"));
    setPartsValue(prefix, "Pメッセージ（小）2", getValue("メッセージ（小）2"));

    setPartsValue(prefix, "P役職名（英語）1", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P部署名（英語）1", getValue("部署名（英語）1"));
    setPartsValue(prefix, "P資格名（英語）1", getValue("資格名（英語）1"));
    setPartsValue(prefix, "P資格名（英語）2", getValue("資格名（英語）2"));
    setPartsValue(prefix, "P資格名（英語）3", getValue("資格名（英語）3"));

    setPartsValue(prefix, "PNAME", getValue("NAME"));
    setPartsValue(prefix, "Pmobile", addPrePostText("Mobile ", getValue("mobile"), ""));
    setPartsValue(prefix, "PADDRESS1", getValue("ADDRESS1"));
    setPartsValue(prefix, "PADDRESS2", getValue("ADDRESS2"));
    setPartsValue(prefix, "PREPRESENTATIVE", addPrePostText("Direct ", getValue("REPRESENTATIVE"), ""));
    setPartsValue(prefix, "PTELEPHONE", addPrePostText("Tel ", getValue("TELEPHONE"),""));
    setPartsValue(prefix, "PFAX", addPrePostText("Fax ", getValue("FAX"),""));
}
*/

def pageUpdateF0101(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel ", getValue("代表電話番号"), ""));
//	setPartsValue(prefix, "P電話番号", addPrePostText("／ ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText((getValue("代表電話番号")? "／" : "Tel "), getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
    
    
    fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行4","P行5","P行6"], [getValue("資格名1"), getValue("資格名2"), getValue("資格名3")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 55.8, parent);

	//レイアウト調整
	setGroupingParts(["P行2","P行1","P行3","P行4","P行5","P行6", "jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P電話番号","PFAX番号"], [1.4, 2.5], 69.9, parent);
	leftAlign(["P携帯電話番号", "Pemail", "Purl"], [3.0, 3.0], 69.9, parent);
	bottomAlign([["P携帯電話番号", "Pemail","Purl"], ["P代表電話番号","P電話番号","PFAX番号"], ["P郵便番号","P住所"]], [0.3,0.3], parent);
}


def pageUpdateF0201(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel ", getValue("代表電話番号"), ""));
//	setPartsValue(prefix, "P電話番号", addPrePostText("/ ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText((getValue("代表電話番号")? "/ " : "Tel "), getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
    setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));
            
    fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行4","P行5"], [getValue("資格名1"), getValue("資格名2")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 70.1, parent);

	//レイアウト調整
	setGroupingParts(["P行2","P行1","P行3","P行4","P行5", "jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P電話番号","PFAX番号"], [1.4, 2.0], 70.1, parent);
	leftAlign(["P携帯電話番号", "Pemail"], [3.0], 70.1, parent);
	bottomAlign([["Purl"], ["P携帯電話番号", "Pemail"], ["P代表電話番号","P電話番号","PFAX番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [-0.3,-0.3,-0.3,0], parent);
}

def pageUpdateF0301(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));//4行追加
	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 81.0, parent);

	//レイアウト調整
	moveToPageCenter(["jidoriG"], parent);
	moveToPageCenter(["P郵便番号","P住所1"], parent);
	alignXByBoxRight("P住所1",["P住所2"], parent);
	alignXByReferencePoint("jidoriG", ["P行3","P行2","P行1"], parent);
	leftAlign(["P電話番号","PFAX番号"], [2.6], 81.0, parent);
	moveToPageCenter(["P電話番号","PFAX番号"], parent);
	setGroupingParts(["P行2","P行1","P行3", "jidoriG"],"block1",parent);
    bottomAlign([["P電話番号","PFAX番号"], ["P住所2"], ["P郵便番号","P住所1"], ["P社名ロゴ"]], [-0.5,-0.5,0], parent);
}

def pageUpdateF0302(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));//4行追加
	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 81.0, parent);

	//レイアウト調整
	moveToPageCenter(["jidoriG"], parent);
	moveToPageCenter(["P郵便番号","P住所1"], parent);
	alignXByBoxRight("P住所1",["P住所2"], parent);
	alignXByReferencePoint("jidoriG", ["P行3","P行2","P行1"], parent);
	leftAlign(["P電話番号","PFAX番号"], [2.6], 81.0, parent);
	moveToPageCenter(["P電話番号","PFAX番号"], parent);
	setGroupingParts(["P行2","P行1","P行3", "jidoriG"],"block1",parent);
    bottomAlign([["P電話番号","PFAX番号"], ["P住所2"], ["P郵便番号","P住所1"], ["P社名ロゴ"]], [-0.5,-0.5,-0], parent);
}

def pageUpdateF0401(prefix, parent) {
    //差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel  ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯番号補足"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));
    
    //このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "782af937ac1f078324d185ae7bf1ddb5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "764a520aac1f07830edf26446e0f3b34");
		break;

	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}
	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 45.1, parent);

    /*
	//差し込み
	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
    */

	//レイアウト調整
    def groupList = [];
    //氏名（jidoriG）を基準としただるま落とし↓
    moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
    moveParts("P行2", "","", 0, "P行3", "upper", 1, parent); //P行2 を P行3 の上1mmの位置に移動
    moveParts("P行1", "","", 0, "P行2", "upper", 1, parent); //P行1 を P行2 の上1mmの位置に移動
    //この場合、P行1～P行3は【左下基準】

    //氏名（jidoriG）を基準としたぶら下がり↓
    moveParts("P行4", "","", 0, "jidoriG", "lower", -0.7, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.5, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
    moveParts("P選択画像2", "","", 0, "P行5", "lower", -0.2, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動

	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout([ "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P代表電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P代表電話番号", "P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 1.0, "", "", 0, parent);
	moveParts("P携帯電話補足", "P携帯電話番号", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語", "P携帯電話番号", "P携帯電話補足"], "lower-left","GMobileLine1", parent);
	moveParts("Purl", "Pemail", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail", "Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMobileLine1", "","", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.7, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}
    
def pageUpdateF0402(prefix, parent) {
    //差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel  ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯番号補足"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));
    
    //このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "782af937ac1f078324d185ae7bf1ddb5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "764a520aac1f07830edf26446e0f3b34");
		break;

	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}
	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);

    /*
	//差し込み
	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
    */

	//レイアウト調整
	//レイアウト調整
    def groupList = [];
    //氏名（jidoriG）を基準としただるま落とし↓
    moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
    moveParts("P行2", "","", 0, "P行3", "upper", 1, parent); //P行2 を P行3 の上1mmの位置に移動
    moveParts("P行1", "","", 0, "P行2", "upper", 1, parent); //P行1 を P行2 の上1mmの位置に移動
    //この場合、P行1～P行3は【左下基準】

    //氏名（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.7, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.7, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.5, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
    moveParts("P選択画像2", "","", 0, "P行5", "lower", -0.2, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動

	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout([ "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P代表電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P代表電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 1.0, "", "", 0, parent);
	moveParts("P携帯電話補足", "P携帯電話番号", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語","P携帯電話番号","P携帯電話補足"], "lower-left","GMobileLine1", parent);
	moveParts("Purl", "Pemail", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMobileLine1", "","", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.7, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}
    
def pageUpdateF0501(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P行6", addPrePostText("", getValue("資格名3"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel  ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));



//このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "782af937ac1f078324d185ae7bf1ddb5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "764a520aac1f07830edf26446e0f3b34");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

//このカセットの 選択画像2 も文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像3 は画像
	switch(getValue("選択画像3"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像3", "276fc218ac1f0f851b169c8ac9779123");
		break;
	default:
		setPartsValue(prefix, "P選択画像3", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}




	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 45.1, parent);

	def groupList = [];
	//氏名（jidoriG）を基準としただるま落とし↓
	moveParts("P行3", "","", 0, "jidoriG", "upper", 0.1, parent); //P行3 を jidoriG の上1mmの位置に移動
	moveParts("P行2", "","", 0, "P行3", "upper", 0.6, parent); //P行2 を P行3 の上1mmの位置に移動
	moveParts("P行1", "","", 0, "P行2", "upper", 0.6, parent); //P行1 を P行2 の上1mmの位置に移動
	//この場合、P行1～P行3は【左下基準】

	//氏名（jidoriG）を基準としたぶら下がり↓
	moveParts("P行4", "","", 0, "jidoriG", "lower", -1, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
	moveParts("P行5", "","", 0, "P行4", "lower", -1, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P行6", "","", 0, "P行5", "lower", -1, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P選択画像2", "","", 0, "P行6", "lower", -0.18, parent); //P選択画像2 を P行5 の下-0.5mmの位置に移動
	//この場合、P選択画像2～P氏名補足は【左上基準】

	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P代表電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P代表電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.8, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語","P携帯電話番号"], "lower-left","GMobileLine1", parent);
	moveParts("Purl", "Pemail", "right", 3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);

	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GMobileLine1", "","", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -0.5, parent);
    
	ungroupingAllPartsForLayout(groupList, parent);
	}

def pageUpdateF0502(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P行6", addPrePostText("", getValue("資格名3"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯電話補足"), ""));

//このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "12ebee85ac1f078373b3c562a91e43d5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "12ebed2bac1f07830e9d620bb1652ecb");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

    
	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);

	def groupList = [];
	//氏名（jidoriG）を基準としただるま落とし↓
	moveParts("P行3", "","", 0, "jidoriG", "upper", 0.4, parent); //P行3 を jidoriG の上1mmの位置に移動
	moveParts("P行2", "","", 0, "P行3", "upper", 0.7, parent); //P行2 を P行3 の上1mmの位置に移動
	moveParts("P行1", "","", 0, "P行2", "upper", 0.7, parent); //P行1 を P行2 の上1mmの位置に移動
	//この場合、P行1～P行3は【左下基準】

	//氏名（jidoriG）を基準としたぶら下がり↓
	moveParts("P行4", "","", 0, "jidoriG", "lower", 0, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
	moveParts("P行5", "","", 0, "P行4", "lower", -0.4, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P行6", "","", 0, "P行5", "lower", -0.4, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P選択画像2", "","", 0, "P行6", "lower", 0, parent); //P選択画像2 を P行5 の下-0.5mmの位置に移動
	//この場合、P選択画像2～P氏名補足は【左上基準】

	moveParts("P住所", "P郵便番号", "right", 0.8, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.7, "", "", 0, parent);
	moveParts("P電話番号", "P携帯電話番号", "right", 1.8, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語","P携帯電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);

	moveParts("Pemail", "", "", 0, "Pキャッチ2", "upper", 0, parent);
	moveParts("P携帯電話補足", "","", 0, "Pemail", "upper", 0.4, parent);
	moveParts("GTelLine1", "", "", 0, "P携帯電話補足", "upper", 0.4, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.4, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.45, parent);
    
	ungroupingAllPartsForLayout(groupList, parent);
	}


def pageUpdateF0601(prefix, parent) {
//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel  ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel  ", getValue("住所2代表電話番号"), ""));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText(getValue("住所2代表電話番号")?"/ ":"Tel  ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax ", getValue("住所2FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像2（資格選択） は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "782af937ac1f078324d185ae7bf1ddb5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "764a520aac1f07830edf26446e0f3b34");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 45.1, parent);

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.1, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.3, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.3, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
    moveParts("P選択画像2", "","", 0, "P行5", "lower", -0.18, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P代表電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 3.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P代表電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.8, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語","P携帯電話番号"], "lower-left","GMobileLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号","P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P住所2電話番号", "P住所2代表電話番号", "right", 3.5, "", "", 0, parent);
	moveParts("P住所2FAX番号", "P住所2電話番号", "right", -0.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2代表電話番号","P住所2電話番号", "P住所2FAX番号"], "lower-left","GTelLine2", parent);
	moveParts("Purl", "Pemail", "right", 3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine2", "","", 0, "GMailLine", "upper", 1, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 1, parent);
	moveParts("GMobileLine1", "", "", 0, "GAddrLine2", "upper", 1, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 1, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 1, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -1.0, parent);
	ungroupingAllPartsForLayout(groupList, parent);
}


def pageUpdateF0602(prefix, parent) {
//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P行6", addPrePostText("", getValue("資格名3"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax  ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText("Tel  ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯電話補足"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));

//このカセットの 選択画像2（資格選択） は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "12ebee85ac1f078373b3c562a91e43d5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "12ebed2bac1f07830e9d620bb1652ecb");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	//字取り
	justJidori(true, "jidoriG", 45.1, parent);


def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.55, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.55, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.55, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】


//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P行4", "","", 0, "jidoriG", "lower", 0, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.4, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行6", "","", 0, "P行5", "lower", -0.4, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
    moveParts("P選択画像2", "","", 0, "P行6", "lower",0, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.2, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.8, "", "", 0, parent);
	moveParts("Pemail", "P携帯電話番号", "right", 2.6, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語", "P携帯電話番号", "Pemail"], "lower-left","GMobileLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.2, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号", "P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P携帯電話補足", "P住所2電話番号", "right", 2.3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2電話番号", "P携帯電話補足"], "lower-left","GTelLine2", parent);

	moveParts("GTelLine2", "", "", 0, "Pキャッチ2", "upper", 0.12, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 0.4, parent);
	moveParts("GMobileLine1", "", "", 0, "GAddrLine2", "upper", 0.5, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 0.4, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.4, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.45, parent);

	ungroupingAllPartsForLayout(groupList, parent);
}



def pageUpdateF0701(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel ", getValue("代表電話番号"), ""));
//	setPartsValue(prefix, "P電話番号", addPrePostText("/ ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText((getValue("代表電話番号")? "／" : "Tel "), getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));//4行追加
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
    
    fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行4","P行5","P行6"], [getValue("資格名1"), getValue("資格名2"), getValue("資格名3")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 55, parent);

	//レイアウト調整
	setGroupingParts(["P行3","P行2","P行1","P行4","P行5","P行6","jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P電話番号","PFAX番号"], [1.4, 2.5], 71.8, parent);
	leftAlign(["P携帯電話番号", "Pemail", "Purl"], [2.8, 2.8], 71.8, parent);
    bottomAlign([["P携帯電話番号", "Pemail","Purl"], ["P代表電話番号", "P電話番号","PFAX番号"], ["P住所2住所名称","P住所2郵便番号","P住所2住所"], ["P住所名称","P郵便番号","P住所"]], [-0.3,-0.3,-0.3], parent);//1行追加、間隔修正
}

def pageUpdateF0801(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText((getValue("携帯電話番号")? "／ " : "Tel "), getValue("電話番号"), ""));
    setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	//setPartsValue(prefix, "P電話番号", addPrePostText("／ ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), "（携帯）"));
	setPartsValue(prefix, "Pふりがな", getValue("ふりがな").replaceAll("/", ""));
	setPartsValue(prefix, "Pemail", addPrePostText("E-mail : ", getValue("email"), ""));

	fillText(prefix, ["P行2","P行1"], [getValue("役職名1"), getValue("部署名1")]);
	fillText(prefix, ["P行3","P行4"], [getValue("資格名1"), getValue("資格名2")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 0, parent);

	//レイアウト調整
	leftAlign(["jidoriG", "Pふりがな"], [3.5], 79.0, parent);
	setGroupingParts(["P行2","P行1","P行3","P行4", "jidoriG", "Pふりがな"],"block1",parent);
	leftAlign(["P携帯電話番号", "P電話番号","PFAX番号"], [1.1, 2.1], 79.0, parent);
    leftAlign(["Pemail", "Purl"], [2.5], 76.9, parent);
	bottomAlign([["Pemail","Purl"], ["P携帯電話番号", "P電話番号","PFAX番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [-0.5,-0.2,0], parent);
}

def pageUpdateF0901(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel : ", getValue("住所2代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel : ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("FAX : ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Mobile : ", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("E-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("URL : ", getValue("url"), ""));


	//このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像1")) {
	case "点字表記あり":
		setPartsValue(prefix, "P選択画像1", "この名刺の点字は、福祉施設に委託して制作されております。");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}
	
	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 48, parent);

	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)

	def groupList = [];

	moveParts("P行2", "","", 0, "P行3", "upper", 0.5, parent);
	moveParts("P行1", "","", 0, "P行2", "upper", 0.5, parent);
	moveParts("P行4", "","", 0, "jidoriG", "lower", -0.5, parent);

	moveParts("PFAX番号", "P電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("P携帯電話番号", "PFAX番号", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P電話番号","PFAX番号","P携帯電話番号"], "lower-left","GTelLine", parent);
	moveParts("Purl", "Pemail", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine", parent);
	
	moveParts("GTelLine", "","", 0, "P選択画像1", "lower", -3, parent);
	moveParts("GMailLine", "", "", 0, "GTelLine", "upper", 0.3, parent);
	moveParts("GAddrLine", "", "", 0, "GMailLine", "upper", 0.3, parent);

	ungroupingAllPartsForLayout(groupList, parent);
}

def pageUpdateF1001(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail ", getValue("email"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("電話 ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("携帯電話 ", getValue("携帯電話番号"), ""));

	fillText(prefix, ["P行2","P行1"], [getValue("役職名1"), getValue("部署名1")]);
	fillText(prefix, ["P行3","P行4"], [getValue("資格名1"), getValue("資格名2")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 81.0, parent);

	//レイアウト調整
	leftAlign(["jidoriG", "Pふりがな"], [3.5], 79.0, parent);
	setGroupingParts(["P行2","P行1","P行3","P行4", "jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P携帯電話番号"], [1.5], 79.0, parent);
	bottomAlign([["Pemail"], ["P代表電話番号", "P携帯電話番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [-0.5,0.5,0], parent);
}

def pageUpdateF1101(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel  ", getValue("代表電話番号"), ""));
//	setPartsValue(prefix, "P電話番号", addPrePostText("/ ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText((getValue("代表電話番号")? "/" : "Tel "), getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));

    fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行4","P行5"], [getValue("資格名1"), getValue("資格名2")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(false, "jidoriG", 70.1, parent);

	//レイアウト調整
	setGroupingParts(["P行2","P行1","P行3","P行4","P行5", "jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P電話番号","PFAX番号"], [0.4, 2.0], 70.1, parent);
	leftAlign(["P携帯電話番号", "Pemail"], [3.0], 70.1, parent);
	bottomAlign([["Purl"], ["P携帯電話番号", "Pemail"], ["P代表電話番号","P電話番号","PFAX番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [-0.3,-0.3,-0.3,0], parent);
}

/*

def pageUpdateF1201(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), " （携帯）"));
    setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel ", getValue("住所2代表電話番号"), ""));

	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行5","P行4","P氏名補足"], [getValue("資格名2"), getValue("資格名1"), getValue("氏名補足")]);
	resetGroupingParts("block1", parent);


//このカセットの 選択画像5は画像
switch(getValue("選択画像5"))
{
case "あり":
setPartsValue(prefix, "P選択画像5", "b2e645a5ac1f0783184c290032804efa");
break;
default:
setPartsValue(prefix, "P選択画像5", "");
break;
}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}




	partialUpdatePartsByInjectionDataMap(prefix, parent);

	//字取り
	justJidori(true, "jidoriG", 45.1, parent);

	//レイアウト調整
	setGroupingParts(["P行2","P行1","P行3","P行4","P行5", "jidoriG", "P氏名補足"],"block1",parent);
	leftAlign(["P代表電話番号", "P電話番号","PFAX番号"], [1.4, 2.5], 76.9, parent);
	leftAlign(["P住所2代表電話番号", "P住所2電話番号","P住所2FAX番号"], [2.5, 2.5], 76.9, parent);
	leftAlign(["Pemail", "Purl"], [2.5], 76.9, parent);
	bottomAlign([["Pemail", "Purl"], ["P住所2代表電話番号","P住所2FAX番号"], ["P住所2住所名称","P住所2郵便番号","P住所2住所"], ["P携帯電話番号"], ["P住所名称","P郵便番号","P住所"], ["P社名ロゴ"]], [-0.7,-0.7,-0.7,-0.7,0], parent);
}
*/

def pageUpdateF1201(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel ", getValue("住所2代表電話番号"), ""));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText(getValue("住所2代表電話番号")?"/ ":"Tel ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax ", getValue("住所2FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像5は画像
switch(getValue("選択画像5"))
{
case "あり":
setPartsValue(prefix, "P選択画像5", "b2e645a5ac1f0783184c290032804efa");
break;
default:
setPartsValue(prefix, "P選択画像5", "");
break;
}


//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);


	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.5, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.5, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】

	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号", "P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P住所2FAX番号", "P住所2電話番号", "right", 3.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2電話番号", "P住所2FAX番号"], "lower-left","GTelLine2", parent);

	moveParts("Purl", "Pemail", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);

	moveParts("P選択画像1", "","", 0, "Pキャッチ1", "upper", 0.1, parent);
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine2", "","", 0, "GMailLine", "upper", 1, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 1, parent);
	moveParts("P携帯電話番号", "", "", 0, "GAddrLine2", "upper", 1, parent);
	moveParts("GAddrLine1", "", "", 0, "P携帯電話番号", "upper", 1, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -5.5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
}



def pageUpdateF1301(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("携帯 ", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText("Tel  ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax  ", getValue("住所2FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);


	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.5, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.5, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】

	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P携帯電話番号", "P電話番号", "right", 5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P電話番号","P携帯電話番号"], "lower-left","GTelLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号", "P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P住所2FAX番号", "P住所2電話番号", "right", 3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2電話番号", "P住所2FAX番号"], "lower-left","GTelLine2", parent);

	moveParts("Purl", "Pemail", "right", 4.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);

	moveParts("P選択画像1", "","", 0, "Pキャッチ1", "upper", 0.1, parent);
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine2", "","", 0, "GMailLine", "upper", 1, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 1, parent);
	moveParts("GTelLine1", "", "", 0, "GAddrLine2", "upper", 1, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 1, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
}


/*
def pageUpdateF1301(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("Tel ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("代表電話番号")?"/ ":"Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("携帯 ", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1 も文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);
	

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", -0.05, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", -0.05, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", -0.05, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行5 を P行4 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P代表電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P代表電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("Purl", "Pemail", "right", 3.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine1", "", "", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -0.5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}
*/

def pageUpdateF1401(prefix, parent) {
    env.pageOffsetX = 3; //左余白mm
    env.pageOffsetY = 3; //上余白mm
    env.pageWidth = 55; //ページ幅（余白無し）
    env.pageHeight = 91; // ページ高さ（余白無し）

    //差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
    setPartsValue(prefix, "P電話番号", addPrePostText("電 話 ", getValue("電話番号"), ""));

    //郵便番号を先頭3桁と末尾4桁に分離
    def zip = getValue("郵便番号");
    def zipcode1 = "";
    def zipcode2 = "";
    if(zip && zip.length() >= 7) {
        zipcode1 = zip.substring(0, 3);
        zipcode2 = zip.substring(zip.length() - 4);
    }
    setPartsValue(prefix, "P局番号", zipcode1);
    setPartsValue(prefix, "P町域番号", zipcode2);


    //レイアウト調整
    def groupList = [];
    //役職名1と部署名1パーツをグループ化して、基準点を下中央にして氏名パーツのX座標と合わせる
    groupList << groupingPartsForLayout(["P部署名1", "P役職名1"], "lower-center","GDeptPos", parent);
    moveParts("GDeptPos", "P氏名","center", -6.977, "", "", 0, parent);

    //住所1を基準に住所2、電話番号パーツの右詰め
    moveParts("P住所2", "P住所1","left", 0.235, "", "", 0, parent);
    moveParts("P電話番号", "P住所2","left", 0.085, "", "", 0, parent);
    //住所1、住所2、電話番号パーツをグループ化して、基準点を右上にして氏名パーツの左辺から-6.977の位置に合わせる
    groupList << groupingPartsForLayout(["P住所1", "P住所2", "P電話番号"], "upper-right","GAddress", parent);
    moveParts("GAddress", "P氏名","left", -6.977, "", "", 0, parent);

    //レイアウト用のグループ解除
    ungroupingAllPartsForLayout(groupList, parent);
}


def pageUpdateF1501(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("TEL ", getValue("電話番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail ", getValue("email"), ""));

	fillText(prefix, ["P行2","P行1"], [getValue("役職名1"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);

	switch(getValue("選択画像4"))
    {
	case "Administrator":
		setPartsValue(prefix, "P選択画像4", "4cd53447ac1f0f852ef33bb20fc73422");
		break;
	default:
		setPartsValue(prefix, "P選択画像4", "");
		break;
	}
//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("選択画像5"))
    {
	case "Platform App Builder":
		setPartsValue(prefix, "P選択画像5", "6ba20e50ac1f0f855a1305bcd15c8a3e");
		break;
	default:
		setPartsValue(prefix, "P選択画像5", "");
		break;	
	}
    //ページ上のパーツを更新（差し込み処理）
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 55.0, parent);

	//レイアウト調整
	moveParts("P選択画像5", "P選択画像4", "left", -1.5, "", "", 0, parent);
	leftAlign(["jidoriG", "Pふりがな"], [3.5], 79.0, parent);
	setGroupingParts(["P行2","P行1","P行3","P行4", "jidoriG"],"block1",parent);
	leftAlign(["P電話番号", "Pemail"], [2.1], 79.0, parent);
	bottomAlign([["Purl"], ["P電話番号", "Pemail"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [0.5,0.5,0], parent);
}

def pageUpdateF1601(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), "（携帯）"));
    //	setPartsValue(prefix, "P代表電話番号", addPrePostText("/ ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "P代表電話番号", addPrePostText((getValue("携帯電話番号")? "/" : "Tel "), getValue("代表電話番号"), ""));
    setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));

	fillText(prefix, ["P行3","P行2","P行1"], [getValue("役職名1"), getValue("部署名2"), getValue("部署名1")]);
	fillText(prefix, ["P行5","P行4","P氏名補足"], [getValue("資格名2"), getValue("資格名1"), getValue("氏名補足")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 55.0, parent);

def groupList = [];
	//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
	moveParts("P行3", "","", 0, "jidoriG", "upper", -0.05, parent); //P行3 を jidoriG の上1mmの位置に移動
	moveParts("P行2", "","", 0, "P行3", "upper", -0.05, parent); //P行2 を P行3 の上1mmの位置に移動
	moveParts("P行1", "","", 0, "P行2", "upper", -0.05, parent); //P行1 を P行2 の上1mmの位置に移動
	//この場合、P行1～P行3は【左下基準】

	//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
	moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
	moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
	moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行5 を P行4 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】
	//レイアウト調整
	putCFPMarkR(["P行4","P行5","P行6"], parent);
	setGroupingParts(["P行2","P行1","P行3","P行4","P行5","P行6", "jidoriG", "P氏名補足"],"block1",parent);
	leftAlign(["P携帯電話番号", "P代表電話番号","PFAX番号"], [1.4, 2.5], 76.9, parent);
	leftAlign(["Pemail", "Purl"], [2.5], 76.9, parent);
	bottomAlign([["Pemail", "Purl"], ["P携帯電話番号", "P代表電話番号","PFAX番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [0,0,0], parent);
}

def pageUpdateF1701(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), "（携帯）"));
	setPartsValue(prefix, "P電話番号", addPrePostText(getValue("携帯電話番号")?"/ ":"Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1 は文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像4 は画像パーツ
	switch(getValue("選択画像4"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像4", "ae664b37ac1f078361ca7af0a0cbfc35");
		break;
	default:
		setPartsValue(prefix, "P選択画像4", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}


	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);
	

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.5, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.4, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P携帯電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("Purl", "Pemail", "right", 3.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine1", "", "", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -0.5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}

/*
def pageUpdateF1701(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", (getValue("携帯電話番号")? "/" : ""), getValue("電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1 は文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}

//このカセットの 選択画像4 は画像パーツ
	switch(getValue("選択画像4"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像4", "ae664b37ac1f078361ca7af0a0cbfc35");
		break;
	default:
		setPartsValue(prefix, "P選択画像4", "");
		break;
	}


	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);
	

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", -0.05, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", -0.05, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", -0.05, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行5 を P行4 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P電話番号", "P携帯電話番号", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("Purl", "Pemail", "right", 3.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine1", "", "", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -0.5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}
*/

def pageUpdateF1801(prefix, parent)	
{
	
//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P氏名補足", addPrePostText("", getValue("氏名補足"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("Tel ", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel ", getValue("住所2代表電話番号"), ""));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText(getValue("住所2代表電話番号")?"/ ":"Tel ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax ", getValue("住所2FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail : ", getValue("email"), ""));
	setPartsValue(prefix, "Purl", addPrePostText("", getValue("url"), ""));

//このカセットの 選択画像1 は文字パーツ
	switch(getValue("選択画像1"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1", "355d6e15ac1f13b81b1ce69273f5e341");
		break;
	default:
		setPartsValue(prefix, "P選択画像1", "");
		break;
	}



//このカセットの 選択画像5は画像
switch(getValue("選択画像5"))
{
case "あり":
setPartsValue(prefix, "P選択画像5", "b2e645a5ac1f0783184c290032804efa");
break;
default:
setPartsValue(prefix, "P選択画像5", "");
break;
}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像6"))
    {
	case "M＆A仲介協会":
		setPartsValue(prefix, "P選択画像6", "52509e3cac1f0f851016c3daec71c082");
		break;
    case "M＆A支援機関協会":
		setPartsValue(prefix, "P選択画像6", "d87854ccac1f07835ac728145b87d2c6");
		break;

	default:
		setPartsValue(prefix, "P選択画像6", "");
		break;
	}
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);

	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)

def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.5, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.4, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】

//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P氏名補足", "","", 0, "jidoriG", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行4", "","", 0, "P氏名補足", "lower", -0.9, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.9, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】

	//レイアウト調整
// moveParts(label, baseX, baseY, baseXPos, baseYPos, offsetX, offsetY, parent)
// groupingPartsForLayout(labelList, referencePoint, groupLabel, parent)
// ungroupingPartsForLayout(groupLabel, parent)


	moveParts("P住所", "P郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.45, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号", "P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P住所2FAX番号", "P住所2電話番号", "right", 3.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2電話番号", "P住所2FAX番号"], "lower-left","GTelLine2", parent);

	moveParts("Purl", "Pemail", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail","Purl"], "lower-left","GMailLine", parent);
	
	moveParts("GMailLine", "","", 0, "P選択画像1", "upper", 0.1, parent);
	moveParts("GTelLine2", "","", 0, "GMailLine", "upper", 1, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 1, parent);
	moveParts("P携帯電話番号", "", "", 0, "GAddrLine2", "upper", 1, parent);
	moveParts("GAddrLine1", "", "", 0, "P携帯電話番号", "upper", 1, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", -0.5, parent);
	ungroupingAllPartsForLayout(groupList, parent);
}

def pageUpdateF1901(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P代表電話番号", addPrePostText("電話 ", getValue("代表電話番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail ", getValue("email"), ""));
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("携帯電話 ", getValue("携帯電話番号"), ""));


	fillText(prefix, ["P行2","P行1"], [getValue("役職名1"), getValue("部署名1")]);
	resetGroupingParts("block1", parent);

/*
	switch(getValue("選択画像4"))
    {
	case "Administrator":
		setPartsValue(prefix, "P選択画像4", "4cd53447ac1f0f852ef33bb20fc73422");
		break;
	default:
		setPartsValue(prefix, "P選択画像4", "");
		break;
	}
*/
//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("キャッチ2"))
    {
	case "あり":
		setPartsValue(prefix, "Pキャッチ2", "fb441d15ac1f0783046dc962c596a5a8");
		break;
	default:
		setPartsValue(prefix, "Pキャッチ2", "");
		break;	
	}
    //ページ上のパーツを更新（差し込み処理）
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	//字取り
	justJidori(true, "jidoriG", 55.0, parent);

    def groupList = [];
    //氏名（jidoriG）を基準としただるま落とし↓
    moveParts("P行2", "","", 0, "jidoriG", "upper", -0.5, parent); //P行3 を jidoriG の上1mmの位置に移動
    moveParts("P行1", "","", 0, "P行2", "upper", 0, parent); //P行1 を P行2 の上1mmの位置に移動
    //この場合、P行1～P行3は【左下基準】

	moveParts("Pmobile（英語）", "PTELEPHONE（英語）", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["PTELEPHONE（英語）", "Pmobile（英語）"], "lower-left","GTelLine1(英語)", parent);


	//レイアウト調整

	leftAlign(["jidoriG", "Pふりがな"], [4.0], 79.0, parent);
	setGroupingParts(["P行2","P行1", "jidoriG"],"block1",parent);
	leftAlign(["P代表電話番号", "P携帯電話番号"], [2.1], 79.0, parent);
	bottomAlign([["Pキャッチ2"], ["Pemail"], ["P代表電話番号", "P携帯電話番号"], ["P郵便番号","P住所"], ["P社名ロゴ"]], [-0.5,-0.3,0.5,0.6], parent);
}

def pageUpdateF2001(prefix, parent) {
	//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P行6", addPrePostText("", getValue("資格名3"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯電話補足"), ""));

//このカセットの 選択画像1 は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "12ebee85ac1f078373b3c562a91e43d5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "12ebed2bac1f07830e9d620bb1652ecb");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像7"))
    {
	case "いばらき経営相談窓口":
		setPartsValue(prefix, "P選択画像7", "9d0c192fac1f0783058296e6283530c2");
		break;
	default:
		setPartsValue(prefix, "P選択画像7", "");
		break;
	}

    def photo = getValue("スポーツロゴ");
    if(photo && photo.startsWith("cms:")) {
        photo = photo.substring(4);
    } else {
        photo = "ae664b37ac1f078361ca7af0a0cbfc35";
    }
    setPartsValue(prefix, "Pスポーツロゴ", photo);

	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//字取り
	justJidori(true, "jidoriG", 45.1, parent);

	def groupList = [];
	//氏名（jidoriG）を基準としただるま落とし↓
	moveParts("P行3", "","", 0, "jidoriG", "upper", 0.4, parent); //P行3 を jidoriG の上1mmの位置に移動
	moveParts("P行2", "","", 0, "P行3", "upper", 0.7, parent); //P行2 を P行3 の上1mmの位置に移動
	moveParts("P行1", "","", 0, "P行2", "upper", 0.7, parent); //P行1 を P行2 の上1mmの位置に移動
	moveParts("P選択画像7", "","", 0, "P行1", "upper", 0.7, parent); //P行1 を P行2 の上1mmの位置に移動
	//この場合、P行1～P行3は【左下基準】

	//氏名（jidoriG）を基準としたぶら下がり↓
	moveParts("P行4", "","", 0, "jidoriG", "lower", 0, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
	moveParts("P行5", "","", 0, "P行4", "lower", -0.4, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P行6", "","", 0, "P行5", "lower", -0.4, parent); //P行5 を P行4 の下-0.5mmの位置に移動
	moveParts("P選択画像2", "","", 0, "P行6", "lower", 0, parent); //P選択画像2 を P行5 の下-0.5mmの位置に移動
	//この場合、P選択画像2～P氏名補足は【左上基準】

	moveParts("P住所", "P郵便番号", "right", 0.8, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.7, "", "", 0, parent);
	moveParts("P電話番号", "P携帯電話番号", "right", 1.8, "", "", 0, parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語","P携帯電話番号","P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);

	moveParts("Pemail", "", "", 0, "Pキャッチ2", "upper", 0, parent);
	moveParts("P携帯電話補足", "","", 0, "Pemail", "upper", 0.4, parent);
	moveParts("GTelLine1", "", "", 0, "P携帯電話補足", "upper", 0.4, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.4, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.45, parent);
    
	ungroupingAllPartsForLayout(groupList, parent);
	}

def pageUpdateF2002(prefix, parent) {
//差し込み
	setPartsValue(prefix, "P行1", addPrePostText("", getValue("部署名1"), ""));
	setPartsValue(prefix, "P行2", addPrePostText("", getValue("部署名2"), ""));
	setPartsValue(prefix, "P行3", addPrePostText("", getValue("役職名1"), ""));
	setPartsValue(prefix, "Pふりがな", addPrePostText("", getValue("ふりがな"), ""));
	setPartsValue(prefix, "P氏名", addPrePostText("", getValue("氏名"), ""));
	setPartsValue(prefix, "P行4", addPrePostText("", getValue("資格名1"), ""));
	setPartsValue(prefix, "P行5", addPrePostText("", getValue("資格名2"), ""));
	setPartsValue(prefix, "P行6", addPrePostText("", getValue("資格名3"), ""));
	setPartsValue(prefix, "P携帯電話番号接頭語", getValue("携帯電話番号")?"携帯 ":""); //値が空なら非表示になります。
	setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
	setPartsValue(prefix, "P住所名称", addPrePostText("", getValue("住所名称"), ""));
	setPartsValue(prefix, "P郵便番号", addPrePostText("〒", getValue("郵便番号"), ""));
	setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], " "));
	setPartsValue(prefix, "P電話番号", addPrePostText("Tel  ", getValue("電話番号"), ""));
	setPartsValue(prefix, "PFAX番号", addPrePostText("Fax  ", getValue("FAX番号"), ""));
	setPartsValue(prefix, "P住所2住所名称", addPrePostText("", getValue("住所2住所名称"), ""));
	setPartsValue(prefix, "P住所2郵便番号", addPrePostText("〒", getValue("住所2郵便番号"), ""));
	setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
	setPartsValue(prefix, "P住所2電話番号", addPrePostText("Tel  ", getValue("住所2電話番号"), ""));
	setPartsValue(prefix, "P携帯電話補足", addPrePostText("", getValue("携帯電話補足"), ""));
	setPartsValue(prefix, "Pemail", addPrePostText("e-mail: ", getValue("email"), ""));

//このカセットの 選択画像2（資格選択） は画像ではなく文字パーツ
	switch(getValue("選択画像2"))
    {
	case "CFP":
		setPartsValue(prefix, "P選択画像2", "12ebee85ac1f078373b3c562a91e43d5");
		break;
    case "CMA":
		setPartsValue(prefix, "P選択画像2", "12ebed2bac1f07830e9d620bb1652ecb");
		break;
	default:
		setPartsValue(prefix, "P選択画像2", "");
		break;
	}

//このカセットの 選択画像6 は画像
	switch(getValue("選択画像7"))
    {
	case "しずおか経営相談窓口":
		setPartsValue(prefix, "P選択画像7", "2472e088ac1f0f853830d7200e73df15");
		break;
	case "みやぎ経営相談窓口":
		setPartsValue(prefix, "P選択画像7", "525c40c0ac1f0f851014756a4378abad");
		break;
	case "にいがた経営相談窓口":
		setPartsValue(prefix, "P選択画像7", "aa12e050ac1f07835fbd99dd6c9db4de");
		break;
	default:
		setPartsValue(prefix, "P選択画像7", "");
		break;
	}


    def photo = getValue("スポーツロゴ");
    if(photo && photo.startsWith("cms:")) {
        photo = photo.substring(4);
    } else {
        photo = "b2e645a5ac1f0783184c290032804efa";
    }
    setPartsValue(prefix, "Pスポーツロゴ", photo);



	//値を反映
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	//字取り
	justJidori(true, "jidoriG", 45.1, parent);


def groupList = [];
//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
moveParts("P行3", "","", 0, "jidoriG", "upper", 0.55, parent); //P行3 を jidoriG の上1mmの位置に移動
moveParts("P行2", "","", 0, "P行3", "upper", 0.55, parent); //P行2 を P行3 の上1mmの位置に移動
moveParts("P行1", "","", 0, "P行2", "upper", 0.55, parent); //P行1 を P行2 の上1mmの位置に移動
moveParts("P選択画像7", "","", 0, "P行1", "upper", 0.55, parent); //P行1 を P行2 の上1mmの位置に移動
//この場合、P行1～P行3は【左下基準】


//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
    moveParts("P行4", "","", 0, "jidoriG", "lower", 0, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行5", "","", 0, "P行4", "lower", -0.4, parent); //P氏名補足 を jidoriG の下-0.5mmの位置に移動
    moveParts("P行6", "","", 0, "P行5", "lower", -0.4, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
    moveParts("P選択画像2", "","", 0, "P行6", "lower",0, parent); //P行4 を P氏名補足 の下-0.5mmの位置に移動
//この場合、P選択画像2～P氏名補足は【左上基準】


	moveParts("P住所", "P郵便番号", "right", 1.2, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所名称", "P郵便番号","P住所"], "lower-left","GAddrLine1", parent);
	moveParts("PFAX番号", "P電話番号", "right", 2.3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P電話番号", "PFAX番号"], "lower-left","GTelLine1", parent);
	moveParts("P携帯電話番号", "P携帯電話番号接頭語", "right", 0.8, "", "", 0, parent);
	moveParts("Pemail", "P携帯電話番号", "right", 2.6, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P携帯電話番号接頭語", "P携帯電話番号", "Pemail"], "lower-left","GMobileLine1", parent);
	moveParts("P住所2住所", "P住所2郵便番号", "right", 1.2, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2住所名称", "P住所2郵便番号", "P住所2住所"], "lower-left","GAddrLine2", parent);
	moveParts("P携帯電話補足", "P住所2電話番号", "right", 2.3, "", "", 0, parent);
	groupList << groupingPartsForLayout(["P住所2電話番号", "P携帯電話補足"], "lower-left","GTelLine2", parent);

	moveParts("GTelLine2", "", "", 0, "Pキャッチ2", "upper", 0.12, parent);
	moveParts("GAddrLine2", "", "", 0, "GTelLine2", "upper", 0.4, parent);
	moveParts("GMobileLine1", "", "", 0, "GAddrLine2", "upper", 0.5, parent);
	moveParts("GTelLine1", "", "", 0, "GMobileLine1", "upper", 0.4, parent);
	moveParts("GAddrLine1", "", "", 0, "GTelLine1", "upper", 0.4, parent);
	moveParts("P社名ロゴ", "", "", 0, "GAddrLine1", "upper", 0.45, parent);

	ungroupingAllPartsForLayout(groupList, parent);
}


def pageUpdateB0101(prefix, parent) {
	//差し込み無し
}

def pageUpdateB0201(prefix, parent) {
	//差し込み無し
}

def pageUpdateB0301(prefix, parent) {
	
    setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail : ", getValue("email（英語）"), ""));

    
    fillText(prefix, ["P行2（英語）","P行1（英語）"], [getValue("部署名（英語）1"), getValue("役職名（英語）1")]);
	fillText(prefix, ["P行3（英語）","P行4（英語）", "P行5（英語）"], [getValue("資格名（英語）1"), getValue("資格名（英語）2"), getValue("資格名（英語）3")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	setGroupingParts(["P行2（英語）","P行1（英語）","PNAME（英語）", "P行3（英語）","P行4（英語）", "P行5（英語）"],"block1",parent);
	leftAlign(["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], [2.0, 2.0], 77.0, parent);
	leftAlign(["Pmobile（英語）", "Pemail（英語）","Purl（英語）"], [2.0, 2.0], 77.0, parent);
    bottomAlign([["Pmobile（英語）", "Pemail（英語）","Purl（英語）"], ["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], ["PADDRESS2（英語）"], ["PADDRESS1（英語）"], ["P社名ロゴ（英語）"]], [0,0,0,1], parent);
}

def pageUpdateB0401(prefix, parent) {
	//差し込み無し
}

def pageUpdateB0501(prefix, parent) {
		//差し込み
    setPartsValue(prefix, "P行1（英語）", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P行2（英語）", getValue("部署名（英語）1"));
    setPartsValue(prefix, "PNAME（英語）", getValue("NAME（英語）"));
    setPartsValue(prefix, "PADDRESS1（英語）", getValue("ADDRESS1（英語）"));
    setPartsValue(prefix, "PADDRESS2（英語）", getValue("ADDRESS2（英語）"));
    setPartsValue(prefix, "PREPRESENTATIVE（英語）", addPrePostText("Direct ", getValue("REPRESENTATIVE（英語）"),""));
	setPartsValue(prefix, "PTELEPHONE（英語）", addPrePostText(getValue("PREPRESENTATIVE（英語）")?"Tel ":"Tel ", getValue("TELEPHONE（英語）"), ""));
    setPartsValue(prefix, "PFAX（英語）", addPrePostText("Fax ", getValue("FAX（英語）"),""));
    setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail : ", getValue("email（英語）"),""));
    setPartsValue(prefix, "Purl（英語）", getValue("url（英語）"));

//このカセットの 選択画像7 は画像
	switch(getValue("選択画像2（英語）"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像2（英語）", "52508c98ac1f0f8545dbb5a175eafe75");
		break;
	default:
		setPartsValue(prefix, "P選択画像2（英語）", "");
		break;
	}
	
//このカセットの 選択画像7 は画像
	switch(getValue("選択画像1（英語）"))
    {
	case "あり":
		setPartsValue(prefix, "P選択画像1（英語）", "f6647edeac1f0783798324885fa0fff8");
		break;
	default:
		setPartsValue(prefix, "P選択画像1（英語）", "");
		break;
	}

//このカセットの 選択画像7 は画像
	switch(getValue("キャッチ1（英語）"))
    {
	case "あり":
		setPartsValue(prefix, "Pキャッチ1（英語）", "f66497a5ac1f0783403bb6c7421c433c");
		break;
	default:
		setPartsValue(prefix, "Pキャッチ1（英語）", "");
		break;
	}
	partialUpdatePartsByInjectionDataMap(prefix, parent);




	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	def groupList = [];
	//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
	moveParts("P行1（英語）", "","", 0, "PNAME（英語）", "lower", -0.25, parent);
	moveParts("P行2（英語）", "","", 0, "P行1（英語）", "lower", -0.25, parent);

	moveParts("PTELEPHONE（英語）", "PREPRESENTATIVE（英語）", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX（英語）", "PTELEPHONE（英語）", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）", "PFAX（英語）"], "lower-left","GTelLine1", parent);
	moveParts("Purl（英語）", "Pemail（英語）", "right", 2.0, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pemail（英語）", "Purl（英語）"], "lower-left","GMailLine", parent);

	moveParts("P選択画像1（英語）", "","", 0, "Pキャッチ1（英語）", "upper", 0.2, parent);
	moveParts("GMailLine", "","", 0, "P選択画像1（英語）", "upper", 0.2, parent);
	moveParts("Pmobile（英語）", "","", 0, "GMailLine", "upper", 0.7, parent);
	moveParts("GTelLine1", "", "", 0, "Pmobile（英語）", "upper", 0.7, parent);
	moveParts("PADDRESS2（英語）", "", "", 0, "GTelLine1", "upper", 0.7, parent);
	moveParts("PADDRESS1（英語）", "", "", 0, "PADDRESS2（英語）", "upper", 0.7, parent);
	moveParts("P社名ロゴ（英語）", "", "", 0, "PADDRESS1（英語）", "upper", 0.1, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}

def pageUpdateB0502(prefix, parent) {
		//差し込み
    setPartsValue(prefix, "P行1（英語）", getValue("部署名（英語）1"));
    setPartsValue(prefix, "P行2（英語）", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P行3（英語）", getValue("資格名（英語）1"));
    setPartsValue(prefix, "P行4（英語）", getValue("資格名（英語）2"));
    setPartsValue(prefix, "P行5（英語）", getValue("資格名（英語）3"));
    setPartsValue(prefix, "PNAME（英語）", getValue("NAME（英語）"));
    setPartsValue(prefix, "PADDRESS1（英語）", getValue("ADDRESS1（英語）"));
    setPartsValue(prefix, "PADDRESS2（英語）", getValue("ADDRESS2（英語）"));
    setPartsValue(prefix, "PADDRESS3（英語）", getValue("ADDRESS3（英語）"));
	setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
	setPartsValue(prefix, "PTELEPHONE（英語）", addPrePostText("Tel ", getValue("TELEPHONE（英語）"), ""));
	setPartsValue(prefix, "PFAX（英語）", addPrePostText("", getValue("FAX（英語）"), ""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail: ", getValue("email（英語）"),""));
	
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	def groupList = [];
	//氏名（jidoriG）を基準としただるま落とし↓
	moveParts("P行2（英語）", "","", 0, "PNAME（英語）", "upper", 0.2, parent); //P行2 を P行3 の上1mmの位置に移動
	moveParts("P行1（英語）", "","", 0, "P行2（英語）", "upper", 0.15, parent); //P行1 を P行2 の上1mmの位置に移動

	
	//氏名・ふりがな（jidoriG）を基準としたぶら下がり↓
	moveParts("P行3（英語）", "","", 0, "PNAME（英語）", "lower", 3.0, parent);
	moveParts("P行4（英語）", "","", 0, "P行3（英語）", "lower", -0.15, parent);
	moveParts("P行5（英語）", "","", 0, "P行4（英語）", "lower", -0.25, parent);

	moveParts("PTELEPHONE（英語）", "Pmobile（英語）", "right", 1.5, "", "", 0, parent);
	moveParts("PFAX（英語）", "PTELEPHONE（英語）", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["Pmobile（英語）", "PTELEPHONE（英語）", "PFAX（英語）"], "lower-left","GTelLine1", parent);

	moveParts("GTelLine1", "","", 0, "Pemail（英語）", "upper", 0.3, parent);
	moveParts("PADDRESS3（英語）", "", "", 0, "GTelLine1", "upper", 0.2, parent);
	moveParts("PADDRESS2（英語）", "", "", 0, "PADDRESS3（英語）", "upper", 0.2, parent);
	moveParts("PADDRESS1（英語）", "", "", 0, "PADDRESS2（英語）", "upper", 0.2, parent);
	moveParts("P社名ロゴ（英語）", "", "", 0, "PADDRESS1（英語）", "upper", -0.25, parent);
	ungroupingAllPartsForLayout(groupList, parent);
	}

/*
	fillText(prefix, ["P行1（英語）","P行2（英語）"], [getValue("役職名（英語）1"), getValue("部署名（英語）1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	setGroupingParts(["P行2（英語）","P行1（英語）","PNAME（英語）"],"block1",parent);
	leftAlign(["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], [2.0, 2.0], 70.0, parent);
	leftAlign(["Pemail（英語）","Purl（英語）"], [2.0], 70.0, parent);
	bottomAlign([["Pemail（英語）","Purl（英語）"], ["Pmobile（英語）"], ["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], ["PADDRESS2（英語）"], ["PADDRESS1（英語）"], ["P社名ロゴ（英語）"]], [-0.1,-0.1,-0.1,-0.1,0], parent);
}
*/



def pageUpdateB0601(prefix, parent) {
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail : ", getValue("email（英語）"), ""));

	fillText(prefix, ["P行1（英語）","P行2（英語）"], [getValue("役職名（英語）1"), getValue("部署名（英語）1")]);
	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	setGroupingParts(["P行2（英語）","P行1（英語）","PNAME（英語）"],"block1",parent);
	leftAlign(["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], [2.5, 2.5], 70.0, parent);
	leftAlign(["Pemail（英語）","Purl（英語）"], [2.5], 70.0, parent);
	bottomAlign([["Pemail（英語）","Purl（英語）"], ["Pmobile（英語）"], ["PREPRESENTATIVE（英語）", "PTELEPHONE（英語）","PFAX（英語）"], ["PADDRESS2（英語）"], ["PADDRESS1（英語）"], ["P社名ロゴ（英語）"]], [-0.5,-0.5,-0.5,-0.5,0], parent);
}

def pageUpdateB0701(prefix, parent) {
//	fillText(prefix, ["Pメッセージ2","Pメッセージ1"], [getValue("メッセージ2"), getValue("メッセージ1")]);
//	fillText(prefix, ["Pメッセージ（小）1","Pメッセージ（小）2"], [getValue("メッセージ（小）1"), getValue("メッセージ（小）2")]);
//	resetGroupingParts("block1", parent);
	partialUpdatePartsByInjectionDataMap(prefix, parent);
	
	//レイアウト調整
//	setGroupingParts(["Pメッセージ1","Pメッセージ2","Pメッセージ（小）1","Pメッセージ（小）2"],"block1",parent);
}

def pageUpdateB0801(prefix, parent) {
	//差し込み無し
}

def pageUpdateB0901(prefix, parent) {
	//差し込み無し
}

def pageUpdateB1001(prefix, parent) {
	//差し込み無し
}

def pageUpdateB1101(prefix, parent) {
	//差し込み無し
}

def pageUpdateB1201(prefix, parent) {
	//差し込み無し
}


def pageUpdateB1301(prefix, parent) {
    setPartsValue(prefix, "PNAME（英語）", getValue("NAME（英語）"));
    setPartsValue(prefix, "P行3（英語）", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P行1（英語）", getValue("部署名（英語）1"));
    setPartsValue(prefix, "PADDRESS1（英語）", getValue("ADDRESS1（英語）"));
    setPartsValue(prefix, "PTELEPHONE（英語）", addPrePostText("Tel ", getValue("TELEPHONE（英語）"), ""));
    setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail : ", getValue("email（英語）"),""));
	partialUpdatePartsByInjectionDataMap(prefix, parent);

	def groupList = [];
	//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
	moveParts("P行3（英語）", "","", 0, "PNAME（英語）", "lower", 1, parent);
	moveParts("P行1（英語）", "","", 0, "P行3（英語）", "lower", 1, parent);

	moveParts("Pmobile（英語）", "PTELEPHONE（英語）", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["PTELEPHONE（英語）", "Pmobile（英語）"], "lower-left","GTelLine1(英語)", parent);

	moveParts("Pemail（英語）", "","", 0, "Pキャッチ2（英語）", "upper", 0.7, parent);
	moveParts("GTelLine1(英語)", "", "", 0, "Pemail（英語）", "upper", 0.7, parent);
	moveParts("PADDRESS1（英語）", "", "", 0, "GTelLine1(英語)", "upper", 0.7, parent);
	moveParts("P社名ロゴ（英語）", "", "", 0, "PADDRESS1（英語）", "upper", 0.7, parent);

	ungroupingAllPartsForLayout(groupList, parent);
	}

def pageUpdateB1401(prefix, parent) {
	//差し込み無し
}

def pageUpdateB1501(prefix, parent) {
	//差し込み無し
}
def pageUpdateB1701(prefix, parent) {
    setPartsValue(prefix, "PNAME（英語）", getValue("NAME（英語）"));
    setPartsValue(prefix, "P行2（英語）", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P行1（英語）", getValue("部署名（英語）1"));
    setPartsValue(prefix, "PADDRESS1（英語）", getValue("ADDRESS1（英語）"));
    setPartsValue(prefix, "PTELEPHONE（英語）", addPrePostText("Tel ", getValue("TELEPHONE（英語）"), ""));
    setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail ", getValue("email（英語）"),""));
	partialUpdatePartsByInjectionDataMap(prefix, parent);

//このカセットの 選択画像1（グループ表記） も文字パーツ
	switch(getValue("キャッチ2（英語）"))
    {
	case "あり":
		setPartsValue(prefix, "Pキャッチ2（英語）", "fb442b10ac1f07830d0f8b2fdb5c89c2");
		break;
	default:
		setPartsValue(prefix, "Pキャッチ2（英語）", "");
		break;	
	}

	def groupList = [];
	//氏名・ふりがな（jidoriG）を基準としただるま落とし↓
	moveParts("P行2（英語）", "","", 0, "PNAME（英語）", "lower", -0.2, parent);
	moveParts("P行1（英語）", "","", 0, "P行2（英語）", "lower", -0.5, parent);

	moveParts("Pmobile（英語）", "PTELEPHONE（英語）", "right", 1.5, "", "", 0, parent);
	groupList << groupingPartsForLayout(["PTELEPHONE（英語）", "Pmobile（英語）"], "lower-left","GTelLine1(英語)", parent);

	moveParts("Pemail（英語）", "","", 0, "Pキャッチ2（英語）", "upper", -1.1, parent);
	moveParts("GTelLine1(英語)", "", "", 0, "Pemail（英語）", "upper", -0.2, parent);
	moveParts("PADDRESS1（英語）", "", "", 0, "GTelLine1(英語)", "upper", -0.2, parent);
	moveParts("P社名ロゴ（英語）", "", "", 0, "PADDRESS1（英語）", "upper", -0.5, parent);

	ungroupingAllPartsForLayout(groupList, parent);
	}


/*
 * カセットごとの表示更新専用関数（ここまで）
 */

//---------------------------------------------------------
// ----- 以下は共通処理（原則変更しない）
//---------------------------------------------------------

//ウイザード編集時のみ呼ばれます
//差し込みデータを、ステップイン編集用に、表面・裏面用データにコピーする。
//その際に変更フラグを false にする。　変更フラグはウイザードで変更されたラベルだけ true になり、ステップアウトしてきたときにトップに反映する
def updateVirtualByReal() {
	def labels = getLabels();
	if(labels) {
		labels.each {
			def value = getInjectionDataValue(it);
			if(!value) value = "";
			setInjectionDataValue("オモテ/" + it, value);
			setInjectionDataValue("オモテ/flg_" + it, "");
			setInjectionDataValue("ウラ/" + it, value);
			setInjectionDataValue("ウラ/flg_" + it, "");
		};
	}
}

//ウイザード編集時のみ呼ばれます
//ステップイン中に変更されたラベルのデータを、トップのデータに反映する。
//加えて、テンプレート側文字パーツ（可変データ資材のデータ編集用）に設定する
def updateRealByVirtual() {
	def labels = getLabels();
	if(labels) {
		labels.each {
			if(!getInjectionDataValue(it)) {
				setInjectionDataValue(it,"");
			}
			def p = getPartsByLabel(it);
			def flg = getInjectionDataValue("オモテ/flg_" + it);
			if(flg) {
				def value = getInjectionDataValue("オモテ/" + it);
				setInjectionDataValue(it, value);
			} else {
				flg = getInjectionDataValue("ウラ/flg_" + it);
				if(flg) {
					def value = getInjectionDataValue("ウラ/" + it);
					setInjectionDataValue(it, value);
				}
			}
		};
	}
	partialUpdatePartsByInjectionDataMap("", null);
}

//ウイザード編集時のみ呼ばれます
//ウイザード編集で入力があった際に、そのラベルに対して入力があった事をマークします。
def valueEdited(label) {
	setInjectionDataValue("flg_" + label, "true");
}

//未使用
def setValue(prefix, label, value) {
	setInjectionDataValue((prefix ? prefix + "/" : "") + label, value);
	setInjectionDataValue((prefix ? prefix + "/" : "") + "flg_" + label, "true");
}

//ルートラベルの値を取得
//ステップインしている状態では、ルートラベルが、「オモテ/」「ウラ/」に一段下がる
def getValue(label) {
	return getInjectionDataValue(label);
}

//基本的にはPラベル（表示用ラベル）の値の更新
def setPartsValue(prefix, label, value) {
	if(!value) {
		value = "";
	}
	setInjectionDataValue((prefix ? prefix + "/" : "") + label, value);
}

//カセットの切り替え
//主にウイザード編集開始時や、可変データ資材のレコード切替時に呼ばれます
def switchCassette(omotePtn, uraPtn, parent) {
	def omote = getPartsByLabel("オモテ", 1, parent);
	def ura = getPartsByLabel("ウラ", 1, parent);

	if(omote) {
		def c = getCassetteByCode(omotePtn);
		if(c) {
			def s = replaceCassette(omote, c.trackingId);
			s.param.trackingId = c.trackingId;
		}
	} else if(ura) {
		def c = getCassetteByCode(uraPtn);
		if(c) {
			def s = replaceCassette(ura, c.trackingId);
			s.param.trackingId = c.trackingId;
		}
	}
}

//trackingId を指定して、カセット情報を取得します。
def getCassetteByTrackingId(trackingId) {
	if(!trackingId) {
		return null;
	}
	def map = getCassetteMap();
	return map.find { return it.trackingId == trackingId};
}

//カセットの管理番号（コード）を指定して、カセット情報を取得します。
def getCassetteByCode(code) {
	if(!code) {
		return null;
	}
	def map = getCassetteMap();
	return map.find { return it.code == code};
}

//ページ（表示）を更新します
//trackingId : 表面・裏面のカセットの trackingId
//prefix : 面付け時の面カセットのラベル　※常に "" でも良いかも...
//parent : 更新対象の親　つまり、「オモテ」か「ウラ」のカセット
def callPageUpdate(trackingId, prefix, parent) {
	if(!trackingId) {
		return null;
	}
	
	if(parent) {
		prefix = (prefix ? prefix + "/" : "") + parent.logic.label;
	}

	injectionCommon(prefix);

	def c = getCassetteByTrackingId(trackingId);
	if(c) {
		def code = c.code;
		"pageUpdate$code"(prefix, parent);
	}
}

//ウイザード編集時のみ
//ウイザード編集時は、ステップインしているので、callPageUpdate() の代わりにこちらを呼ぶ
//現在のカセットの trackingId は、getOmoteTrackingId() / getUraTrackingId()の呼び出しで自動セットされる
def callMyPageUpdate() {
	def trackingId = getInjectionDataValue("cassetteTrackingId");
	if(trackingId) {
		callPageUpdate(trackingId, "", null);
	}
}

//表面の 「オモテ」カセットの 現在の trackingId
def getOmoteTrackingId(parent) {
	def p = getPartsByLabel("オモテ", 1, parent);
	if(p) {
		setInjectionDataValue("オモテ/cassetteTrackingId", p.param.trackingId);
		return p.param.trackingId;
	}
	return null;
}

//裏面の 「ウラ」カセットの 現在の trackingId
def getUraTrackingId(parent) {
	def p = getPartsByLabel("ウラ", 1, parent);
	if(p) {
		setInjectionDataValue("ウラ/cassetteTrackingId", p.param.trackingId);
		return p.param.trackingId;
	}
	return null;
}

//カセット全体の共通流し込み値設定
// カセットごとにこれと異なるものは、各カセット専用関数で上書き設定する
// 　専用関数の関数名は、"pageUpdate" + カセットのコード
def injectionCommon(prefix) {
	env.pageOffsetX = 3; //左余白mm
	env.pageOffsetY = 3; //上余白mm
	env.pageWidth = 91; //ページ幅（余白無し）
	env.pageHeight = 55; // ページ高さ（余白無し）

	def labels = getLabels();
	if(labels) {
		labels.each {
			setPartsValue(prefix, "P"+it, getValue(it));
		}
	}
//ここに入れる↓↓↓
//injectionCommon() 関数内で呼び出していた、setPartsValue() 関数呼び出しを追加する
    setPartsValue(prefix, "P役職名1", getValue("役職名1"));
    setPartsValue(prefix, "P部署名1", getValue("部署名1"));
    setPartsValue(prefix, "P部署名2", getValue("部署名2"));
    setPartsValue(prefix, "Pふりがな", getValue("ふりがな"));
    setPartsValue(prefix, "P氏名", getValue("氏名"));
    setPartsValue(prefix, "P氏名補足", getValue("氏名補足"));
    setPartsValue(prefix, "P資格名1", getValue("資格名1"));
    setPartsValue(prefix, "P資格名2", getValue("資格名2"));
    setPartsValue(prefix, "P資格名3", getValue("資格名3"));
    setPartsValue(prefix, "P携帯電話番号", addPrePostText("", getValue("携帯電話番号"), ""));
    setPartsValue(prefix, "P携帯電話補足", getValue("携帯電話補足"));
    setPartsValue(prefix, "P住所名称", getValue("住所名称"));
    setPartsValue(prefix, "P郵便番号", addPrePostText("", getValue("郵便番号"), ""));
    setPartsValue(prefix, "P住所", connectText([getValue("住所1"), getValue("住所2")], ""));
    setPartsValue(prefix, "P住所1", getValue("住所1"));
    setPartsValue(prefix, "P住所2", getValue("住所2"));
    setPartsValue(prefix, "P代表電話番号", addPrePostText("", getValue("代表電話番号"), "（代表）"));
    setPartsValue(prefix, "P電話番号", addPrePostText("", getValue("電話番号"), ""));
    setPartsValue(prefix, "PFAX番号", addPrePostText("", getValue("FAX番号"), ""));
    setPartsValue(prefix, "Pemail", addPrePostText("", getValue("email"), ""));
    setPartsValue(prefix, "Purl", getValue("url"));

    setPartsValue(prefix, "P住所2住所名称", getValue("住所2住所名称"));
    setPartsValue(prefix, "P住所2郵便番号", addPrePostText("", getValue("住所2郵便番号"), ""));
    setPartsValue(prefix, "P住所2住所", connectText([getValue("住所2住所1"), getValue("住所2住所2")], " "));
    setPartsValue(prefix, "P住所2住所1", getValue("住所2住所1"));
    setPartsValue(prefix, "P住所2住所2", getValue("住所2住所2"));
    setPartsValue(prefix, "P住所2代表電話番号", addPrePostText("Tel ", getValue("住所2代表電話番号"), "（代表）"));
    setPartsValue(prefix, "P住所2電話番号", addPrePostText("/ ", getValue("住所2電話番号"), ""));
    setPartsValue(prefix, "P住所2FAX番号", addPrePostText("Fax ", getValue("住所2FAX番号"), ""));

    setPartsValue(prefix, "Pメッセージ1", getValue("メッセージ1"));
    setPartsValue(prefix, "Pメッセージ2", getValue("メッセージ2"));
    setPartsValue(prefix, "Pメッセージ（小）1", getValue("メッセージ（小）1"));
    setPartsValue(prefix, "Pメッセージ（小）2", getValue("メッセージ（小）2"));

    setPartsValue(prefix, "P行1（英語）", getValue("部署名（英語）1"));
    setPartsValue(prefix, "P行2（英語）", getValue("部署名（英語）2"));
    setPartsValue(prefix, "P行3（英語）", getValue("役職名（英語）1"));
    setPartsValue(prefix, "P行4（英語）", getValue("資格名（英語）1"));
    setPartsValue(prefix, "P行5（英語）", getValue("資格名（英語）2"));
    setPartsValue(prefix, "P行6（英語）", getValue("資格名（英語）3"));

    setPartsValue(prefix, "PNAME（英語）", getValue("NAME（英語）"));
    setPartsValue(prefix, "Pmobile（英語）", addPrePostText("Mobile ", getValue("mobile（英語）"), ""));
    setPartsValue(prefix, "PADDRESS1（英語）", getValue("ADDRESS1（英語）"));
    setPartsValue(prefix, "PADDRESS2（英語）", getValue("ADDRESS2（英語）"));
    setPartsValue(prefix, "PREPRESENTATIVE（英語）", addPrePostText("Direct ", getValue("REPRESENTATIVE（英語）"), ""));
    setPartsValue(prefix, "PTELEPHONE（英語）", addPrePostText("Tel ", getValue("TELEPHONE（英語）"),""));
    setPartsValue(prefix, "PFAX（英語）", addPrePostText("Fax ", getValue("FAX（英語）"),""));
    setPartsValue(prefix, "Pemail（英語）", addPrePostText("e-mail ", getValue("email（英語）"),""));
    setPartsValue(prefix, "Purl（英語）", getValue("url（英語）"));

    
    def photo = getValue("顔写真");
    if(photo && photo.startsWith("cms:")) {
        photo = photo.substring(4);
    } else {
        photo = "3a56fd69ac1f13b86f26f3dcf759697c";
    }
    setPartsValue(prefix, "P顔写真", photo);
//ここまで↑↑↑
}

/*
 * カセットごとの表示更新専用関数（ここまで）
 */

//文字列を連結する
//空文字は除外
def connectText(strList, delimiter) {
	def str = "";
	strList.each {
		if(it) {
			if(str) {
				str += delimiter;
			}
			str += it;
		}
	}
	return str;
}

//文字列の前後に、文字列を付加する。
//空文字の場合は、空のまま
def addPrePostText(pre, body, post) {
	if(body) {
		return pre + body + post;
	}
	return "";
}

//複数行に文字列を詰める
//空の文字はスキップする
//スキップがあった場合、残った部分には、 *hide* を設定して、setGroupingParts()で非表示とする
def fillText(prefix, targetList, textList) {
	def idx = 0;
	targetList.each {
		setPartsValue(prefix, it, "*hide*");
	}
	textList.each {
		if(it) {
			def label = targetList.get(idx);
			setPartsValue(prefix, label, it);
			idx++;
		}
	}
}

//字取り処理の呼び出し
//P氏名と、Pふりがな から値や設定を取り出して、字取り・ルビ表現のグループパーツを新規生成する
//再度生成時は、グループパーツがあったら一旦削除してやりなおし
//P氏名と、Pふりがなは、非表示となります。
def justJidori(bRubi, jLabel, maxWidth, parent) {
	//字取り
	def jg = getPartsByLabel(jLabel, 1, parent);
	if(jg) {
		deleteParts(jg);
	}
	def pName = getPartsByLabel('P氏名', 1, parent);
	def pRubi = getPartsByLabel('Pふりがな', 1, parent);
	jg = jidori(pName, bRubi?pRubi:null, jLabel, 'lt', parent);
	
	if(maxWidth > 0) {
		def box = jg.getBoundBox();
		if(box.width > maxWidth) {
			jg.transform.scaleX = (maxWidth / box.width);
		}
	}
	
	if(pName) {
		pName.setDisplay("none");
	}
	if(bRubi && pRubi) {
		pRubi.setDisplay("none");
	}
	
}

//F0301 住所1の右端に住所2を左寄せするための処理
def alignXByBoxRight(base, targetList, parent) {
	def baseP = getPartsByLabel(base, 1, parent);
	if(!baseP) {
		return;
	}
	def gt = baseP.getGroupTransform();
	def gbox = baseP.getBoundBox();
	def right = gt.translateX + gbox.width;
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			def pt = p.getGroupTransform();
			p.moveTo(right, pt.translateY);
		}
	}
}

//基準点ベースで横方向の位置を合わせる（基本、基準点左パーツの左寄せ）
def alignXByReferencePoint(base, targetList, parent) {
	def baseP = getPartsByLabel(base, 1, parent);
	if(!baseP) {
		return;
	}
	def gt = baseP.getGroupTransform();
	def left = gt.translateX;
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			def pt = p.getGroupTransform();
			p.moveTo(left, pt.translateY);
		}
	}
}

//複数パーツをグルーピングして、そのグループパーツをページ中央に配置してグループ解除
def moveToPageCenter(targetList, parent) {
	def children = [];
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			children.add(p);
		}
	}
	if(children) {
		def centerX = env.pageWidth / 2.0 + env.pageOffsetX;
		def g = groupingParts(children);
		g.editReferencePoint("center-center");
		def gt = g.getGroupTransform();
		g.moveTo(centerX, gt.translateY);
		ungroupingParts(g);
	}
}

//CFPの右側のRマークをCFPの後ろに移動する
def putCFPMarkR(targetList, parent) {
	def mr = getPartsByLabel("markR", 1, parent);
	if(!mr) {
		return;
	}
	mr.setDisplay("none");
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p && p.param.text == "CFP") {
			def pt = p.getGroupTransform();
			def mt = mr.getGroupTransform();
			mr.moveTo(mt.translateX, pt.translateY);
			mr.setDisplay("inline");
			return;
		}
	}
}

//オレンジ枠の処理
//複数のパーツをグルーピングして、そのグループのセンター固定で、上下にシュリンクさせる
//資格名などの処理で、グループ内パーツの上下の不要行を非表示にすることで、まとめてセンターリング
def setGroupingParts(childLabelList, label, parent) {
	def children = [];
	childLabelList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			children.add(p);
		}
	}
	if(children) {
		def g = groupingParts(children);
		if(g) {
			g.logic.label = label;
			g.editReferencePoint("center-left");
			childLabelList.each {
				def p = getPartsByLabel(it, 1, g);
				if(p && p.param.text == "*hide*") {
					p.setDisplay("none");
				}
			}
			return g;
		}
	}
	return null;
}

//setGroupingParts()でグルーピングされたパーツをリセットする
//センター基準なので、非表示を表示に戻して、グループ解除すれば戻る
def resetGroupingParts(label, parent) {
	def g = getPartsByLabel(label,1,parent);
	if(g) {
		def all = getPartsAll(null, g);
		all.each { it.setDisplay("inline"); }
		g.getBoundBox();
		ungroupingParts(g);
		return true;
	}
	return false;
}

//パーツがサイズを持つかどうかの判定
def hasPartsSize(p) {
	if(!p) {
		return false;
	}
	if(p.display == "none") {
		return false;
	}
	if(p.typeId == "group") {
		def box = p.getBoundBox();
		if(box.width == 0 || box.height == 0) {
			return false;
		}
	}
	return true;
}

//パーツの移動処理（親に対する絶対位置移動と、指定パーツからの相対移動）
/*
 * @param label 移動対象のパーツのラベル
 * @param baseX 横方向移動の基準となるパーツのラベル　""なら移動しない
 * @param baseXPos 横方向移動時の基準となるパーツ（もしくは親）の位置。 left / center / right
 * @param offsetX 基準となるパーツ（もしくは親）の基準位置からの横方向オフセット
 * @param baseY 縦方向移動の基準となるパーツのラベル　""なら移動しない
 * @param baseYPos 縦方向移動時の基準となるパーツ（もしくは親）の位置。 upper / center / lower
 * @param offsetY 基準となるパーツ（もしくは親）の基準位置からの縦方向オフセット
 * @param parent parent
 */
def moveParts(label, baseX, baseXPos, offsetX, baseY, baseYPos,offsetY, parent) {
	def p = getPartsByLabel(label, 1, parent);
	if(!p) {
		return;
	}
	def pt = p.getGroupTransform();
	def x = 0;
	def y = 0;
	if(baseX != "") {
		def baseXParts = getPartsByLabel(baseX, 1, parent);
		if(baseXParts) {
			x += getPointX(baseXParts, baseXPos, offsetX);
		}
	} else {
		x = pt.translateX - env.pageOffsetX;
	}
	if(baseY != "") {
		def baseYParts = getPartsByLabel(baseY, 1, parent);
		if(baseYParts) {
			y += getPointY(baseYParts, baseYPos, offsetY);
		}
	}else {
		y = pt.translateY - env.pageOffsetY;
	}
	p.moveTo(x + env.pageOffsetX,y + env.pageOffsetY);
	return true;
}

//親に対して移動する
def movePartsAbsolute(label, baseXPos, offsetX, baseYPos,offsetY, parent) {
	def p = getPartsByLabel(label, 1, parent);
	if(!p) {
		return;
	}
	def pt = p.getGroupTransform();
	def x = 0;
	def y = 0;
	if(baseXPos != "") {
		x += getPagePointX(baseXPos);
		x += offsetX;
	} else {
		x = pt.translateX - env.pageOffsetX;
	}
	if(baseYPos != "") {
		y += getPagePointY(baseYPos);
		y += offsetY;
	}else {
		y = pt.translateY - env.pageOffsetY;
	}
	p.moveTo(x + env.pageOffsetX,y + env.pageOffsetY);
	return true;
}

//リストの最初のパーツを固定して、残りのパーツを左寄せ
//maxWidth が 0 の場合は、左のパーツの左側の余白と左寄せした結果の右側のパーツの右側の余白を同じとして
//その幅を超える場合は、ギャップ以外の文字パーツを横方向に縮小する（長体化）
//maxWidthが 0 以外で指定されている場合は、それを最大幅として、必要に応じて長体化する
def leftAlign(targetList, gaps, maxWidth, parent) {
	if(!targetList) {
		return;
	}
	def basePartsLabel = targetList.get(0);
	def baseParts = getPartsByLabel(basePartsLabel, 1, parent);
	if(!baseParts) {
		return;
	}
	if(maxWidth == 0) {
		def gt = baseParts.getGroupTransform();
		maxWidth = env.pageWidth - (gt.translateX * 2);
	}
	//CalcurateTotalWidth
	def totalWidth = 0;
	def totalGaps = 0;
	def baseFlag = true;
	def right = 0;
	def idx = 0;

	gaps.add(0);
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			p.transform.scaleX = 1.0;
			def box = p.getBoundBox();
			if(hasPartsSize(p)) {
				totalWidth += (box.width + gaps.get(idx));
			}
			idx++;
		}
	}

	def sx = 1.0;
	if(totalWidth > (maxWidth - totalGaps)) {
		sx = (maxWidth - totalGaps) / totalWidth;
	}
	baseFlag = true;
	right = 0;
	idx = 0;
	targetList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			p.transform.scaleX = sx;
			def box = p.getBoundBox();
			def gt = p.getGroupTransform();
			if(baseFlag) {
				right = gt.translateX;
				baseFlag = false;
			} else {
				if(hasPartsSize(p)) {
					p.moveTo(right, gt.translateY);
				}
			}
			if(hasPartsSize(p)) {
				right = right + box.width + gaps.get(idx)
			}
			idx++;
		}
	}
}

//リストのリストの最初のパーツを固定して、残りのパーツを下寄せ
// bottomAlign([["Pemail","Purl"], ["P代表電話番号","P電話番号","PFAX番号"], ["P郵便番号","P住所"]], [-0.5,-0.5], parent);
// のように、行を構成するパーツのラベルのリストを、下から順にリストしたものを指定します。
// gaps には、下の行からの距離を指定します。（重なっている場合は負の値を指定します）
def bottomAlign(targetList, gaps, parent) {
	if(!targetList) {
		return;
	}
	def baseTarget = targetList.get(0);
	def basePartsLabel = baseTarget.get(0);
	def baseParts = getPartsByLabel(basePartsLabel, 1, parent);
	if(!baseParts) {
		return;
	}

	def baseFlag = true;
	def bottom = 0;
	def idx = 0;
	gaps.add(0);
	targetList.each { pare ->
		def maxHeight = 0;
		pare.each {
			def p = getPartsByLabel(it, 1, parent);
			if(p) {
				def box = p.getBoundBox();
				def gt = p.getGroupTransform();
				if(hasPartsSize(p)) {
					if(maxHeight < box.height) {
						maxHeight = box.height;
					}
				}
				if(baseFlag) {
					bottom = gt.translateY;
				}
			}
		}
		if(baseFlag) {
			baseFlag = false;
		}
		pare.each {
			def p = getPartsByLabel(it, 1, parent);
			if(p) {
				def gt = p.getGroupTransform();
				if(!baseFlag) {
					if(hasPartsSize(p)) {
						p.moveTo(gt.translateX, bottom);
					}
				}
			}
		}
		if(maxHeight > 0) {
			bottom -= (maxHeight + gaps.get(idx));
		}
		idx++;
	}
}

//ページ上のパーツを更新（差し込み処理）
def partialUpdatePartsByInjectionDataMap(prefix, parent) {
	def partsList = getPartsAll(null, parent);
	if(partsList) {
		partsList.each {
			def label = it.logic.label;
			if(label) {
				def value = getInjectionDataValue((prefix?prefix+"/":"") + label);
				if(value != null) {
					String typeId = it.typeId;
					switch(typeId) {
					case "image":
						if(value) {
							it.injectionData(value);
						} else {
							//画像パーツのラベルの値が空文字の場合、それを非表示のスイッチとして使い
							//その後 Wizard 編集が空の値をパーツにセットしてしまわないように、injectionData をその時点のパーツの値に戻す
							def org = it.param.trackingId;
							setInjectionDataValue((prefix?prefix+"/":"") + label, org);
						}
						it.setDisplay(value?"inline":"none");
						break;
					case "group":
						break;
					default:
						it.injectionData(value);
						it.setDisplay(value?"inline":"none");
						break;
					}
				}
			}
		}
	}
}

//JustJidori

/* ---------------------- */
/* ----- 字取り処理 ----- */
/* ---------------------- */

/*
 * 姓名、ルビパーツから設定値を取得して呼び出す場合
 * @param pName 氏名パーツ
 * @param pRubi ルビパーツ
 * @param label 字取りパーツに設定するラベル
 * @param parent グループパーツ（面付けなどカセットの中にある場合）
 */
public jidori(pName, pRubi, label, logicmode, parent = null) {
	def name = pName.param.text.replaceAll("　", " ");
	def rubi = "";
	def dyRubi = 0.8;
	def tf = pName.getGroupTransform();
	if(pRubi) {
		rubi = pRubi.param.text.replaceAll("　", " ");
		def b1 = pName.getBoundBox(true);
		def b2 = pRubi.getBoundBox(true);
		dyRubi = b2.y + b2.height - b1.y;
	}
	def jidoriParam = [
		'label':label,
		'logicmode':logicmode,
		'shimei':pName.param,
		'rubi':pRubi?pRubi.param:null,
		'dyRubi':dyRubi,
		'referencePoint':pName.param.referencePoint,
		'x':tf.translateX,
		'y':tf.translateY
	];
	return nameArrange(name, rubi, jidoriParam, parent);
}

//サンプルのパラメータを取得
public getDefaultJidoriParam() {
	return [
		'label':'氏名G',
		'logicmode':'lt',
		'referencePoint':'upper-left',
		'shimei':[
				'font':'ＭＳ 明朝',
				'size':64,
				'color':'#000',
				'colorCmyk':'cmyk(0,0,0,100%)',
				'widthAdjust':false,
				'maxWidth':0
			],
		'rubi':[
				'font':'ＭＳ 明朝',
				'size':10,
				'color':'#000',
				'colorCmyk':'cmyk(0,0,0,100%)'
			],
		'dyRubi':0.8,
		'x':10,
		'y':10
	];
}

/*
 * 字取り処理
 * @param name 氏名 姓と名の間は半角スペース
 * @param rubi ルビ（文字区切りは / で、姓と名の間は半角スペース）
 * @param jidoriParam 生成するパーツの設定
 * @param parent グループパーツ（面付けなどカセットの中にある場合）
 */
public nameArrange(name, rubi, jidoriParam = null, parent = null) {
	if(!jidoriParam) {
		jidoriParam = getDefaultJidoriParam();
	}
	def label = jidoriParam.label;
	def logicmode = jidoriParam.logicmode;
	def param = jidoriParam.shimei;
	def rubiParam = jidoriParam.rubi;
	def dyRubi = jidoriParam.dyRubi;
	def referencePoint = jidoriParam.referencePoint;
	def baseX = jidoriParam.x;
	def baseY = jidoriParam.y;

	def gp = getPartsByLabel(label, 1, parent);
	if(gp) {
		deleteParts(gp);
	}

	//基本設定
	def fontName = param.font;
	def fontSize = param.size; //pt
	def color = param.color;
	def colorCmyk = param.colorCmyk;
	def widthAdjust = param.widthAdjust;
	def maxWidth = param.maxWidth;
	def rubiFontName = "ＭＳ 明朝"; // ルビのフォント
	def rubiFontSizeRatio = 0.2; // ルビのフォントサイズ比率
	def rubiColor = color;
	def rubiColorCmyk = colorCmyk;
	def defaultBlockGap = 0.25; // デフォルトのブロック間ギャップ
	
	if(rubiParam) {
		rubiFontSizeRatio = rubiParam.size / fontSize;
		rubiFontName = rubiParam.font;
		rubiColor = rubiParam.color;
		rubiColorCmyk = rubiParam.colorCmyk;
	}

	//処理
	def names = name.split(" ");
	def rubis = rubi.split(" ");

	def allPartsList = []; // 全構成パーツ
	
	def outOfPattern = true;
	if(names.size() == 2) {
		//姓名の字取りパターン
		def fname = names[0]; // 氏(family)
		def lname = names[1]; // 名(last)

		def flen = getStrLength(fname);
		def llen = getStrLength(lname);

		def frubi = rubis[0]; // 名ルビ
		def frubis = frubi.split("/"); // 文字単位の名ルビ
		def lrubi = ""; // 氏ルビ
		def lrubis = []; // 文字単位の氏ルビ
		if(rubis.size() == 2) {
			lrubi = rubis[1]; // 氏ルビ
			lrubis = lrubi.split("/"); // 文字単位の氏ルビ
		}

		//字取りルールの取得
		def gaps = calcJidori(flen, llen, fontSize, defaultBlockGap);
		if(gaps.pattern) {
			outOfPattern = false;
			def currentX = baseX;
			
			//family name
			def p1 = addNameTextParts(parent, fname, currentX, baseY, fontName, fontSize, color, colorCmyk, gaps.f, gaps.w, widthAdjust, maxWidth);
			allPartsList << p1;
			
			//Rubi on familyName
			addRubi(parent, allPartsList, p1, flen, frubis, rubiFontName, fontSize * rubiFontSizeRatio, rubiColor, rubiColorCmyk, dyRubi);
			
			def box = p1.getBoundBox(true);
			currentX += box.width + gaps.g;

			//last name
			def p2 = addNameTextParts(parent, lname, currentX, baseY, fontName, fontSize, color, colorCmyk, gaps.l, gaps.w, widthAdjust, maxWidth);
			allPartsList << p2;
			
			//Rubi on familyName
			addRubi(parent, allPartsList, p2, llen, lrubis, rubiFontName, fontSize * rubiFontSizeRatio, rubiColor, rubiColorCmyk, dyRubi);
		}
	}
	if(outOfPattern) {
		//姓名のパターン以外
		def nameStr = name;
		def rubiStr = rubi.replaceAll('/', '');
		def currentX = baseX;
		def p = addNameTextParts(parent, nameStr, baseX, baseY, fontName, fontSize, color, colorCmyk, 0, 100, widthAdjust, maxWidth);
		allPartsList << p;
		if(rubiStr) {
			addRubiLeft(parent, allPartsList, p, rubiStr, rubiFontName, fontSize * rubiFontSizeRatio, rubiColor, rubiColorCmyk, widthAdjust, maxWidth, dyRubi);
		}
	}

	//グルーピング
	def g = groupingParts(allPartsList);
	g.param.referencePoint = referencePoint;
	g.getBoundBox();
	g.moveTo(baseX, baseY);
	g.logic.label = label;
	g.logic.mode = logicmode;
	return g;
}

//字取りパラメータの取得（字取り対象外の場合は、flen=0, llen = 0)
public calcJidori(flen, llen, fontSize, defaultBlockGap) {
	def dGapmm = fontSize * 0.352778; // mm フォントサイズ（文字幅）をmmに換算したもの
	//字取りパラメータ
	def gapMap = [
		1:[
			1:[f:0,l:0,g:3,w:1],
			2:[f:0,l:0.75,g:1.5,w:1],
			3:[f:0,l:0.25,g:1,w:1]
		],
		2:[
			1:[f:0.75,l:0,g:1.5,w:1],
			2:[f:0.5,l:0.45,g:1,w:1],
			3:[f:0.25,l:0.2,g:0.75,w:1]
		],
		3:[
			1:[f:0.25,l:0,g:1.25,w:1],
			2:[f:0.15,l:0.25,g:0.5,w:1],
			3:[f:0.15,l:0.15,g:0.5,w:0.9]
		],
		4:[
			1:[f:0.15,l:0,g:0.75,w:1],
			2:[f:0,l:0.1,g:0.5,w:0.95],
			3:[f:0,l:0,g:0,w:1]
		],
	];
	
	def pattern = false;
	def gaps = [];
	if(flen < 1 || flen > 4 || llen < 0 || llen > 3) {
		gaps = [f:0,l:0,g:defaultBlockGap,w:1];
	} else {
		gaps = gapMap[flen][llen];
		pattern = true;
	}
	//字取りパラメータをフォントサイズベースで実サイズに変換
	gaps = [pattern:pattern, f:gaps.f * fontSize, l:gaps.l * fontSize, g:gaps.g * dGapmm, w:gaps.w * 100.0];
	return gaps;
}

// 名前文字パーツを生成
public addNameTextParts(parent, str, x, y, fontName, fontSize, color, colorCmyk, letterSpacing, levelRatio, widthAdjust, maxWidth) {
	def p1 = addPartsToGroup("richtext", parent);
	p1.param.font = fontName;
	p1.param.text = str;
	p1.param.size = fontSize;
	p1.param.color = color;
	p1.param.colorCmyk = colorCmyk;
	p1.param.referencePoint = "center-left";
	p1.moveTo(x, y);
	p1.param.lineAlignMode = "justify-all-lines";
	p1.param.letterSpacing = letterSpacing;
	p1.param.levelRatio = levelRatio;
	if(widthAdjust != 0) {
		p1.param.widthAdjust = widthAdjust;
		p1.param.maxWidth = maxWidth;
	}
	return p1; 
}

//文字パーツにルビを設定する
public addRubi(parent, allPartsList, p, len, rubis, rubiFontName, fontSize, color, colorCmyk, dyRubi) {
	if(!rubis) {
		return;
	}
	if(rubis.size() > len) {
		return;
	}
	
	//文字間を考慮したルビ位置決め
	def sLetterSpacing = p.param.letterSpacing;
	p.param.letterSpacing = 0; // 一旦文字間無しで1文字の幅を計算する
	def boxWithoutSpacing = p.getBoundBox(true);
	p.param.letterSpacing = sLetterSpacing;
	def box = p.getBoundBox(true);
	
	def lsm = (box.width - boxWithoutSpacing.width) / (len-1); // letterSpacing in mm
	def dw = boxWithoutSpacing.width / len;
	def x = box.x + dw/2.0;
	def y = box.y + dyRubi; // ルビの位置
	rubis.each { rubi ->
		def letterSpacing = (rubi.length() == 2) ? 0.8 * fontSize:0;
		def p1 = addPartsToGroup("richtext", parent);
		p1.param.font = rubiFontName;
		p1.param.text = rubi;
		p1.param.size = fontSize;
		p1.param.color = color;
		p1.param.colorCmyk = colorCmyk;
		p1.param.referencePoint = "lower-center";
		p1.moveTo(x, y);
		p1.param.lineAlignMode = "justify-all-lines";
		p1.param.letterSpacing = letterSpacing;
		allPartsList << p1;
		x += (dw+lsm);
	}
}

//文字パーツにルビを設定する（左詰めルビ用）
public addRubiLeft(parent, allPartsList, p, rubi, rubiFontName, fontSize, color, colorCmyk, widthAdjust, maxWidth, dyRubi) {
	if(!rubi) {
		return;
	}
	
	//文字間を考慮したルビ位置決め
	def box = p.getBoundBox(true);
	def x = box.x;
	def y = box.y + dyRubi; // ルビの位置
	def p1 = addPartsToGroup("richtext", parent);
	if(widthAdjust) {
		p1.param.widthAdjust = widthAdjust;
		p1.param.maxWidth = maxWidth;
	}
	p1.param.font = rubiFontName;
	p1.param.text = rubi;
	p1.param.size = fontSize;
	p1.param.color = color;
	p1.param.colorCmyk = colorCmyk;
	p1.param.referencePoint = "lower-left";
	p1.moveTo(x, y);
	p1.param.lineAlignMode = "justify-all-lines";
	p1.param.letterSpacing = 0;
	allPartsList << p1;
}






// BizCardコンバーターでコンバートした結果を扱う処理

//白紙ページにパーツを追加する
def doAddParts(layoutList) {
	layoutList.each {
		if(it.addParts) {
			if(it.type=="VSTRING" || it.type=="SSTRING") {
				addTextPartsByInfo(it);
			} else if(it.type=="VPICT" || it.type=="SPICT") {
				addImagePartsByInfo(it);
			}
		} 
	}
}

//パーツを移動する
def doMoveParts(layoutList, parent) {
	def moveCount = 1;
	while(moveCount > 0) {
		moveCount = 0;
		layoutList.each {
			if(it.moveParts) {
				if(movePartsByInfo(it, parent)) {
					moveCount++
				}
			}
		}
	}
}

//BizCardのブロックに相当するものを一時的にグループパーツで表現する
def doGrouping(groups, parent) {
	groups.each {
		groupingByInfo(it, parent);
	}
}

//BizCardのブロックに相当するものを移動させた後に、グループを解除する
def doUngrouping(groups, parent) {
	groups.each {
		ungroupingByInfo(it, parent);
	}
}

//BizCardのブロックに相当するものを移動する
def doMoveGroupParts(groups, parent) {
	moveCount = 1;
	while(moveCount > 0) {
		moveCount = 0;
		groups.each {
			if(movePartsByInfo(it, parent)) {
				moveCount++
			}
		}
	}
}

//テキストパーツの追加
def addTextPartsByInfo(info) {
	def referencePoint = info.referencePoint;
	def x = info.offsetX;
	def y = info.offsetY;
	def label = info.label;
	def font = info.font;
	def fonte = info.fonte;
	def fontn = info.fontn;
	def fontSize = info.fontSize;
	def str = label;
	
	if(env.altFont) {
		font = env.altFont;
	}
	if(fonte && env.altFontE) {
		fonte = env.altFontE;
	}
	if(fontn && env.altFontN) {
		fontn = env.altFontN;
	}
	
	def p = addParts("richtext");
	p.x = 0;
	p.y = 0;
	p.getBoundBox();
	p.editReferencePoint(referencePoint);
	p.moveTo(x + env.pageOffsetX,y + env.pageOffsetY);
	p.logic.label = label;
	p.param.text = str;
	p.param.font = font;
	if(fonte) {
		p.param.compositeFont=(env.compositeFontOff)?"0":"1";
		p.param.compositeFont_romanFont="1";
		p.param.compositeFont_romanFont_face=fonte;
	}
	if(fontn) {
		p.param.compositeFont=(env.compositeFontOff)?"0":"1";
		p.param.compositeFont_numbersFont="1";
		p.param.compositeFont_numbersFont_face=fontn;
	}
	p.param.size = fontSize;
	p.logic.mode = "eo";
}

//イメージパーツの追加
def addImagePartsByInfo(info) {
	def referencePoint = info.referencePoint;
	def x = info.offsetX;
	def y = info.offsetY;
	def label = info.label;
	def width  = info.width;
	def height  = info.height;
	def str = label;
	
	def p = addParts("image");
	p.editReferencePoint(referencePoint);
	p.param.width = width ? width : env.dummyImageWidth;
	p.param.height = height ? height : env.dummyImageHeight;
	p.param.trackingId = env.dummyImageTrackingId;
	p.moveTo(x + env.pageOffsetX,y + env.pageOffsetY);
	p.logic.label = label;
	p.logic.mode = "eo";
	p.param.resizeAuto = "1";
	p.param.preserveAspectRatio = "none";
}

/*
 * @param labelList グループ化するパーツのラベルリスト
 * @param referencePoint グループパーツの基準点　upper-left / center-center / lower-right など
 * @param groupLabel グループパーツのラベル
 */
def groupingPartsForLayout(labelList, referencePoint, groupLabel, parent) {
	def gplist = []
	labelList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			gplist << p;
		}
	}
	if(gplist.size() > 0) {
		def g = groupingParts(gplist);
		if(g) {
			g.logic.label = groupLabel;
			g.editReferencePoint(referencePoint);
		}
	}
	return groupLabel;
}

def ungroupingPartsForLayout(groupLabel, parent) {
	def g = getPartsByLabel(groupLabel, 1, parent);
	if(g) {
		ungroupingParts(g);
	}
}

def ungroupingAllPartsForLayout(labelList, parent) {
	labelList.each {
		ungroupingPartsForLayout(it, parent);
	}
}

//グループ化
def groupingByInfo(info, parent) {
	def labelList = info.partsList;
	def referencePoint = info.referencePoint;
	def x = info.offsetX;
	def y = info.offsetY;
	def groupLabel = info.label;
	def gplist = []
	labelList.each {
		def p = getPartsByLabel(it, 1, parent);
		if(p) {
			gplist << p;
		}
	}
	if(gplist.size() > 0) {
		def g = groupingParts(gplist);
		if(g) {
			g.logic.label = groupLabel;
			g.editReferencePoint(referencePoint);
			g.moveTo(x,y);
		}
	}
}

//グループ解除
def ungroupingByInfo(info, parent) {
	def g = getPartsByLabel(info.label, 1, parent);
	if(g && g.typeId == "group") {
		ungroupingParts(g);
	}
}

//パーツの移動処理（親に対する絶対位置移動と、指定パーツからの相対移動）
def movePartsByInfo(info, parent) {
	def label = info.label;
	def baseX = info.baseX;
	def baseY = info.baseY;
	def baseXPos = info.baseXPos;
	def baseYPos = info.baseYPos;
	def offsetX = info.offsetX;
	def offsetY = info.offsetY;

	if(env.moved[label]) {
		return false;
	}

	def p = getPartsByLabel(label, 1, parent);
	
	if(!p) {
		return;
	}
	def x = 0;
	def y = 0;
	def baseXParts = null;
	def baseYParts = null;
	if(baseX != "") {
		if(!env.moved[baseX]) {
			return false;
		}
		baseXParts = getPartsByLabel(baseX, 1, parent);
		if(baseXParts) {
			x += getPointX(baseXParts, baseXPos, offsetX);
		}
	} else {
		x += getPagePointX(baseXPos);
		x += offsetX;
	}
	if(baseY != "") {
		if(!env.moved[baseY]) {
			return false;
		}
		baseYParts = getPartsByLabel(baseY, 1, parent);
		if(baseYParts) {
			y += getPointY(baseYParts, baseYPos, offsetY);
		}
	}else {
		y += getPagePointY(baseYPos);
		y += offsetY;
	}
	env.moved[label] = true;
	p.moveTo(x + env.pageOffsetX,y + env.pageOffsetY);
	return true;
}

//相対移動時に基準となるパーツから基準となるX座標を取得
def getPointX(p, pos, offsetX) {
	def box = p.getBoundBox();
	def width = box.width;
	if(!hasPartsSize(p)) {
		def gt = p.getGroupTransform();
		return gt.translateX - env.pageOffsetX;
	}
	def pointX = 0;
	switch(pos) {
	case "left":
		pointX = box.x + offsetX;
		break;
	case "right":
		pointX = box.x + width + offsetX;
		break;
	case "center":
		pointX = box.x + width / 2.0;
		break;
	}
	return pointX - env.pageOffsetX;
}

//相対移動時に基準となるパーツから基準となるY座標を取得
def getPointY(p, pos, offsetY) {
	def box = p.getBoundBox();
	def height = box.height;
	if(!hasPartsSize(p)) {
		def gt = p.getGroupTransform();
		return gt.translateY - env.pageOffsetY;
	}
	def pointY = 0;
	switch(pos) {
	case "upper":
		pointY = box.y + offsetY;
		break;
	case "lower":
		pointY = box.y + height + offsetY;
		break;
	case "center":
		pointY = box.y + height / 2.0 + offsetY;
		break;
	}
	return pointY - env.pageOffsetY;
}

//親（ページ）の基準となるX座標を取得
def getPagePointX(pos) {
	def width = env.pageWidth;
	def pointX = 0;
	switch(pos) {
	case "left":
		pointX = 0;
		break;
	case "right":
		pointX =  width;
		break;
	case "center":
		pointX = width / 2.0;
		break;
	}
	return pointX;
}

//親（ページ）の基準となるY座標を取得
def getPagePointY(pos) {
	def height = env.pageHeight;
	def pointY = 0;
	switch(pos) {
	case "upper":
		pointY = 0;
		break;
	case "lower":
		pointY =  height;
		break;
	case "center":
		pointY = height / 2.0;
		break;
	}
	return pointY;
}

//-------------------------------- Debug

def debug(str) {
	def p = getPartsByLabel("debug");
	if(p) {
		if(!str) {
			p.param.text = "";
			p.setDisplay("none");
		} else {
			p.param.text = p.param.text + "\n" + str;
			p.setDisplay("inline");
		}
	}
}



//カセットに印刷範囲の枠を表示する
//addWaku(印刷範囲の幅,印刷範囲の高さ,余白,null);
def addWaku(pageWidth, pageHeight, pageMargin, cassette) {
    def omote = getPartsByLabel("オモテ",1, cassette);
    def ura = getPartsByLabel("ウラ",1, cassette);
    def parent = omote?omote:ura;
    
    if(parent) {
        def strokeWidth = pageMargin/0.352777; //pt
        def strokeWidthmm = strokeWidth * 0.352777;
        def pWaku = addPartsToGroup("rect", parent);
        pWaku.param.x = pageMargin - strokeWidthmm / 2.0;
        pWaku.param.y = pageMargin - strokeWidthmm / 2.0;
        pWaku.param.width = pageWidth + strokeWidthmm;
        pWaku.param.height = pageHeight + strokeWidthmm;
        pWaku.param.fill = "none";
        pWaku.param.stroke = "#DDDDDD";
        pWaku.param.strokeWidth = strokeWidth;
        pWaku.logic.mode = "cdeilprstxz";
        pWaku.logic.label = "余白表示枠";
        pWaku.getBoundBox();
        pWaku.editReferencePoint("center-center");
        def centerX = pageWidth / 2.0 + pageMargin;
        def centerY = pageHeight / 2.0 + pageMargin;
        pWaku.moveTo(centerX,centerY);
    }
}

//addWaku()で追加した印刷範囲の枠を削除する
//deleteWaku(null);
def deleteWaku(cassette) {
    def omote = getPartsByLabel("オモテ",1, cassette);
    def ura = getPartsByLabel("ウラ",1, cassette);
    def parent = omote?omote:ura;
    
    if(parent) {
        def pWaku = getPartsByLabel("余白表示枠", 1, parent);
        if(pWaku) {
            deleteParts(pWaku);
        }
    }
}