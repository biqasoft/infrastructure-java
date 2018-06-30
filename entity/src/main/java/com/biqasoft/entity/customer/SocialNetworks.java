/*
* Copyright (c) 2016 biqasoft.com




 */

package com.biqasoft.entity.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.javers.core.metamodel.annotation.Value;

@Data
@Value
@ApiModel("Social network info for customer")
public class SocialNetworks {

    @ApiModelProperty("Facebook user id")
    private String fb = null;

    @ApiModelProperty("VKontakte user id")
    private String vk = null;

    @ApiModelProperty("Twitter user id")
    private String twitter = null;

    @ApiModelProperty("Odnoklassniki user id")
    private String ok = null;

    @ApiModelProperty("instagram user id")
    private String instagram = null;

    @ApiModelProperty("linkedin user id")
    private String linkedin = null;

    @ApiModelProperty("youtube user id")
    private String youtube = null;

}
