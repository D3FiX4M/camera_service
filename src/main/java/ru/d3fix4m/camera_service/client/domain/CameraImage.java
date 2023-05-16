package ru.d3fix4m.camera_service.client.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "camera_image")
@Entity
public class CameraImage {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private byte[] image;

    @Column(nullable = false)
    private Long height;

    @Column(nullable = false)
    private Long width;

    @Column(nullable = false)
    private Long size;
}
