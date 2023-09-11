package com.pwc.sdc.archive.common.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CryptoJSUtil {
    private final static Digester md5Digester = new Digester(DigestAlgorithm.MD5);

    public String md5(String value) {
        return md5Digester.digestHex(value);
    }

}
