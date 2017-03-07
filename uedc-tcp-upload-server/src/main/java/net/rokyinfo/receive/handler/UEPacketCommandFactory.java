package net.rokyinfo.receive.handler;


import net.rokyinfo.receive.bean.PacketTypeConst;
import net.rokyinfo.receive.bean.UEPacket;
import net.rokyinfo.receive.handler.packet.T14PacketCommand;
import net.rokyinfo.receive.handler.packet.T1PacketCommand;
import net.rokyinfo.receive.handler.packet.T2PacketCommand;
import net.rokyinfo.receive.handler.packet.T3PacketCommand;
import net.rokyinfo.receive.writepacket.WritePacketDirectToDB;
import net.rokyinfo.receive.writepacket.WritePacketInLocalMemoryAndBatchInsert;

public class UEPacketCommandFactory {

    private static UEPacketCommandFactory factory = null;

    private UEPacketCommandFactory() {

    }

    public static UEPacketCommandFactory getUEPacketCommandFactory() {

        if (factory == null) {

            factory = new UEPacketCommandFactory();
        }

        return factory;
    }

    public UEPacketCommand getUEPacketCommand(UEPacket uePacket) {

        UEPacketCommand packetCommand = null;

        /**
         * 如果该报文不需要写数据库，则不用定义写策略
         */

        if (PacketTypeConst.T_LOGIN.equalsIgnoreCase(uePacket.getPacketType())) {        // T1：登陆报文

            packetCommand = new T1PacketCommand();

            // 定义写策略
            packetCommand.setWritePacketStrategy(WritePacketDirectToDB.getWritePacketDirectToDB());

        } else if (PacketTypeConst.T_LOCATION.equalsIgnoreCase(uePacket.getPacketType())) {        // T3：状态实时上报（位置信息上报）

            packetCommand = new T3PacketCommand(uePacket);

            // 定义写策略
            packetCommand.setWritePacketStrategy(WritePacketInLocalMemoryAndBatchInsert.getWritePacketInLocalMemory());

        } else if (PacketTypeConst.T_LOOKUP_PARAM.equalsIgnoreCase(uePacket.getPacketType())) {    // T14：终端上报参数

            packetCommand = new T14PacketCommand();

        } else if (PacketTypeConst.T_SETUP_PARAM.equalsIgnoreCase(uePacket.getPacketType())) {    // T2：参数设置请求应答报文

            packetCommand = new T2PacketCommand();
        }

        return packetCommand;
    }
}
