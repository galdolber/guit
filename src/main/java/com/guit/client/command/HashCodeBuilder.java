/*
 * Copyright 2010 Daniel Bell
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.guit.client.command;

/**
 * <p>
 * Assists in implementing {@link Object#hashCode()} methods.
 * </p>
 * <p>
 * This class enables a good <code>hashCode</code> method to be built for any
 * class. It follows the rules laid out in the book <a
 * href="http://java.sun.com/docs/books/effective/index.html">Effective Java</a>
 * by Joshua Bloch. Writing a good <code>hashCode</code> method is actually
 * quite difficult. This class aims to simplify the process.
 * </p>
 * <p>
 * All relevant fields from the object should be included in the
 * <code>hashCode</code> method. Derived fields may be excluded. In general, any
 * field used in the <code>equals</code> method must be used in the
 * <code>hashCode</code> method.
 * </p>
 * <p>
 * To use this class write code as follows:
 * </p>
 * 
 * <pre>
 * public class Person {
 *   String name;
 *   int age;
 *   boolean smoker;
 *   ...
 *
 *   public int hashCode() {
 *     // you pick a hard-coded, randomly chosen, non-zero, odd number
 *     // ideally different for each class
 *     return new HashCodeBuilder(17, 37).
 *       append(name).
 *       append(age).
 *       append(smoker).
 *       toHashCode();
 *   }
 * }
 * </pre>
 * <p>
 * If required, the superclass <code>hashCode()</code> can be added using
 * {@link #appendSuper}.
 * </p>
 * <p>
 * Alternatively, there is a method that uses reflection to determine the fields
 * to test. Because these fields are usually private, the method,
 * <code>reflectionHashCode</code>, uses
 * <code>AccessibleObject.setAccessible</code> to change the visibility of the
 * fields. This will fail under a security manager, unless the appropriate
 * permissions are set up correctly. It is also slower than testing explicitly.
 * </p>
 * <p>
 * A typical invocation for this method would look like:
 * </p>
 * 
 * <pre>
 * public int hashCode() {
 *   return HashCodeBuilder.reflectionHashCode(this);
 * }
 * </pre>
 * 
 * @since 1.0
 */
public class HashCodeBuilder {

  public static final class DoubleUtil {

    static long doubleToLongBits(final double v) {
      if (Double.isNaN(v)) {
        // NaN -> 0 11111111 10000000000000000000000
        // IEEE754, NaN exponent bits all 1s, and mantissa is non-zero
        return 0x0FFFL << 51;
      }

      // value < 0 -> 1 ???????? ???????????????????????
      // value > 0 -> 0 ???????? ???????????????????????
      long sign = (v < 0 ? 0x1L << 63 : 0);
      long exponent = 0;

      double absV = Math.abs(v);
      // Infinity -> ? 11111111 00000000000000000000000
      // IEEE754 infinite numbers, exponent all 1s, mantissa is 0
      if (Double.isInfinite(v)) {
        exponent = 0x07FFL << 52;
      } else {
        if (absV == 0.0) {
          // 0.0 -> ? 00000000 00000000000000000000000
          // IEEE754, exponent is 0, mantissa is zero
          // we don't handle negative zero at the moment, it is treated as
          // positive zero
          exponent = 0L;
        } else {
          // get an approximation to the exponent
          // let d = 1.M * 2^E
          // log2(d) = log2(1.M * 2^E)
          // log2(d) = log2(1.M) + log2(2^E)
          // log2(d) = log2(1.M) + E
          // floor(log2(d)) = floor(log(1.M) + E)
          // floor(log2(d)) = E (because log(1.M) always < 1)
          // E = floor(log2(d))
          // E = floor(log(d)/log(2))
          int guess = (int) Math.floor(Math.log(absV) / Math.log(2));
          // force it to -1023, 1023 interval (<= -1023 = denorm/zero)
          guess = Math.max(-1023, Math.min(guess, 1023));

          // Recall that d = 1.M * 2^E, so dividing by 2^E should leave
          // us with 1.M
          // divide away exponent guess
          double exp = Math.pow(2, guess);
          absV = absV / exp;

          // while the number is still bigger than a normalized number
          // increment exponent guess
          // This might occur if there is some precision loss in determining
          // the exponent
          while (absV > 2.0) {
            guess++;
            absV /= 2.0;
          }
          // if the number is smaller than a normalized number
          // decrement exponent. If the exponent becomes zero, and we
          // fail to achieve a normalized mantissa, then this number
          // must be a denormalized value
          while (absV < 1 && guess > 1024) {
            guess--;
            absV *= 2;
          }
          exponent = (guess + 1023L) << 52;
        }
      }
      // if denormalized
      if (exponent <= 0) {
        // denormalized numbers have an exponent of zero, but pretend
        // they have an exponent of 1, so since there is an implicit
        // * 2^1 for denorms, we correct by dividing by 2
        absV /= 2;
      }

      // the input value has now been stripped of its exponent
      // and is in the range [0,2), we strip off the leading decimal
      // and use the remainer as a percentage of the significand value (2^52)
      long mantissa = (long) ((absV % 1) * Math.pow(2, 52));
      return sign | exponent | (mantissa & 0xfffffffffffffL);
    }

