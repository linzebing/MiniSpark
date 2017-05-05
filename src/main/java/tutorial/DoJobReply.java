/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package tutorial;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-05-05")
public class DoJobReply implements org.apache.thrift.TBase<DoJobReply, DoJobReply._Fields>, java.io.Serializable, Cloneable, Comparable<DoJobReply> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DoJobReply");

  private static final org.apache.thrift.protocol.TField LINES_FIELD_DESC = new org.apache.thrift.protocol.TField("lines", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField PAIRS_FIELD_DESC = new org.apache.thrift.protocol.TField("pairs", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DoJobReplyStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DoJobReplyTupleSchemeFactory();

  public java.util.List<java.lang.String> lines; // required
  public java.util.List<StringIntPair> pairs; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LINES((short)1, "lines"),
    PAIRS((short)2, "pairs");

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
        case 1: // LINES
          return LINES;
        case 2: // PAIRS
          return PAIRS;
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
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LINES, new org.apache.thrift.meta_data.FieldMetaData("lines", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    tmpMap.put(_Fields.PAIRS, new org.apache.thrift.meta_data.FieldMetaData("pairs", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, StringIntPair.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DoJobReply.class, metaDataMap);
  }

  public DoJobReply() {
  }

  public DoJobReply(
    java.util.List<java.lang.String> lines,
    java.util.List<StringIntPair> pairs)
  {
    this();
    this.lines = lines;
    this.pairs = pairs;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DoJobReply(DoJobReply other) {
    if (other.isSetLines()) {
      java.util.List<java.lang.String> __this__lines = new java.util.ArrayList<java.lang.String>(other.lines);
      this.lines = __this__lines;
    }
    if (other.isSetPairs()) {
      java.util.List<StringIntPair> __this__pairs = new java.util.ArrayList<StringIntPair>(other.pairs.size());
      for (StringIntPair other_element : other.pairs) {
        __this__pairs.add(new StringIntPair(other_element));
      }
      this.pairs = __this__pairs;
    }
  }

  public DoJobReply deepCopy() {
    return new DoJobReply(this);
  }

  @Override
  public void clear() {
    this.lines = null;
    this.pairs = null;
  }

  public int getLinesSize() {
    return (this.lines == null) ? 0 : this.lines.size();
  }

  public java.util.Iterator<java.lang.String> getLinesIterator() {
    return (this.lines == null) ? null : this.lines.iterator();
  }

  public void addToLines(java.lang.String elem) {
    if (this.lines == null) {
      this.lines = new java.util.ArrayList<java.lang.String>();
    }
    this.lines.add(elem);
  }

  public java.util.List<java.lang.String> getLines() {
    return this.lines;
  }

  public DoJobReply setLines(java.util.List<java.lang.String> lines) {
    this.lines = lines;
    return this;
  }

  public void unsetLines() {
    this.lines = null;
  }

  /** Returns true if field lines is set (has been assigned a value) and false otherwise */
  public boolean isSetLines() {
    return this.lines != null;
  }

  public void setLinesIsSet(boolean value) {
    if (!value) {
      this.lines = null;
    }
  }

  public int getPairsSize() {
    return (this.pairs == null) ? 0 : this.pairs.size();
  }

  public java.util.Iterator<StringIntPair> getPairsIterator() {
    return (this.pairs == null) ? null : this.pairs.iterator();
  }

  public void addToPairs(StringIntPair elem) {
    if (this.pairs == null) {
      this.pairs = new java.util.ArrayList<StringIntPair>();
    }
    this.pairs.add(elem);
  }

  public java.util.List<StringIntPair> getPairs() {
    return this.pairs;
  }

  public DoJobReply setPairs(java.util.List<StringIntPair> pairs) {
    this.pairs = pairs;
    return this;
  }

  public void unsetPairs() {
    this.pairs = null;
  }

  /** Returns true if field pairs is set (has been assigned a value) and false otherwise */
  public boolean isSetPairs() {
    return this.pairs != null;
  }

  public void setPairsIsSet(boolean value) {
    if (!value) {
      this.pairs = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case LINES:
      if (value == null) {
        unsetLines();
      } else {
        setLines((java.util.List<java.lang.String>)value);
      }
      break;

    case PAIRS:
      if (value == null) {
        unsetPairs();
      } else {
        setPairs((java.util.List<StringIntPair>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case LINES:
      return getLines();

    case PAIRS:
      return getPairs();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case LINES:
      return isSetLines();
    case PAIRS:
      return isSetPairs();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof DoJobReply)
      return this.equals((DoJobReply)that);
    return false;
  }

  public boolean equals(DoJobReply that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_lines = true && this.isSetLines();
    boolean that_present_lines = true && that.isSetLines();
    if (this_present_lines || that_present_lines) {
      if (!(this_present_lines && that_present_lines))
        return false;
      if (!this.lines.equals(that.lines))
        return false;
    }

    boolean this_present_pairs = true && this.isSetPairs();
    boolean that_present_pairs = true && that.isSetPairs();
    if (this_present_pairs || that_present_pairs) {
      if (!(this_present_pairs && that_present_pairs))
        return false;
      if (!this.pairs.equals(that.pairs))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetLines()) ? 131071 : 524287);
    if (isSetLines())
      hashCode = hashCode * 8191 + lines.hashCode();

    hashCode = hashCode * 8191 + ((isSetPairs()) ? 131071 : 524287);
    if (isSetPairs())
      hashCode = hashCode * 8191 + pairs.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(DoJobReply other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetLines()).compareTo(other.isSetLines());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLines()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lines, other.lines);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetPairs()).compareTo(other.isSetPairs());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPairs()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.pairs, other.pairs);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("DoJobReply(");
    boolean first = true;

    sb.append("lines:");
    if (this.lines == null) {
      sb.append("null");
    } else {
      sb.append(this.lines);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("pairs:");
    if (this.pairs == null) {
      sb.append("null");
    } else {
      sb.append(this.pairs);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DoJobReplyStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DoJobReplyStandardScheme getScheme() {
      return new DoJobReplyStandardScheme();
    }
  }

  private static class DoJobReplyStandardScheme extends org.apache.thrift.scheme.StandardScheme<DoJobReply> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DoJobReply struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LINES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.lines = new java.util.ArrayList<java.lang.String>(_list0.size);
                java.lang.String _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = iprot.readString();
                  struct.lines.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setLinesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // PAIRS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list3 = iprot.readListBegin();
                struct.pairs = new java.util.ArrayList<StringIntPair>(_list3.size);
                StringIntPair _elem4;
                for (int _i5 = 0; _i5 < _list3.size; ++_i5)
                {
                  _elem4 = new StringIntPair();
                  _elem4.read(iprot);
                  struct.pairs.add(_elem4);
                }
                iprot.readListEnd();
              }
              struct.setPairsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, DoJobReply struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.lines != null) {
        oprot.writeFieldBegin(LINES_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.lines.size()));
          for (java.lang.String _iter6 : struct.lines)
          {
            oprot.writeString(_iter6);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.pairs != null) {
        oprot.writeFieldBegin(PAIRS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.pairs.size()));
          for (StringIntPair _iter7 : struct.pairs)
          {
            _iter7.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DoJobReplyTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DoJobReplyTupleScheme getScheme() {
      return new DoJobReplyTupleScheme();
    }
  }

  private static class DoJobReplyTupleScheme extends org.apache.thrift.scheme.TupleScheme<DoJobReply> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DoJobReply struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetLines()) {
        optionals.set(0);
      }
      if (struct.isSetPairs()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetLines()) {
        {
          oprot.writeI32(struct.lines.size());
          for (java.lang.String _iter8 : struct.lines)
          {
            oprot.writeString(_iter8);
          }
        }
      }
      if (struct.isSetPairs()) {
        {
          oprot.writeI32(struct.pairs.size());
          for (StringIntPair _iter9 : struct.pairs)
          {
            _iter9.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DoJobReply struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list10 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.lines = new java.util.ArrayList<java.lang.String>(_list10.size);
          java.lang.String _elem11;
          for (int _i12 = 0; _i12 < _list10.size; ++_i12)
          {
            _elem11 = iprot.readString();
            struct.lines.add(_elem11);
          }
        }
        struct.setLinesIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.pairs = new java.util.ArrayList<StringIntPair>(_list13.size);
          StringIntPair _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new StringIntPair();
            _elem14.read(iprot);
            struct.pairs.add(_elem14);
          }
        }
        struct.setPairsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

