package com.example.demo.controller;

import com.example.demo.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * http://127.0.0.1:8088/file/upload
 * 注意：必须使用@Controller而非@RestController，否着会报错
 */
@Controller
@RequestMapping(value = "/file")
@Slf4j
public class FileController {

    private static final String FILE_PATH = "D:\\static\\uploadfile\\";

    @GetMapping(value = "/upload")
    public String upload() {
        return "upload";
    }

    @PostMapping(value = "/uploadFile")
    @ResponseBody
    public String uploadFile(MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return "上传的文件不能为空！请重新上传";
        }
        if (file.getSize() <= 0) {
            return "上传的文件大小需要大于0kb";
        }

        List<String> list = new ArrayList<>();
        String line;
        try {
            InputStreamReader read = new InputStreamReader(file.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(read);
            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            log.warn("bufferedReader exception : ", e);
        }


        log.info("file.getContentType() : {}", file.getContentType());//image/png
        Date date = new Date();
        Long time = date.getTime();
        String originFileName = file.getOriginalFilename();//获取文件原始的名称
        String newFileName = time + originFileName;
        //获取项目运行的绝对路径
        String filePath = System.getProperty("user.dir");
        log.info("filePath : {}", filePath);
        //由于我是创建的多模块项目，所以获取到的项目运行路径为外层的项目路径，
        // 这时候我们就需要在项目相对路径这里加上项目的名称demo-upload


        //当然你也可以自己设置一个绝对路径用于图片上传，文件上传。
        //比如说：D:\\images\\
        File file1 = new File(FILE_PATH);

        if (!file1.exists()) {
            file1.mkdirs();
        }
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(FILE_PATH + newFileName);
            fileOutputStream.write(file.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return "localhost:8088/file/uploadfile/" + newFileName;
        } catch (java.io.IOException e) {
            log.info("upload exception : ", e);
        }
        return "上传失败";
    }

    @PostMapping(value = "/uploadFiles")
    @ResponseBody
    public String uploadFiles(MultipartFile[] files, HttpServletRequest request) {
        StringBuilder paths = new StringBuilder();
        for (MultipartFile file : files) {
            //中间的代码和上面的一样
            System.out.println(file.getContentType());//image/png
            Date date = new Date();
            Long time = date.getTime();
            String originFileName = file.getOriginalFilename();//获取文件原始的名称
            String newFileName = time + originFileName;
            //这里根据实际情况修改，可以用数组
            paths.append("localhost:8088/file/uploadfile/" + newFileName + "\n");
        }
        return paths.toString();
    }

    /**
     * 响应对象无需通过return返回
     * 响应对象是可以不用作为方法返回值返回的，其在方法执行时已经开始输出，且其无法与@RestController配合，以JSON格式返回给前端
     *
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    @GetMapping("/download")
    public ResponseEntity<Map<String, Object>> download(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) {
        File file = new File(FILE_PATH + fileName);
        if (!file.exists()) {
            return ResponseUtils.paramError("下载文件不存在");
        }

        // 清空response
        response.reset();
        String characterEncoding = request.getCharacterEncoding();
        response.setCharacterEncoding(characterEncoding);
        response.setContentType("application/octet-stream");
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, characterEncoding));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + file.length());
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(file);
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            //关闭资源
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            log.warn("download exception ", e);
            return ResponseUtils.serverError("download exception");
        }
        return ResponseUtils.ok(null);
    }
}