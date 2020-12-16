package com.webank.keygen.key.impl;

import com.webank.keygen.encrypt.PemEncrypt;
import com.webank.keygen.enums.EccTypeEnums;
import com.webank.keygen.model.DecryptResult;
import com.webank.keygen.key.KeyEncryptAlgorithm;

/**
 * @author aaronchu
 * @Description
 * @data 2020/06/30
 */
public class PemEncryptAlgorithm implements KeyEncryptAlgorithm {
    @Override
    public String encrypt(String password, byte[] privateKey, String address, String eccName) throws Exception {
        return PemEncrypt.encryptPrivateKey(privateKey, EccTypeEnums.getEccByName(eccName));
    }

    @Override
    public DecryptResult decryptFully(String password, String encryptPrivateKey) throws Exception {
        return PemEncrypt.decryptFully(encryptPrivateKey);
    }

    @Override
    public byte[] decrypt(String password, String encryptPrivateKey) throws Exception {
        return PemEncrypt.decryptPrivateKey(encryptPrivateKey);
    }

    @Override
    public byte[] decryptFile(String password, String filePath) throws Exception {
        return PemEncrypt.decryptPrivateKeyByFile(filePath);
    }

    @Override
    public String exportKey(String encryptKey, String address, String destinationDirectory) throws Exception {
        return PemEncrypt.storePrivateKey(encryptKey, address, destinationDirectory);
    }

    @Override
    public String getName() {
        return "pem";
    }
}
