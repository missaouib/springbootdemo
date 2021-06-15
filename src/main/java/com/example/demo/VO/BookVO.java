package com.example.demo.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("书籍VO")
public class BookVO {
    /**
     * 唯一标识id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 书名
     */
    @ApiModelProperty("书名")
    @NotBlank(message = "书名不能为空")
    private String name;

    @ApiModelProperty("价格")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于等于0")
    private BigDecimal price;

    @ApiModelProperty(value = "创建时间", example = "2021-06-15 14:43:09")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", example = "2021-06-15 14:43:09")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
