package ru.d3fix4m.camera_service.service.config;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;

@Slf4j
@Configuration
public class GrpcConfig {

	@Bean
	public Server grpcServer(ApplicationContext context, Environment env) throws IOException {
		ServerBuilder<?> builder = ServerBuilder.forPort(env.getRequiredProperty("grpc.server.port", Integer.class))
				.addService(ProtoReflectionService.newInstance());


		context.getBeansOfType(BindableService.class).values().forEach(item -> {
			builder.addService(item);
			log.info("GRPC Registry: " + item.getClass().getName());
		});

		Server server = builder.build();
		server.start();
		log.info("GRPC Server started on port: {}", server.getPort());
		return server;
	}
}
