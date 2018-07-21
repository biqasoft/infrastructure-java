/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.storage.entity;

import com.biqasoft.entity.constants.DOCUMENT_FILE;
import com.biqasoft.entity.constants.TOKEN_TYPES;
import com.biqasoft.entity.core.BaseClass;
import com.biqasoft.entity.system.ConnectedInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("Storage File ( jpg, zip, doc, excel etc ). " +
        " This is meta information, such as size, where it is located etc")
@JsonInclude(JsonInclude.Include.NON_NULL)
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

}
