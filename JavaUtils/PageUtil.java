package com.calabar.commons.utils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import com.calabar.bw.Const;

public class PageUtil<T> {
	
	private Map<String, Object[]> params;
	private List<Order> orders;
	
	private List<String> selectFilds;
	
	public PageUtil() {
		params = RequestUtil.getQueryMap();
		//参数样试：S_name_DESC,S_name_ASC
		orders = RequestUtil.getOrders();
		
		/*String sortField = RequestUtil.getString(Const.SFIELD_KEY);
		String sortDir = RequestUtil.getString(Const.SDIR_KEY);
		if(StringUtils.isNotBlank(sortField)){
			if("desc".equalsIgnoreCase(sortDir)){
				Order o = new Order(Sort.Direction.DESC, sortField);
				orders.add(o);
			}else {
				Order o = new Order(Sort.Direction.ASC, sortField);
				orders.add(o);
			}
		}*/
	}
	
	public PageUtil(String sortField,String sortDir) {
		params = RequestUtil.getQueryMap();
		orders = new ArrayList<Sort.Order>();
		if(StringUtils.isNotBlank(sortField)){
			if("desc".equalsIgnoreCase(sortDir)){
				Order o = new Order(Sort.Direction.DESC, sortField);
				orders.add(o);
			}else {
				Order o = new Order(Sort.Direction.ASC, sortField);
				orders.add(o);
			}
		}
	}
	
	public void setSort(String sortField,String sortDir){
		orders = new ArrayList<Sort.Order>();
		if(StringUtils.isNotBlank(sortField)){
			if("desc".equalsIgnoreCase(sortDir)){
				Order o = new Order(Sort.Direction.DESC, sortField);
				orders.add(o);
			}else {
				Order o = new Order(Sort.Direction.ASC, sortField);
				orders.add(o);
			}
		}
	}
	
	public void addSort(String sortField,String sortDir){
		if(StringUtils.isNotBlank(sortField)){
			if("desc".equalsIgnoreCase(sortDir)){
				Order o = new Order(Sort.Direction.DESC, sortField);
				orders.add(o);
			}else {
				Order o = new Order(Sort.Direction.ASC, sortField);
				orders.add(o);
			}
		}
	}

	public Pageable getPageable() {
		int pnumber = RequestUtil.getInt(Const.PNUMBER_KEY);
		int psize = RequestUtil.getInt(Const.PSIZE_KEY);
		if(psize == 0){
			psize = Const.DEFAULT_PSIZE;
		}
		return new PageRequest(pnumber, psize, getSort());
	}
	
	public Sort getSort(){
		Sort sort = null;
		if(orders!=null&&orders.size()>0){
			sort = new Sort(orders);
		}
		return sort;
	}

