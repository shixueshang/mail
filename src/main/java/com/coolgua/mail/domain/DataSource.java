package com.coolgua.mail.domain;

/**
 * Created by lihongde on 2018/1/25 17:48.
 */
public class DataSource {

  private String id;
  private Integer orgId;
  private String name;
  private Integer dsType;
  private String fields;
  private boolean deleted;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getDsType() {
    return dsType;
  }

  public void setDsType(Integer dsType) {
    this.dsType = dsType;
  }

  public String getFields() {
    return fields;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}
