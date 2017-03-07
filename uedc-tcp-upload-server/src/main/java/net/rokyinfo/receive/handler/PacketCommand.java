package net.rokyinfo.receive.handler;

import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.receive.bean.BasePacket;

public interface PacketCommand {
	
	void parsePacket(ChannelHandlerContext ctx, BasePacket basePacket);
	
}
