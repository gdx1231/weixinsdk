package com.gdxsoft.weixin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gdxsoft.easyweb.utils.UFile;
import com.gdxsoft.easyweb.utils.Utils;

public class Test {

	private Config cfg_;

	public Test() {
		/**
		 * SUP_WEIXIN_APPID SUP_WEIXIN_APPSECRET SUP_WEIXIN_TOKEN
		 * SUP_WEIXIN_SHOP_ID SUP_WEIXIN_SHOP_KEY wxd6d954c618642b7c
		 * 7e78030290f4f572463fddf9cf58e3fa SBcmedpB41SDdwiQrPMx 1226722702
		 * abcdefg1234567ABCDEFG1234567love
		 */
		// lusir
		// String appID = "wxd6d954c618642b7c";
		// String appsecret = "7e78030290f4f572463fddf9cf58e3fa";
		// String token = "SBcmedpB41SDdwiQrPMx";
		// String shopId = "1226722702";
		// String shopKey = "abcdefg1234567ABCDEFG1234567love";

		// oneworld.cc fwh
		String appID = "wx1f8053002c27a15c";
		String appsecret = "2371bcd2ec6ab11744d9df2b0cbc8b73";
		String token = "oneworldfwh";
		String shopId = "";
		String shopKey = "";

		Config cfg = null;
		String catch_name = "weixin_config.json";
		File f = new File(catch_name);
		System.out.println(f.getAbsolutePath());
		if (f.exists()
				&& (System.currentTimeMillis() - f.lastModified()) < 7000 * 1000) {
			try {
				String content = UFile.readFileText(f.getAbsolutePath());
				JSONObject o = new JSONObject(content);
				String access_token = o.getString("access_token");
				long end_time = o.getLong("end_time");
				cfg = new Config(appID, appsecret, token, access_token,
						end_time);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		if (cfg == null) {
			cfg = Config.instance(appID, appsecret, token);
			cfg.setShopId(shopId);
			cfg.setShopKey(shopKey);

			JSONObject o = new JSONObject();
			o.put("access_token", cfg.getAccessToken());
			o.put("end_time", cfg.getEndTime());

			try {
				UFile.createNewTextFile(f.getAbsolutePath(), o.toString());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}

		}
		// debug 方式，已经获取access_token，避免2000次限制
		// String access_token =
		// "sM53qIabtAfGRVpq2m9eG9CGyHJaApi33n46fM9IDjO_bWkNut1Bo0iX5HXRME-3bxe6Ah0HKUejyuuiX3DQ5V33msFTH1fcd7BNU9deP6c";
		// long end_time = 1429087587910L;
		// Config cfg = new Config(appID, appsecret, token, access_token,
		// end_time);
		this.cfg_ = cfg;
	}

	public void getMaterials() {
		List<WeiXinMaterialArticle> al = cfg_.getWeiXinMaterialArticles();
		for (int i = 0; i < al.size(); i++) {
			WeiXinMaterialArticle m = al.get(i);
			System.out.println(m.toJson());
		}
	}

	public void addImage() {
		String file1 = "/Users/admin/Desktop/LOGO/LOGO1_13.jpg";

		String mid = cfg_.createWeiXinMaterial(file1, "image");
		System.out.println(mid);

		String file2 = "/Users/admin/a.mp3";

		String mid1 = cfg_.createWeiXinMaterial(file2, "image");
		System.out.println(mid1);

		String file3 = "/Users/admin/test.mp4";
		String mid2 = cfg_.createWeiXinMaterialVideo(file3, "郭磊", "郭磊自拍项");

		System.out.println(mid2);
	}

	public void buts() {
		WeiXinButton root = new WeiXinButton();
		WeiXinButton lvl0But0 = new WeiXinButton();
		lvl0But0.setName("测试0");
		lvl0But0.setType(WeiXinButtonClickEvents.view);
		lvl0But0.setUrl("http://www.baidu.com/");

		root.addSub(lvl0But0);

		boolean rst = cfg_.createWeiXinButtons(root);
		System.out.println(rst);
	}

	public void card_colors() {
		JSONArray arr = cfg_.getCard().getColors();
		System.out.println(arr);
	}

	public void card() throws Exception {
		// String img="/Users/admin/LOGO1_05.jpg";
		// String logoUrl=cfg_.getCard().uploadLogo(img);
		//
		// System.out.println(logoUrl);

		String logoUrl = "http://mmbiz.qpic.cn/mmbiz/OibAaudaAhcNvPgC2qVf9xZW3kVZMrg7UY8icKol22wibic7dExfgzNibRhcp6Rr6R1ZJ7zs5M5H0X7KnXZ1tibdTxJQ/0";

		WxCardGeneralCoupon c = new WxCardGeneralCoupon();
		WxCardBaseInfo info = c.getBaseInfo();
		info.setLogoUrl(logoUrl);

		info.setTitle("免费听课券");
		info.setSubTitle("动物园奇妙夜免费听课券");
		info.setBrandName("CCA夏令营协会");
		info.setCanGiveFriend(true);
		info.setCanShare(true);
		info.setColor("Color100");
		String sd = "2015-05-10";
		String ed = "2015-05-31";

		Date dsd = Utils.getDate(sd);
		Date ded = Utils.getDate(ed);

		info.setDateInfoTimeRange(dsd, ded);

		info.setDescription("参加由高级研究院主持的动物知识讲座");

		info.setGetLimit(1);
		info.setQuantity(10);
		info.setNotice("请出示二维码核销");
		info.setServicePhone("13910409333");
		info.setUseCustomCode(false);

		c.setDefaultDetail("参加由高级研究院主持的动物知识讲座");
		String rst = cfg_.getCard().createCard(c);
		System.out.println(rst);
	}

	public void card1() {
		WeiXinQRCode a = cfg_.getCard().createCardQRCode(
				"pWZH5jppRTxWrTm6yQaYbI4goxlY", null,
				"oWZH5jhddsxAPGIMvwSkbhtNnTQI", true, 1231);
		System.out.println(a);
	}

	public void ips() {
		Config cfg = this.cfg_;
		// ip
		System.out.println(cfg.checkVaildIp("11.11.11.11"));
		System.out.println(cfg.checkVaildIp("101.226.62.82"));
		System.out.println(cfg.checkVaildIp("182.254.11.198"));
	}

	public void users() {
		Config cfg = this.cfg_;
		WeiXinUserList lst = cfg.getWeiXinUserList("");

		for (String key : lst.getUsers().keySet()) {
			WeiXinUser userInfo = cfg.getWeiXinUserInfo(key);
			System.out.println(userInfo.toString());
		}
	}

	public void msg() {
		Config cfg = this.cfg_;
		String touser = "oBZ37t1_T1J5LyTnupeuWye5cA1c";

		cfg.getWeiXinUserInfo(touser);
		String content = "你好，这是地方的确定，回到宿舍地冯绍峰";
		cfg.sendWeiXinServiceMsgText(touser, content);

		String title = "总裁寄语";
		String des = "2014新年王文清新年祝语";
		String url = "http://www.oneworld.cc/OneWorld_Web/us.jsp?NWS_GUID=8c09616e-0b70-462f-a9bf-20ada0a0255d&NWS_ID=10608";
		String imgurl = "http://www.oneworld.cc/OneWorld_Web///news_images/2014/00/22/c1eeec42-fcd3-41c0-ab4f-361e0a286de3_0.jpg";

		WeiXinArticle article = new WeiXinArticle(title, des, url, imgurl);
		ArrayList<WeiXinArticle> al = new ArrayList<WeiXinArticle>();
		al.add(article);
		cfg.sendWeiXinServiceMsgNews(touser, al);
	}

	public void serviceStaff() {
		Config cfg = this.cfg_;
		cfg.getWeiXinServiceStaffs();

		String kf_account = "demotest@aa.bb.cc";
		String nickname = "大姐夫";
		String password = "12345678";
		cfg.addWeiXinServiceStaff(kf_account, nickname, password);
	}

	public void qrcode() {
		Config cfg = this.cfg_;
		WeiXinQRCode rst = cfg.createWeiXinQRCode();
		System.out.println(rst);
	}

	public void addWhite() {
		cfg_.getCard().addWhite("oWZH5jhddsxAPGIMvwSkbhtNnTQI", "gdx1231");
	}

	public static void main(String[] args) throws Exception {
		Test t = new Test();
		// t.ips();
		// t.msg();
		// t.users();
		// t.serviceStaff();
		// t.qrcode();
		// t.card();
		// t.card_colors();

		// t.buts();
		// t.addImage();// 8KpDQNLNsmE9gbv32VzgoRtyZx0NAAn5Pnnh8lr95jI
		t.getMaterials();
	}

}
