/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.storage.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.biqasoft.auth.core.Domain;
import com.biqasoft.auth.core.UserAccount;
import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.entity.dto.httpresponse.SampleDataResponse;
import com.biqasoft.storage.entity.StorageFile;
import com.biqasoft.entity.system.ExternalServiceToken;
import com.biqasoft.common.exceptions.ThrowExceptionHelper;
import com.biqasoft.storage.DefaultStorageService;
import com.biqasoft.storage.StorageFileRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.biqasoft.entity.constants.TOKEN_TYPES.DEFAULT_STORAGE;

/**
 * The level of abstraction over AWS S3 Java SDK
 */
public class AmazonS3FileRepository implements StorageFileRepository {

    protected final AmazonS3 conn;
    protected final MongoOperations ops;
    protected final DefaultStorageService defaultStorageService;

    /**
     * For AWS S3
     *
     * @param accessKey
     * @param secretKey
     * @param ops
     * @param defaultStorageService
     */
    public AmazonS3FileRepository(String accessKey, String secretKey, MongoOperations ops, DefaultStorageService defaultStorageService) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.conn = new AmazonS3Client(credentials);
        this.defaultStorageService = defaultStorageService;
        this.ops = ops;
    }

    /**
     * For S3 compatible APIs, such as minio
     *
     * @param accessKey
     * @param secretKey
     * @param ops
     * @param defaultStorageService
     * @param endpoint
     */
    public AmazonS3FileRepository(String accessKey, String secretKey, MongoOperations ops, DefaultStorageService defaultStorageService, String endpoint) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTP);
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3Client amazonS3Client = new AmazonS3Client(credentials, clientConfiguration);
        amazonS3Client.setEndpoint(endpoint);
        amazonS3Client.setS3ClientOptions(new S3ClientOptions());

        S3ClientOptions s3ClientOptions = new S3ClientOptions();
        s3ClientOptions.setPathStyleAccess(true);

        amazonS3Client.setS3ClientOptions(s3ClientOptions);

        this.conn = amazonS3Client;
        this.defaultStorageService = defaultStorageService;
        this.ops = ops;
    }

    @Override
    public void onBeforeUploadMetaInfoFile(StorageFile documentFile) {
        defaultStorageService.addStorageFile(documentFile);
    }

    @Override
    public String getStorageName() {
        return TOKEN_TYPES.S3;
    }

    @Override
    public String processListingPath(String path) {
        return path;
    }

    @Override
    public void onAfterUploadFile(File file, StorageFile documentFile) {
        defaultStorageService.updateStorageFile(documentFile);
    }

    /**
     * Set file name and other flags for uploaded now file
     *
     * @param file
     * @param documentFile
     * @param userAccount
     */
    private void processFileName(File file, StorageFile documentFile, UserAccount userAccount, Domain domain) {
        documentFile.setUploaded(true);
        documentFile.setUploadedFileDate(new Date());
        documentFile.setFileSize(file.length());

        // this is prefix for uploaded files
        // for example it will be '/d5f5htr/4356b50e8b0a362a181000308c/img.png'
        documentFile.setFullName(domain.getDomain() + "/" + documentFile.getId() + "/" + documentFile.getName());
    }

    @Override
    public void createFolder(ExternalServiceToken externalServiceToken, StorageFile documentFile, String folderName, String path) {

    }

    @Override
    public StorageFile uploadFile(File file, StorageFile documentFile, UserAccount userAccount, Domain domain) {
        processFileName(file, documentFile, userAccount,domain );
        uploadFile(file, documentFile.getBucket(), documentFile.getFullName());
        return documentFile;
    }

    @Override
    public List<? extends StorageFile> getMetaInfo(String dir, ExternalServiceToken externalServiceToken) {
        return null;
    }

    @Override
    public ByteArrayOutputStream downloadFileWithToken(StorageFile documentFile, ExternalServiceToken externalServiceToken) {
        checkFullNameAndBucket(documentFile.getBucket(), documentFile.getFullName());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream in = conn.getObject(documentFile.getBucket(), documentFile.getFullName()).getObjectContent();

        try {
            IOUtils.copy(in, byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return byteArrayOutputStream;
    }

    @Override
    public List<StorageFile> getListing(ExternalServiceToken externalServiceToken, String path) {

        Criteria criteria = new Criteria();
        criteria.and("parentId").is(path);
        criteria.and("uploaded").is(true);
        criteria.and("uploadStoreType").is(externalServiceToken.getType());

        if (!externalServiceToken.getType().equals(DEFAULT_STORAGE)) {
            criteria.and("uploadStoreID").is(externalServiceToken.getId());
        }

        Query query = new Query(criteria);
        return ops.find(query, StorageFile.class);
    }

    /**
     * So, check that we have uploaded this file to database
     * and we have access to it
     * We can download only files, not folders
     * We must check this, because instead we will have security issues
     * when everyone can list folder and download files of every user
     * {@code https://trello.com/c/doSUw5gn/316-security-issue-s3}
     *
     * @param bucket
     * @param fullName
     */
    protected void checkFullNameAndBucket(String bucket, String fullName) {
        Criteria criteria = new Criteria();
        criteria.and("bucket").is(bucket);
        criteria.and("fullName").is(fullName);
        criteria.and("file").is(true);

        Query query = new Query(criteria);
        long result = ops.count(query, StorageFile.class);

        if (result < 1) {
            ThrowExceptionHelper.throwExceptionInvalidRequest("No such file uploaded");
        }

    }

    public SampleDataResponse getDirectLinkForFileByBucketAndFullNameAndTTL(String bucket, String fullName, int ttl) {

        checkFullNameAndBucket(bucket, fullName);

        Date expiration = new Date();
        long msec = expiration.getTime();
        msec += ttl;
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, fullName);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
        generatePresignedUrlRequest.setExpiration(expiration);

        SampleDataResponse url = new SampleDataResponse();
        url.setData(getConn().generatePresignedUrl(generatePresignedUrlRequest).toString());

        return url;
    }

    public Bucket createNewBucket(String name) {
        return conn.createBucket(name);
    }

    public ObjectListing getListByBucket(String bucketName) {
        return conn.listObjects(bucketName);
    }

    /**
     * @param file
     * @param existingBucketName
     * @param keyName            - full file name
     */
    private void uploadFile(File file, String existingBucketName, String keyName) {

        List<PartETag> partETags = new ArrayList<>();

        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(existingBucketName, keyName);
        InitiateMultipartUploadResult initResponse = conn.initiateMultipartUpload(initRequest);

        long contentLength = file.length();
        long partSize = 5242880; // Set part size to 5 MB.

        try {
            // Step 2: Upload parts.
            long filePosition = 0;
            for (int i = 1; filePosition < contentLength; i++) {
                // Last part can be less than 5 MB. Adjust part size.
                partSize = Math.min(partSize, (contentLength - filePosition));

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(existingBucketName).withKey(keyName)
                        .withUploadId(initResponse.getUploadId()).withPartNumber(i)
                        .withFileOffset(filePosition)
                        .withFile(file)
                        .withPartSize(partSize);

                // Upload part and add response to our list.
                partETags.add(conn.uploadPart(uploadRequest).getPartETag());

                filePosition += partSize;
            }

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new
                    CompleteMultipartUploadRequest(
                    existingBucketName,
                    keyName,
                    initResponse.getUploadId(),
                    partETags);

            conn.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            conn.abortMultipartUpload(new AbortMultipartUploadRequest(
                    existingBucketName, keyName, initResponse.getUploadId()));
        }
    }

    /**
     * Request is already authorized to this action
     *
     * @param documentFile
     * @return
     */
    @Override
    public boolean deleteDocumentFile(StorageFile documentFile) {
        if (documentFile.isFile()) {
            conn.deleteObject(documentFile.getBucket(), documentFile.getFullName());
        }
        defaultStorageService.deleteStorageFileFromDataBase(documentFile);
        return true;
    }

    public AmazonS3 getConn() {
        return conn;
    }

}
