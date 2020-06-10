package com.ai.commonUtils.collectionUtils;

import java.util.*;


import org.apache.log4j.Logger;

public class CollectionUtils {
	private static final Logger logger = Logger.getLogger(CollectionUtils.class);

	/*private CollectionUtils(){}ƒ
	private static CollectionUtils collectionUtils = null;
	public static CollectionUtils getInstance(){
		if (collectionUtils == null) {
			collectionUtils = new CollectionUtils();
			logger.info("单例模式创建CollectionUtils对象");
		}
		return collectionUtils;
	}*/


	/**
	 * 在一个list里边制定listSize后分出多个子list
	 * @param list
	 * @param toIndex
	 * @param <T>
	 * @return
	 */
	public <T> List<List<T>> subList(List<T> list, int toIndex) {
		List<List<T>> result = new ArrayList<>();
		int listSize = list.size();
		for (int i = 0; i < list.size(); i += toIndex) {
			if (i + toIndex > listSize) {
				toIndex = listSize - i;
			}
			List newList = list.subList(i, i + toIndex);
			result.add(newList);
		}
		return result;
	}


	/**
	 * 已知Map，获取key中最大的值
	 * @param map
	 * @return
	 */
	public static String getKeyOrderByDesc(Map<String,String>map){
		String value = null;
		List<String> list = new ArrayList();
		for(Map.Entry<?, ?> en : map.entrySet()){
			value = en.getKey().toString();
			list.add(value);
		}
		Collections.sort(list);
		logger.info("获取map"+map+"中倒序第一个key的值为："+list.get(list.size()-1));
		return list.get(list.size()-1);
	}
	/**
	 * 已知Map，获取value中最大的值
	 * @param map
	 * @return
	 */
	public static String getValueOrderByDesc(Map<String,String>map){
		String value = null;
		List<String> list = new ArrayList();
		for(Map.Entry<?, ?> en : map.entrySet()){
			value = en.getValue().toString();
			list.add(value);
		}
		Collections.sort(list);
		logger.info("获取map"+map+"中倒序第一个value的值为："+list.get(list.size()-1));
		return list.get(list.size()-1);
	}
	
	/**
	 * list集合 正序
	 * @param list
	 * @return
	 */
	public static List sortListAsc(List list){
		Collections.sort(list);
		logger.info("升序排列后的list为"+list);
		return list;
	}
	
	/**
	 * List集合倒序
	 * @param list
	 * @return
	 */
	public static List sortListDesc(List list){
		Collections.reverse(list);
		logger.info("倒序排列后的list为"+list);
		return list;
	}
	
	/**
	 * 将Object[]数组转化成List<?>
	 * @param arr
	 * @return
	 */
	public static List<?> arrayToList(Object[] arr) {
		ArrayList list = new ArrayList();
		if (arr == null) {
			logger.info("数组对象[" +arr+"]为空");
			return list;
		}
			List list1 = Arrays.asList(arr);
			logger.info("将其数组对象转化为List");
			return list1;
	}
	
	/**
	 * List<?> 转化Object []数组
	 * @param list
	 * @return
	 */
	public static String[] listToStringArray(List<?> list){
		String [] strs = new String[list.size()];
		logger.info("将其list对象"+list+"转化为数组");
		return list.toArray(strs);
	}


	/**
	 * 获取Map类型中的key，装入List里
	 * @param map
	 * @return
	 */
	public static List<String> keyAddIntoList(Map<String,String> map){
		List mapKey = new ArrayList();
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			mapKey.add(iterator.next().toString());
		}
		logger.info("将map"+map+"中的key装入list中,为"+mapKey);
		return mapKey;

	}
	
	/**
	 * 获取Map类型中的value，装入List里
	 * @param map
	 * @return
	 */
	public static List<String> vauleAddIntoList(Map<String,String>map){
		List mapVaule = new ArrayList();
		Iterator iterator = map.keySet().iterator();
		while(iterator.hasNext()){
			mapVaule.add(map.get(iterator.next().toString()));
		}
		logger.info("将map"+map+"中的value装入list中,为"+mapVaule);
		return mapVaule;
	}

	/**
	 * 遍历List<Map<String,String>>,给出key 返回对应的值
	 * @param list
	 * @param mapKey
	 * @return
	 */
	public static String parseList_T_isMap(List<Map<String,String>> list, String mapKey){
		String mapVaule = null;
		for(Map<String,String> map: list){
			for(Map.Entry<String,String> m : map.entrySet()){
					if(mapKey.equals(m.getKey())){
						logger.info(list+"中，当mapKey ="+mapKey+"时，获取对应的mapVaule="+m.getValue());
						return m.getValue();
					}
			}
		}
		return mapVaule;
	}

	/**
	 * 删除list中重复的元素
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicate(List<String> list){
		LinkedHashSet<String> set = new LinkedHashSet<String>(list.size());
		set.addAll(list);
		list.clear();
		list.addAll(set);
		logger.info("删除list中重复的元素");
		return list;
	}

	/**
	 *
	 * @param list 总记录
	 * @param pageCount 每页放多少记录
	 */
	public static List pageList (List<String> list, int pageCount){
		List<String> strings = new ArrayList<String>();
		int pageSize = list.size(); //总记录
		int pageNO = (int)Math.ceil(pageSize / (double)pageCount);  //总页数
		for(int i=0;i<pageNO;i++){
			strings = list.subList(i*pageCount,(i*pageCount)+pageCount>pageSize?pageSize:(i*pageCount)+pageCount);
			System.out.println(strings);
		}
		return strings;
	}

	/**
	 * 获取分离的list数据   list.add(a), list.add(b), 则获取到[[a], [b]]
	 * @param LIMIT_ROW
	 * @param dataList
	 * @return
	 */
	public static List getSplitedListData(Integer LIMIT_ROW, List<String> dataList){
		List<List<String>> rets = new ArrayList<List<String>>();
		if (dataList.size() > LIMIT_ROW) {
			for (int a = 0; dataList != null && a < dataList.size(); a = a+ LIMIT_ROW) {
				List<String> subList = new ArrayList<String>();
				if((a + LIMIT_ROW.intValue())>= dataList.size()){
					subList = (List) dataList.subList(a, dataList.size());
				}
				else{
					subList = (List) dataList.subList(a, a + (LIMIT_ROW));
				}
				if (subList != null && subList.size() > 0) {
					rets.add(subList);
				}
			}
		} else {
			if (dataList.size() > 0) {
				rets.add(dataList);
			}
		}
		return rets;
	}




}
