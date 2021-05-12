package com.jingchen.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jingchen.ann.DataNode;

public class DataUtil
{
	private static DataUtil instance = null;
	//结果集
	private Map<String, Float> mTypes;
	private int mTypeCount;//一组输入对应一个结果

	private DataUtil()
	{
		mTypes = new HashMap<String, Float>();
		mTypeCount = 1;
	}

	public static synchronized DataUtil getInstance()
	{
		if (instance == null)
			instance = new DataUtil();
		return instance;

	}

	public Map<String, Float> getTypeMap()
	{
		return mTypes;
	}

	public int getTypeCount()
	{
		return mTypeCount;
	}

	public String getTypeName(float type)//不需要此方法（类名），直接输出结果
	{
		if (type == -1)
			return new String("无法判断");
		Iterator<String> keys = mTypes.keySet().iterator();
		while (keys.hasNext())
		{
			String key = keys.next();
			if (mTypes.get(key) == type)
				return key;
		}
		return null;
	}

	/**
	 * 根据文件生成训练集，注意：程序将以第一个出现的非数字的属性作为类别名称
	 * 
	 * @param fileName
	 *            文件名
	 * @param sep
	 *            分隔符
	 * @return
	 * @throws Exception
	 */
	public List<DataNode> getDataList(String fileName, String sep)
			throws Exception
	{
		List<DataNode> list = new ArrayList<DataNode>();
		BufferedReader br = new BufferedReader(new FileReader(
				new File(fileName)));
		String line = null;
		while ((line = br.readLine()) != null)
		{
			String splits[] = line.split(sep);
			DataNode node = new DataNode();
			int i = 0;
			for (; i < splits.length; i++)
			{
				try{
					if(i<splits.length-1){
						//输入值太大，需要要预处理
						node.addAttrib(Float.valueOf(splits[i])/10000);
					}// 非输入层的数字，则为结果层数字，将结果放入结果集
					if (i==splits.length-1)
					{
						//结果值太大，需要预处理
						node.setType(Float.valueOf(splits[i])/10000);
						list.add(node);
						break;
					}

				}catch (NumberFormatException e){

				}

			}
		}
		return list;
	}
}
