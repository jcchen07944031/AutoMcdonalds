# AutoMcdonalds

## Login Json  
*By decompiling tw.com.mcddaily apk*

Key        | Value
-----------|-------------------------
account    | account  
password   | password
OrderNo    | DeviceUUID + yyyyMMddHHmmssSSS  
mask       | encrypt(Build.VERSION.RELEASE + Build.MODEL + DeviceUUID + yyyy/MM/dd HH:mm:ss + appVersion + account + password + OrderNo + "Android")

Note  
encrypt = MD5("McD" + Build.VERSION.RELEASE + Build.MODEL + DeviceUUID + yyyy/MM/dd HH:mm:ss + appVersion + account + password + OrderNo + "AndroidAPP")