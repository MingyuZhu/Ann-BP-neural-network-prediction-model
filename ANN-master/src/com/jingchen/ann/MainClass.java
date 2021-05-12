package com.jingchen.ann;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import com.jingchen.util.ConsoleHelper;
import com.jingchen.util.DataUtil;

/**
 * 说明：目前使用的这份测试集是从原始数据iris中随机抽取26个组成的
 * 要判断某一个样本test属于已知样本种类中的哪一类时，通过计算找出所有样本中与测试样本最近或者最相似的K个样本，统计这K个样本中哪一种类最多则把测试样本归位该类
 *
 * 
 */
public class MainClass
{
	public static void main(String[] args) throws Exception
	{
		if (args.length < 5)
		{
			System.out
					.println("Usage: \n\t-train trainfile\n\t-test predictfile\n\t-sep separator, default:','\n\t-eta eta, default:0.5\n\t-iter iternum, default:5000\n\t-out outputfile");
			return;
		}
		//学习率eta的调整很关键
		ConsoleHelper helper = new ConsoleHelper(args);
		String trainfile = helper.getArg("-train", "");
		String testfile = helper.getArg("-test", "");
		String separator = helper.getArg("-sep", ",");
		String outputfile = helper.getArg("-out", "");
		float eta = helper.getArg("-eta", 0.02f);
		int nIter = helper.getArg("-iter", 1000);
		DataUtil util = DataUtil.getInstance();
		List<DataNode> trainList = util.getDataList(trainfile, separator);
		List<DataNode> testList = util.getDataList(testfile, separator);
		BufferedWriter output = new BufferedWriter(new FileWriter(new File(
				outputfile)));
		int typeCount = util.getTypeCount();
		AnnClassifier annClassifier = new AnnClassifier(trainList.get(0)
				.getAttribList().size(), trainList.get(0).getAttribList()
				.size() + 8, typeCount);
		annClassifier.setTrainNodes(trainList);
		annClassifier.train(eta, nIter);
		for (int i = 0; i < testList.size(); i++)
		{
			DataNode test = testList.get(i);
			float type = annClassifier.test(test);
			List<Float> attribs = test.getAttribList();
			for (int n = 0; n < attribs.size(); n++)
			{
				output.write(attribs.get(n) + ",");
				output.flush();
			}
			output.write("结果："+type*10000 + "\n");
			output.flush();
		}
		output.close();

	}

}
