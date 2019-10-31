package com.gin.admin.cg.model;

import java.util.List;

public class ModelInfo {
	private String packageName;
	private String className;
	private String remark;
	private List<String> fieldNames;
	private List<String> capitalizedNames;
	private List<String> fieldClasses;
	private List<String> remarks;

	public ModelInfo() {
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(List<String> fieldNames) {
		this.fieldNames = fieldNames;
	}

	public List<String> getCapitalizedNames() {
		return capitalizedNames;
	}

	public void setCapitalizedNames(List<String> capitalizedNames) {
		this.capitalizedNames = capitalizedNames;
	}

	public List<String> getFieldClasses() {
		return fieldClasses;
	}

	public void setFieldClasses(List<String> fieldClasses) {
		this.fieldClasses = fieldClasses;
	}

	public List<String> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(packageName).append(";\r\n");
		sb.append("/** ").append(remark).append(" */\r\n");
		sb.append("public class ").append(className).append("{\r\n");
		int size = fieldNames.size();
		for (int i = 0; i < size; i++) {
			sb.append("/** ").append(remarks.get(i)).append(" */\r\n");
			sb.append("private ").append(fieldClasses.get(i)).append(" ").append(fieldNames.get(i)).append(";\r\n");
		}
		sb.append("}");
		return sb.toString();
	}

}
