package com.coolgua.mail.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihongde on 2018/1/26 9:52.
 */
public enum DataSourceType {

  LOCAL("本地上传"), EXTERNAL("外部数据源");

  private String name;

  private DataSourceType(String name){
    this.name= name;
  }

  public static Map<Integer, String> getDataSourceType(){
    DataSourceType[] dst = DataSourceType.values();
    Map<Integer, String> map = new HashMap<>();
    for(DataSourceType s : dst){
      map.put(s.ordinal(), s.name());
    }
    return map;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
