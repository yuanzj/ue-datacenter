package net.rokyinfo.appcontroller.bean;

import io.netty.handler.codec.http.FullHttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class HttpNettyRequest {

    private FullHttpRequest httpRequest;

    private String routeName;

    private String mappingMethodName;

    private Map<String, String> parameters;

    private Map<String, ArrayList<String>> arrayParameters;

    /**
     * 上传文件
     */
    private File uploadFile;

    public HttpNettyRequest(FullHttpRequest httpRequest) {

        this.httpRequest = httpRequest;
    }

    public FullHttpRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public File getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(File uploadFile) {
        this.uploadFile = uploadFile;
    }

    public Map<String, ArrayList<String>> getArrayParameters() {
        return arrayParameters;
    }

    public void setArrayParameters(Map<String, ArrayList<String>> arrayParameters) {
        this.arrayParameters = arrayParameters;
    }

    public String getMappingMethodName() {
        return mappingMethodName;
    }

    public void setMappingMethodName(String mappingMethodName) {
        this.mappingMethodName = mappingMethodName;
    }
}
