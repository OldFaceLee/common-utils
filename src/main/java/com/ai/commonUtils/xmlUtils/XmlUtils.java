package com.ai.commonUtils.xmlUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlUtils {
	
	private static Document document;
	private static SAXBuilder builder;
	private static Element root;
	
	
	private static class Singleton{
		public static XmlUtils xmlUtilInstance = new XmlUtils();
	}
	public XmlUtils(){
		
	}
	public XmlUtils getInstance(){
		return Singleton.xmlUtilInstance;
	}

	public static void main(String[] args) {
//		generateTestNGXML();
	}

	public enum ParallelLevel{
		METHODS,FALSE
	}

	public static void generateTestNGXML(String suiteName,ParallelLevel parallelLevel,int threadCount,
										 String testName,List<String> includeTestGroupName,List<String> listenerClass,
										 String generateFilePath){
		//创建一个Document对象，代表xml
		org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
//		document.addDocType("suite","SYSTEM","http://testng.org/testng-1.0.dtd");
		org.dom4j.Element root = document.addElement("suite");
		root.addAttribute("name",suiteName);
		root.addAttribute("parallel",parallelLevel.toString());
		root.addAttribute("thread-count",String.valueOf(threadCount));
		org.dom4j.Element rootChildNode = root.addElement("test").addAttribute("name",testName).addAttribute("preserve-order","true");
		org.dom4j.Element groups = rootChildNode.addElement("groups");
		org.dom4j.Element run = groups.addElement("run");

		for (int i = 0; i < includeTestGroupName.size(); i++) {
			org.dom4j.Element include = run.addElement("include").addAttribute("name",includeTestGroupName.get(i));
		}
		org.dom4j.Element listeners = root.addElement("listeners");
		for (int i = 0; i < listenerClass.size(); i++) {
			listeners.addElement("class-name",listenerClass.get(i));
		}
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		XMLWriter os = null;
		try {
			os = new XMLWriter(new FileWriter(new File(generateFilePath)),outputFormat);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}



	
	
	public static List <String> getTestNGXmlIncludeAttribute(String xmlFilePath) throws JDOMException, IOException{
		List list = new ArrayList();
		builder = new SAXBuilder(); 
		document = builder.build(xmlFilePath);
		Element root = document.getRootElement();
		List<Element> children = new ArrayList();
		children = root.getChild("test").getChild("classes").getChild("class").getChild("methods").getChildren();
		for(int i=0; i<=children.size()-1;i++){
			list.add(children.get(i).getAttributeValue("name"));
		}
		return list;
	}
	
	public static List<List<Content>> getFirstChildNodeContentOfNodeName(String xmlFilePath,String nodeName) throws JDOMException, IOException{
		List <Element> list = XmlUtils.getChildNode(xmlFilePath, nodeName);
		List <List<Content>> failedCaseContent = new ArrayList();
		if(list.size() !=0){
			for(int i=0;i<=list.size()-1;i++){
				 List <Element> failure =  list.get(i).getChildren();
				if(failure.size() !=0){
					failedCaseContent.add(list.get(i).getChildren().get(0).getContent());
				}
			}
		}
		
		return failedCaseContent;
	}
	
	/**
	 * 获取跟目录root的属性值
	 * @param filePath
	 * @param attname
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static String getRootAttributeValue(String filePath, String attname) throws JDOMException, IOException{
		builder = new SAXBuilder(); 
		document = builder.build(filePath);
		Element root = document.getRootElement();
		return root.getAttributeValue(attname);
	
	}
	
	public static List <Element>  getChildNode (String filePath, String childNode) throws JDOMException, IOException{
		String attributeValue = null;
		builder = new SAXBuilder();  
		document = builder.build(filePath);
		
        Element root = document.getRootElement();
        List <Element> list = root.getChildren(childNode);
		return list;
	}

}
