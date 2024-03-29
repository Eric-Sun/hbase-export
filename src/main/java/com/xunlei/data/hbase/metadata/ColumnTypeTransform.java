package com.xunlei.data.hbase.metadata;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * 从HBase获取的值都是byte[]，然后根据元数据中的类型解析，最后统一是String类型写入json进文件
 * 
 * @author q
 *
 */
public class ColumnTypeTransform {

	/**
	 * 普通的column，根据columnType判断类型，解析数据
	 * 
	 * @param columnType
	 * @param originalValue
	 * @return
	 */
	public static String transform(DataType columnType, byte[] originalValue) {
		if (columnType == null) {
			return null;
		}

		switch (columnType) {
		case LONG:
			return String.valueOf(Bytes.toLong(originalValue));
		case STRING:
			return String.valueOf(Bytes.toString(originalValue));
		case INT:
			return String.valueOf(Bytes.toInt(originalValue));
		default:
			return null;
		}
	}

	/**
	 * rowkey，先判断RowKeyAssembleType对象，然后通过columnType来解析
	 * 
	 * @param columnType
	 * @param assembleType
	 * @param originalValue
	 * @return
	 */
	public static String transformRowKey(DataType columnType, RowKeyAssembleType assembleType, byte[] originalValue) {
		if (columnType == null || assembleType == null) {
			return null;
		}
		String value = null;
		switch (assembleType) {
		case NO_ASSEMBLE:
			value = ColumnTypeTransform.transform(columnType, originalValue);
			break;
		case NEED_SPLIT:
			value = ColumnTypeTransform.transform(columnType, originalValue);
			break;
		case DIFFERENT_START:
			value = ColumnTypeTransform.transform(columnType, originalValue);
			break;
		case LENGTH_BYTE_TO_LONG:
			if (originalValue.length != Bytes.SIZEOF_LONG * 2) {
				// 不正常情况
				long v = Bytes.toLong(originalValue);
				value = String.valueOf(v);
			} else {
				// 正常情况，两个long都换成byte[]
				byte[] b1 = new byte[]{originalValue[0], originalValue[1], originalValue[2], originalValue[3], originalValue[4], originalValue[5], originalValue[6], originalValue[7]};
				byte[] b2 = new byte[]{originalValue[8], originalValue[9], originalValue[10], originalValue[11], originalValue[12], originalValue[13], originalValue[14], originalValue[15]};
				value = String.valueOf(b1) + "_" + String.valueOf(b2);
			}
			break;
		default:
			value = "";
		}
		return value;
	}

}
