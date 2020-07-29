package com.laobi.utils;

public class StringUtil {

    final static String s = "0";

    public static String hex(Long v) {

        if (v <= 0xF) {
            return s + Long.toHexString(v);
        } else {
            return Long.toHexString(v);
        }
    }

    public static String evaluate(Long value) {
        if (value == 0) return null;
        Long seg_0 = value & 0xFF;
        Long seg_1 = (value >>> 8) & 0xFF;
        Long seg_2 = (value >>> 16) & 0xFF;
        Long seg_3 = (value >>> 24) & 0xFF;
        Long seg_4 = (value >>> 32) & 0xFF;
        Long seg_5 = (value >>> 40) & 0xFF;
        return String.format("%s:%s:%s:%s:%s:%s", hex(seg_5), hex(seg_4), hex(seg_3), hex(seg_2), hex(seg_1), hex(seg_0));
    }

    public static long macToLong(String mac){
        return Long.parseLong(mac.replace(":",""),16);
    }

    public static void main(String[] args) {

        System.out.println(macToLong("ff:ff:ff:ff:ff:ff"));
        System.out.println(evaluate(macToLong("ff:ff:ff:ff:ff:ff")));
        System.out.println(evaluate(251595739992354l));

    }
}
