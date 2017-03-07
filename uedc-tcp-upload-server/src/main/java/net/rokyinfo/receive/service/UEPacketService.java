package net.rokyinfo.receive.service;


import io.netty.channel.ChannelHandlerContext;
import net.rokyinfo.receive.bean.UEPacket;
import net.rokyinfo.receive.handler.UEPacketCommand;
import net.rokyinfo.receive.handler.UEPacketCommandFactory;
import net.rokyinfo.receive.redis.UpdateRedisFromLocalMemory;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component("uePacketService")
public class UEPacketService {

    public void handlePacket(ChannelHandlerContext ctx, UEPacket uePacket) {

        UEPacketCommand packetCommand = UEPacketCommandFactory.getUEPacketCommandFactory().getUEPacketCommand(uePacket);

        if (packetCommand != null) {

            // 定义缓存更新策略
            packetCommand.setUpdateUEStatusStrategy(UpdateRedisFromLocalMemory.getUpdateRedisFromLocalMemory());

            // 对登陆报文进行处理
            packetCommand.parsePacket(ctx, uePacket);
        }
    }
}
