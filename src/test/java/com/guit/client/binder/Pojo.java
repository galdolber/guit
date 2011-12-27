package com.guit.client.binder;

import java.util.Date;

public class Pojo {

  private String name;

  private Integer votes;

  private Double age;

  private Boolean enabled;

  private Date birthday;

  public Pojo() {
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setVotes(Integer votes) {
    this.votes = votes;
  }

  public Integer getVotes() {
    return votes;
  }

  public void setAge(Double age) {
    this.age = age;
  }

  public Double getAge() {
    return age;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public Date getBirthday() {
    return birthday;
  }
}
