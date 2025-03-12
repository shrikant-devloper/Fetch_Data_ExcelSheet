package com.digiquad.model;

import java.util.List;

public class RowData 
{
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	private List<String> columns;
	public RowData(List<String> columns) 
	{
		super();
		this.columns = columns;
	}
	public RowData() {
		super();
	}
	public RowData(String value, List<String> columns) {
		super();
		this.value = value;
		this.columns = columns;
	}
	public List<String> getColumns() 
	{
		return columns;
	}
	public void setColumns(List<String> columns) 
	{
		this.columns = columns;
	}
}
