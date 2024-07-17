package com.challenge.urlshortener.factory;

import com.challenge.urlshortener.domain.entity.UrlEntity;

public interface UrlFactory {

  UrlEntity buildUrlEntity();
}
