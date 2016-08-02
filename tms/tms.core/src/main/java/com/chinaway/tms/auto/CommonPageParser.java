package com.chinaway.tms.auto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Scanner;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CommonPageParser {

	public static void WriterPage(Map<String, Object> root, String templateName, String fileDirPath, String targetFile){
		 File file = null;
		 BufferedWriter out = null;
		try {
    	  file = new File(fileDirPath + targetFile);
    	  if (file.exists()) {
    		  //已存在提示是否覆盖
    		  Scanner scanner = new Scanner(System.in);
    		  System.out.println("====="+file.getName()+" 文件已存在,是否覆盖(y:覆盖,n：不覆盖)=====");
    		  String isCopy = scanner.next();
    		  if("n".equals(isCopy)){
    			  return;
    		  }
    		  
    	  }else{
    		  new File(file.getParent()).mkdirs();
    	  }
    	  FileOutputStream fos = new FileOutputStream(file);
    	  out = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
    	  
    	  // 通过Freemaker的Configuration读取相应的ftl
          Configuration config = new Configuration();
          //"/template"相对于this的文件路径(bin文件夹[普通项目]或target/class文件夹[maven项目])
          config.setClassForTemplateLoading(CommonPageParser.class, "/template");
          
          // 在模板文件目录中找到名称为name的文件
          Template temp = config.getTemplate(templateName);
          temp.process(root, out);
          
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          try {
              if (out != null)
                  out.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
	}
	
}