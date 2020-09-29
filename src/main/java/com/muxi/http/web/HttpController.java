package com.muxi.http.web;

import com.muxi.http.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * HttpClient 接收方
 *
 * @author muxi
 * @date 2020/9/28
 */
@RestController
public class HttpController {

    @GetMapping("get")
    public String get() {
        return "get";
    }

    @GetMapping("getByParams")
    public String getByParams(String name, Integer age) {
        return "[ name: " + name + ", age: " + age + " ]";
    }

    @PostMapping("post")
    public String post() {
        return "post";
    }

    @PostMapping("postByParams")
    public String postByParams(String name, Integer age) {
        return "[ name: " + name + ", age: " + age + " ]";
    }

    @PostMapping("postByObj")
    public String postByObj(@RequestBody User user) {
        return user.toString();
    }

    @PostMapping("postByParamsObj")
    public String postByParamsObj(@RequestBody User user, Integer flag, String meaning) {
        return user.toString() + "\n" + flag + ">>>" + meaning;
    }

    @GetMapping("https")
    public String https() {
        return "https";
    }

    @PostMapping("postForm")
    public String postForm(String name, Integer age) {
        return "[ name: " + name + ", age: " + age + " ]";
    }

    /**
     * httpclient传文件测试
     * <p>
     * 即multipart/form-data测试。
     * 多文件的话，可以使用数组MultipartFile[]或集合List<MultipartFile>来接收
     * 单文件的话，可以直接使用MultipartFile来接收
     */
    @PostMapping("files")
    public String files(
            @RequestParam("name") String name,
            @RequestParam("age") Integer age,
            @RequestParam("files") List<MultipartFile> multipartFiles)
            throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(64);
        // 防止中文乱码
        sb.append("\n");
        sb.append("name=").append(name)
                .append("\tage=").append(age);
        String fileName;
        for (MultipartFile file : multipartFiles) {
            sb.append("\n文件信息:\n");
            fileName = file.getOriginalFilename();
            if (fileName == null) {
                continue;
            }
            // 防止中文乱码
            // 在传文件时，将文件名URLEncode，然后在这里获取文件名时，URLDecode。就能避免乱码问题。
            fileName = URLDecoder.decode(fileName, "utf-8");
            sb.append("\t文件名: ").append(fileName);
            sb.append("\t文件大小: ").append(file.getSize() * 1.0 / 1024).append("KB");
            sb.append("\tContentType: ").append(file.getContentType());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * httpclient传流测试
     */
    @PostMapping("dataFlow")
    public String dataFlow(@RequestParam("name") String name, InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder(64);
        sb.append("\nname值为: ").append(name);
        sb.append("\n输入流数据内容为: ");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
