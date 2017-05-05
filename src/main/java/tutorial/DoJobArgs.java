/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package tutorial;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-05-04")
public class DoJobArgs implements org.apache.thrift.TBase<DoJobArgs, DoJobArgs._Fields>, java.io.Serializable, Cloneable, Comparable<DoJobArgs> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DoJobArgs");

  private static final org.apache.thrift.protocol.TField WORKER_OP_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("workerOpType", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField PARTITION_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("partitionId", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField HDFS_SPLIT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("hdfsSplitId", org.apache.thrift.protocol.TType.I32, (short)3);
  private static final org.apache.thrift.protocol.TField FILE_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("filePath", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DoJobArgsStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DoJobArgsTupleSchemeFactory();

  /**
   * 
   * @see WorkerOpType
   */
  public WorkerOpType workerOpType; // required
  public int partitionId; // required
  public int hdfsSplitId; // required
  public java.lang.String filePath; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see WorkerOpType
     */
    WORKER_OP_TYPE((short)1, "workerOpType"),
    PARTITION_ID((short)2, "partitionId"),
    HDFS_SPLIT_ID((short)3, "hdfsSplitId"),
    FILE_PATH((short)4, "filePath");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // WORKER_OP_TYPE
          return WORKER_OP_TYPE;
        case 2: // PARTITION_ID
          return PARTITION_ID;
        case 3: // HDFS_SPLIT_ID
          return HDFS_SPLIT_ID;
        case 4: // FILE_PATH
          return FILE_PATH;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __PARTITIONID_ISSET_ID = 0;
  private static final int __HDFSSPLITID_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.WORKER_OP_TYPE, new org.apache.thrift.meta_data.FieldMetaData("workerOpType", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, WorkerOpType.class)));
    tmpMap.put(_Fields.PARTITION_ID, new org.apache.thrift.meta_data.FieldMetaData("partitionId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.HDFS_SPLIT_ID, new org.apache.thrift.meta_data.FieldMetaData("hdfsSplitId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.FILE_PATH, new org.apache.thrift.meta_data.FieldMetaData("filePath", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DoJobArgs.class, metaDataMap);
  }

  public DoJobArgs() {
  }

  public DoJobArgs(
    WorkerOpType workerOpType,
    int partitionId,
    int hdfsSplitId,
    java.lang.String filePath)
  {
    this();
    this.workerOpType = workerOpType;
    this.partitionId = partitionId;
    setPartitionIdIsSet(true);
    this.hdfsSplitId = hdfsSplitId;
    setHdfsSplitIdIsSet(true);
    this.filePath = filePath;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DoJobArgs(DoJobArgs other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetWorkerOpType()) {
      this.workerOpType = other.workerOpType;
    }
    this.partitionId = other.partitionId;
    this.hdfsSplitId = other.hdfsSplitId;
    if (other.isSetFilePath()) {
      this.filePath = other.filePath;
    }
  }

  public DoJobArgs deepCopy() {
    return new DoJobArgs(this);
  }

  @Override
  public void clear() {
    this.workerOpType = null;
    setPartitionIdIsSet(false);
    this.partitionId = 0;
    setHdfsSplitIdIsSet(false);
    this.hdfsSplitId = 0;
    this.filePath = null;
  }

  /**
   * 
   * @see WorkerOpType
   */
  public WorkerOpType getWorkerOpType() {
    return this.workerOpType;
  }

  /**
   * 
   * @see WorkerOpType
   */
  public DoJobArgs setWorkerOpType(WorkerOpType workerOpType) {
    this.workerOpType = workerOpType;
    return this;
  }

  public void unsetWorkerOpType() {
    this.workerOpType = null;
  }

  /** Returns true if field workerOpType is set (has been assigned a value) and false otherwise */
  public boolean isSetWorkerOpType() {
    return this.workerOpType != null;
  }

  public void setWorkerOpTypeIsSet(boolean value) {
    if (!value) {
      this.workerOpType = null;
    }
  }

  public int getPartitionId() {
    return this.partitionId;
  }

  public DoJobArgs setPartitionId(int partitionId) {
    this.partitionId = partitionId;
    setPartitionIdIsSet(true);
    return this;
  }

  public void unsetPartitionId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __PARTITIONID_ISSET_ID);
  }

  /** Returns true if field partitionId is set (has been assigned a value) and false otherwise */
  public boolean isSetPartitionId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __PARTITIONID_ISSET_ID);
  }

  public void setPartitionIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __PARTITIONID_ISSET_ID, value);
  }

  public int getHdfsSplitId() {
    return this.hdfsSplitId;
  }

  public DoJobArgs setHdfsSplitId(int hdfsSplitId) {
    this.hdfsSplitId = hdfsSplitId;
    setHdfsSplitIdIsSet(true);
    return this;
  }

  public void unsetHdfsSplitId() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __HDFSSPLITID_ISSET_ID);
  }

  /** Returns true if field hdfsSplitId is set (has been assigned a value) and false otherwise */
  public boolean isSetHdfsSplitId() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __HDFSSPLITID_ISSET_ID);
  }

  public void setHdfsSplitIdIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __HDFSSPLITID_ISSET_ID, value);
  }

  public java.lang.String getFilePath() {
    return this.filePath;
  }

  public DoJobArgs setFilePath(java.lang.String filePath) {
    this.filePath = filePath;
    return this;
  }

  public void unsetFilePath() {
    this.filePath = null;
  }

  /** Returns true if field filePath is set (has been assigned a value) and false otherwise */
  public boolean isSetFilePath() {
    return this.filePath != null;
  }

  public void setFilePathIsSet(boolean value) {
    if (!value) {
      this.filePath = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case WORKER_OP_TYPE:
      if (value == null) {
        unsetWorkerOpType();
      } else {
        setWorkerOpType((WorkerOpType)value);
      }
      break;

    case PARTITION_ID:
      if (value == null) {
        unsetPartitionId();
      } else {
        setPartitionId((java.lang.Integer)value);
      }
      break;

    case HDFS_SPLIT_ID:
      if (value == null) {
        unsetHdfsSplitId();
      } else {
        setHdfsSplitId((java.lang.Integer)value);
      }
      break;

    case FILE_PATH:
      if (value == null) {
        unsetFilePath();
      } else {
        setFilePath((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case WORKER_OP_TYPE:
      return getWorkerOpType();

    case PARTITION_ID:
      return getPartitionId();

    case HDFS_SPLIT_ID:
      return getHdfsSplitId();

    case FILE_PATH:
      return getFilePath();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case WORKER_OP_TYPE:
      return isSetWorkerOpType();
    case PARTITION_ID:
      return isSetPartitionId();
    case HDFS_SPLIT_ID:
      return isSetHdfsSplitId();
    case FILE_PATH:
      return isSetFilePath();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof DoJobArgs)
      return this.equals((DoJobArgs)that);
    return false;
  }

  public boolean equals(DoJobArgs that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_workerOpType = true && this.isSetWorkerOpType();
    boolean that_present_workerOpType = true && that.isSetWorkerOpType();
    if (this_present_workerOpType || that_present_workerOpType) {
      if (!(this_present_workerOpType && that_present_workerOpType))
        return false;
      if (!this.workerOpType.equals(that.workerOpType))
        return false;
    }

    boolean this_present_partitionId = true;
    boolean that_present_partitionId = true;
    if (this_present_partitionId || that_present_partitionId) {
      if (!(this_present_partitionId && that_present_partitionId))
        return false;
      if (this.partitionId != that.partitionId)
        return false;
    }

    boolean this_present_hdfsSplitId = true;
    boolean that_present_hdfsSplitId = true;
    if (this_present_hdfsSplitId || that_present_hdfsSplitId) {
      if (!(this_present_hdfsSplitId && that_present_hdfsSplitId))
        return false;
      if (this.hdfsSplitId != that.hdfsSplitId)
        return false;
    }

    boolean this_present_filePath = true && this.isSetFilePath();
    boolean that_present_filePath = true && that.isSetFilePath();
    if (this_present_filePath || that_present_filePath) {
      if (!(this_present_filePath && that_present_filePath))
        return false;
      if (!this.filePath.equals(that.filePath))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetWorkerOpType()) ? 131071 : 524287);
    if (isSetWorkerOpType())
      hashCode = hashCode * 8191 + workerOpType.getValue();

    hashCode = hashCode * 8191 + partitionId;

    hashCode = hashCode * 8191 + hdfsSplitId;

    hashCode = hashCode * 8191 + ((isSetFilePath()) ? 131071 : 524287);
    if (isSetFilePath())
      hashCode = hashCode * 8191 + filePath.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(DoJobArgs other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetWorkerOpType()).compareTo(other.isSetWorkerOpType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWorkerOpType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.workerOpType, other.workerOpType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPartitionId()).compareTo(other.isSetPartitionId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPartitionId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.partitionId, other.partitionId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetHdfsSplitId()).compareTo(other.isSetHdfsSplitId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetHdfsSplitId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.hdfsSplitId, other.hdfsSplitId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetFilePath()).compareTo(other.isSetFilePath());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFilePath()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.filePath, other.filePath);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("DoJobArgs(");
    boolean first = true;

    sb.append("workerOpType:");
    if (this.workerOpType == null) {
      sb.append("null");
    } else {
      sb.append(this.workerOpType);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("partitionId:");
    sb.append(this.partitionId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("hdfsSplitId:");
    sb.append(this.hdfsSplitId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("filePath:");
    if (this.filePath == null) {
      sb.append("null");
    } else {
      sb.append(this.filePath);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DoJobArgsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DoJobArgsStandardScheme getScheme() {
      return new DoJobArgsStandardScheme();
    }
  }

  private static class DoJobArgsStandardScheme extends org.apache.thrift.scheme.StandardScheme<DoJobArgs> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DoJobArgs struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // WORKER_OP_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.workerOpType = tutorial.WorkerOpType.findByValue(iprot.readI32());
              struct.setWorkerOpTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PARTITION_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.partitionId = iprot.readI32();
              struct.setPartitionIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // HDFS_SPLIT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.hdfsSplitId = iprot.readI32();
              struct.setHdfsSplitIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // FILE_PATH
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.filePath = iprot.readString();
              struct.setFilePathIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DoJobArgs struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.workerOpType != null) {
        oprot.writeFieldBegin(WORKER_OP_TYPE_FIELD_DESC);
        oprot.writeI32(struct.workerOpType.getValue());
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(PARTITION_ID_FIELD_DESC);
      oprot.writeI32(struct.partitionId);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(HDFS_SPLIT_ID_FIELD_DESC);
      oprot.writeI32(struct.hdfsSplitId);
      oprot.writeFieldEnd();
      if (struct.filePath != null) {
        oprot.writeFieldBegin(FILE_PATH_FIELD_DESC);
        oprot.writeString(struct.filePath);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DoJobArgsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DoJobArgsTupleScheme getScheme() {
      return new DoJobArgsTupleScheme();
    }
  }

  private static class DoJobArgsTupleScheme extends org.apache.thrift.scheme.TupleScheme<DoJobArgs> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DoJobArgs struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetWorkerOpType()) {
        optionals.set(0);
      }
      if (struct.isSetPartitionId()) {
        optionals.set(1);
      }
      if (struct.isSetHdfsSplitId()) {
        optionals.set(2);
      }
      if (struct.isSetFilePath()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetWorkerOpType()) {
        oprot.writeI32(struct.workerOpType.getValue());
      }
      if (struct.isSetPartitionId()) {
        oprot.writeI32(struct.partitionId);
      }
      if (struct.isSetHdfsSplitId()) {
        oprot.writeI32(struct.hdfsSplitId);
      }
      if (struct.isSetFilePath()) {
        oprot.writeString(struct.filePath);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DoJobArgs struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.workerOpType = tutorial.WorkerOpType.findByValue(iprot.readI32());
        struct.setWorkerOpTypeIsSet(true);
      }
      if (incoming.get(1)) {
        struct.partitionId = iprot.readI32();
        struct.setPartitionIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.hdfsSplitId = iprot.readI32();
        struct.setHdfsSplitIdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.filePath = iprot.readString();
        struct.setFilePathIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

