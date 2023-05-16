package ru.d3fix4m.camera_service.client.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CameraImageRepository extends JpaRepository<CameraImage, UUID> {
}
