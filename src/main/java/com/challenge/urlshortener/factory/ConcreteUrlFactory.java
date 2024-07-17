package com.challenge.urlshortener.factory;

import com.challenge.urlshortener.domain.entity.UrlEntity;
import org.springframework.stereotype.Component;

@Component
public class ConcreteUrlFactory implements UrlFactory {

  @Override
  public UrlEntity buildUrlEntity() {

    return new UrlEntity();
  }
}
