/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.keysign.service;

import com.webank.keysign.face.PrivateKeySigner;
import com.webank.keysign.utils.KeyUtils;
import com.webank.keysign.utils.Numeric;
import com.webank.keysign.utils.StringUtils;
import com.webank.wedpr.crypto.CryptoResult;
import com.webank.wedpr.crypto.NativeInterface;
import lombok.extern.slf4j.Slf4j;

/**
 * SM2SignService
 *
 * @Description: SM2SignService
 * @author graysonzhang
 * @author yuzhichu
 * @date 2020-01-02 16:53:33
 *
 */
@Slf4j
public class SM2SignService implements PrivateKeySigner {

	@Override
	public String sign(byte[] plain, String privateKey) {
		if (plain == null || StringUtils.isEmpty(privateKey)) {
			log.error("Args cannot be empty");
			return null;
		}
		privateKey = KeyUtils.ensureStandard32BytesPrivateKey(privateKey);//rust ffi only support 32 bytes
		//Hash
		CryptoResult hashResult = NativeInterface.sm3(Numeric.toHexString(plain));
		if(hashResult.hash == null){
			log.error("error on generating sm3 {}",hashResult.getWedprErrorMessage());
			return null;
		}
		//Sign
		CryptoResult result = NativeInterface.sm2Sign(privateKey, hashResult.hash);
		if (result.wedprErrorMessage != null){
			log.error("error on generating sm2 signature {}", result.wedprErrorMessage);
			return null;
		}
		return result.signature;
	}

	/**
	 * Sign data with sm2p256v1 curve.
	 * @param utf8Msg
	 * @param privateKey Hex format of private key. Can be 64 bytes or shorter.
	 * @return
	 */
	@Override
	public String sign(String utf8Msg, String privateKey) {
		if (StringUtils.isEmpty(utf8Msg)) {
			log.error("Args cannot be empty");
			return null;
		}
		return sign(utf8Msg.getBytes(), privateKey);
	}

	@Override
	public boolean verify(byte[] plain, String signStr, String publicKey) {
		if(plain == null || StringUtils.isEmpty(signStr) || StringUtils.isEmpty(publicKey)){
			log.error("Args cannot be empty");
			return false;
		}
		publicKey = KeyUtils.ensureStandard65BytesPublickey(publicKey);
		//Hash
		CryptoResult hashResult = NativeInterface.sm3(Numeric.toHexString(plain));
		if(hashResult.hash == null){
			log.error("error on generating sm3 {}",hashResult.getWedprErrorMessage());
			return false;
		}
		CryptoResult result = NativeInterface.sm2verify(publicKey, hashResult.hash, signStr);
		if (result.wedprErrorMessage != null){
			log.error("error on verifying signature {}", result.wedprErrorMessage);
			return false;
		}
		return result.result;
	}

	/**
	 * verify signature
	 * @param utf8Msg msg
	 * @param signStr signature
	 * @param publicKey Hex format of public key. Can be 64 bytes or 65 bytes which with a prefix 0x04
	 * @return
	 */
	@Override
	public boolean verify(String utf8Msg, String signStr, String publicKey){
		if(StringUtils.isEmpty(utf8Msg)){
			log.error("Args cannot be empty");
			return false;
		}
		return verify(utf8Msg.getBytes(), signStr, publicKey);
	}

}









