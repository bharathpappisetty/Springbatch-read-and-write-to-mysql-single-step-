package com.example.sb.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.sb.model.DeviceInfo;
import com.example.sb.repository.DeviceRepository;

@Component
public class DBWriter implements ItemWriter<DeviceInfo> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void write(List<? extends DeviceInfo> devices) throws Exception {

        deviceRepository.saveAll(devices);
    }
}