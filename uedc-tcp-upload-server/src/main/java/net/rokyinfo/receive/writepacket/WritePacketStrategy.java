package net.rokyinfo.receive.writepacket;


import net.rokyinfo.receive.bean.UEPacket;

public interface WritePacketStrategy {

    public void writePacket(UEPacket uePacket);

}
