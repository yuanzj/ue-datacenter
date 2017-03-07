package net.rokyinfo.receive.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.basecommon.config.SpringContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/8/9.
 */
public class Main {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(2048);

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024).option(
                    ChannelOption.SO_KEEPALIVE, false);
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ReceiveServerInitializer());

            Channel channel = serverBootstrap
                    .bind(Integer.parseInt(Config.getConfig().getParameter(
                            "port"))).sync().channel();

            SpringContainer.getSpringContainer();

            logger.info("Receive Server start successfully");

            SpringContainer.getSpringContainer().getBean("commandToUEListener");

            channel.closeFuture().sync();

        } finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
