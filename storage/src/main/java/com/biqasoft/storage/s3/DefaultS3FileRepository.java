/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.storage.s3;

import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.microservice.database.TenantDatabase;
import com.biqasoft.storage.DefaultStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * Default storage for all users/domains
 */
@Service
public class DefaultS3FileRepository extends AmazonS3FileRepository {

    public final static String BACKUP_FOLDER_ALIAS = "BACKUPS";

    @Autowired
    public DefaultS3FileRepository(@Value("${aws.s3.credentials.access.key}") String accessKey,
                                   @Value("${aws.s3.credentials.secret.key}") String secretKey,
                                   @TenantDatabase MongoOperations ops,
                                   DefaultStorageService defaultStorageService
    ) {
       super(accessKey, secretKey, ops, defaultStorageService);
    }


    @Override
    public String getStorageName() {
        return TOKEN_TYPES.DEFAULT_STORAGE;
    }

}
