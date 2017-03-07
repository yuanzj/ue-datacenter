package net.rokyinfo.receive.main;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import net.rokyinfo.basecommon.config.Config;
import net.rokyinfo.receive.handler.ReceiveHandler;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ReceiveServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipeline = socketChannel.pipeline();

        ByteBuf[] delimiters = new ByteBuf[] {
                Unpooled.wrappedBuffer(new byte[] { 'E', 'D' }),
                Unpooled.wrappedBuffer(new byte[] { ']' }),
        };

        pipeline.addLast("frameDecoder", new DelimiterBasedFrameDecoder(4096, delimiters));

        // 字符串编码和解码
        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.ISO_8859_1));
        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.ISO_8859_1));

        // 空闲处理
        pipeline.addLast("idleStateHandler", new IdleStateHandler(
                Integer.parseInt(Config.getConfig().getParameter("CHANNEL_READ_TIMEOUT")),
                Integer.MAX_VALUE, 0));
        pipeline.addLast(new ChannelDuplexHandler() {

            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
                    throws Exception {

                if (evt instanceof IdleStateEvent) {

                    IdleStateEvent e = (IdleStateEvent) evt;
                    if (e.state() == IdleState.READER_IDLE) {

                        ctx.close();
                    }
                }
            }
        });

        // handler
        pipeline.addLast("handler", new ReceiveHandler());
    }
}
