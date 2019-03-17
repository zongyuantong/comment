package com.xuanxuan.csu.util;

import org.junit.Test;

public class CommonUtilTest {

    @Test
    public void unicode_test1() {
        String source = "你好呀,我叫陈志轩";
        String res = CommonUtil.unicode(source);
        System.out.println(res);
    }

    @Test
    public void decodeUnicode_test2() {
        String source = "\uD83D\uDE11 \uD83D\uDE11 \uD83D\uDE11 \uD83D\uDE11";
        String res = CommonUtil.decodeUnicode(source);
        System.out.println(res);
    }
}