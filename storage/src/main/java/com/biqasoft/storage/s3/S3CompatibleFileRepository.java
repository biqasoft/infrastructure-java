/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.storage.s3;

import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.entity.system.ExternalServiceToken;
import com.biqasoft.storage.DefaultStorageService;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 *
 * Tested with https://github.com/minio/minio
 *
 * minio has a lot of issues, so see constructor {@link AmazonS3FileRepository#AmazonS3FileRepository(String, String, MongoOperations, DefaultStorageService, String)}
 * where a lot of disabled features
 *
 * https://github.com/minio/minio/blob/master/AWS-SDK-GO.md
 * https://github.com/minio/minio-java
 *
 * http://docs.aws.amazon.com/AmazonS3/latest/dev/UsingAWSSDK.html
 * http://docs.aws.amazon.com/AmazonS3/latest/API/sig-v4-examples-using-sdks.html
 *
 * minio example
 * <code>
 *  docker run -d -p 9000:9000 --name minio1 \
 *    -e "MINIO_ACCESS_KEY=AKIAct4cXAMPLE" \
 *    -e "MINIO_SECRET_KEY=wJalrXUtnFEvt54ghEXAMPLEKEY" \
 *    -v /home/nbakaev/minio/export/minio1:/export \
 *    -v /home/nbakaev/minio/config/minio1:/root/.minio \
 *    minio/minio /export
 * </code>
 *
 * Token data example
 * <code>
 *     ExternalServiceToken token = new ExternalServiceToken();
 *     token.setToken("wJalrXUtnFEvt54ghEXAMPLEKEY");
 *     token.setType("S3_COMPATIBLE");
 *     token.setLogin("AKIAct4cXAMPLE");
 *     token.setServer("http://192.168.127.131:9000/some_bucket_name");
 * </code>
 *
 */
public class S3CompatibleFileRepository extends AmazonS3FileRepository {

    /**
     * Must be init per every request
     * @param ops
     * @param defaultStorageService
     * @param externalServiceToken token where externalServiceToken.login = accessKey, externalServiceToken.token = secretKey, externalServiceToken.server = s3 endpoint
     */
    public S3CompatibleFileRepository(MongoOperations ops, DefaultStorageService defaultStorageService,  ExternalServiceToken externalServiceToken) {
       super(externalServiceToken.getLogin(), externalServiceToken.getToken(), ops, defaultStorageService, externalServiceToken.getServer());
    }

    @Override
    public String getStorageName() {
        return TOKEN_TYPES.S3_COMPATIBLE;
    }

    @Override
    protected void checkFullNameAndBucket(String bucket, String fullName) {

    }
}
