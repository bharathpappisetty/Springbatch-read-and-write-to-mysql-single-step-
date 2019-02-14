package com.example.sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.sb.model.DeviceInfo;

public interface DeviceRepository extends JpaRepository<DeviceInfo, Long> {

}
