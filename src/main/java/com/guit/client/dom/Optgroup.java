package com.guit.client.dom;

import com.guit.client.Implementation;
import com.guit.client.dom.impl.OptgroupImpl;
import com.guit.client.junit.Mock;
import com.guit.junit.dom.OptgroupMock;

@Implementation(OptgroupImpl.class)
@Mock(OptgroupMock.class)
public interface Optgroup extends Element {

  String label();

  boolean disabled();

  void disabled(boolean disabled);

  void setDisabled(String disabled);

  void label(String label);
}