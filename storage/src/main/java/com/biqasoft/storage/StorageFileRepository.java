/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.storage;

import com.biqasoft.auth.core.Domain;
import com.biqasoft.auth.core.UserAccount;
import com.biqasoft.common.exceptions.InvalidStateException;
import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.entity.system.ExternalServiceToken;
import com.biqasoft.storage.entity.StorageFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * @author Nikita Bakaev, ya@nbakaev.ru
 * Date: 7/31/2015
 * All Rights Reserved
 */
public interface StorageFileRepository {

    /**
     * Download file from external storage to byte[]
     *
     * @param documentFile         file that will be downloaded
     * @param externalServiceToken information about storage type and credentials
     * @return download file via server
     */
    ByteArrayOutputStream downloadFileWithToken(StorageFile documentFile, ExternalServiceToken externalServiceToken);

    /**
     * upload new file
     *
     * @param file         file that will be uploaded
     * @param documentFile meta info for file that will be uploaded
     * @param userAccount  who will be creator of file
     * @return meta info for file that was uploaded
     */
    StorageFile uploadFile(File file, StorageFile documentFile, UserAccount userAccount, Domain domain);

    /**
     * @param dir                  full path in storage
     * @param externalServiceToken information about storage type and credentials
     * @return files(and sub folders) in folder
     */
    List<? extends StorageFile> getMetaInfo(String dir, ExternalServiceToken externalServiceToken);

    /**
     * @param externalServiceToken information about storage type and credentials
     * @param path                 dir full path in storage
     * @return file listing - files, folders...
     */
    List<StorageFile> getListing(ExternalServiceToken externalServiceToken, String path);

    boolean deleteDocumentFile(StorageFile documentFile);

    void createFolder(ExternalServiceToken externalServiceToken, StorageFile documentFile, String folderName, String path);

    /**
     * @return unique storage name, see {@link TOKEN_TYPES}
     */
    default String getStorageName() {
        throw new InvalidStateException("You must set storage name");
    }

    default void onBeforeUploadFile(File file, StorageFile documentFile) {
    }

    default void onAfterUploadFile(File file, StorageFile documentFile) {
    }

    default void onBeforeUploadMetaInfoFile(StorageFile documentFile) {
    }

    /**
     * process path for different storage. For example dropbox use path /folder/file.ext and google drive use IDs
     *
     * @param path object path from client
     * @return processed object path
     */
    default String processListingPath(String path) {
        throw new InvalidStateException("Provider is supported but listing - not");
    }

}
