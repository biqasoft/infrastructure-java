/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("Base filter & criteria for all BaseClass. All others filters implements it")
public abstract class BaseClassFilter implements Serializable {

    protected boolean useRelativeCreatedDateFrom;
    protected boolean useRelativeCreatedDateTo;

    protected String relativeCreatedDateFrom;
    protected String relativeCreatedDateTo;

    @ApiModelProperty("use objects created with some user account ID ")
    protected boolean useCreatedByUserId;
    @ApiModelProperty("useAccount id if `useCreatedByUserId=true` ")
    protected String createdByUserId;

    @ApiModelProperty(" show only some part of data ")
    protected boolean usePagination;
    @ApiModelProperty("Pagination record from")
    protected int recordFrom;
    @ApiModelProperty("Pagination record to")
    protected int recordTo;

    @ApiModelProperty(" true , if you WANT to receive ONLY some fields in response. Response can be quicker ")
    protected boolean useFieldsPartly = false;
    protected List<String> partlyFields = new ArrayList<>();

    @ApiModelProperty(" true , if you DON'T WANT to receive some fields in response. Response can be quicker ")
    protected boolean useExcludeFieldsPartly = false;
    protected List<String> partlyExcludeFields = new ArrayList<>();

    @ApiModelProperty(" is sort result by some custom field name ")
    protected boolean useCustomSort = false;
    @ApiModelProperty("  sort result by field name such as `firstName` for customer ")
    protected String customSortField;
    @ApiModelProperty(" Reverse sorting results ")
    protected boolean reverseSortDirectionByCustomField;

    @ApiModelProperty("latest created objects to the top ")
    protected boolean sortDESCbyCreatedDate;

    @ApiModelProperty(" if we want to only count resulted diffs - we do it and return result without objects. That's faster than get objects ")
    protected boolean onlyCount = false;

    @ApiModelProperty(" is use search by name (for example first name for customer ) etc ")
    protected boolean useFullTextSearch = false;
    @ApiModelProperty("  text to search in resource data ")
    protected String fullTextSearchRequest;

    @ApiModelProperty("show archived objects. By default - false")
    protected boolean showArchived = false;

    @ApiModelProperty("Use regex `regexBuilderNodes` field ")
    protected boolean useRegex = false;
    @ApiModelProperty("Regex list of patterns")
    protected List<RegexBuilderNode> regexBuilderNodes = new ArrayList<>();

    @ApiModelProperty("include object with some Ids only")
    protected boolean useObjectIds = false;

    @ApiModelProperty("object Ids ")
    protected List<String> objectIds = new ArrayList<>();

    @ApiModelProperty("list of object alias. No get objects with some alias - this should not be empty or null")
    protected List<String> aliases = null;

}
