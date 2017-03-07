package net.rokyinfo.receive.writepacket;


import net.rokyinfo.receive.bean.UEPacket;

public abstract class UEWritePacketStrategy implements WritePacketStrategy {

    @Override
    public void writePacket(UEPacket uePacket) {

        doWrite(uePacket);
    }

    protected abstract void doWrite(UEPacket uePacket);
}
