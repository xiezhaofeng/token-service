package com.xunxintech.ruyue.coach.base;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "root")
@Data
public class BaseResponse<T>
{

	public BaseResponse(T data)
	{
		this.data = new ArrayList<T>();
		this.data.add(data);
		this.response = new Response();
	}

	public BaseResponse(List<T> data)
	{
		this.data = data;
		this.response = new Response();
	}

	public BaseResponse(String errcode, String errmsg)
	{
		this.data = new ArrayList<T>();
		this.response = new Response(errcode, errmsg);
	}

	private Response response;

	@JacksonXmlProperty(localName = "row")
	@JacksonXmlElementWrapper(localName = "data")
	private List<T> data;
	
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Response
{

	private String errcode = "0";

	private String errmsg = "";
}
