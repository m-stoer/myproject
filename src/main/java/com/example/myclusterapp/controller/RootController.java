package com.example.myclusterapp.controller;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.eclipse.store.afs.aws.s3.types.S3Connector;
import org.eclipse.store.afs.blobstore.types.BlobStoreFileSystem;
import org.eclipse.store.storage.types.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Controller("/")
public class RootController
{
	private static final Logger LOG = LoggerFactory.getLogger(RootController.class);

	private final StorageManager storage;

	public RootController(StorageManager storage)
	{
		this.storage = storage;
	}

	@Post("/backup")
	public void postBackup()
	{
		LOG.info("Creating a storage backup");

		final var s3 = S3Client.builder().credentialsProvider(null).region(Region.EU_CENTRAL_1).build();

		final String dateTime = Instant.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
		final String backupFilePath = "/backups/STORAGE_%s.tgz".formatted(dateTime);

		final var fs = BlobStoreFileSystem.New(S3Connector.Caching(s3));
		storage.issueFullBackup(fs.ensureDirectoryPath(backupFilePath));

		LOG.info("Storage backup is now being created in the background");
	}
}
