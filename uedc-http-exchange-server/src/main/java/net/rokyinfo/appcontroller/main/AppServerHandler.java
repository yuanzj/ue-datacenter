package net.rokyinfo.appcontroller.main;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import net.rokyinfo.appcontroller.bean.HttpNettyRequest;
import net.rokyinfo.appcontroller.config.Config;
import net.rokyinfo.appcontroller.controller.HttpBaseController;
import net.rokyinfo.basecommon.config.SpringContainer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class AppServerHandler extends
        SimpleChannelInboundHandler<FullHttpRequest> {

    private static Logger logger = LoggerFactory.getLogger(AppServerHandler.class);

    private Map<String, String> parameters = new HashMap<String, String>();

    private Map<String, ArrayList<String>> arrayParameters = new HashMap<String, ArrayList<String>>();

    private String routeName;

    private String mappingMethodName;

    private File uploadFile;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FullHttpRequest httpRequest) throws Exception {

        try {
            String uri = httpRequest.getUri();

            // uri校验
            if (uri.equals("/favicon.ico")) {
                ctx.close();
                return;
            }

            if (!uri.startsWith("/SpiritServiceApp")) {
                logger.info("invalid request path: " + uri);
                ctx.close();
                return;
            }

//            logger.info("解析请求路径，参数开始,请求方式为："
//                    + httpRequest.getMethod().toString());

            String[] pathNames = null;
            // get
            if (httpRequest.getMethod().equals(HttpMethod.GET)) {
                getRequestParameters(ctx, httpRequest);
                pathNames = getRequestRouteName(uri);
            } else if (httpRequest.getMethod().equals(HttpMethod.POST)) {

//                logger.info("start to decode post parameters 11");

                getPostParameters(ctx, httpRequest);
                pathNames = getPostRouteName(uri);
            } else if (httpRequest.getMethod().equals(HttpMethod.OPTIONS)) {
                getPostParameters(ctx, httpRequest);
                pathNames = getPostRouteName(uri);
            }

            if (pathNames == null || pathNames.length == 0) {
                logger.info("invalid request path: " + uri);
                ctx.close();
                return;
            }

            routeName = pathNames[0];
            if (pathNames.length > 1) {
                mappingMethodName = pathNames[1];
            }

            if (mappingMethodName != null && mappingMethodName.endsWith("/")) {
                mappingMethodName = StringUtils.substringBeforeLast(mappingMethodName, "/");
            }

//            logger.info("处理请求：" + uri + " 参数：" + parameters);

            HttpNettyRequest httpNettyRequest = new HttpNettyRequest(
                    httpRequest);
            httpNettyRequest.setRouteName(routeName);
            httpNettyRequest.setMappingMethodName(mappingMethodName);
            httpNettyRequest.setParameters(parameters);
            httpNettyRequest.setUploadFile(uploadFile);
            httpNettyRequest.setArrayParameters(arrayParameters);

//            logger.info("解析请求路径，参数结束" + ",routeName:" + routeName);

            if (StringUtils.isEmpty(routeName)) {
                String content = "{\"state\":" + -3 + ",\"message\":\""
                        + "错误的请求地址！" + "\"}";
                writeResponse(ctx, content, httpNettyRequest);
                return;
            }

            HttpBaseController httpBaseController = null;
            try {
                httpBaseController = (HttpBaseController) SpringContainer
                        .getSpringContainer().getBean(routeName);
            } catch (org.springframework.beans.factory.NoSuchBeanDefinitionException e) {

                String content = "{\"state\":" + -3 + ",\"message\":\""
                        + "错误的请求地址！" + "\"}";
                writeResponse(ctx, content, httpNettyRequest);
                return;
            }

            httpBaseController.setRequestParameters(parameters);
            httpBaseController.execute(ctx, httpNettyRequest);

        } catch (Exception e) {

            e.printStackTrace();
        }

        // sendError(ctx, HttpResponseStatus.BAD_REQUEST);
    }

    private void getRequestParameters(ChannelHandlerContext ctx,
                                      FullHttpRequest httpRequest) throws UnsupportedEncodingException {

        QueryStringDecoder decoder = new QueryStringDecoder(
                httpRequest.getUri());
        for (String k : decoder.parameters().keySet()) {
            List<String> v = decoder.parameters().get(k);
            parameters.put(k, URLDecoder.decode(v.get(0), "UTF-8"));
        }
    }

    private void getPostParameters(ChannelHandlerContext ctx,
                                   FullHttpRequest httpRequest) throws IOException {

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                new DefaultHttpDataFactory(false), httpRequest);
        List<InterfaceHttpData> dataList = decoder.getBodyHttpDatas();

        /**
         * HttpDataType有三种类型 Attribute, FileUpload, InternalAttribute
         */
        for (InterfaceHttpData data : dataList) {
            String name = data.getName();

            String value = null;
            if (InterfaceHttpData.HttpDataType.Attribute == data
                    .getHttpDataType()) {
                Attribute attribute = (Attribute) data;
                attribute.setCharset(CharsetUtil.UTF_8);
                try {
                    value = attribute.getValue();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }

                // array
                if (parameters.containsKey(name)) {
                    ArrayList<String> listValue = arrayParameters.get(name);
                    if (listValue == null) {
                        listValue = new ArrayList<String>();
                        listValue.add(parameters.get(name));
                    }

                    listValue.add(value);
                    arrayParameters.put(name, listValue);
                }
                parameters.put(name, value);
            } else if (InterfaceHttpData.HttpDataType.FileUpload == data
                    .getHttpDataType()) {

                logger.info("开始解析上传文件");

                FileUpload fileUpload = (FileUpload) data;
                if (fileUpload.isCompleted()) {

                    String directory = Config.getConfig().getParameter("uploadfile_directory");

                    logger.info("上传文件目录：" + directory);

                    uploadFile = new File(directory + fileUpload.getFilename());

                    logger.info("上传文件名：" + fileUpload.getFilename());

                    logger.info(String.valueOf("is in memory" + fileUpload.isInMemory()));

                    fileUpload.renameTo(uploadFile);

                    logger.info("上传文件解析成功");
                }
            } else if (InterfaceHttpData.HttpDataType.InternalAttribute == data
                    .getHttpDataType()) {

            }
        }
    }

    private String[] getRequestRouteName(String uri) {

//        logger.info("request uri:" + uri);

        String url = uri.substring(uri.lastIndexOf("SpiritServiceApp/") + 17);

        String routerName = "";
        if (url.indexOf("?") != -1) {
            routerName = url.substring(0, url.indexOf("?"));
        } else {
            routerName = url;
        }

        return StringUtils.split(routerName, "/", 2);
    }

    private String[] getPostRouteName(String uri) {

//        logger.info("post uri:" + uri);

        String routerName = uri.substring(uri.lastIndexOf("SpiritServiceApp/") + 17);

        // if (url.indexOf("/") != -1) {
        // return url.substring(0, url.indexOf("/"));
        // } else {
        return StringUtils.split(routerName, "/", 2);
        // }
    }

    @SuppressWarnings("unused")
    private static void sendError(ChannelHandlerContext ctx,
                                  HttpResponseStatus status) {

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, status, Unpooled.copiedBuffer("Failure: "
                + status.toString() + "\r\n", CharsetUtil.UTF_8));

        response.headers().set("CONTENT_TYPE", "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    final public void writeResponse(ChannelHandlerContext ctx, String content,
                                    HttpNettyRequest request) throws UnsupportedEncodingException {

        if (is100ContinueExpected(request.getHttpRequest())) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        boolean keepAlive = isKeepAlive(request.getHttpRequest());
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                Unpooled.wrappedBuffer(content.getBytes("utf8")));
        response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());

        response.headers().set("Access-Control-Allow-Origin", "*");

        if (!keepAlive) {
            ctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.writeAndFlush(response);
        }
    }

    public static void main(String[] args) {

        AppServerHandler handler = new AppServerHandler();
        String[] array = handler.getRequestRouteName("/SpiritServiceApp");

        System.out.println("length:" + array.length);

        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
}
