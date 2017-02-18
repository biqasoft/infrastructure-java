/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.storage;

import com.biqasoft.entity.annotations.BiqaAddObject;
import com.biqasoft.entity.annotations.BiqaCheckSecuredModifyObject;
import com.biqasoft.entity.filters.StorageFileFilter;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.microservice.database.TenantDatabase;
import com.biqasoft.persistence.base.BiqaObjectFilterService;
import com.biqasoft.storage.entity.StorageFile;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.biqasoft.storage.s3.DefaultS3FileRepository.BACKUP_FOLDER_ALIAS;

/**
 * Created by Nikita Bakaev, ya@nbakaev.ru on 3/21/2016.
 * All Rights Reserved
 */
@Service
public class DefaultStorageService {

    private MongoOperations ops;
    private BiqaObjectFilterService biqaObjectFilterService;

    @Autowired
    public DefaultStorageService(BiqaObjectFilterService biqaObjectFilterService, @TenantDatabase MongoOperations ops) {
        this.biqaObjectFilterService = biqaObjectFilterService;
        this.ops = ops;
    }

    @BiqaAddObject
    public StorageFile addStorageFile(StorageFile documentFile) {
        documentFile.setUploaded(false);

        if (documentFile.isFolder()) {
            documentFile.setUploaded(true);
        }

        ops.insert(documentFile);
        return documentFile;
    }

    @BiqaCheckSecuredModifyObject
    public StorageFile updateStorageFile(StorageFile documentFile) {

        try {
            documentFile.setMimeType(getContentTypeFromFileName(documentFile.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return biqaObjectFilterService.safeUpdate(documentFile, ops);
    }

    public StorageFile findStorageFileById(String id) {
        StorageFile documentFile = ops.findOne(Query.query(Criteria.where("id").is(id)), StorageFile.class);
        return documentFile;
    }

    public List<StorageFile> findAll() {
        return ops.findAll(StorageFile.class);
    }

    public BiqaPaginationResultList<StorageFile> getStorageFileByFilter(StorageFileFilter filter) {

        Criteria criteria = biqaObjectFilterService.getCriteriaFromAbstractBuilder(filter);
        Query query = biqaObjectFilterService.getQueryFromFilter(filter, criteria);

        if (filter.isUseListOfIDs()) criteria.and("id").in(filter.getListOfIDs());
        if (filter.isOnlyUploaded()) criteria.and("uploaded").is(true);
        if (!StringUtils.isEmpty(filter.getConnectedCustomerId())) criteria.and("connectedCustomerId").is(filter.getConnectedCustomerId());

        return biqaObjectFilterService.getPaginationResultList(filter, criteria, query, StorageFile.class, ops);
    }

    public boolean deleteStorageFileFromDataBase(StorageFile documentFile) {
        ops.remove(documentFile);
        return true;
    }

    /**
     * This is system folder for user backups for admin
     * see {@link com.biqasoft.database.backup.services.BackupService}
     *
     * @return StorageFile for uploading system backups for all user date. Usually this is root folder with backup name
     */
    public StorageFile checkBackupFolder() {
        Criteria criteria = new Criteria();
        criteria.and("folder").is(true);
        criteria.and("parentId").is("ROOT");
        criteria.and("alias").is(BACKUP_FOLDER_ALIAS);

        Query query = new Query(criteria);

        StorageFile documentFile = ops.findOne(query, StorageFile.class);

        if (documentFile != null) return documentFile;

        documentFile = new StorageFile();
        documentFile.setParentId("ROOT");
        documentFile.setName("Бэкапы");
        documentFile.setAlias("BACKUPS");
        documentFile.setFolder(true);
        documentFile.setSecured(true);
        documentFile.setUploaded(true);

        ops.insert(documentFile);
        return documentFile;
    }

    /**
     * get content type, based on file name(extension)
     *
     * @param fileName filename
     * @return mime type
     */
    public String getContentTypeFromFileName(String fileName) {
        Tika tika = new Tika();
        return tika.detect(fileName);
    }

}
