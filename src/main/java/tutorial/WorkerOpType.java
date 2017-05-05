/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package tutorial;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum WorkerOpType implements org.apache.thrift.TEnum {
  ReadHdfsSplit(1),
  GetSplit(2),
  DelSplit(3);

  private final int value;

  private WorkerOpType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static WorkerOpType findByValue(int value) { 
    switch (value) {
      case 1:
        return ReadHdfsSplit;
      case 2:
        return GetSplit;
      case 3:
        return DelSplit;
      default:
        return null;
    }
  }
}
