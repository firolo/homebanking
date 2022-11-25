package com.mindhub.homebanking.utils;

public final class CardUtils {
    private CardUtils() {}

    public static String getCardNumber() {
        String randomCardNumber =  String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000))+"-"+
                String.valueOf((int) (Math.random() * (9999 - 1000) + 1000));
        return randomCardNumber;
    }

    public static int getCVV() {
        return (int) (Math.random() * (999 - 1) + 1);
    }
}