    private DoubleUtil() {
    }
  }

  public static class FloatUtil {

    private static final double LN2 = Math.log(2);

    // Theory of operation: Let a double number d be represented as
    // 1.M * 2^E, where the leading bit is assumed to be 1,
    // the fractional mantissa M is multiplied 2 to the power of E.
    // We want to reliably recover M and E, and then encode them according
    // to IEEE754 (see http://en.wikipedia.org/wiki/IEEE754)
    static int floatToIntBits(float value) {
      if (Float.isNaN(value)) {
        return 0x7fc00000;
      }
      int signBit;
      if (value == 0) {
        return (1 / value == Float.NEGATIVE_INFINITY) ? 0x80000000 : 0;
      } else if (value < 0) {
        value = -value;
        signBit = 0x80000000;
      } else {
        signBit = 0;
      }
      if (value == Float.POSITIVE_INFINITY) {
        return signBit | 0x7f800000;
      }

      int exponent = (int) (Math.log(value) / LN2);
      if (exponent < -126) {
        exponent = -126;
      }
      int significand = (int) (0.5 + value * Math.exp(-(exponent - 23) * LN2));

      // Handle exponent rounding issues & denorm
      if ((significand & 0x01000000) != 0) {
        significand >>= 1;
        exponent++;
      } else if ((significand & 0x00800000) == 0) {
        if (exponent == -126) {
          return signBit | significand;
        } else {
          significand <<= 1;
          exponent--;
        }
      }
      return signBit | ((exponent + 127) << 23) | (significand & 0x007fffff);
    }

    static String toBinaryIeee754String(long decimal) {
      String binary = Long.toBinaryString(decimal);
      StringBuilder result = new StringBuilder(binary);
      for (long i = binary.length(); i < 32; i++) {
        result.insert(0, "0");
      }
      result.insert(9, " ");
      result.insert(1, " ");
      return result.toString();
    }

    private FloatUtil() {
    }
  }

  /**
   * Constant to use in building the hashCode.
   */
  private final int iConstant;

  /**
   * Running total of the hashCode.
   */
  private int iTotal = 0;

  /**
   * <p>
   * Uses two hard coded choices for the constants needed to build a
   * <code>hashCode</code>.
   * </p>
   */
  public HashCodeBuilder() {
    iConstant = 37;
    iTotal = 17;
  }

