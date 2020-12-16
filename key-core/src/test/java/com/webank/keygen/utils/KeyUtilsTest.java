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
package com.webank.keygen.utils;

import java.math.BigInteger;

import com.webank.keygen.exception.KeyGenException;
import com.webank.wedpr.crypto.CryptoResult;
import com.webank.wedpr.crypto.NativeInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.web3j.utils.Numeric;

import com.webank.keygen.enums.EccTypeEnums;
import com.webank.keygen.handler.SM2KeyHandler;
import com.webank.keygen.model.PkeyInfo;

/**
 * @Description KeyUtilsTest
 * @author yuzhichu
 * @date 2019-12-26 
 */
@Slf4j
public class KeyUtilsTest {

	/**
	 * Test that even private key is small, it still get a small private key
	 * @throws Exception
	 */
	@Test
	public void test256bitEnsured() throws Exception {
		String pkeyHex = "a";
		byte[] privKeyBytes = Numeric.hexStringToByteArray(pkeyHex);
		Assert.assertEquals(1, privKeyBytes.length);
		BigInteger pkey = Numeric.toBigInt(privKeyBytes);
		BigInteger pub = SM2KeyHandler.create(privKeyBytes).getPublicKey();
		PkeyInfo pkeyInfo = KeyUtils.createPkeyInfo(pkey, pub, EccTypeEnums.SM2P256V1.getEccName());
		Assert.assertEquals(32, pkeyInfo.getPrivateKey().length);
	}	

	@Test
	public void test() throws Exception{
		CryptoResult result = NativeInterface.sm2keyPair();
		if(result.getWedprErrorMessage() != null){
			log.error("Failed to generate sm2 keypair: {}",result.getWedprErrorMessage());
			throw new KeyGenException(result.getWedprErrorMessage());
		}
		byte[] bytes = Numeric.hexStringToByteArray(result.getPublicKey());
		System.out.println(bytes.length);
	}
}


















