package io.github.jcchen07944031.Entities;

import io.github.jcchen07944031.API.Encrypt;
import io.github.jcchen07944031.Entities.Constants;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostContent {
	private Constants.POSTCONTENT MODE;

	private String username;
	private String password;
	private String orderNo;
	private String mask;
	private String accessToken;
	private ArrayList<String> redeemSticker;

	private class SourceInfo {
		String deviceUUID = "e43b4f1d26bcae41";
		final String appVersion = "2.3.0";
		final String versionRelease = "9";
		String model = "Pixel 3";
		final String platform = "Android";
		String date;

		public SourceInfo() {
	
		}
	
		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public void setDeviceUUID(String deviceUUID) {
			this.deviceUUID = deviceUUID;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getJson() {
			return "{" + "\"app_version\": " + "\"" + appVersion + "\"," + 
					"\"device_time\": " + "\"" + date + "\"," +
					"\"device_uuid\": " + "\"" + deviceUUID + "\"," +
					"\"model_id\": " + "\"" + model + "\"," +
					"\"os_version\": " + "\"" + versionRelease + "\"," +
					"\"platform\": " + "\"" + platform + "\"}";
		}
	}
	private SourceInfo sourceInfo;

	public PostContent(Constants.POSTCONTENT MODE) {
		this.sourceInfo = new SourceInfo();
		this.MODE = MODE;
		this.redeemSticker = new ArrayList<String>();
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setDeviceUUID(String deviceUUID) {
		sourceInfo.setDeviceUUID(deviceUUID);
	}

	public void setModel(String model) {
		sourceInfo.setModel(model);
	}
	
	public void addRedeemSticker(String stickerID) {
		redeemSticker.add(stickerID);
	}
		
	private void setMask() {
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date mDate = new Date(System.currentTimeMillis());
		sourceInfo.setDate(mSimpleDateFormat.format(mDate));
		mSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		this.orderNo = sourceInfo.deviceUUID + mSimpleDateFormat.format(mDate) + "000";

		String data = sourceInfo.versionRelease + sourceInfo.model + sourceInfo.deviceUUID + sourceInfo.date + sourceInfo.appVersion;
		
		switch(MODE) {
			case MODE_LOGIN:
				data += this.username + this.password;
				break;
			case MODE_VERIFY_ACCESS_TOKEN:
				data += this.accessToken;
				break;
			case MODE_LOTTERY_GET:
			case MODE_WEATHER_GET:
			case MODE_COUPON_GET_LIST:
			case MODE_STICKER_GET_LIST:
			case MODE_STICKER_REDEEM:
				return;
			default:
				break;
		}
				
		data += this.orderNo + sourceInfo.platform;
		
		mask = convertSymbolsToUnicode(Encrypt.encode(null, data));
	}

	private String convertSymbolsToUnicode(String mask) {
		StringBuffer unicode = new StringBuffer();

    		for (int i = 0; i < mask.length(); i++) {
        		char c = mask.charAt(i);
			if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))) {
				String str = Integer.toHexString(c);
				for(int j = str.length(); j < 4; j++) str = "0" + str;
        			unicode.append("\\u" + str);
			}
			else
				unicode.append(c);
    		}
		return unicode.toString();
	}

	public String getJson() {
		setMask();
		String retJson = "";

		switch(MODE) {
			case MODE_LOTTERY_GET:
			case MODE_COUPON_GET_LIST:
			case MODE_STICKER_GET_LIST:
				return "{" + "\"access_token\": " + "\"" + this.accessToken + "\"," + "\"source_info\": " + this.sourceInfo.getJson() + "}";
			case MODE_WEATHER_GET:
				return "{" + "\"city\": " + "\"" + "2306179" + "\"" + "}";
			case MODE_LOGIN:
				retJson = "\"account\": " + "\"" + this.username + "\"," + "\"password\": " + "\"" + this.password + "\",";
				retJson += "\"OrderNo\": " + "\"" + this.orderNo + "\",";
				break;
			case MODE_VERIFY_ACCESS_TOKEN:
				retJson = "\"OrderNo\": " + "\"" + this.orderNo + "\",";
				retJson += "\"access_token\": " + "\"" + this.accessToken + "\",";
				break;
			case MODE_STICKER_REDEEM:
				retJson = "{" + "\"access_token\": " + "\"" + this.accessToken + "\"," + "\"source_info\": " + this.sourceInfo.getJson() + ",\"sticker_ids\": [";
				for(int i = 0; i < redeemSticker.size(); i++) {
					retJson += redeemSticker.get(i);
					if(i < redeemSticker.size() - 1)
						retJson += ",";
				}
				retJson += "]}";
				return retJson;
			default:
				break;
		}
		
		return "{" + retJson + "\"mask\": " + "\"" + this.mask + "\"," + "\"source_info\": " + this.sourceInfo.getJson() + "}";
	}
}
