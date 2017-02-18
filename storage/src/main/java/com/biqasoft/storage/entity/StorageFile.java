/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.storage.entity;

import com.biqasoft.entity.constants.DOCUMENT_FILE;
import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.system.ConnectedInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;
import java.util.Map;

@ApiModel("Storage File ( jpg, zip, doc, excel etc ). " +
        " This is meta information, such as size, where it is located etc")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class StorageFile extends BaseClass {

    @ApiModelProperty("Full name, including file name, extension and folder")
    @TextIndexed
    private String fullName;

    @ApiModelProperty("Bucket - only for Amazon S3")
    private String bucket;

    @ApiModelProperty("File size in bytes")
    private long fileSize;

    @ApiModelProperty("Id of parent folder; If this file/folder in root - 'ROOT' will be; ROOT by default")
    @Indexed
    private String parentId = DOCUMENT_FILE.ROOT_FOLDER_NAME;

    @ApiModelProperty("does file upload to server, or it is just meta information")
    @Indexed
    private boolean uploaded;

    @ApiModelProperty("Date when file uploaded")
    private Date uploadedFileDate;

    @ApiModelProperty("When file is stored - Dropbox, default storage etc")
    private String uploadStoreType = TOKEN_TYPES.DEFAULT_STORAGE;

    @ApiModelProperty("Storage ID in store. For example, if it is dropbox - it is login of account etc or TOKEN ID...")
    private String uploadStoreID;

    @ApiModelProperty("Document mime type")
    private String mimeType;

    @ApiModelProperty("Is a folder")
    private boolean folder;

    @ApiModelProperty("Is a file")
    private boolean file;

    // currently used by google drive
    private boolean onlyExport = false;

    // currently used by google drive
    private Map<String, String> exportLinks;

    private ConnectedInfo connectedInfo = new ConnectedInfo();

    @ApiModelProperty(notes = "this temp object which will be deleted")
    @Indexed
    private boolean temp;

    public boolean isTemp() {
        return temp;
    }

    public void setTemp(boolean temp) {
        this.temp = temp;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Map<String, String> getExportLinks() {
        return exportLinks;
    }

    public void setExportLinks(Map<String, String> exportLinks) {
        this.exportLinks = exportLinks;
    }

    public boolean isOnlyExport() {
        return onlyExport;
    }

    public void setOnlyExport(boolean onlyExport) {
        this.onlyExport = onlyExport;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public boolean isFile() {
        return file;
    }

    public void setFile(boolean file) {
        this.file = file;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getUploadStoreType() {
        return uploadStoreType;
    }

    public void setUploadStoreType(String uploadStoreType) {
        this.uploadStoreType = uploadStoreType;
    }

    public String getUploadStoreID() {
        return uploadStoreID;
    }

    public void setUploadStoreID(String uploadStoreID) {
        this.uploadStoreID = uploadStoreID;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public ConnectedInfo getConnectedInfo() {
        return connectedInfo;
    }

    public void setConnectedInfo(ConnectedInfo connectedInfo) {
        this.connectedInfo = connectedInfo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getUploadedFileDate() {
        return uploadedFileDate;
    }

    public void setUploadedFileDate(Date uploadedFileDate) {
        this.uploadedFileDate = uploadedFileDate;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageFile that = (StorageFile) o;

        if (fileSize != that.fileSize) return false;
        if (uploaded != that.uploaded) return false;
        if (folder != that.folder) return false;
        if (file != that.file) return false;
        if (onlyExport != that.onlyExport) return false;
        if (fullName != null ? !fullName.equals(that.fullName) : that.fullName != null) return false;
        if (bucket != null ? !bucket.equals(that.bucket) : that.bucket != null) return false;
        if (parentId != null ? !parentId.equals(that.parentId) : that.parentId != null) return false;
        if (uploadedFileDate != null ? !uploadedFileDate.equals(that.uploadedFileDate) : that.uploadedFileDate != null)
            return false;
        if (uploadStoreType != null ? !uploadStoreType.equals(that.uploadStoreType) : that.uploadStoreType != null)
            return false;
        if (uploadStoreID != null ? !uploadStoreID.equals(that.uploadStoreID) : that.uploadStoreID != null)
            return false;
        if (mimeType != null ? !mimeType.equals(that.mimeType) : that.mimeType != null) return false;
        if (exportLinks != null ? !exportLinks.equals(that.exportLinks) : that.exportLinks != null) return false;
        return connectedInfo != null ? connectedInfo.equals(that.connectedInfo) : that.connectedInfo == null;

    }

    @Override
    public int hashCode() {
        int result = fullName != null ? fullName.hashCode() : 0;
        result = 31 * result + (bucket != null ? bucket.hashCode() : 0);
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (uploaded ? 1 : 0);
        result = 31 * result + (uploadedFileDate != null ? uploadedFileDate.hashCode() : 0);
        result = 31 * result + (uploadStoreType != null ? uploadStoreType.hashCode() : 0);
        result = 31 * result + (uploadStoreID != null ? uploadStoreID.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        result = 31 * result + (folder ? 1 : 0);
        result = 31 * result + (file ? 1 : 0);
        result = 31 * result + (onlyExport ? 1 : 0);
        result = 31 * result + (exportLinks != null ? exportLinks.hashCode() : 0);
        result = 31 * result + (connectedInfo != null ? connectedInfo.hashCode() : 0);
        return result;
    }
}
