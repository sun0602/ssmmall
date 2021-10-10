package com.ssm.mall.action.console;

import com.google.common.collect.Maps;
import com.ssm.mall.common.Const;
import com.ssm.mall.common.Result;
import com.ssm.mall.common.ServerRes;
import com.ssm.mall.dao.pojo.Product;
import com.ssm.mall.dao.pojo.User;
import com.ssm.mall.dao.vo.ProductDetailVo;
import com.ssm.mall.service.iservice.ProductService;
import com.ssm.mall.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductAdminController {
    @Autowired
    ProductService productService;
    @ResponseBody
    @RequestMapping(value = "/save.do",method= RequestMethod.POST) //新增商品
    public ServerRes save(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        return productService.saveOrUpdate(product);
    }
    @RequestMapping(value="/update.do",method=RequestMethod.POST)//更新商品
    @ResponseBody
    public ServerRes update(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        return productService.saveOrUpdate(product);
    }
    @RequestMapping(value="/updateStatus.do",method = RequestMethod.POST)//更新商品状态（上架或下架）
    @ResponseBody
    public ServerRes updateProductStatus(HttpSession session, Integer id, Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        if (id == null || status == null) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return productService.setProductStatus(id,status);
    }

    @RequestMapping(value="/productDetail.do",method=RequestMethod.POST)//管理页面：根据商品id查询产品详情
    @ResponseBody
    public ServerRes<ProductDetailVo> getManageDetail(HttpSession session, Integer id){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        if (id == null) {
            return ServerRes.error(Result.ILLEAGLE_ARGUMENT);
        }
        return productService.getManageDetail(id);
    }

    @RequestMapping(value = "/list.do",method=RequestMethod.POST)//管理界面：分页查询商品列表
    @ResponseBody
    public ServerRes getManageList(HttpSession session,
                                   @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        return productService.getManageList(pageNum,pageSize);
    }


    @RequestMapping(value="/search.do",method = RequestMethod.POST)//管理界面：根据id、name搜索商品(name为模糊查询 )
    @ResponseBody
    public ServerRes search(HttpSession session,
                            @RequestParam(value = "id",required = false) Integer id,
                            @RequestParam(value = "name",required = false) String name,
                            @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        return productService.searchByIdName(id,name,pageNum,pageSize);
    }
    @ResponseBody
    @RequestMapping(value = "upload.do",method=RequestMethod.POST)
    public ServerRes upload(MultipartFile multipartFile, HttpSession session){
        //设定临时保存上传文件的地址是webapp下的upload_temp，然后再从临时地址上传到ftp服务器中
        String path = session.getServletContext().getRealPath("product_imgs");
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            return ServerRes.error(Result.NEED_ADMIN_LOGIN);
        }
        String targetFileName = productService.upload(multipartFile,path);
        String url = PropertyUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        //使用Map对结果集进行封装
        Map<String,String> resultMap = Maps.newHashMap();
        resultMap.put("uri",targetFileName);
        resultMap.put("url",url);
        return ServerRes.success(Result.UPLOAD_IMAGE_SUCCESS,resultMap);
    }

    @ResponseBody
    @RequestMapping(value = "upload_richtext_img.do",method=RequestMethod.POST)
    public Map uploadRichTextImg(MultipartFile multipartFile, HttpSession session, HttpServletResponse response){
        //设定临时保存上传文件的地址是webapp下的upload_temp，然后再从临时地址上传到ftp服务器中
        String path = session.getServletContext().getRealPath("richtext_imgs");
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        Map resultMap = Maps.newHashMap();
        //判断是否已经登录并具有管理员权限
        if(user == null || user.getRole() != Const.Role.ADMIN){
            resultMap.put("success",false);
            resultMap.put("msg","该功能需要管理员权限");
            return resultMap;
        }
        String targetFileName = productService.upload(multipartFile,path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success",false);
            resultMap.put("msg","文件上传失败");
            return resultMap;
        }
        String url = PropertyUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success",true);
        resultMap.put("msg","文件上传成功");
        resultMap.put("file_path",url);
        //simditor需要设置response格式
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        return resultMap;
    }
}
