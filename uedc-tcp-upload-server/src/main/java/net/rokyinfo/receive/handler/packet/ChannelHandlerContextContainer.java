package net.rokyinfo.receive.handler.packet;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

public class ChannelHandlerContextContainer {

    private static Map<String, ChannelHandlerContext> map = new HashMap<String, ChannelHandlerContext>();

    private static ChannelHandlerContextContainer instance;

    private ChannelHandlerContextContainer() {

    }

    public static ChannelHandlerContextContainer getHandlerContainer() {

        if (instance == null) {

            synchronized (ChannelHandlerContextContainer.class) {

                if (instance == null) {

                    instance = new ChannelHandlerContextContainer();
                }
            }
        }

        return instance;
    }

    public void putHandlerContext(String ueSn, ChannelHandlerContext channelHandlerContext) {

        map.put(ueSn, channelHandlerContext);
    }

    public void removeHandlerContext(String ueSn) {

        map.remove(ueSn);
    }

    public ChannelHandlerContext getHandlerContext(String ueSn) {

        return map.get(ueSn);
    }
}
