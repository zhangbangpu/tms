package ${bussPackage}.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ${bussPackage}.model.${className};
import ${bussPackage}.service.${className}Service;
import ${appPackagePrefix}.utils.json.JsonUtil;
import ${appPackagePrefix}.utils.page.PageBean;

@Controller
@RequestMapping("/${lowerName}")
public class ${className}Controller {

	@Autowired
	private ${className}Service ${lowerName}Service;

	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping("/init")
	public String init() {
		
		return "${autoModel.appName}/${lowerName}";
	}
	
	/**
	 * 分页列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public String list(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		//easyUI的grid分页参数,具体处理在service层
		map.put("pageNo", request.getParameter("page"));
		map.put("pageSize", request.getParameter("rows"));
		//查询域的查询条件
		//map.put("deptid", request.getParameter("deptid"));
		map.put("name", request.getParameter("name"));
		PageBean<${className}> list = ${lowerName}Service.select2PageBean(map);
		
		return JsonUtil.obj2JsonStr(list);
	}
	
	/**
     * 保存
     * 返回的是text
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(${className} ${lowerName}){
//    	Map<String,Object> map = new HashMap<>();
//    	try {
//    		${lowerName}Service.insert(${lowerName});
//    		map.put("success", true);
//        	map.put("msg", "保存成功");
//		} catch (Exception e) {
//			map.put("success", false);
//			map.put("msg", "保存失败");
//		}
//    	return JsonUtil.obj2JsonStr(map);
    	try {
    		${lowerName}Service.insert(${lowerName});
    		return "success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "error";
    	}
    }
    
    /**
     * 修改
     * 返回的是text
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(${className} ${lowerName}){
    	try {
    		${lowerName}Service.updateSelective(${lowerName});
    		return "success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "error";
    	}
    }
    
    /**
     * 删除
     * 返回的是text
     */
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public String delete(@PathVariable String ids){
    	try {
    		${lowerName}Service.deleteById(ids);
    		return "success";
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "error";
    	}
    }
	
}