  /**
   * <p>
   * Two randomly chosen, non-zero, odd numbers must be passed in. Ideally these
   * should be different for each class, however this is not vital.
   * </p>
   * <p>
   * Prime numbers are preferred, especially for the multiplier.
   * </p>
   * 
   * @param initialNonZeroOddNumber a non-zero, odd number used as the initial
   *          value
   * @param multiplierNonZeroOddNumber a non-zero, odd number used as the
   *          multiplier
   * @throws IllegalArgumentException if the number is zero or even
   */
  public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
    if (initialNonZeroOddNumber == 0) {
      throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
    }
    if (initialNonZeroOddNumber % 2 == 0) {
      throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
    }
    if (multiplierNonZeroOddNumber == 0) {
      throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
    }
    if (multiplierNonZeroOddNumber % 2 == 0) {
      throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
    }
    iConstant = multiplierNonZeroOddNumber;
    iTotal = initialNonZeroOddNumber;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>boolean</code>.
   * </p>
   * <p>
   * This adds <code>iConstant * 1</code> to the <code>hashCode</code> and not a
   * <code>1231</code> or <code>1237</code> as done in java.lang.Boolean. This
   * is in accordance with the <quote>Effective Java</quote> design.
   * </p>
   * 
   * @param value the boolean to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(boolean value) {
    iTotal = iTotal * iConstant + (value ? 0 : 1);
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>boolean</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(boolean[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  // -------------------------------------------------------------------------

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>byte</code>.
   * </p>
   * 
   * @param value the byte to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(byte value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  // -------------------------------------------------------------------------

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>byte</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(byte[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>char</code>.
   * </p>
   * 
   * @param value the char to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(char value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>char</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(char[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>double</code>.
   * </p>
   * 
   * @param value the double to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(double value) {
    return append(DoubleUtil.doubleToLongBits(value));
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>double</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(double[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>float</code>.
   * </p>
   * 
   * @param value the float to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(float value) {
    iTotal = iTotal * iConstant + FloatUtil.floatToIntBits(value);
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>float</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(float[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>int</code>.
   * </p>
   * 
   * @param value the int to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(int value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>int</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(int[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>long</code>.
   * </p>
   * 
   * @param value the long to add to the <code>hashCode</code>
   * @return this
   */
  // NOTE: This method uses >> and not >>> as Effective Java and
  // Long.hashCode do. Ideally we should switch to >>> at
  // some stage. There are backwards compat issues, so
  // that will have to wait for the time being. cf LANG-342.
  public HashCodeBuilder append(long value) {
    iTotal = iTotal * iConstant + ((int) (value ^ (value >> 32)));
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>long</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(long[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>Object</code>.
   * </p>
   * 
   * @param object the Object to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(Object object) {
    if (object == null) {
      iTotal = iTotal * iConstant;

    } else {
      if (object.getClass().isArray()) {
        // 'Switch' on type of array, to dispatch to the correct handler
        // This handles multi dimensional arrays
        if (object instanceof long[]) {
          append((long[]) object);
        } else if (object instanceof int[]) {
          append((int[]) object);
        } else if (object instanceof short[]) {
          append((short[]) object);
        } else if (object instanceof char[]) {
          append((char[]) object);
        } else if (object instanceof byte[]) {
          append((byte[]) object);
        } else if (object instanceof double[]) {
          append((double[]) object);
        } else if (object instanceof float[]) {
          append((float[]) object);
        } else if (object instanceof boolean[]) {
          append((boolean[]) object);
        } else {
          // Not an array of primitives
          append((Object[]) object);
        }
      } else {
        iTotal = iTotal * iConstant + object.hashCode();
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>Object</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(Object[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>short</code>.
   * </p>
   * 
   * @param value the short to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(short value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>short</code> array.
   * </p>
   * 
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(short[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (int i = 0; i < array.length; i++) {
        append(array[i]);
      }
    }
    return this;
  }

  /**
   * <p>
   * Adds the result of super.hashCode() to this builder.
   * </p>
   * 
   * @param superHashCode the result of calling <code>super.hashCode()</code>
   * @return this HashCodeBuilder, used to chain calls.
   * @since 2.0
   */
  public HashCodeBuilder appendSuper(int superHashCode) {
    iTotal = iTotal * iConstant + superHashCode;
    return this;
  }

  /**
   * <p>
   * Return the computed <code>hashCode</code>.
   * </p>
   * 
   * @return <code>hashCode</code> based on the fields appended
   */
  public int toHashCode() {
    return iTotal;
  }

  /**
   * Returns the computed <code>hashCode</code>.
   * 
   * @return <code>hashCode</code> based on the fields appended
   * @since 3.0
   */
  public Integer build() {
    return toHashCode();
  }

  /**
   * <p>
   * The computed <code>hashCode</code> from toHashCode() is returned due to the
   * likelyhood of bugs in mis-calling toHashCode() and the unlikelyness of it
   * mattering what the hashCode for HashCodeBuilder itself is.
   * 
   * @return <code>hashCode</code> based on the fields appended
   * @since 2.5
   */
  @Override
  public int hashCode() {
    return toHashCode();
  }

}
