package com.kongbox.pdm.controller;

import com.kongbox.pdm.service.JdbcTest;
import com.kongbox.pdm.service.PdmTableService;
import com.sun.deploy.net.HttpResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * Title:
 * Description:
 *
 * @author kdc
 * @version 1.0
 * @date 2019/1/24.11:28 首次创建
 * @date 2019/1/24.11:28 最后修改
 * @copyright 中科软科技股份有限公司
 */
@Controller
@RequestMapping("/")
public class CommonController {

    @Autowired
    private JdbcTest jdbcTest;

    @Autowired
    private PdmTableService pdmTableService;

    public CommonController() {
    }

    @RequestMapping("index")
    public String index(){

        return "index.html";
    }

    @RequestMapping(value = "/pdmToXls",method = RequestMethod.GET)
    public String pdmTableToEexcl(String filePath, HttpRequest request, HttpResponse response){
        Workbook wb = pdmTableService.pdmTable(filePath);

//        response.getHeaders("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

        return "";
    }
}
