package com.guit.scaffold;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class Person implements IsSerializable {

  private Long id;

  private Date date;

  private String name;

  private Integer age;

  private Double money;

  private Gender gender;

  public Person() {
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getAge() {
    return age;
  }

  public void setMoney(Double money) {
    this.money = money;
  }

  public Double getMoney() {
    return money;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Gender getGender() {
    return gender;
  }
}
