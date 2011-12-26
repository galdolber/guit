package com.guit.junit.dom;

import com.guit.client.dom.Table;

public class TableMock extends ElementMock implements Table {

  public TableMock() {
    super("table");
  }

  // public TableCaptionElement createCaption() {
  // return this.createCaption();
  // }
  //
  // public TableSectionElement createTFoot() {
  // return this.createTFoot();
  // }
  //
  // public TableSectionElement createTHead() {
  // return this.createTHead();
  // }
  //
  // public void deleteCaption() {
  // this.deleteCaption();
  // }
  //
  // public void deleteRow(int index) {
  // this.deleteRow(index);
  // }
  //
  // public void deleteTFoot() {
  // this.deleteTFoot();
  // }
  //
  // public void deleteTHead() {
  // this.deleteTHead();
  // }

  @Override
  public int border() {
    return attrInt("border");
  }

  // public TableCaptionElement getCaption() {
  // return this.caption;
  // }

  @Override
  public int cellPadding() {
    return attrInt("cellPadding");
  }

  @Override
  public int cellSpacing() {
    return attrInt("cellSpacing");
  }

  @Override
  public String frame() {
    return attr("frame");
  }

  // public NodeList<TableRowElement> getRows() {
  // return this.rows;
  // }

  @Override
  public String rules() {
    return attr("rules");
  }

  // public NodeList<TableSectionElement> getTBodies() {
  // return this.tBodies;
  // }

  // public TableSectionElement getTFoot() {
  // return this.tFoot;
  // }
  //
  // public TableSectionElement getTHead() {
  // return this.tHead;
  // }

  @Override
  public String tableWidth() {
    return attr("width");
  }

  // public TableRowElement insertRow(int index) {
  // return this.insertRow(index);
  // }

  @Override
  public void border(int border) {
    attr("border", String.valueOf(border));
  }

  // public void setCaption(TableCaptionElement caption) {
  // this.caption = caption;
  // }

  @Override
  public void cellPadding(int cellPadding) {
    attr("cellPadding", String.valueOf(cellPadding));
  }

  @Override
  public void cellSpacing(int cellSpacing) {
    attr("cellSpacing", String.valueOf(cellSpacing));
  }

  @Override
  public void frame(String frame) {
    attr("frame", frame);
  }

  @Override
  public void rules(String rules) {
    attr("rules", rules);
  }

  // public void setTFoot(TableSectionElement tFoot) {
  // this.tFoot = tFoot;
  // }
  //
  // public void setTHead(TableSectionElement tHead) {
  // this.tHead = tHead;
  // }

  @Override
  public void width(String width) {
    attr("width", width);
  }
}
