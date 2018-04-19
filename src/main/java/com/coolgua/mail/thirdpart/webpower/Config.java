package com.coolgua.mail.thirdpart.webpower;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by lihongde on 2018/2/2 22:41.
 */
@Component
@PropertySource("classpath:mail.properties")
public class Config {

  @Value("${location}")
  private String location;
  @Value("${namespace}")
  private String namespace;
  @Value("${isTest}")
  private boolean isTest;

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public boolean isTest() {
    return isTest;
  }

  public void setTest(boolean test) {
    isTest = test;
  }
}
