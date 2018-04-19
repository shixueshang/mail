package com.coolgua.mail.enums;

/**
 * Created by lihongde on 2018/1/26 17:31.
 */
public enum TemplateStatus {

  ONGOING("进行中"), CLOSED("已关闭"), DELETED("已删除");

  private String name;

  private TemplateStatus(String name){
    this.name = name;
  }

  public static TemplateStatus getTemplateStatus(int status){
    TemplateStatus[] values = TemplateStatus.values();
    for (TemplateStatus ts : values) {
      if(ts.ordinal() == status){
        return ts;
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
