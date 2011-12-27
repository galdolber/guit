package com.guit.client.dom;

public interface Touch {

  /**
   * Gets the touch x-position within the browser window's client area.
   * 
   * @return the touch x-position
   */
  int getClientX();

  /**
   * Gets the touch y-position within the browser window's client area.
   * 
   * @return the touch y-position
   */
  int getClientY();

  /**
   * Gets a unique identifier for this touch.
   * 
   * @return the unique identifier for this touch
   */
  int getIdentifier();

  /**
   * Gets the touch x-position within the browser document.
   * 
   * @return the touch x-position
   */
  int getPageX();

  /**
   * Gets the touch y-position within the browser document.
   * 
   * @return the touch y-position
   */
  int getPageY();

  /**
   * Gets the touch x-position relative to a given element.
   * 
   * @param target the element whose coordinate system is to be used
   * @return the relative x-position
   */
  int getRelativeX(Element target);

  /**
   * Gets the touch y-position relative to a given element.
   * 
   * @param target the element whose coordinate system is to be used
   * @return the relative y-position
   */
  int getRelativeY(Element target);

  /**
   * Gets the touch x-position on the user's display.
   * 
   * @return the touch x-position
   */
  int getScreenX();

  /**
   * Gets the touch y-position on the user's display.
   * 
   * @return the touch y-position
   */
  int getScreenY();

  /**
   * Gets the target element for the current touch.
   * 
   * @return the target element
   */
  Element getTarget();
}
