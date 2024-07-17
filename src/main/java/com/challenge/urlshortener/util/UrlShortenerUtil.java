package com.challenge.urlshortener.util;

import java.util.concurrent.atomic.AtomicLong;

public class UrlShortenerUtil {
  private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int BASE = BASE62.length();
  private static final AtomicLong counter = new AtomicLong();

  public static String generateShortUrl() {
    long id = counter.incrementAndGet();
    return encodeBase62(id);
  }

  private static String encodeBase62(long id) {

    StringBuilder sb = new StringBuilder();

    while (id > 0) {

      sb.append(BASE62.charAt((int) (id % BASE)));

      id /= BASE;
    }

    return sb.reverse().toString();
  }
}
