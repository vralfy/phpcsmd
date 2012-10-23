rm private/keystore
keytool -genkeypair -storepass phpcsmd -alias phpcsmd -validity 730 -keystore private/keystore -dname "CN=Norman Specht, OU=Unknown, O=www.foopara.de, L=Unkown, ST=Unknown, C=DE"
