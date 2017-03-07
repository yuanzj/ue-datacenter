package net.rokyinfo.receive.redis;

import net.rokyinfo.receive.bean.UEPacket;

public interface UpdateUEStatusStrategy {

	public void updateRedis(UEPacket uePacket);
}