	/**
	 * Q_S_name_L
	 * Q_S_name.name_L 联接查询，目前只支持一级的查询
	 * 最后一个关键字含义：
	 * L-like
	 * NL-not like
	 * LR-右like
	 * LL-左like
	 * GT-大于
	 * LT-小于
	 * GE-大于或等于
	 * LE-小于或等于
	 * NULL-is null
	 * NOTNULL - is not null
	 * EQ-等于
	 * NEQ-不等于
	 * IN-in
	 * NIN-not in 暂不支持
	 * @return
	 */
	public Specification<T> getSpecification() {
		Specification<T> spc = new Specification<T>() {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			private Predicate tt(String[] strs,Object value,Root<T> root,CriteriaBuilder cb){
				Path<?> path;
				if(strs[2].contains(".")){
					String[] ss = strs[2].split("[.]");
					if(ss.length==4){
						path = root.join(ss[0],JoinType.LEFT).join(ss[1],JoinType.LEFT).join(ss[2],JoinType.LEFT).get(ss[3]);
					}else if (ss.length==3){
						path = root.join(ss[0],JoinType.LEFT).join(ss[1],JoinType.LEFT).get(ss[2]);
					}else {
						path = root.join(ss[0],JoinType.LEFT).get(ss[1]);
					}
				}else {
					path = root.get(strs[2]);
				}
				
				String op = strs[3];
				Object v = getTypeValue(strs[1], value);
				Predicate predicate;
				if("L".equals(op)){
					predicate = cb.like(path.as(String.class), "%"+value+"%");
				} else if("NL".equals(op)){
					predicate = cb.notLike(path.as(String.class), "%"+value+"%");
				} else if("LR".equals(op)){
					predicate = cb.like(path.as(String.class), value+"%");
				} else if("LL".equals(op)){
					predicate = cb.like(path.as(String.class), "%"+value);
				} else if("LT".equals(op)){
					predicate = cb.lessThan((Expression<Comparable>)path.as(v.getClass()), (Comparable)v);
				} else if("GT".equals(op)){
					predicate = cb.greaterThan((Expression<Comparable>)path.as(v.getClass()), (Comparable)v);
				} else if("GE".equals(op)){
					predicate = cb.greaterThanOrEqualTo((Expression<Comparable>) path.as(v.getClass()), (Comparable)v);
				} else if("LE".equals(op)){
					predicate = cb.lessThanOrEqualTo((Expression<Comparable>) path.as(v.getClass()), (Comparable)v);
				} else if("NULL".equals(op)){
					predicate = cb.isNull(path.as(v.getClass()));
				} else if("NOTNULL".equals(op)){
					predicate = cb.isNotNull(path.as(v.getClass()));
				} else if("EQ".equals(op)){
					predicate = cb.equal(path.as(v.getClass()),value);
				} else if("NEQ".equals(op)){
					predicate = cb.notEqual(path.as(v.getClass()),value);
				} else if("IN".equals(op)){
					//如果是枚举类型的IN查询
					if("E".equals(strs[1])){
						List vs = (List)value;
						Enum ev = (Enum)vs.get(0);
						predicate = path.as(ev.getDeclaringClass()).in(vs);
					}else{
						List<String> t = Arrays.asList(v.toString().split(","));
						predicate = path.as(String.class).in(t);
					}
				} else if("NIN".equals(op)){
					if("E".equals(strs[1])){
						List vs = (List)value;
						Enum ev = (Enum)vs.get(0);
						predicate = path.as(ev.getDeclaringClass()).in(vs).not();
					}else{
						List<String> t = Arrays.asList(v.toString().split(","));
						predicate = path.as(String.class).in(t).not();
					}
				} else {
					predicate = null;
				}
				return predicate;
			}
			
			@Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate p = null;
                for (String key : params.keySet()) {
                	Object[] value = params.get(key);
					String[] strs = key.split("[_]");
					if(strs.length==4){
						Predicate pp = tt(strs,value[0],root,cb);
						if(pp!=null){
							if(p!=null){
								p = cb.and(p,pp);
							}else{
								p = cb.and(pp);
							}
						}
					}else if(strs.length>=9){
						String[] tjs = key.split("_or_");
						Predicate p0 = null;
						for (int i=0;i<tjs.length;i++) {
							String tj = tjs[i];
							String[] ss = tj.split("[_]");
							if(ss.length==4){
								Predicate p1 = tt(ss,value[i],root,cb);
								if(p1!=null){
									if(p0!=null){
										p0 = cb.or(p0,p1);
									}else{
										p0 = cb.or(p1);
									}
								}
							}
						}
						if(p0!=null){
							if(p!=null){
								p = cb.and(p,p0);
							}else{
								p = cb.and(p0);
							}
						}
					}
				}
                 //去重
				query.distinct(true);
				if(selectFilds!=null&&selectFilds.size()>0){
					List<Selection<?>> ss = new ArrayList<>();
					for (String fild : selectFilds) {
						ss.add(root.get(fild));
					}
					query.multiselect(ss);
				}
                return p;
            }
        };
		return spc;
	}
	
	public void addParameter(String key,Object... value){
		this.params.put(key, value);
	}
	
	/**
	 * S	表示String
	 * L	表示Long
	 * N	表示Integer
	 * DB	表示Double
	 * BD	表示BigDecimal
	 * FT	表示Float
	 * SN	表示Short
	 * D	表示Date
	 * DE	表示结束时间，为查询两个时间之间的数据方便，这里会把天数加1
	 * E	表示枚举类型，value需手动处理成具体的枚举类
	 * U	表示UUID
	 * B	表示Boolean
	 * @param type
	 * @param value
	 * @return
	 */
	private Object getTypeValue(String type,Object value){
		if("S".equals(type)){
			return value.toString();
		}else if ("L".equals(type)){
			return Long.parseLong(value.toString());
		}else if ("N".equals(type)){
			return Integer.parseInt(value.toString());
		}else if ("DB".equals(type)){
			return Double.parseDouble(value.toString());
		}else if ("BD".equals(type)){
			return new BigDecimal(value.toString());
		}else if ("FT".equals(type)){
			return Float.parseFloat(value.toString());
		}else if ("SN".equals(type)){
			return Short.parseShort(value.toString());
		}else if ("D".equals(type)){
			Date d = DateFormatUtil.parse(value.toString());
			return d;
		}else if ("DE".equals(type)){
			Date d = DateFormatUtil.parse(value.toString());
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.DAY_OF_MONTH, 1);
			return c.getTime();
		}else if("E".equals(type)){
			return value;
		}else if("U".equals(type)){
			if(value !=null){
				return value;
			}else {
				return UUID.randomUUID();
			}
		}else if("B".equals(type)){
			return Boolean.parseBoolean(value.toString());
		}else {
			return value;
		}
	}

	public List<String> getSelectFilds() {
		return selectFilds;
	}

	public void setSelectFilds(List<String> selectFilds) {
		this.selectFilds = selectFilds;
	}
	
	/**
	 * 
	 * <li>Description: 清空当前的查询条件</li>
	 *
	 */
	public void clearParameter() {
		this.params = null;
		this.params = new HashMap<>();
	}
	
	/**
	 * 
	 * <li>Description: 清空当前的排序规则</li>
	 *
	 */
	public void clearSort() {
		this.orders = null;
		this.orders = new ArrayList<Order>(5);
	}
	
	/**
	 * 
	 * <li>Description: 清空当前的选择列</li>
	 *
	 */
	public void clearSelectFilds() {
		this.selectFilds = null;
		this.selectFilds = new ArrayList<String>(10);
	}
	
	/*
	 * 不将request参数加入的pageUtil
	 */
	public PageUtil(boolean isAddRequestParam) {
		this.params = new HashMap<>();
		this.orders = new ArrayList<Order>(5);
	}
	
}
