package net.rokyinfo.appcontroller.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import net.rokyinfo.appcontroller.annotation.MappingMethod;
import net.rokyinfo.appcontroller.bean.HttpNettyRequest;
import net.rokyinfo.appcontroller.bean.HttpResponseEntity;
import net.rokyinfo.appcontroller.config.Config;
import net.rokyinfo.basecommon.util.JSONUtils;
import net.rokyinfo.basedao.entity.UEBasicInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public abstract class HttpBaseController {

    private static Logger logger = LoggerFactory.getLogger(HttpBaseController.class);

    protected String errorMessage = "系统异常，请稍候重试！";

    protected Map<String, String> requestParameters = null;

    private Map<String, Method> mappingMethods = Maps.newHashMap();

    @Autowired
    private DataSourceTransactionManager transactionManager;

    @PostConstruct
    public void init() {

        Method[] declareMethods = this.getClass().getDeclaredMethods();
        for (Method method : declareMethods) {

            Class[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length == 2 && parameterTypes[0] == ChannelHandlerContext.class
                    && parameterTypes[1] == HttpNettyRequest.class) {

                MappingMethod mappingMethod = method.getAnnotation(MappingMethod.class);
                if (mappingMethod != null) {

                    mappingMethods.put(mappingMethod.value(), method);
                }
            }
        }
    }

    /**
     * 业务执行方法
     */
    public void execute(ChannelHandlerContext ctx, HttpNettyRequest request) {

        String sessionId = request.getParameters().get("sessionId");

        Method method = mappingMethods.get(request.getMappingMethodName());
        if (method == null) {
            String content = "{\"state\":" + -3 + ",\"message\":\""
                    + "错误的请求地址！" + "\"}";
            writeResponse(ctx, content, request);
            return;
        }

        // spring 编程式事务
        TransactionStatus status = null;

        boolean withTransactionAnnotation = withTransactionAnnotation(method);

        if (withTransactionAnnotation) {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = transactionManager.getTransaction(def);
        }

        try {
            Object result = method.invoke(this, ctx, request);

            Integer state = Integer.parseInt(result.toString());

            if (withTransactionAnnotation) {
                if (state == 0) {
                    transactionManager.commit(status);
                } else {
                    transactionManager.rollback(status);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (withTransactionAnnotation) {
                transactionManager.rollback(status);
            }

            logger.info("error msg:" + e.toString());

            String content = "{\"state\":" + -1 + ",\"message\":\""
                    + errorMessage + "\"}";

            writeResponse(ctx, content, request);
        }
    }

    @MappingMethod("doExecute")
    protected int doExecute(ChannelHandlerContext ctx, HttpNettyRequest request) throws Exception {

        String content = "{\"state\":" + -3 + ",\"message\":\""
                + "错误的请求地址！" + "\"}";
        writeResponse(ctx, content, request);
        return 1;
    }

    /**
     * 提供默认的权限判断，具体的权限由子类负责：有权限操作，返回true，没有权限操作，返回false
     *
     * @param request
     * @return
     */
    protected boolean doJudgeAuth(HttpNettyRequest request) {

        return true;
    }

    final public void writeResponse(ChannelHandlerContext ctx,
                                    HttpResponseEntity result, HttpNettyRequest request) {

        String message = JSONUtils.toJSONString(result);
        writeResponse(ctx, message, request);
    }

    /**
     * @param ctx
     * @param content
     * @param request
     * @throws java.io.UnsupportedEncodingException 响应请求，返回数据
     */
    final public void writeResponse(ChannelHandlerContext ctx, String content,
                                    HttpNettyRequest request) {

        if (is100ContinueExpected(request.getHttpRequest())) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        boolean keepAlive = isKeepAlive(request.getHttpRequest());
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                    Unpooled.wrappedBuffer(content.getBytes("utf8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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

    /**
     * @param ctx
     * @param array
     * @param request
     * @throws java.io.UnsupportedEncodingException 响应请求，返回数据
     */
    final public void writeResponseByte(ChannelHandlerContext ctx, byte[] array,
                                    HttpNettyRequest request) {

        if (is100ContinueExpected(request.getHttpRequest())) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }

        boolean keepAlive = isKeepAlive(request.getHttpRequest());
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(array));

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

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(Map<String, String> requestParameters) {
        this.requestParameters = requestParameters;
    }

    private boolean withTransactionAnnotation(Method method) {
//        return method.isAnnotationPresent(TransactionMethod.class);
        return false;
    }

    protected boolean judgeEmptySn(String ueSn, ChannelHandlerContext ctx, HttpNettyRequest request) {

        HttpResponseEntity result = new HttpResponseEntity();

        if (StringUtils.isEmpty(ueSn)) {
            result.setState(1);
            result.setMessage("设备序列号为空");
            writeResponse(ctx, result, request);
            return true;
        }

        return false;
    }

}
