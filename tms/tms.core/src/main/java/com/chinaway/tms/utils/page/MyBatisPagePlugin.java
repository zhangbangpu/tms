package com.chinaway.tms.utils.page;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mybatis分页插件
 * 
 * @ClassName MyBatisPagePlugin 
 * @author shuheng
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class MyBatisPagePlugin implements Interceptor{
	private static final Logger log = LoggerFactory.getLogger(MyBatisPagePlugin.class);
	private String dialect="mysql";
	private String pageMatch=".*Page$";
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(invocation.getTarget() instanceof StatementHandler){
			StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
			MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
			// 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)  
			while(metaObject.hasGetter("h")){
				Object object = metaObject.getValue("h");
				metaObject = SystemMetaObject.forObject(object);
			}
			// 分离最后一个代理对象的目标类 
			while(metaObject.hasGetter("target")){
				Object object = metaObject.getValue("target");
				metaObject = SystemMetaObject.forObject(object);
			}
			MappedStatement mappedStatement = (MappedStatement)metaObject.getValue("delegate.mappedStatement");
			if(mappedStatement.getId().matches(pageMatch)){
				BoundSql boundSql = (BoundSql)metaObject.getValue("delegate.boundSql");
				String sql = boundSql.getSql();
				
				PageBean<?> pageBean = (PageBean<?>)metaObject.getValue("delegate.boundSql.parameterObject.pageBean");
				Object needPageObj = metaObject.getValue("delegate.boundSql.parameterObject.needPage");
//				Object needCountObj = metaObject.getValue("delegate.boundSql.parameterObject.needCount");
//				boolean needCount = true;
				boolean needPage = false;//默认不分页
//				if(needCountObj!=null){
//					needCount = (Boolean)needCountObj;
//				}
				if(needPageObj!=null){
					needPage = (Boolean)needPageObj;
				}
				//是否分页
				if(needPage){
					String pageSql = buildPageSql(sql, pageBean);
					metaObject.setValue("delegate.boundSql.sql", pageSql);
//					if(needCount){
					Connection conn = (Connection)invocation.getArgs()[0];
					setPageParameters(sql, pageBean, conn, mappedStatement, boundSql);
//					}
				}
			}
		}
		return invocation.proceed();
	}
	
	@Override
	public Object plugin(Object target) {
		//当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的  
	    // 次数 
		if(target instanceof StatementHandler){
			return Plugin.wrap(target, this);
		}else{
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
		//由于有默认值，暂时不采用配置
//		this.dialect = properties.getProperty("dialect");
//		this.pageMatch = properties.getProperty("pageMatch");
	}
	
	/**
	 * 重写sql
	 * @param sql
	 * @param pageBean
	 * @return
	 */
	private String buildPageSql(String sql,PageBean<?> pageBean){
//		Assert.notNull(pageBean, "pageBean is null");
//		return buildPageSqlForDialect(dialect, sql, pageBean).toString();
		StringBuilder builder = new StringBuilder();
		if("oracle".equalsIgnoreCase(dialect)){
			builder.append("SELECT * FROM (SELECT A.*,ROWNUM R FROM (");
			builder.append(sql);
			builder.append(") A WHERE ROWNUM<=").append(pageBean.getPageLast());
			builder.append(") WHERE R>").append(pageBean.getStart());
		}else if("mysql".equalsIgnoreCase(dialect)){
			builder.append(sql);
			builder.append(" LIMIT ").append(pageBean.getStart()).append(",").append(pageBean.getPageLast());
		}else{
			builder.append(sql);
		}
		log.debug("分页sql:{}",builder);
		return builder.toString();
	}
	
	/**
	 * 重设分页参数里的总页数等 
	 * @param sql
	 * @param pageBean
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 */
	private void setPageParameters(String sql,PageBean<?> pageBean,Connection connection,MappedStatement mappedStatement,BoundSql boundSql){
		String countSql = "SELECT COUNT(*) FROM ("+sql+") as temp";
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
			//支持 foreach 注意mybatis 版本在3.3.x以下
			Field metaParamsField = ReflectUtil.getFieldByFieldName(boundSql, "metaParameters");
            if (metaParamsField != null) {
                MetaObject mo = (MetaObject) ReflectUtil.getValueByFieldName(boundSql, "metaParameters");
                ReflectUtil.setValueByFieldName(countBS, "metaParameters", mo);
            }
			
			setParameters(statement, mappedStatement, countBS, boundSql.getParameterObject());
			rs = statement.executeQuery();
			int totalCount = 0;
			if(rs.next()){
				totalCount = rs.getInt(1);
			}
			pageBean.setTotalCount(totalCount);
			
		} catch (SQLException e) {
			log.error("PagePlugin error get total count:",e);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				statement.close();
			} catch (SQLException e) {
				log.error("PagePlugin error close rs or stmt:",e);
			}
		}
	}
	
	private void setParameters(PreparedStatement statement,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject)throws SQLException{
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(statement);
	}

}
